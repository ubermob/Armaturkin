package armaturkin.workers;

import armaturkin.core.Main;
import armaturkin.interfaces.CellEmptyChecker;
import armaturkin.interfaces.ParseExpression;
import armaturkin.interfaces.ParseInt;
import armaturkin.interfaces.RowEmptyChecker;
import armaturkin.reinforcement.Reinforcement;
import armaturkin.reinforcement.ReinforcementProduct;
import armaturkin.reinforcement.StandardsRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import utools.stopwatch.Stopwatch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Formatter;
import java.util.HashMap;

public class CalculatingFileWorker implements Runnable, CellEmptyChecker, RowEmptyChecker, ParseInt, ParseExpression {

	private final String path;
	private final HashMap<Integer, Reinforcement> reinforcementHashMap;
	private final HashMap<Integer, ReinforcementProduct> reinforcementProductHashMap;
	private Workbook workbook;
	private Sheet sheet;
	private Row row;
	private int currentRow;
	private Stopwatch stopwatch;

	private int majorNumberColumn = -1;
	private int majorNumber;
	private int diameterColumn = -1;
	private int diameter;
	private int lengthColumn = -1;
	private int length;
	private int minorNumberColumn = -1;
	private int minorNumber;
	private int positionColumn = -1;
	private int position;

	public CalculatingFileWorker(String path, HashMap<Integer, Reinforcement> reinforcementHashMap,
	                             HashMap<Integer, ReinforcementProduct> reinforcementProductHashMap) {
		this.path = path;
		this.reinforcementHashMap = reinforcementHashMap;
		this.reinforcementProductHashMap = reinforcementProductHashMap;
	}

	@Override
	public void run() {
		stopwatch = new Stopwatch();
		Main.app.log(Main.app.getProperty("thread_start").formatted(getClass()));
		Main.app.log(Main.app.getProperty("thread_file").formatted(getClass(), path));
		try {
			workbook = WorkbookFactory.create(Files.newInputStream(Path.of(path)));
		} catch (IOException e) {
			Main.app.log(e);
		}
		sheet = workbook.getSheetAt(0);
		if (tableHeadVerification()) {
			currentRow = 1;
			while (!isRowEmpty(sheet.getRow(currentRow)) && !isCellEmpty(sheet.getRow(currentRow).getCell(0))) {
				readRow();
				currentRow++;
			}
		} else {
			tableHeadDoNotValid();
		}
		Main.app.addNotification(Main.app.getProperty("file_successfully_read_2").formatted(currentRow));
		Main.app.log(Main.app.getProperty("thread_complete").formatted(getClass(), stopwatch.getElapsedTime()));
	}

	private void readRow() {
		Main.app.log(Main.app.getProperty("current_row").formatted(getClass(), currentRow));
		row = sheet.getRow(currentRow);
		majorNumber = parseIntFromNumber(row.getCell(majorNumberColumn));
		diameter = parseIntFromString(row.getCell(diameterColumn));
		length = parseIntFromString(row.getCell(lengthColumn));
		try {
			minorNumber = parseIntFromString(row.getCell(minorNumberColumn));
		} catch (NumberFormatException e) {
			// Try parsing expression
			int localCurrentRow = currentRow;
			String localParsedString = row.getCell(minorNumberColumn).getStringCellValue();
			Main.app.log(Main.app.getProperty("expression_console").formatted(getClass(), localCurrentRow, localParsedString));
			Main.app.addNotification(Main.app.getProperty("expression_notification").formatted(localCurrentRow + 1, localParsedString));
			try {
				minorNumber = parseIntFromExpression(row.getCell(minorNumberColumn));
			} catch (UnsupportedOperationException e2) {
				// Unsupported expression
				Main.app.addNotification(Main.app.getProperty("interface_exception_notification").formatted(localParsedString, localCurrentRow + 1));
				Main.app.log(Main.app.getProperty("interface_exception_console").formatted(getClass(), localParsedString, localCurrentRow));
				Main.app.log(e2);
			}
		}
		position = parseIntFromString(row.getCell(positionColumn));

		Formatter formatter = new Formatter();
		formatter.format(getClass() + " RAW parsing values: [position: %d],[diameter: %d],[length: %d],[majorNumber: %d],[minorNumber: %d]",
				position, diameter, length, majorNumber, minorNumber);
		Main.app.log(formatter.toString());
		checkPosition();
		buildMap();
		Main.app.log(reinforcementHashMap.get(position).toString());
	}

	private void buildMap() {
		if (reinforcementHashMap.containsKey(position)) {
			editPosition();
		} else {
			insertPosition();
		}
	}

