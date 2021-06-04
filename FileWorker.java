import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class FileWorker implements Runnable {

	private final String path;
	private final HashMap<Integer, Reinforcement> reinforcementHashMap;
	private final String downloadFileTableHead;
	private final String backgroundReinforcement;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private XSSFRow row;
	private XSSFCell cell;
	private CellStyle cellStyle;
	private XSSFFont font;
	private int rowInt;

	FileWorker(String path, HashMap<Integer, Reinforcement> reinforcementHashMap,
	           String downloadFileTableHead, String backgroundReinforcement) {
		this.path = path;
		this.reinforcementHashMap = reinforcementHashMap;
		this.downloadFileTableHead = downloadFileTableHead;
		this.backgroundReinforcement = backgroundReinforcement;
	}

	@Override
	public void run() {
		buildTableHead();
		addBackgroundReinforcement();
		fillTable();

		LocalDateTime localDateTime = LocalDateTime.now();
		String fileName = localDateTime.format(DateTimeFormatter.ofPattern("HH-mm-ss dd-MM-yyyy")) + ".xlsx";
		String parentPath = Path.of(path).getParent().toString();
		try (OutputStream outputStream = Files.newOutputStream(Path.of(parentPath, fileName))) {
			workbook.write(outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void fillTable() {
		rowInt = 4;
		for (int i = 1; i <= Pattern.maxPosition; i++) {
			if (reinforcementHashMap.containsKey(i)) {
				Reinforcement reinforcement = reinforcementHashMap.get(i);
				row = sheet.createRow(rowInt);
				row.setHeight((short) 675);
				for (int j = 0; j < 8; j++) {
					cell = row.createCell(j);
					cell.setCellStyle(cellStyle);
				}
				font.setColor(new XSSFColor(Pattern.getRgb(reinforcement.getDiameter()), null));
				cellStyle.setFont(font);

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
					//CellStyle cs = workbook.createCellStyle();
					cell = row.getCell(6);
					cell.setCellValue("-");
					cell = row.getCell(7);
					cell.setCellValue(reinforcement.getMass());
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
					cell.setCellValue(reinforcement.getLength() * reinforcement.getNumber() * 1.0);
					cell = row.getCell(6);
					cell.setCellValue(reinforcement.getMass());
					cell = row.getCell(7);
					cell.setCellValue(reinforcement.getMass() * reinforcement.getNumber());
				}
				rowInt++;
			}
		}
	}

	private void addBackgroundReinforcement() {
		if (backgroundReinforcement.length() != 0) {
			String[] splittedString = backgroundReinforcement.split("-");
			if (splittedString.length % 2 != 0) {
				Log.add(getClass() +
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
					Log.add(getClass() + " parse background reinforcement: [diameter: " + diameter + "]" +
							",[length: " + length + "],[lengthInt: " + lengthInt + "]"
					);
					int reservedDiameterIndex = Pattern.getReservedDiameterIndex(diameter);
					if (reservedDiameterIndex == -1) {
						Log.add(getClass() + " do not found [diameter: " + diameter + "] in Pattern");
						Main.addNotification("Указанного диаметра: " + diameter +
								" нет в зарезервированном списке диаметров (class Pattern)"
						);
					} else {
						int position = Pattern.reservedPosition[reservedDiameterIndex];
						if (reinforcementHashMap.containsKey(position)) {
							Reinforcement calculatedReinforcement = reinforcementHashMap.get(position);
							Reinforcement currentReinforcement = new Reinforcement(position,
									diameter,
									Pattern.getReservedRFClass(position),
									lengthInt,
									Pattern.getMass(diameter) * lengthInt / 1000
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
									Pattern.getReservedRFClass(position),
									lengthInt,
									Pattern.getMass(diameter) * lengthInt / 1000
							));
						}
						Log.add(reinforcementHashMap.get(position).toString());
					}
				}
			}
		}
	}

	private void buildTableHead() {
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("Лист1");
		cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		BorderStyle borderStyle = BorderStyle.THIN;
		cellStyle.setBorderTop(borderStyle);
		cellStyle.setBorderBottom(borderStyle);
		cellStyle.setBorderLeft(borderStyle);
		cellStyle.setBorderRight(borderStyle);
		font = workbook.createFont();
		font.setFontName("CS Standart");
		font.setFontHeight((short) 170); // 8.5 * 20
		cellStyle.setFont(font);

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
		font.setFontHeight((short) 140); // 7 * 20
		sheet.addMergedRegion(CellRangeAddress.valueOf("A1:H1"));
		cell.setCellStyle(cellStyle);

		row = sheet.createRow(1);
		row.setHeight((short) 450);
		cell = row.createCell(0);
		cell.setCellValue("№ поз.");
		cell.setCellStyle(cellStyle);
		cell = row.createCell(1);
		cell.setCellValue("Эскиз");
		cell.setCellStyle(cellStyle);
		cell = row.createCell(2);
		cell.setCellValue("Диаметр, класс ар-ры");
		cell.setCellStyle(cellStyle);
		cell = row.createCell(3);
		cell.setCellValue("Длина эл-та,мм");
		cell.setCellStyle(cellStyle);
		cell = row.createCell(4);
		cell.setCellValue("Кол-во,шт.");
		cell.setCellStyle(cellStyle);
		cell = row.createCell(5);
		cell.setCellValue("Общ.длинамп");
		cell.setCellStyle(cellStyle);
		cell = row.createCell(6);
		cell.setCellValue("Масса, кг");
		cell.setCellStyle(cellStyle);

		row = sheet.createRow(2);
		row.setHeight((short) 450);
		cell = row.createCell(6);
		cell.setCellValue("Одного элемента");
		cell.setCellStyle(cellStyle);
		cell = row.createCell(7);
		cell.setCellValue("Всех элементов");
		cell.setCellStyle(cellStyle);
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
		Log.add(getClass() + " table head created");
	}
}