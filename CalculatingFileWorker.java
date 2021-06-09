import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Formatter;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.*;

public class CalculatingFileWorker implements Runnable, CellEmptyChecker, RowEmptyChecker, ParseInt {

	private final String path;
	private final HashMap<Integer, Reinforcement> reinforcementHashMap;
	private final HashMap<Integer, ReinforcementProduct> reinforcementProductHashMap;
	private Workbook workbook;
	private Sheet sheet;
	private Row row;
	private int rowInt;

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
		try {
			workbook = WorkbookFactory.create(Files.newInputStream(Path.of(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = workbook.getSheetAt(0);
		if (tableHeadVerification()) {
			rowInt = 1;
			while (!isRowEmpty(sheet.getRow(rowInt)) && !isCellEmpty(sheet.getRow(rowInt).getCell(0))) {
				readRow();
				rowInt++;
			}
		} else {
			tableHeadNotValid();
		}
		Log.add(getClass() + ": Thread complete");
		Main.addNotification("☻ Файл с армированием прочитан до строки с номером: " + rowInt + " (включительно)");
	}

	private void readRow() {
		row = sheet.getRow(rowInt);
		majorNumber = parseIntFromNumber(row.getCell(majorNumberColumn));
		diameter = parseIntFromString(row.getCell(diameterColumn));
		length = parseIntFromString(row.getCell(lengthColumn));
		minorNumber = parseIntFromString(row.getCell(minorNumberColumn));
		position = parseIntFromString(row.getCell(positionColumn));

		Log.add(getClass() + ": [rowInt: " + rowInt + "]");
		Formatter formatter = new Formatter();
		formatter.format(getClass() + " RAW parsing values: [position: %d],[diameter: %d],[length: %d],[majorNumber: %d],[minorNumber: %d]",
				position, diameter, length, majorNumber, minorNumber);
		Log.add(formatter.toString());
		buildMap();
		Log.add(reinforcementHashMap.get(position).toString());
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
			compareDiameter(reservedPositionIndex);
			compareMaxLength();
			reinforcementHashMap.put(position, getReinforcement(true, number));
		} else if (reinforcementProductHashMap.containsKey(position)) {
			// Reinforcement product
			compareDiameter();
			compareMaxLength();
			compareLength();
			reinforcementHashMap.put(position, getReinforcement(false, number));
		} else {
			Main.addNotification("Позиция: " + position + " отсутствует в списке изделий");
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

	private void compareDiameter(int reservedPositionIndex) {
		int productDiameter = StandardsRepository.reservedDiameter[reservedPositionIndex];
		if (diameter != productDiameter) {
			Main.addNotification("В строке " + (rowInt + 1) + " не совпадает диаметр: " + diameter +
					" (в зарезервированных позициях: " + productDiameter + ")");
		}
	}

	private void compareDiameter() {
		int productDiameter = reinforcementProductHashMap.get(position).getDiameter();
		if (diameter != productDiameter) {
			Main.addNotification("В строке " + (rowInt + 1) + " не совпадает диаметр: " + diameter +
					" (в списке изделий: " + productDiameter + ")");
		}
	}

	private void compareMaxLength() {
		if (length > StandardsRepository.maxLength) {
			Main.addNotification("В строке " + (rowInt + 1) + " длина изделия/стержня: " + length);
		}
	}

	private void compareLength() {
		int productLength = reinforcementProductHashMap.get(position).getLength();
		if (length != productLength) {
			Main.addNotification("В строке " + (rowInt + 1) + " не совпадает длина: " + length +
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
		Log.add(formatter.toString());
		return majorNumberColumn != -1 && diameterColumn != -1 && lengthColumn != -1 && minorNumberColumn != -1 && positionColumn != -1;
	}

	private boolean equals(int column, String pattern) {
		return row.getCell(column).getStringCellValue().equalsIgnoreCase(pattern);
	}

	private void tableHeadNotValid() {
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
		Main.addNotification("Я ненашел колонку (" + string +") в файле");
	}
}