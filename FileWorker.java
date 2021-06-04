import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class FileWorker implements Runnable {

	private final String path;
	private final HashMap<Integer, Reinforcement> reinforcementHashMap;
	private final HashMap<Integer, ReinforcementProduct> reinforcementProductHashMap;
	private Workbook workbook;
	private Sheet sheet;
	private Row row;
	private int rowInt;

	FileWorker(String path, HashMap<Integer, Reinforcement> reinforcementHashMap, HashMap<Integer, ReinforcementProduct> reinforcementProductHashMap) {
		this.path = path;
		this.reinforcementHashMap = reinforcementHashMap;
		this.reinforcementProductHashMap = reinforcementProductHashMap;
	}

	@Override
	public void run() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("name");
		var row = sheet.createRow(0);
		row.createCell(0).setCellValue("hello");
		row.createCell(1).setCellValue(1);
		row.createCell(2).setCellValue(String.valueOf(2));

		LocalDateTime localDateTime = LocalDateTime.now();
		String fileName = localDateTime.format(DateTimeFormatter.ofPattern("HH-mm-ss dd-MM-yyyy")) + ".xlsx";
		String parentPath = Path.of(Main.pathToCalculatingFile).getParent().toString();
		try (OutputStream outputStream = Files.newOutputStream(Path.of(parentPath, fileName))) {
			workbook.write(outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}