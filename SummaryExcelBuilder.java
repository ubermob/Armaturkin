import javafx.scene.control.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class SummaryExcelBuilder implements Runnable, Stopwatch {

	private final ContentContainer contentContainer;
	private final String path;
	private final String fileName;
	private final String tableHead;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private XSSFRow row;
	private XSSFCell cell;
	private int columnInt = 1;
	private int baseRowInt = 6;
	private int rowInt = baseRowInt;
	private long millis;

	public SummaryExcelBuilder(ContentContainer contentContainer, String path, String fileName, String tableHead) {
		this.contentContainer = contentContainer;
		this.path = path;
		this.fileName = fileName;
		this.tableHead = tableHead;
	}

	@Override
	public void run() {
		millis = getStartTime();
		Main.log.add(Main.properties.getProperty("threadStart").formatted(getClass()));
		initWorkbook();
		boolean[] headBlockFullness = contentContainer.getHeadBlockFullness();
		String[] rowStrings = contentContainer.getRowStrings();
		int totalExcelRows = 6 + rowStrings.length + 1;
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
		String parentPath = Path.of(path).getParent().toString();
		try (OutputStream outputStream = Files.newOutputStream(Path.of(parentPath, fileName))) {
			workbook.write(outputStream);
			Main.log.add(Main.properties.getProperty("file_download").formatted(getClass(), fileName, parentPath));
			Main.addNotification(Main.properties.getProperty("fileSuccessfullyDownload").formatted(fileName));
		} catch (Exception e) {
			Main.log.add(e);
		}
		Main.log.add(Main.properties.getProperty("threadComplete").formatted(getClass(), getStopwatch(millis)));
	}

	private void buildBlock(int i) {
		int startBlockColumn = columnInt;
		SummaryBlock block = contentContainer.getBlock(i);
		Main.log.add(block.toString());
		for (int j = 0; j < block.getBodyWidth(); j++) {
			rowInt = baseRowInt;
			for (int k = 0; k < block.getBodyHeight(); k++) {
				writeCell(sheet.getRow(rowInt++).createCell(columnInt), block.getValue(k, j));
			}
			writeCell(sheet.getRow(rowInt).createCell(columnInt), block.getHorizontalSummaryMass(j));
			// Write column name
			sheet.getRow(5).createCell(columnInt).setCellValue(Main.properties.getProperty("column_name_1") + block.getDiameter(j));
			// Next column
			columnInt++;
		}
		rowInt = baseRowInt;
		for (int j = 0; j < block.getBodyHeight(); j++) {
			writeCell(sheet.getRow(rowInt++).createCell(columnInt), block.getVerticalSummaryMass(j));
		}
		writeCell(sheet.getRow(rowInt).createCell(columnInt), block.getBlockSummaryMass());
		// Write column name
		sheet.getRow(5).createCell(columnInt).setCellValue(Main.properties.getProperty("column_name_2"));
		// Write standart document name
		sheet.getRow(4).createCell(startBlockColumn).setCellValue(Main.properties.getProperty("standart_document_name"));
		sheet.addMergedRegion(new CellRangeAddress(4, 4, startBlockColumn, columnInt));
		// Write reinforcement class
		sheet.getRow(3).createCell(startBlockColumn).setCellValue(RFClass.toString(block.getRFClass()));
		sheet.addMergedRegion(new CellRangeAddress(3, 3, startBlockColumn, columnInt));
		// Next column
		columnInt++;
	}

	private void writeCell(XSSFCell cell, Double value) {
		if (value != null) {
			cell.setCellValue(value);
			//cell.getCellStyle(workbook.createCellStyle().setDataFormat(workbook.createDataFormat().getFormat("0.0")));
		} else {
			cell.setCellValue("-");
		}
	}

	private void buildFinallyVerticalSummaryMass() {
		Double[] mass = contentContainer.getFinallyVerticalSummaryMass();
		rowInt = baseRowInt;
		for (Double d : mass) {
			writeCell(sheet.getRow(rowInt++).createCell(columnInt), d);
		}
		// Write column name
		sheet.getRow(5).createCell(columnInt).setCellValue(Main.properties.getProperty("column_name_3"));
	}

	private void buildUpperHead() {
		sheet.getRow(0).createCell(0).setCellValue(tableHead);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnInt));
		sheet.getRow(1).createCell(1).setCellValue(Main.properties.getProperty("table_head_1"));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, columnInt));
		sheet.getRow(2).createCell(1).setCellValue(Main.properties.getProperty("table_head_2"));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, columnInt));
	}

	private void buildLeftRows(String[] rowStrings) {
		sheet.getRow(1).createCell(0).setCellValue(Main.properties.getProperty("left_string_head"));
		sheet.addMergedRegion(new CellRangeAddress(1, 5, 0, 0));
		rowInt = baseRowInt;
		for (String string : rowStrings) {
			sheet.getRow(rowInt++).createCell(0).setCellValue(string);
		}
		sheet.getRow(rowInt).createCell(0).setCellValue(Main.properties.getProperty("left_string_last"));
	}

	private void initWorkbook() {
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("Лист1");
	}
}