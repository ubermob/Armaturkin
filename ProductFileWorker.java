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
	private boolean realDiameter;
	private int length;

	public ProductFileWorker(String path, HashMap<Integer, ReinforcementProduct> reinforcementProductHashMap) {
		this.path = path;
		this.reinforcementProductHashMap = reinforcementProductHashMap;
	}

	@Override
	public void run() {
		Log.add(Main.properties.getProperty("threadStart").formatted(getClass()));
		Log.add(Main.properties.getProperty("threadFile").formatted(getClass(), path));
		try {
			workbook = WorkbookFactory.create(Files.newInputStream(Path.of(path)));
		} catch (IOException e) {
			Log.add(e);
		}
		sheet = workbook.getSheetAt(0);
		rowInt = 2;
		while (!isRowEmpty(sheet.getRow(rowInt)) && !isCellEmpty(sheet.getRow(rowInt).getCell(0))) {
			readRow();
			rowInt++;
		}
		Log.add(Main.properties.getProperty("threadComplete").formatted(getClass()));
		Main.addNotification(Main.properties.getProperty("fileSuccessfullyRead1").formatted(rowInt));
	}

	private void readRow() {
		row = sheet.getRow(rowInt);
		int position = parseIntFromNumber(row.getCell(0));
		checkPosition(position);
		reinforcementProductHashMap.put(position, new ReinforcementProduct(position,
				writeAndCheckDiameter(parseIntFromNumber(row.getCell(2))),
				writeAndCheckRFClass(row.getCell(3).getStringCellValue()),
				writeAndCheckMaxLength(parseIntFromNumber(row.getCell(4))),
				writeAndCheckMass(row.getCell(5).getNumericCellValue())
		));
		Log.add(Main.properties.getProperty("currentRow").formatted(getClass(), rowInt));
		Log.add(reinforcementProductHashMap.get(position).toString());
	}

	private void checkPosition(int position) {
		if (reinforcementProductHashMap.containsKey(position)) {
			Main.addNotification(Main.properties.getProperty("positionNotification1").formatted((rowInt + 1), position));
		}
		if (StandardsRepository.contains(StandardsRepository.reservedPosition, position)) {
			Main.addNotification(Main.properties.getProperty("positionNotification2").formatted((rowInt + 1), position));
		}
		if (position <= 0) {
			Main.addNotification(Main.properties.getProperty("positionNotification3").formatted((rowInt + 1), position));
		}
		if (position > StandardsRepository.maxPosition) {
			Main.addNotification(Main.properties.getProperty("positionNotification4").formatted((rowInt + 1), position));
		}
	}

	private int writeAndCheckDiameter(int diameter) {
		realDiameter = false;
		for (int i = 0; i < StandardsRepository.diameter.length; i++) {
			if (diameter == StandardsRepository.diameter[i]) {
				realDiameter = true;
				diameterArrayIndex = i;
				return diameter;
			}
		}
		Main.addNotification(Main.properties.getProperty("diameterNotification").formatted((rowInt + 1), diameter));
		return diameter;
	}

	private RFClass writeAndCheckRFClass(String string) {
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
		Main.addNotification(Main.properties.getProperty("rfClassNotification").formatted((rowInt + 1), string));
		return RFClass.MISS_VALUE;
	}

	private int writeAndCheckMaxLength(int length) {
		if (length > StandardsRepository.maxLength || length <= 0) {
			Main.addNotification(Main.properties.getProperty("lengthNotification").formatted((rowInt + 1), length));
		}
		this.length = length;
		return length;
	}

	private double writeAndCheckMass(double mass) {
		if (realDiameter) {
			double mass1 = length / 1000.0 * StandardsRepository.mass3Digit[diameterArrayIndex];
			double mass2 = length / 1000.0 * StandardsRepository.mass2Digit1[diameterArrayIndex];
			double mass3 = length / 1000.0 * StandardsRepository.mass2Digit2[diameterArrayIndex];
			boolean match1 = Math.abs(mass1 - mass) < 0.01;
			boolean match2 = Math.abs(mass2 - mass) < 0.01;
			boolean match3 = Math.abs(mass3 - mass) < 0.01;
			if (!match1 && !match2 && !match3) {
				Main.addNotification(Main.properties.getProperty("massNotification").formatted((rowInt + 1), mass, mass1));
			}
		}
		return mass;
	}
}