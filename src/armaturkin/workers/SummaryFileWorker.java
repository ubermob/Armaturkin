package armaturkin.workers;

import armaturkin.core.*;
import armaturkin.interfaces.*;
import armaturkin.reinforcement.*;
import armaturkin.utils.MassCounter;
import armaturkin.utils.RegEx;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
	private int rowInt = 4;
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

	public SummaryFileWorker(String path, HashMap<Integer, ReinforcementLiteInfo> hashMap, int labelID, Log log, int set) {
		this.path = path;
		this.labelID = labelID;
		this.hashMap = hashMap;
		this.log = log;
		this.set = set;
		setup();
	}

	@Override
	public void run() {
		stopwatch = new Stopwatch();
		fileHashCode = getFileHashCode(path);
		log.add(Main.properties.getProperty("summary_thread_start").formatted(getClass(), labelID, path, fileHashCode));
		try {
			workbook = WorkbookFactory.create(Files.newInputStream(Path.of(path)));
			sheet = workbook.getSheetAt(0);
		} catch (IOException e) {
			log.add(e);
		}
		if (set == SummaryThreadStarter.RAW) {
			majorNumber = RegEx.parseNumberOfElements(sheet.getRow(4).getCell(0).getStringCellValue());
			checkMajorNumber(majorNumber);
			massCounter = new MassCounter();
		}
		while (!isRowEmpty(sheet.getRow(rowInt)) &&
				!isCellEmpty(sheet.getRow(rowInt).getCell(diameterAndRfClassColumn)) &&
				!isCellEmpty(sheet.getRow(rowInt).getCell(massColumn))) {
			readRow();
			rowInt++;
		}
		if (set == SummaryThreadStarter.RAW) {
			productMass = sheet.getRow(4).getCell(productMassColumn).getNumericCellValue();
			if (Math.abs(massCounter.getValue() - productMass) >= 0.01) {
				Main.addNotification(Main.properties.getProperty("product_mass_multiply_notification").formatted(
						path,
						productMass,
						massCounter.getValue()
				));
			}
		}
		Main.addNotification(Main.properties.getProperty("file_successfully_read_3").formatted(path, rowInt));
		log.add(Main.properties.getProperty("summary_thread_complete").formatted(getClass(), stopwatch.getElapsedTime(), labelID, path, fileHashCode));
	}

	private void readRow() {
		row = sheet.getRow(rowInt);
		String[] parsedString = row.getCell(diameterAndRfClassColumn).getStringCellValue().split(" ");
		diameter = Integer.parseInt(parsedString[0].split("%%C")[1]);
		rfClassString = parsedString[1];
		rfClass = RFClass.parseRFClass(rfClassString);
		mass = row.getCell(massColumn).getNumericCellValue();
		hashCode = RfHashCode.getHashCode(diameter, rfClass);
		if (set == SummaryThreadStarter.RAW || set == SummaryThreadStarter.RAW_STAIRWAY) {
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
			if (set == SummaryThreadStarter.RAW) {
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
		log.add(Main.properties.getProperty("summary_thread_read_row").formatted(
				getClass(), labelID, fileHashCode, rowInt, hashMap.get(hashCode).toString())
		);
	}

	private void checkMajorNumber(int number) {
		if (number <= 0) {
			Main.addNotification(Main.properties.getProperty("product_major_number_notification").formatted(path));
		}
	}

	private void checkPosition() {
		if (position <= 0) {
			notification = Main.properties.getProperty("position_notification_3").formatted((rowInt + 1), position);
			Main.addNotification(Main.properties.getProperty("summary_file_name_notification").formatted(path, notification));
		}
		if (position > StandardsRepository.maxPosition) {
			notification = Main.properties.getProperty("position_notification_4").formatted((rowInt + 1), position);
			Main.addNotification(Main.properties.getProperty("summary_file_name_notification").formatted(path, notification));
		}
	}

	private void checkDiameter() {
		checker.checkDiameter();
		if (!checker.isCorrectDiameter()) {
			notification = Main.properties.getProperty("diameter_notification").formatted((rowInt + 1), diameter);
			Main.addNotification(Main.properties.getProperty("summary_file_name_notification").formatted(path, notification));
		}
	}

	private void checkRFClass() {
		if (!checker.isCorrectRFClass()) {
			notification = Main.properties.getProperty("rf_class_notification").formatted((rowInt + 1), rfClass);
			Main.addNotification(Main.properties.getProperty("summary_file_name_notification").formatted(path, notification));
		}
	}

	private void checkSingleLength() {
		if (!checker.isCorrectLength()) {
			notification = Main.properties.getProperty("length_notification").formatted((rowInt + 1), length);
			Main.addNotification(Main.properties.getProperty("summary_file_name_notification").formatted(path, notification));
		}
	}

	private void checkSingleMass() {
		checker.checkMass(singleMass);
		if (!checker.isCorrectMass()) {
			notification = Main.properties.getProperty("mass_notification").formatted((rowInt + 1), singleMass, checker.getCorrectMass());
			Main.addNotification(Main.properties.getProperty("summary_file_name_notification").formatted(path, notification));
		}
	}

	private void checkMultiply() {
		if ((Math.abs(length / 1000.0 * minorNumber) - totalLength) >= 0.01) {
			Main.addNotification(Main.properties.getProperty("length_multiply_notification").formatted(
					path,
					(rowInt + 1),
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
			Main.addNotification(Main.properties.getProperty("mass_multiply_notification").formatted(
					path,
					(rowInt + 1),
					mass,
					checker2.getCorrectMass()
			));
		}
	}

	private void setup() {
		if (set == SummaryThreadStarter.PRETTY) {
			diameterAndRfClassColumn = 2;
			massColumn = 7;
		}
		if (set == SummaryThreadStarter.RAW) {
			diameterAndRfClassColumn = 3;
			massColumn = 8;
			positionColumn = 1;
			lengthColumn = 4;
			singleMassColumn = 7;
			minorNumberColumn = 5;
			totalLengthColumn = 6;
			productMassColumn = 9;
		}
		if (set == SummaryThreadStarter.RAW_STAIRWAY) {
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
}