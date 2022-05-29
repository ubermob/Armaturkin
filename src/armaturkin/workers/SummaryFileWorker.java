package armaturkin.workers;

import armaturkin.core.Log;
import armaturkin.core.Main;
import armaturkin.interfaces.CellEmptyChecker;
import armaturkin.interfaces.FileHashCode;
import armaturkin.interfaces.RowEmptyChecker;
import armaturkin.reinforcement.*;
import armaturkin.summaryoutput.ReportBundle;
import armaturkin.summaryoutput.SummaryThreadPool;
import armaturkin.summaryoutput.TableHeaderResult;
import armaturkin.utils.MassCounter;
import armaturkin.utils.RegEx;
import org.apache.poi.ss.usermodel.*;
import utools.stopwatch.Stopwatch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class SummaryFileWorker implements Runnable, CellEmptyChecker, RowEmptyChecker, FileHashCode {

	private final String path;
	private final HashMap<Integer, ReinforcementLiteInfo> hashMap;
	private final int labelID;
	private final Log log;
	private final int set;
	private String fileHashCode;
	private Workbook workbook;
	private Sheet sheet;
	private Row row;
	private int diameter;
	private String rfClassString;
	private RFClass rfClass;
	private double mass;
	private int hashCode;
	private ProductReinforcementChecker checker;
	private int position;
	private int length;
	private int majorNumber;
	private double singleMass;
	private int minorNumber;
	private double totalLength;
	private int rowIndex;
	private int diameterAndRfClassColumn;
	private int massColumn;
	private double productMass;
	private int positionColumn;
	private int lengthColumn;
	private int singleMassColumn;
	private int totalLengthColumn;
	private int minorNumberColumn;
	private int productMassColumn;
	private String notification;
	private MassCounter massCounter;
	private Stopwatch stopwatch;
	private ReportBundle reportBundle;

	public SummaryFileWorker(String path, HashMap<Integer, ReinforcementLiteInfo> hashMap, int labelID, Log log, int set) {
		this.path = path;
		this.labelID = labelID;
		this.hashMap = hashMap;
		this.log = log;
		this.set = set;
		reportBundle = new ReportBundle();
		setup();
	}

	@Override
	public void run() {
		stopwatch = new Stopwatch();
		fileHashCode = getFileHashCode(path);
		log.add(Main.app.getProperty("summary_thread_start").formatted(getClass(), labelID, path, fileHashCode));
		try {
			workbook = WorkbookFactory.create(Files.newInputStream(Path.of(path)));
			sheet = workbook.getSheetAt(0);
		} catch (IOException e) {
			log.add(e);
		}
		findStartRow();
		if (set == SummaryThreadPool.RAW) {
			majorNumber = RegEx.parseNumberOfElements(sheet.getRow(4).getCell(0).getStringCellValue());
			checkMajorNumber(majorNumber);
			massCounter = new MassCounter();
		}
		while (!isRowEmpty(sheet.getRow(rowIndex)) &&
				!isCellEmpty(sheet.getRow(rowIndex).getCell(diameterAndRfClassColumn)) &&
				!isCellEmpty(sheet.getRow(rowIndex).getCell(massColumn))) {
			readRow();
			rowIndex++;
		}
		if (set == SummaryThreadPool.RAW) {
			productMass = sheet.getRow(4).getCell(productMassColumn).getNumericCellValue();
			if (Math.abs(massCounter.getValue() - productMass) >= 0.01) {
				Main.app.addNotification(Main.app.getProperty("product_mass_multiply_notification").formatted(
						path,
						productMass,
						massCounter.getValue()
				));
			}
		}
		Main.app.addNotification(Main.app.getProperty("file_successfully_read_3").formatted(
				path
				, reportBundle.getParsedRange().rangeAsExcelRowNumbers()
		));
		String cellValueListAsString = reportBundle.getTableHeaderResult().getCellValueListAsString();
		if (!reportBundle.getTableHeaderResult().isSince1()) {
			Main.app.addNotification(Main.app.getProperty("summary_head_numeric_line_warning").formatted(
					cellValueListAsString
			));
		}
		log.add(Main.app.getProperty("summary_thread_complete").formatted(
				getClass()
				, stopwatch.getElapsedTime()
				, labelID
				, path
				, fileHashCode
				, cellValueListAsString
				, reportBundle.getParsedRange().rangeAsIndexes()
		));
	}

	private void readRow() {
		row = sheet.getRow(rowIndex);
		reportBundle.getParsedRange().write(row.getRowNum());
		String[] parsedString = row.getCell(diameterAndRfClassColumn).getStringCellValue().split(" ");
		diameter = Integer.parseInt(parsedString[0].split("%%C")[1]);
		rfClassString = parsedString[1];
		rfClass = RFClass.parseRFClass(rfClassString);
		mass = row.getCell(massColumn).getNumericCellValue();
		hashCode = RfHashCode.getHashCode(diameter, rfClass);
		if (set == SummaryThreadPool.RAW || set == SummaryThreadPool.RAW_STAIRWAY) {
			position = (int) row.getCell(positionColumn).getNumericCellValue();
			checkPosition();
			length = (int) row.getCell(lengthColumn).getNumericCellValue();
			singleMass = row.getCell(singleMassColumn).getNumericCellValue();
			mass = row.getCell(massColumn).getNumericCellValue();
			minorNumber = (int) row.getCell(minorNumberColumn).getNumericCellValue();
			totalLength = row.getCell(totalLengthColumn).getNumericCellValue();
			checker = new ProductReinforcementChecker(new ReinforcementProduct(position, diameter, rfClass, length, singleMass));
			checkDiameter();
			checkRFClass();
			checkSingleLength();
			checkSingleMass();
			checkMultiply();
			massCounter.add(mass);
			if (set == SummaryThreadPool.RAW) {
				mass *= majorNumber;
			}
		}
		synchronized (this) {
			if (hashMap.containsKey(hashCode)) {
				hashMap.get(hashCode).addMass(mass);
			} else {
				hashMap.put(hashCode, new ReinforcementLiteInfo(diameter, rfClass, mass));
			}
		}
		log.add(Main.app.getProperty("summary_thread_read_row").formatted(
				getClass(), labelID, fileHashCode, rowIndex, hashMap.get(hashCode).toString())
		);
	}

	private void checkMajorNumber(int number) {
		if (number <= 0) {
			Main.app.addNotification(Main.app.getProperty("product_major_number_notification").formatted(path));
		}
	}

	private void checkPosition() {
		if (position <= 0) {
			notification = Main.app.getProperty("position_notification_3").formatted((rowIndex + 1), position);
			Main.app.addNotification(Main.app.getProperty("summary_file_name_notification").formatted(path, notification));
		}
	}

	private void checkDiameter() {
		checker.checkDiameter();
		if (!checker.isCorrectDiameter()) {
			notification = Main.app.getProperty("diameter_notification").formatted((rowIndex + 1), diameter);
			Main.app.addNotification(Main.app.getProperty("summary_file_name_notification").formatted(path, notification));
		}
	}

	private void checkRFClass() {
		if (!checker.isCorrectRFClass()) {
			notification = Main.app.getProperty("rf_class_notification").formatted((rowIndex + 1), rfClass);
			Main.app.addNotification(Main.app.getProperty("summary_file_name_notification").formatted(path, notification));
		}
	}

	private void checkSingleLength() {
		if (!checker.isCorrectLength()) {
			notification = Main.app.getProperty("length_notification").formatted((rowIndex + 1), length);
			Main.app.addNotification(Main.app.getProperty("summary_file_name_notification").formatted(path, notification));
		}
	}

	private void checkSingleMass() {
		checker.checkMass(singleMass);
		if (!checker.isCorrectMass()) {
			notification = Main.app.getProperty("mass_notification").formatted((rowIndex + 1), singleMass
					, checker.getCorrectMass());
			Main.app.addNotification(Main.app.getProperty("summary_file_name_notification").formatted(path, notification));
		}
	}

	private void checkMultiply() {
		if ((Math.abs(length / 1000.0 * minorNumber) - totalLength) >= 0.01) {
			Main.app.addNotification(Main.app.getProperty("length_multiply_notification").formatted(
					path,
					(rowIndex + 1),
					totalLength,
					(length / 1000.0 * minorNumber)
			));
		}
		ProductReinforcementChecker checker2 = new ProductReinforcementChecker(new ReinforcementProduct(
				0,
				diameter,
				rfClass,
				(int) ((length / 1000.0 * minorNumber) * 1000),
				mass
		));
		checker2.checkDiameter();
		checker2.checkMass(mass);
		if (!checker2.isCorrectMass()) {
			Main.app.addNotification(Main.app.getProperty("mass_multiply_notification").formatted(
					path,
					(rowIndex + 1),
					mass,
					checker2.getCorrectMass()
			));
		}
	}

	private void setup() {
		if (set == SummaryThreadPool.PRETTY) {
			diameterAndRfClassColumn = 2;
			massColumn = 7;
		}
		if (set == SummaryThreadPool.RAW) {
			diameterAndRfClassColumn = 3;
			massColumn = 8;
			positionColumn = 1;
			lengthColumn = 4;
			singleMassColumn = 7;
			minorNumberColumn = 5;
			totalLengthColumn = 6;
			productMassColumn = 9;
		}
		if (set == SummaryThreadPool.RAW_STAIRWAY) {
			diameterAndRfClassColumn = 2;
			massColumn = 7;
			positionColumn = 0;
			lengthColumn = 3;
			singleMassColumn = 6;
			minorNumberColumn = 4;
			totalLengthColumn = 5;
			productMassColumn = 8;
		}
	}

	private void findStartRow() {
		rowIndex = findStartRow(sheet) + 1;
	}

	private int findStartRow(Sheet sheet) {
		log.add("\tFinding start row");
		for (Row row : sheet) {
			TableHeaderResult result = rowValueIsStandart(row);
			reportBundle.setTableHeaderResult(result);
			if (result.isSince1()) {
				log.add("\tStandart style: CORRECT");
			}
			if (result.isSince2()) {
				log.add("\tNovosibirsk style: INCORRECT");
			}
			if (result.isNumeric() && !result.isSince1() && !result.isSince2()) {
				log.add("\tOther style %s: INCORRECT".formatted(result.getCellValueListAsString()));
			}
			if (result.isNumeric() && result.isSince1()) {
				return row.getRowNum();
			}
			if (result.isNumeric() && result.isSince2()) {
				return row.getRowNum();
			}
		}
		return 0;
	}

	private TableHeaderResult rowValueIsStandart(Row row) {
		TableHeaderResult result = new TableHeaderResult();
		for (int i = 0; i < 5; i++) {
			try {
				if (row.getCell(i).getCellType() != CellType.NUMERIC) {
					result.setNumeric(false);
				}
			} catch (Exception e) {
				result.setNumeric(false);
			}
			try {
				// Sample: [1, 2, 3, 4, ...]
				if ((int) row.getCell(i).getNumericCellValue() != (i + 1)) {
					result.setSince1(false);
				}
			} catch (Exception e) {
				result.setSince1(false);
			}
			try {
				// Sample: [2, 3, 4, 5, ...]
				if ((int) row.getCell(i).getNumericCellValue() != (i + 2)) {
					result.setSince2(false);
				}
			} catch (Exception e) {
				result.setSince2(false);
			}
		}
		if (result.isNumeric()) {
			for (Cell v : row) {
				result.addCellValueToList(v.getNumericCellValue());
			}
		}
		return result;
	}
}