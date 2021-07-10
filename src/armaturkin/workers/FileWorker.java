package armaturkin.workers;

import armaturkin.core.*;
import armaturkin.interfaces.FileNameCreator;
import armaturkin.interfaces.Stopwatch;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Formatter;
import java.util.HashMap;

public class FileWorker implements Runnable, FileNameCreator, Stopwatch {

	private final String path;
	private final HashMap<Integer, Reinforcement> reinforcementHashMap;
	private final String backgroundReinforcement;
	private final String downloadFileTableHead;
	private String fileName;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private XSSFRow row;
	private XSSFCell cell;
	private int rowInt;
	private CellStyleRepository cellStyleRepository;
	private long millis;

	public FileWorker(String path, HashMap<Integer, Reinforcement> reinforcementHashMap,
	           String backgroundReinforcement, String downloadFileTableHead, String fileName) {
		this.path = path;
		this.reinforcementHashMap = reinforcementHashMap;
		this.backgroundReinforcement = backgroundReinforcement;
		this.downloadFileTableHead = downloadFileTableHead;
		this.fileName = fileName;
	}

	@Override
	public void run() {
		millis  = getStartTime();
		Main.log.add(Main.properties.getProperty("thread_start").formatted(getClass()));
		buildTableHead();
		addBackgroundReinforcement();
		fillTable();
		fileName = createFileName(fileName);
		try (OutputStream outputStream = Files.newOutputStream(Path.of(path, fileName))) {
			workbook.write(outputStream);
			Main.addNotification(Main.properties.getProperty("file_successfully_download").formatted(fileName));
			Main.log.add(Main.properties.getProperty("file_download").formatted(getClass(), fileName, path));
		} catch (Exception e) {
			Main.addNotification(Main.properties.getProperty("excel_creation_exception").formatted(fileName));
			Main.log.add(e);
		}
		Main.log.add(Main.properties.getProperty("thread_complete").formatted(getClass(), getStopwatch(millis)));
	}

	private void fillTable() {
		rowInt = 4;
		for (int i = 1; i <= StandardsRepository.maxPosition; i++) {
			if (reinforcementHashMap.containsKey(i)) {
				Reinforcement reinforcement = reinforcementHashMap.get(i);
				row = sheet.createRow(rowInt);
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
					formatter.format(getClass() + "write excel row: [%d],[%d %s],[-],[-],[%f],[-],[%f]",
							reinforcement.getPosition(), reinforcement.getDiameter(), RFClass.toString(reinforcement.getRfClass()), reinforcement.getLength() / 1000.0,
							reinforcement.getMass()
					);
					Main.log.add(formatter.toString());
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
					formatter.format(getClass() + "write excel row: [%d],[%d %s],[%d],[%d],[%f],[%f],[%f]",
							reinforcement.getPosition(), reinforcement.getDiameter(), RFClass.toString(reinforcement.getRfClass()), reinforcement.getLength(),
							reinforcement.getNumber(), reinforcement.getLength() * reinforcement.getNumber() / 1000.0, reinforcement.getMass(),
							reinforcement.getMass() * reinforcement.getNumber()
					);
					Main.log.add(formatter.toString());
				}
				rowInt++;
			}
		}
	}

	private void addBackgroundReinforcement() {
		if (backgroundReinforcement.length() != 0) {
			String[] splittedString = backgroundReinforcement.split("-");
			if (splittedString.length % 2 != 0) {
				Main.log.add(getClass() +
						" can not parse background reinforcement, because [backgroundReinforcement] variable have length is " +
						splittedString.length
				);
				Main.addNotification("Ошибка ввода фоновой арматуры");
			}
			if (splittedString.length % 2 == 0) {
				for (int i = 0; i < splittedString.length / 2; i++) {
					int diameter = Integer.parseInt(splittedString[i * 2]);
					double length = Double.parseDouble(splittedString[i * 2 + 1]);
					int lengthInt = (int) (length * 1000);
					Main.log.add(getClass() + " parse background reinforcement: [diameter: " + diameter + "]" +
							",[length: " + length + "],[lengthInt: " + lengthInt + "]"
					);
					int reservedDiameterIndex = StandardsRepository.getReservedDiameterIndex(diameter);
					if (reservedDiameterIndex == -1) {
						Main.log.add(getClass() + " do not found [diameter: " + diameter + "] in Pattern");
						Main.addNotification("Указанного диаметра: " + diameter +
								" нет в зарезервированном списке диаметров (class Pattern)"
						);
					} else {
						int position = StandardsRepository.reservedPositions[reservedDiameterIndex];
						if (reinforcementHashMap.containsKey(position)) {
							Reinforcement calculatedReinforcement = reinforcementHashMap.get(position);
							Reinforcement currentReinforcement = new Reinforcement(position,
									diameter,
									StandardsRepository.getReservedRFClass(position),
									lengthInt,
									StandardsRepository.getMass(diameter) * lengthInt / 1000
							);
							reinforcementHashMap.put(position, new Reinforcement(position,
									currentReinforcement.getDiameter(),
									currentReinforcement.getRfClass(),
									calculatedReinforcement.getLength() + currentReinforcement.getLength(),
									calculatedReinforcement.getMass() + currentReinforcement.getMass()
							));
						} else {
							reinforcementHashMap.put(position, new Reinforcement(position,
									diameter,
									StandardsRepository.getReservedRFClass(position),
									lengthInt,
									StandardsRepository.getMass(diameter) * lengthInt / 1000
							));
						}
						Main.log.add(reinforcementHashMap.get(position).toString());
					}
				}
			}
		}
	}

	private void buildTableHead() {
		workbook = new XSSFWorkbook();
		cellStyleRepository = new CellStyleRepository(workbook);
		sheet = workbook.createSheet(Main.properties.getProperty("default_list_name"));

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
		cell.setCellValue(Main.properties.getProperty("column_name_4"));
		cell.setCellStyle(cellStyle);
		cell = row.createCell(1);
		cell.setCellValue(Main.properties.getProperty("column_name_5"));
		cell.setCellStyle(cellStyle);
		cell = row.createCell(2);
		cell.setCellValue(Main.properties.getProperty("column_name_6"));
		cell.setCellStyle(cellStyleWithTextWrap);
		cell = row.createCell(3);
		cell.setCellValue(Main.properties.getProperty("column_name_7"));
		cell.setCellStyle(cellStyle);
		cell = row.createCell(4);
		cell.setCellValue(Main.properties.getProperty("column_name_8"));
		cell.setCellStyle(cellStyleWithTextWrap);
		cell = row.createCell(5);
		cell.setCellValue(Main.properties.getProperty("column_name_9"));
		cell.setCellStyle(cellStyle);
		cell = row.createCell(6);
		cell.setCellValue(Main.properties.getProperty("column_name_10"));
		cell.setCellStyle(cellStyle);
		cell = row.createCell(7);
		cell.setCellStyle(cellStyle);

		row = sheet.createRow(2);
		row.setHeight((short) 450);
		cell = row.createCell(6);
		cell.setCellValue(Main.properties.getProperty("column_name_11"));
		cell.setCellStyle(cellStyleWithTextWrap);
		cell = row.createCell(7);
		cell.setCellValue(Main.properties.getProperty("column_name_12"));
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
}