	private void editPosition() {
		int reservedPositionIndex = StandardsRepository.getReservedPositionIndex(position);
		int number = majorNumber * minorNumber;
		if (reservedPositionIndex != -1) {
			// Linear reinforcement
			compareDiameter(reservedPositionIndex);
			compareMaxLength();
			Reinforcement calculatedReinforcement = reinforcementHashMap.get(position);
			Reinforcement currentReinforcement = getReinforcement(true, number);
			reinforcementHashMap.put(position, new Reinforcement(currentReinforcement.getPosition(),
					currentReinforcement.getDiameter(),
					currentReinforcement.getRfClass(),
					calculatedReinforcement.getLength() + currentReinforcement.getLength(),
					calculatedReinforcement.getMass() + currentReinforcement.getMass()
			));
		} else {
			// Reinforcement product
			compareDiameter();
			compareMaxLength();
			compareLength();
			Reinforcement calculatedReinforcement = reinforcementHashMap.get(position);
			Reinforcement currentReinforcement = getReinforcement(false, number);
			reinforcementHashMap.put(position, new Reinforcement(currentReinforcement.getPosition(),
					currentReinforcement.getDiameter(),
					currentReinforcement.getRfClass(),
					currentReinforcement.getLength(),
					calculatedReinforcement.getNumber() + currentReinforcement.getNumber(),
					currentReinforcement.getMass()
			));
		}
	}

	private void insertPosition() {
		int reservedPositionIndex = StandardsRepository.getReservedPositionIndex(position);
		int number = majorNumber * minorNumber;
		if (reservedPositionIndex != -1) {
			// Linear reinforcement
			checkPosition();
			compareDiameter(reservedPositionIndex);
			compareMaxLength();
			reinforcementHashMap.put(position, getReinforcement(true, number));
		} else if (reinforcementProductHashMap.containsKey(position)) {
			// Reinforcement product
			checkPosition();
			compareDiameter();
			compareMaxLength();
			compareLength();
			reinforcementHashMap.put(position, getReinforcement(false, number));
		} else {
			Main.app.addNotification("Позиция: " + position + " отсутствует в списке изделий");
		}
	}

	private Reinforcement getReinforcement(boolean isLinear, int number) {
		if (isLinear) {
			return new Reinforcement(position,
					diameter,
					StandardsRepository.getReservedRFClass(position),
					length * number,
					StandardsRepository.getMass(diameter) * length * number / 1000
			);
		}
		return new Reinforcement(position,
				diameter,
				reinforcementProductHashMap.get(position).getRfClass(),
				length,
				number,
				reinforcementProductHashMap.get(position).getMass()
		);
	}

	private void checkPosition() {
		if (position <= 0) {
			Main.app.addNotification(Main.app.getProperty("position_notification_3").formatted((currentRow + 1), position));
		}
		if (position > StandardsRepository.maxPosition) {
			Main.app.addNotification(Main.app.getProperty("position_notification_4").formatted((currentRow + 1), position));
		}
	}

	private void compareDiameter(int reservedPositionIndex) {
		int productDiameter = StandardsRepository.reservedDiameters[reservedPositionIndex];
		if (diameter != productDiameter) {
			Main.app.addNotification("В строке " + (currentRow + 1) + " не совпадает диаметр: " + diameter +
					" (в зарезервированных позициях: " + productDiameter + ")");
		}
	}

	private void compareDiameter() {
		int productDiameter = reinforcementProductHashMap.get(position).getDiameter();
		if (diameter != productDiameter) {
			Main.app.addNotification("В строке " + (currentRow + 1) + " не совпадает диаметр: " + diameter +
					" (в списке изделий: " + productDiameter + ")");
		}
	}

	private void compareMaxLength() {
		if (length > StandardsRepository.maxLength) {
			Main.app.addNotification("В строке " + (currentRow + 1) + " длина изделия/стержня: " + length);
		}
	}

	private void compareLength() {
		int productLength = reinforcementProductHashMap.get(position).getLength();
		if (length != productLength) {
			Main.app.addNotification("В строке " + (currentRow + 1) + " не совпадает длина: " + length +
					" (в списке изделий : " + productLength + ")");
		}
	}

	private boolean tableHeadVerification() {
		int column = 0;
		row = sheet.getRow(0);
		while (!isCellEmpty(row.getCell(column))) {
			if (equals(column, "Количество")) {
				majorNumberColumn = column;
			}
			if (equals(column, "ДИАМЕТР")) {
				diameterColumn = column;
			}
			if (equals(column, "ДЛИНА")) {
				lengthColumn = column;
			}
			if (equals(column, "КОЛ-ВО")) {
				minorNumberColumn = column;
			}
			if (equals(column, "ПОЗИЦИЯ")) {
				positionColumn = column;
			}
			column++;
		}
		Formatter formatter = new Formatter();
		formatter.format(getClass() + " table head: [majorNumberColumn: %d],[diameterColumn: %d],[lengthColumn: %d],[minorNumberColumn: %d],[positionColumn: %d]",
				majorNumberColumn, diameterColumn, lengthColumn, minorNumberColumn, positionColumn);
		Main.app.log(formatter.toString() + "\n");
		return majorNumberColumn != -1 && diameterColumn != -1 && lengthColumn != -1 && minorNumberColumn != -1 && positionColumn != -1;
	}

	private boolean equals(int column, String pattern) {
		return row.getCell(column).getStringCellValue().equalsIgnoreCase(pattern);
	}

	private void tableHeadDoNotValid() {
		String string = "";
		if (majorNumberColumn == -1) {
			string = "majorNumber";
		}
		if (diameterColumn == -1) {
			string = "diameter";
		}
		if (lengthColumn == -1) {
			string = "length";
		}
		if (minorNumberColumn == -1) {
			string = "minorNumber";
		}
		if (positionColumn == -1) {
			string = "position";
		}
		string += " variable";
		Main.app.addNotification("Я ненашел колонку (" + string + ") в файле");
	}
}