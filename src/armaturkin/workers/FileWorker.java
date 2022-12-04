package armaturkin.workers;

import armaturkin.core.Main;
import armaturkin.interfaces.FileNameCreator;
import armaturkin.reinforcement.RFClass;
import armaturkin.reinforcement.Reinforcement;
import armaturkin.reinforcement.StandardsRepository;
import armaturkin.utils.CellStyleRepository;
import armaturkin.utils.ParsedRange;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utools.stopwatch.Stopwatch;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Formatter;
import java.util.HashMap;

public class FileWorker implements Runnable, FileNameCreator {

	private final String path;
	private final HashMap<Integer, Reinforcement> reinforcementHashMap;
	private final ParsedRange parsedRange;
	private final String downloadFileTableHead;
	private String fileName;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private XSSFRow row;
	private XSSFCell cell;
	private int rowIndex;
	private CellStyleRepository cellStyleRepository;
	private Stopwatch stopwatch;

	public FileWorker(String path, HashMap<Integer, Reinforcement> reinforcementHashMap
			, ParsedRange parsedRange, String downloadFileTableHead, String fileName) {
		this.path = path;
		this.reinforcementHashMap = reinforcementHashMap;
		this.parsedRange = parsedRange;
		this.downloadFileTableHead = downloadFileTableHead;
		this.fileName = fileName;
	}

	@Override
	public void run() {
		stopwatch = new Stopwatch();
		Main.app.log(Main.app.getProperty("thread_start").formatted(getClass()));
		initWorkbook();
		buildTableHead();
		addBackgroundReinforcement();
		fillTable();
		fileName = createFileName(fileName, ".xlsx");
		try (OutputStream outputStream = Files.newOutputStream(Path.of(path, fileName))) {
			workbook.write(outputStream);
			Main.app.addNotification(Main.app.getProperty("file_successfully_download").formatted(fileName));
			Main.app.log(Main.app.getProperty("file_download").formatted(getClass(), fileName, path));
		} catch (Exception e) {
			Main.app.addNotification(Main.app.getProperty("excel_creation_exception").formatted(fileName));
			Main.app.log(e);
		}
		Main.app.log(Main.app.getProperty("thread_complete").formatted(getClass(), stopwatch.getElapsedTime()));
	}

