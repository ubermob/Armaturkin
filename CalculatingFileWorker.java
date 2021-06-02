import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.*;

public class CalculatingFileWorker implements Runnable, CellEmptyChecker, RowEmptyChecker, ParseInt {

	private final String path;
	private final ArrayList<Reinforcement> reinforcementArrayList;
	private Workbook workbook;
	private Sheet sheet;
	private Row row;
	private int rowInt;
	private final ArrayList<Integer> positionList = new ArrayList<>();

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

	public CalculatingFileWorker(String path, ArrayList<Reinforcement> reinforcementArrayList) {
		this.path = path;
		this.reinforcementArrayList = reinforcementArrayList;
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
		System.out.println(getClass() + ": Thread complete");
		Main.addNotification("☻ Файл с армированием прочитан до строки с номером: " + rowInt + " (включительно)");
	}

	private void readRow() {
		row = sheet.getRow(rowInt);
		majorNumber = parseIntFromNumber(row.getCell(majorNumberColumn));
		diameter = parseIntFromString(row.getCell(diameterColumn));
		length = parseIntFromString(row.getCell(lengthColumn));
		minorNumber = parseIntFromString(row.getCell(minorNumberColumn));
		position = parseIntFromString(row.getCell(positionColumn));

		System.out.println(getClass() + ": [rowInt: " + rowInt + "]");
		System.out.printf(getClass() + " RAW parsing values: [position: %d],[diameter: %d],[length: %d],[majorNumber: %d],[minorNumber: %d]\n",
				position, diameter, length, majorNumber, minorNumber);
		buildList();
		// toString from ArrayList
	}

	private void buildList() {
		if (positionList.contains(position)) {
			editPosition();
		} else {
			insertPosition();
		}
	}

	private void editPosition() {

	}

	private void insertPosition() {
		positionList.add(position);
		int reservedPositionIndex = Pattern.getReservedPositionIndex(position);
		if (reservedPositionIndex != -1) {
			// Linear reinforcement
			Reinforcement sample = new Reinforcement(position,
					Pattern.reservedDiameter[reservedPositionIndex],
					Pattern.getReservedRFClass(reservedPositionIndex),
					//length,
					// need mass
			);
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
		System.out.printf(getClass() + " table head: [majorNumberColumn: %d],[diameterColumn: %d],[lengthColumn: %d],[minorNumberColumn: %d],[positionColumn: %d]\n",
				majorNumberColumn, diameterColumn, lengthColumn, minorNumberColumn, positionColumn);
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