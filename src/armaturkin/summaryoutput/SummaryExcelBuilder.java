package armaturkin.summaryoutput;

import armaturkin.core.Main;
import armaturkin.core.Specification;
import armaturkin.reinforcement.RFClass;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utools.stopwatch.Stopwatch;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static armaturkin.core.Main.getProperty;

public class SummaryExcelBuilder implements Runnable {

	private final ContentContainer contentContainer;
	private final String path, fileName, tableHead;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private CellStyle cellStyle, textCellStyle, headerCellStyle;
	private int columnInt = 1;
	private final int baseRowInt = 6;
	private int rowInt = baseRowInt;
	private Stopwatch stopwatch;

	public SummaryExcelBuilder(ContentContainer contentContainer, String path, String fileName, String tableHead) {
		this.contentContainer = contentContainer;
		this.path = path;
		this.fileName = fileName;
		this.tableHead = tableHead;
	}

	@Override
	public void run() {
		stopwatch = new Stopwatch();
		Main.log.add(getProperty("thread_start").formatted(getClass()));
		initWorkbook();
		boolean[] headBlockFullness = contentContainer.getHeadBlockFullness();
		String[] rowStrings = contentContainer.getRowStrings();
		int totalExcelRows = baseRowInt + rowStrings.length + 1;
		for (int i = 0; i < totalExcelRows; i++) {
			sheet.createRow(i); // Create all rows
		}
		for (int i = 0; i < headBlockFullness.length; i++) {
			if (headBlockFullness[i]) {
				buildBlock(i);
			}
		}
		buildFinallyVerticalSummaryMass();
		buildUpperHead();
		buildLeftRows(rowStrings);
		try (OutputStream outputStream = Files.newOutputStream(Path.of(path, fileName))) {
			workbook.write(outputStream);
			Main.addNotification(getProperty("file_successfully_download").formatted(fileName));
			Main.log.add(getProperty("file_download").formatted(getClass(), fileName, path));
		} catch (Exception e) {
			Main.addNotification(getProperty("excel_creation_exception").formatted(fileName));
			Main.log.add(e);
		}
		Main.log.add(getProperty("thread_complete").formatted(getClass(), stopwatch.getElapsedTime()));
	}

	private void buildBlock(int i) {
		int startBlockColumn = columnInt;
		SummaryBlock block = contentContainer.getBlock(i);
		Main.log.add(block.toString());
		createCellStyle();
		for (int j = 0; j < block.getBodyWidth(); j++) {
			rowInt = baseRowInt;
			for (int k = 0; k < block.getBodyHeight(); k++) {
				writeCell(sheet.getRow(rowInt++).createCell(columnInt), block.getValue(k, j));
			}
			writeCell(sheet.getRow(rowInt).createCell(columnInt), block.getHorizontalSummaryMass(j));
			// Write column name
			sheet.getRow(5).createCell(columnInt).setCellValue(getProperty("column_name_1") + block.getDiameter(j));
			sheet.getRow(5).getCell(columnInt).setCellStyle(textCellStyle);
			// Next column
			columnInt++;
		}
		rowInt = baseRowInt;
		for (int j = 0; j < block.getBodyHeight(); j++) {
			writeCell(sheet.getRow(rowInt++).createCell(columnInt), block.getVerticalSummaryMass(j));
		}
		writeCell(sheet.getRow(rowInt).createCell(columnInt), block.getBlockSummaryMass());
		// Write column name
		sheet.getRow(5).createCell(columnInt).setCellValue(getProperty("column_name_2"));
		sheet.getRow(5).getCell(columnInt).setCellStyle(textCellStyle);
		// Write specification document
		sheet.getRow(4).createCell(startBlockColumn).setCellValue(Specification.getProperty("reinforcing_rolled"));
		sheet.getRow(4).getCell(startBlockColumn).setCellStyle(textCellStyle);
		for (int j = (startBlockColumn + 1); j <= columnInt; j++) {
			sheet.getRow(4).createCell(j);
			sheet.getRow(4).getCell(j).setCellStyle(textCellStyle);
		}
		sheet.addMergedRegion(new CellRangeAddress(4, 4, startBlockColumn, columnInt));
		// Write reinforcement class
		sheet.getRow(3).createCell(startBlockColumn).setCellValue(RFClass.toString(block.getRFClass()));
		sheet.getRow(3).getCell(startBlockColumn).setCellStyle(textCellStyle);
		for (int j = (startBlockColumn + 1); j <= columnInt; j++) {
			sheet.getRow(3).createCell(j);
			sheet.getRow(3).getCell(j).setCellStyle(textCellStyle);
		}
		sheet.addMergedRegion(new CellRangeAddress(3, 3, startBlockColumn, columnInt));
		// Next column
		columnInt++;
	}