	private void fillTable() {
		rowIndex = 4;
		for (int i = parsedRange.getStartValue(); i <= parsedRange.getLastValue(); i++) {
			if (reinforcementHashMap.containsKey(i)) {
				Reinforcement reinforcement = reinforcementHashMap.get(i);
				row = sheet.createRow(rowIndex);
				row.setHeight((short) 675);
				for (int j = 0; j < 8; j++) {
					cell = row.createCell(j);
					cell.setCellStyle(cellStyleRepository.getCellStyle(reinforcement.getDiameter()));
				}
				cell = row.getCell(5);
				cell.setCellStyle(cellStyleRepository.getCellStyleWithFormat(
						reinforcement.getDiameter(), workbook.createDataFormat().getFormat("0.00")
				));
				cell = row.getCell(7);
				cell.setCellStyle(cellStyleRepository.getCellStyleWithFormat(
						reinforcement.getDiameter(), workbook.createDataFormat().getFormat("0.0")
				));
				if (reinforcement.isLinear()) {
					cell = row.getCell(0);
					cell.setCellValue(reinforcement.getPosition());
					cell = row.getCell(2);
					cell.setCellValue("%%C" + reinforcement.getDiameter() + " " + RFClass.toString(reinforcement.getRfClass()));
					cell = row.getCell(3);
					cell.setCellValue("-");
					cell = row.getCell(4);
					cell.setCellValue("-");
					cell = row.getCell(5);
					cell.setCellValue(reinforcement.getLength() / 1000.0);
					cell = row.getCell(6);
					cell.setCellValue("-");
					cell = row.getCell(7);
					cell.setCellValue(reinforcement.getMass());
					Formatter formatter = new Formatter();
					formatter.format(
							getClass() + " write excel row: [%d],[%d %s],[-],[-],[%f],[-],[%f]",
							reinforcement.getPosition(),
							reinforcement.getDiameter(),
							RFClass.toString(reinforcement.getRfClass()),
							reinforcement.getLength() / 1000.0,
							reinforcement.getMass()
					);
					Main.app.log(formatter.toString());
				}
				if (!reinforcement.isLinear()) {
					cell = row.getCell(0);
					cell.setCellValue(reinforcement.getPosition());
					cell = row.getCell(2);
					cell.setCellValue("%%C" + reinforcement.getDiameter() + " " + RFClass.toString(reinforcement.getRfClass()));
					cell = row.getCell(3);
					cell.setCellValue(reinforcement.getLength());
					cell = row.getCell(4);
					cell.setCellValue(reinforcement.getNumber());
					cell = row.getCell(5);
					cell.setCellValue(reinforcement.getLength() * reinforcement.getNumber() / 1000.0);
					cell = row.getCell(6);
					cell.setCellValue(reinforcement.getMass());
					cell.setCellStyle(cellStyleRepository.getCellStyleWithFormat(
							reinforcement.getDiameter(), workbook.createDataFormat().getFormat("0.00")
					));
					cell = row.getCell(7);
					cell.setCellValue(reinforcement.getMass() * reinforcement.getNumber());
					Formatter formatter = new Formatter();
					formatter.format(
							getClass() + " write excel row: [%d],[%d %s],[%d],[%d],[%f],[%f],[%f]",
							reinforcement.getPosition(),
							reinforcement.getDiameter(),
							RFClass.toString(reinforcement.getRfClass()),
							reinforcement.getLength(),
							reinforcement.getNumber(),
							reinforcement.getLength() * reinforcement.getNumber() / 1000.0,
							reinforcement.getMass(),
							reinforcement.getMass() * reinforcement.getNumber()
					);
					Main.app.log(formatter.toString());
				}
				rowIndex++;
			} else {
				Main.app.log("Reinforcement hash map do not contain key: " + i);
			}
		}
	}

	private void addBackgroundReinforcement() {
		for (var entry : Main.app.getManuallyEntryModel().getBackgroundReinforcementManuallyEntries()) {
			int diameter = entry.getDiameter();
			double length = entry.getParsedValue();
			int lengthInt = (int) (length * 1000);
			RFClass rfClass = entry.getRfClass();
			Main.app.log(getClass() + " parse background reinforcement: [diameter: " + diameter + "]" +
					",[length: " + length + "],[lengthInt: " + lengthInt + "]"
			);
			int reservedDiameterIndex = StandardsRepository.getReservedDiameterIndex(diameter);
			if (reservedDiameterIndex == -1) {
				Main.app.log(getClass() + " do not found [diameter: " + diameter + "] in Repository");
				Main.app.addNotification("Указанного диаметра: " + diameter +
						" нет в зарезервированном списке диаметров (class Repository)"
				);
			} else {
				int position = StandardsRepository.RESERVED_POSITIONS[reservedDiameterIndex];
				if (reinforcementHashMap.containsKey(position)) {
					Reinforcement calculatedReinforcement = reinforcementHashMap.get(position);
					Reinforcement currentReinforcement = new Reinforcement(
							position,
							diameter,
							rfClass,
							lengthInt,
							StandardsRepository.getMass(diameter) * lengthInt / 1000
					);
					reinforcementHashMap.put(position, new Reinforcement(
							position,
							currentReinforcement.getDiameter(),
							currentReinforcement.getRfClass(),
							calculatedReinforcement.getLength() + currentReinforcement.getLength(),
							calculatedReinforcement.getMass() + currentReinforcement.getMass()
					));
				} else {
					reinforcementHashMap.put(position, new Reinforcement(
							position,
							diameter,
							rfClass,
							lengthInt,
							StandardsRepository.getMass(diameter) * lengthInt / 1000
					));
				}
				Main.app.log(reinforcementHashMap.get(position).toString());
			}
		}
	}

