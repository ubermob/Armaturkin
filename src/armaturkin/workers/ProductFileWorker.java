package armaturkin.workers;

import armaturkin.core.Main;
import armaturkin.interfaces.CellEmptyChecker;
import armaturkin.interfaces.ParseInt;
import armaturkin.interfaces.RowEmptyChecker;
import armaturkin.reinforcement.ProductReinforcementChecker;
import armaturkin.reinforcement.RFClass;
import armaturkin.reinforcement.ReinforcementProduct;
import armaturkin.reinforcement.StandardsRepository;
import armaturkin.utils.ParsedRange;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import utools.stopwatch.Stopwatch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class ProductFileWorker implements Runnable, CellEmptyChecker, RowEmptyChecker, ParseInt {

	private final String path;
	private final HashMap<Integer, ReinforcementProduct> reinforcementProductHashMap;
	private final ParsedRange parsedRange;
	private Workbook workbook;
	private Sheet sheet;
	private Row row;
	private ProductReinforcementChecker checker;
	private int position;
	private int diameter;
	private String rfClass;
	private int length;
	private double mass;
	private int rowIndex;
	private Stopwatch stopwatch;

	public ProductFileWorker(String path, HashMap<Integer, ReinforcementProduct> reinforcementProductHashMap
			, ParsedRange parsedRange) {
		this.path = path;
		this.reinforcementProductHashMap = reinforcementProductHashMap;
		this.parsedRange = parsedRange;
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
		rowIndex = 2;
		while (!isRowEmpty(sheet.getRow(rowIndex)) && !isCellEmpty(sheet.getRow(rowIndex).getCell(0))) {
			readRow();
			rowIndex++;
		}
		Main.app.addNotification(Main.app.getProperty("file_successfully_read_1").formatted(rowIndex));
		Main.app.log(Main.app.getProperty("thread_complete").formatted(getClass(), stopwatch.getElapsedTime()));
	}

	private void readRow() {
		row = sheet.getRow(rowIndex);
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
		parsedRange.write(position);
		Main.app.log(Main.app.getProperty("current_row").formatted(getClass(), rowIndex));
		Main.app.log(reinforcementProductHashMap.get(position).toString());
	}

	private void checkPosition(int position) {
		if (reinforcementProductHashMap.containsKey(position)) {
			Main.app.addNotification(Main.app.getProperty("position_notification_1").formatted((rowIndex + 1), position));
		}
		if (StandardsRepository.contains(StandardsRepository.RESERVED_POSITIONS, position)) {
			Main.app.addNotification(Main.app.getProperty("position_notification_2").formatted((rowIndex + 1), position));
		}
		if (position <= 0) {
			Main.app.addNotification(Main.app.getProperty("position_notification_3").formatted((rowIndex + 1), position));
		}
	}

	private void checkDiameter() {
		checker.checkDiameter();
		if (!checker.isCorrectDiameter()) {
			Main.app.addNotification(Main.app.getProperty("diameter_notification").formatted((rowIndex + 1), diameter));
		}
	}

	private void checkRFClass() {
		if (!checker.isCorrectRFClass()) {
			Main.app.addNotification(Main.app.getProperty("rf_class_notification").formatted((rowIndex + 1), rfClass));
		}
	}

	private void checkLength() {
		if (!checker.isCorrectLength()) {
			Main.app.addNotification(Main.app.getProperty("length_notification").formatted((rowIndex + 1), length));
		}
	}

	private void checkMass() {
		checker.checkMass(mass);
		if (!checker.isCorrectMass()) {
			Main.app.addNotification(Main.app.getProperty("mass_notification").formatted((rowIndex + 1), mass, checker.getCorrectMass()));
		}
	}
}