	private void createCellStyle() {
		sheet.setColumnWidth(0, 4717);
		Font font = workbook.createFont();
		font.setFontHeight((short) 142); // 7.1 * 20
		cellStyle = workbook.createCellStyle();
		BorderStyle borderStyle = BorderStyle.THIN;
		cellStyle.setBorderTop(borderStyle);
		cellStyle.setBorderBottom(borderStyle);
		cellStyle.setBorderLeft(borderStyle);
		cellStyle.setBorderRight(borderStyle);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setFont(font);
		textCellStyle = workbook.createCellStyle();
		textCellStyle.cloneStyleFrom(cellStyle);
		cellStyle.setDataFormat(workbook.createDataFormat().getFormat("0.0"));
		headerCellStyle = workbook.createCellStyle();
		headerCellStyle.cloneStyleFrom(textCellStyle);
		BorderStyle headerBorderStyle = BorderStyle.NONE;
		font = workbook.createFont();
		font.setFontHeight((short) 170); // 8.5 * 20
		headerCellStyle.setFont(font);
		headerCellStyle.setBorderTop(headerBorderStyle);
		headerCellStyle.setBorderRight(headerBorderStyle);
		headerCellStyle.setBorderLeft(headerBorderStyle);
	}

	private void writeCell(XSSFCell cell, Double value) {
		if (value != null) {
			cell.setCellValue(value);
			cell.setCellStyle(cellStyle);
		} else {
			cell.setCellValue("-");
			cell.setCellStyle(textCellStyle);
		}
	}

	private void buildFinallyVerticalSummaryMass() {
		Double[] mass = contentContainer.getFinallyVerticalSummaryMass();
		rowInt = baseRowInt;
		for (var d : mass) {
			writeCell(sheet.getRow(rowInt++).createCell(columnInt), d);
		}
		// Write column name
		sheet.getRow(5).createCell(columnInt).setCellValue(getProperty("column_name_3"));
		sheet.getRow(5).getCell(columnInt).setCellStyle(textCellStyle);
		// 2 empty spaces
		sheet.getRow(4).createCell(columnInt).setCellStyle(textCellStyle);
		sheet.getRow(3).createCell(columnInt).setCellStyle(textCellStyle);
	}

	private void buildUpperHead() {
		sheet.getRow(0).createCell(0).setCellValue(tableHead);
		sheet.getRow(0).getCell(0).setCellStyle(headerCellStyle);
		sheet.getRow(0).setHeight((short) 450);
		for (int i = 1; i <= columnInt; i++) {
			sheet.getRow(0).createCell(i).setCellStyle(headerCellStyle);
		}
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnInt));
		sheet.getRow(1).createCell(1).setCellValue(getProperty("table_head_1"));
		sheet.getRow(1).getCell(1).setCellStyle(textCellStyle);
		for (int i = 2; i <= columnInt; i++) {
			sheet.getRow(1).createCell(i).setCellStyle(textCellStyle);
		}
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, columnInt));
		sheet.getRow(2).createCell(1).setCellValue(getProperty("table_head_2"));
		sheet.getRow(2).getCell(1).setCellStyle(textCellStyle);
		for (int i = 2; i <= columnInt; i++) {
			sheet.getRow(2).createCell(i).setCellStyle(textCellStyle);
		}
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, columnInt));
	}

	private void buildLeftRows(String[] rowStrings) {
		sheet.getRow(1).createCell(0).setCellValue(getProperty("left_string_head"));
		sheet.getRow(1).getCell(0).setCellStyle(textCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(1, 5, 0, 0));
		rowInt = baseRowInt;
		for (var string : rowStrings) {
			sheet.getRow(rowInt).createCell(0).setCellValue(string);
			sheet.getRow(rowInt++).getCell(0).setCellStyle(textCellStyle);
		}
		sheet.getRow(rowInt).createCell(0).setCellValue(getProperty("left_string_last"));
		sheet.getRow(rowInt).getCell(0).setCellStyle(textCellStyle);
	}

	private void initWorkbook() {
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet(getProperty("default_list_name"));
		sheet.setZoom(Integer.parseInt(getProperty("sheet_zoom_1")));
	}
}