	private void buildTableHead() {
		cellStyleRepository = new CellStyleRepository(workbook);

		sheet.setColumnWidth(0, 1792); // Width values read from sample
		sheet.setColumnWidth(1, 4827);
		sheet.setColumnWidth(2, 3072);
		sheet.setColumnWidth(3, 3072);
		sheet.setColumnWidth(4, 1901);
		sheet.setColumnWidth(5, 1901);
		sheet.setColumnWidth(6, 2486);
		sheet.setColumnWidth(7, 3072);

		row = sheet.createRow(0);
		row.setHeight((short) 450);
		cell = row.createCell(0);
		cell.setCellValue(downloadFileTableHead);
		cell.setCellStyle(cellStyleRepository.getHeadTableNameCellStyle());
		sheet.addMergedRegion(CellRangeAddress.valueOf("A1:H1"));

		CellStyle cellStyle = cellStyleRepository.getHeadTableCellStyle();
		CellStyle cellStyleWithTextWrap = cellStyleRepository.getHeadTableCellStyleWithTextWrap();
		row = sheet.createRow(1);
		row.setHeight((short) 450);
		cell = row.createCell(0);
		cell.setCellValue(Main.app.getProperty("column_name_4"));
		cell.setCellStyle(cellStyle);
		cell = row.createCell(1);
		cell.setCellValue(Main.app.getProperty("column_name_5"));
		cell.setCellStyle(cellStyle);
		cell = row.createCell(2);
		cell.setCellValue(Main.app.getProperty("column_name_6"));
		cell.setCellStyle(cellStyleWithTextWrap);
		cell = row.createCell(3);
		cell.setCellValue(Main.app.getProperty("column_name_7"));
		cell.setCellStyle(cellStyle);
		cell = row.createCell(4);
		cell.setCellValue(Main.app.getProperty("column_name_8"));
		cell.setCellStyle(cellStyleWithTextWrap);
		cell = row.createCell(5);
		cell.setCellValue(Main.app.getProperty("column_name_9"));
		cell.setCellStyle(cellStyle);
		cell = row.createCell(6);
		cell.setCellValue(Main.app.getProperty("column_name_10"));
		cell.setCellStyle(cellStyle);
		cell = row.createCell(7);
		cell.setCellStyle(cellStyle);

		row = sheet.createRow(2);
		row.setHeight((short) 450);
		cell = row.createCell(6);
		cell.setCellValue(Main.app.getProperty("column_name_11"));
		cell.setCellStyle(cellStyleWithTextWrap);
		cell = row.createCell(7);
		cell.setCellValue(Main.app.getProperty("column_name_12"));
		cell.setCellStyle(cellStyleWithTextWrap);
		for (int i = 0; i < 6; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(cellStyle);
		}
		sheet.addMergedRegion(CellRangeAddress.valueOf("A2:A3"));
		sheet.addMergedRegion(CellRangeAddress.valueOf("B2:B3"));
		sheet.addMergedRegion(CellRangeAddress.valueOf("C2:C3"));
		sheet.addMergedRegion(CellRangeAddress.valueOf("D2:D3"));
		sheet.addMergedRegion(CellRangeAddress.valueOf("E2:E3"));
		sheet.addMergedRegion(CellRangeAddress.valueOf("F2:F3"));
		sheet.addMergedRegion(CellRangeAddress.valueOf("G2:H2"));

		row = sheet.createRow(3);
		row.setHeight((short) 330);
		for (int i = 0; i < 8; i++) {
			cell = row.createCell(i);
			cell.setCellValue(i + 1);
			cell.setCellStyle(cellStyle);
		}
	}

	private void initWorkbook() {
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet(Main.app.getProperty("default_list_name"));
		sheet.setZoom(Integer.parseInt(Main.app.getProperty("sheet_zoom_1")));
		var coreProperties = workbook.getProperties().getCoreProperties();
		coreProperties.setCreator(Main.getAppNameAndVersion());
		coreProperties.setDescription(Main.app.getProperty("summary_excel_builder_commentary")
				.formatted(Main.getAppNameAndVersion()));
	}
}