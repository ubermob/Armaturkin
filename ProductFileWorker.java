import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.*;

public class ProductFileWorker implements Runnable, CellEmptyChecker, RowEmptyChecker, ParseInt {

	private final String path;
	private final HashMap<Integer, ReinforcementProduct> reinforcementProductHashMap;
	private Workbook workbook;
	private Sheet sheet;
	private Row row;
	private int rowInt;
	private int diameterArrayIndex;
	private boolean diameterMatch;
	private int length;

	public ProductFileWorker(String path, HashMap<Integer, ReinforcementProduct> reinforcementProductHashMap) {
		this.path = path;
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
		rowInt = 2;
		while (!isRowEmpty(sheet.getRow(rowInt)) && !isCellEmpty(sheet.getRow(rowInt).getCell(0))) {
			readRow();
			rowInt++;
		}
		Log.add(getClass() + ": Thread complete");
		Main.addNotification("☻ Список изделий прочитан до строки с номером: " + rowInt + " (включительно)");
	}

	private void readRow() {
		row = sheet.getRow(rowInt);
		int position = parseIntFromNumber(row.getCell(0));
		if (reinforcementProductHashMap.containsKey(position)) {
			Main.addNotification("В строке " + (rowInt + 1) + " продублированна позиция: " + position);
		}
		if (StandardsRepository.contains(StandardsRepository.reservedPosition, position)) {
			Main.addNotification("В строке " + (rowInt + 1) + " позиция из зарезервированного списка: " + position);
		}
		reinforcementProductHashMap.put(position, new ReinforcementProduct(position,
				checkDiameter(parseIntFromNumber(row.getCell(2))),
				checkRFClass(row.getCell(3).getStringCellValue()),
				checkMaxLength(parseIntFromNumber(row.getCell(4))),
				checkMass(row.getCell(5).getNumericCellValue())
		));
		Log.add(getClass() + ": [rowInt: " + rowInt + "]");
		Log.add(reinforcementProductHashMap.get(position).toString());
	}

	private int checkDiameter(int diameter) {
		diameterMatch = false;
		for (int i = 0; i < StandardsRepository.diameter.length; i++) {
			if (diameter == StandardsRepository.diameter[i]) {
				diameterMatch = true;
				diameterArrayIndex = i;
				return diameter;
			}
		}
		Main.addNotification("В строке " + (rowInt + 1) + " диаметр равен: " + diameter);
		return diameter;
	}

	private RFClass checkRFClass(String string) {
		for (int i = 0; i < StandardsRepository.rfClass500S.length; i++) {
			if (string.equalsIgnoreCase(StandardsRepository.rfClass500S[i])) {
				return RFClass.A500S;
			}
		}
		for (int i = 0; i < StandardsRepository.rfClass240.length; i++) {
			if (string.equalsIgnoreCase(StandardsRepository.rfClass240[i])) {
				return RFClass.A240;
			}
		}
		for (int i = 0; i < StandardsRepository.rfClass500.length; i++) {
			if (string.equalsIgnoreCase(StandardsRepository.rfClass500[i])) {
				return RFClass.A500;
			}
		}
		for (int i = 0; i < StandardsRepository.rfClass400.length; i++) {
			if (string.equalsIgnoreCase(StandardsRepository.rfClass400[i])) {
				return RFClass.A400;
			}
		}
		for (int i = 0; i < StandardsRepository.rfClass600.length; i++) {
			if (string.equalsIgnoreCase(StandardsRepository.rfClass600[i])) {
				return RFClass.A600;
			}
		}
		Main.addNotification("В строке " + (rowInt + 1) + " класс арматуры: " + string);
		return RFClass.MISS_VALUE;
	}

	private int checkMaxLength(int length) {
		if (length > StandardsRepository.maxLength) {
			Main.addNotification("В строке " + (rowInt + 1) + " длина изделия: " + length);
		}
		this.length = length;
		return length;
	}

	private double checkMass(double mass) {
		if (diameterMatch) {
			double mass1 = length / 1000.0 * StandardsRepository.mass3Digit[diameterArrayIndex];
			double mass2 = length / 1000.0 * StandardsRepository.mass2Digit1[diameterArrayIndex];
			double mass3 = length / 1000.0 * StandardsRepository.mass2Digit2[diameterArrayIndex];
			boolean match1 = mass1 - mass < 0.01 || mass1 - mass < -0.01;
			boolean match2 = mass2 - mass < 0.01 || mass2 - mass < -0.01;
			boolean match3 = mass2 - mass < 0.01 || mass3 - mass < -0.01;
			if (!match1 && !match2 && !match3) {
				Main.addNotification("В строке " + (rowInt + 1) + " масса изделия: " + mass + ". Я думаю, должно быть: " + mass1);
			}
		}
		return mass;
	}
}