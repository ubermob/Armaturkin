import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class SummaryFileWorker implements Runnable, Stopwatch, CellEmptyChecker, RowEmptyChecker, RHashCode, FileHashCode {

	private final String path;
	private final HashMap<Integer, ReinforcementLiteInfo> hashMap;
	private final int labelID;
	private final Log log;
	private String fileHashCode;
	private Workbook workbook;
	private Sheet sheet;
	private int rowInt = 4;
	private final int diameterAndRfClassColumn = 2;
	private final int massColumn = 7;
	private long millis;

	public SummaryFileWorker(String path, HashMap<Integer, ReinforcementLiteInfo> hashMap, int labelID, Log log) {
		this.path = path;
		this.labelID = labelID;
		this.hashMap = hashMap;
		this.log = log;
	}

	@Override
	public void run() {
		millis = getStartTime();
		fileHashCode = getFileHashCode(path);
		log.add(Main.properties.getProperty("summaryThreadStart").formatted(getClass(), labelID, path, fileHashCode));
		try {
			workbook = WorkbookFactory.create(Files.newInputStream(Path.of(path)));
			sheet = workbook.getSheetAt(0);
		} catch (IOException e) {
			log.add(e);
		}
		while (!isRowEmpty(sheet.getRow(rowInt)) &&
				!isCellEmpty(sheet.getRow(rowInt).getCell(diameterAndRfClassColumn)) &&
				!isCellEmpty(sheet.getRow(rowInt).getCell(massColumn))) {
			readRow();
			rowInt++;
		}
		log.add(Main.properties.getProperty("summaryThreadComplete").formatted(getClass(), getStopwatch(millis), labelID, path, fileHashCode));
	}

	void readRow() {
		Row row = sheet.getRow(rowInt);
		String[] parsedString = row.getCell(diameterAndRfClassColumn).getStringCellValue().split(" ");
		int diameter = Integer.parseInt(parsedString[0].split("%%C")[1]);
		RFClass rfClass = RFClass.parseRFClass(parsedString[1]);
		double mass = row.getCell(massColumn).getNumericCellValue();
		int hashCode = getHashCode(diameter, rfClass);
		if (hashMap.containsKey(hashCode)) {
			hashMap.get(hashCode).addMass(mass);
		} else {
			hashMap.put(hashCode, new ReinforcementLiteInfo(diameter, rfClass, mass));
		}
		log.add(Main.properties.getProperty("summaryThreadReadRow").formatted(
				getClass(), labelID, fileHashCode, rowInt, hashMap.get(hashCode).toString())
		);
	}
}
