import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.*;

public class ProductFileWorker implements Runnable, CellEmptyChecker, RowEmptyChecker, ParseInt, Stopwatch {

	private final String path;
	private final HashMap<Integer, ReinforcementProduct> reinforcementProductHashMap;
	private Workbook workbook;
	private Sheet sheet;
	private Row row;
	ProductReinforcementChecker checker;
	private int position;
	private int diameter;
	private String rfClass;
	private int length;
	private double mass;
	private int rowInt;
	private long millis;

	public ProductFileWorker(String path, HashMap<Integer, ReinforcementProduct> reinforcementProductHashMap) {
		this.path = path;
		this.reinforcementProductHashMap = reinforcementProductHashMap;
	}

	@Override
	public void run() {
		millis  = getStartTime();
		Main.log.add(Main.properties.getProperty("threadStart").formatted(getClass()));
		Main.log.add(Main.properties.getProperty("threadFile").formatted(getClass(), path));
		try {
			workbook = WorkbookFactory.create(Files.newInputStream(Path.of(path)));
		} catch (IOException e) {
			Main.log.add(e);
		}
		sheet = workbook.getSheetAt(0);
		rowInt = 2;
		while (!isRowEmpty(sheet.getRow(rowInt)) && !isCellEmpty(sheet.getRow(rowInt).getCell(0))) {
			readRow();
			rowInt++;
		}
		Main.addNotification(Main.properties.getProperty("fileSuccessfullyRead1").formatted(rowInt));
		Main.log.add(Main.properties.getProperty("threadComplete").formatted(getClass(), getStopwatch(millis)));
	}

	private void readRow() {
		row = sheet.getRow(rowInt);
		position = parseIntFromNumber(row.getCell(0));
		checkPosition(position);
		diameter = parseIntFromNumber(row.getCell(2));
		rfClass = row.getCell(3).getStringCellValue();
		length = parseIntFromNumber(row.getCell(4));
		mass = row.getCell(5).getNumericCellValue();
		checker = new ProductReinforcementChecker(new ReinforcementProduct(position, diameter, RFClass.parseRFClass(rfClass), length, mass));
		checkDiameter();
		checkRFClass();
		checkLength();
		checkMass();
		reinforcementProductHashMap.put(position, checker.getReinforcementProduct());
		Main.log.add(Main.properties.getProperty("currentRow").formatted(getClass(), rowInt));
		Main.log.add(reinforcementProductHashMap.get(position).toString());
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

	private void checkDiameter() {
		checker.checkDiameter();
		if (!checker.isCorrectDiameter()) {
			Main.addNotification(Main.properties.getProperty("diameterNotification").formatted((rowInt + 1), diameter));
		}
	}

	private void checkRFClass() {
		if (!checker.isCorrectRFClass()) {
			Main.addNotification(Main.properties.getProperty("rfClassNotification").formatted((rowInt + 1), rfClass));
		}
	}

	private void checkLength() {
		if (!checker.isCorrectLength()) {
			Main.addNotification(Main.properties.getProperty("lengthNotification").formatted((rowInt + 1), length));
		}
	}

	private void checkMass() {
		checker.checkMass(mass);
		if (!checker.isCorrectMass()) {
			Main.addNotification(Main.properties.getProperty("massNotification").formatted((rowInt + 1), mass, checker.getCorrectMass()));
		}
	}
}