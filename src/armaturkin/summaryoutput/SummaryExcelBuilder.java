package armaturkin.summaryoutput;

import armaturkin.core.DesignCode;
import armaturkin.core.Main;
import armaturkin.reinforcement.RFClass;
import armaturkin.steelcomponent.HotRolledSteelType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utools.stopwatch.Stopwatch;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static armaturkin.core.Main.getProperty;

public class SummaryExcelBuilder implements Runnable {

	private final ContentContainer contentContainer;
	private final Object[] contentHeadPlacementBlocks;
	private final String path, fileName, tableHead;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private CellStyle cellStyle, textCellStyle, headerCellStyle;
	private final int baseColumnInt = 1;
	private int columnInt = baseColumnInt;
	private int lastReinforcementBlockColumn = columnInt;
	private final int baseRowInt = 6;
	private int rowInt = baseRowInt;
	private final List<CellRangeAddress> angleCellRanges;
	private Stopwatch stopwatch;

	public SummaryExcelBuilder(ContentContainer contentContainer, String path, String fileName, String tableHead) {
		this.contentContainer = contentContainer;
		contentHeadPlacementBlocks = contentContainer.getContentHeadPlacementBlocks();
		this.path = path;
		this.fileName = fileName;
		this.tableHead = tableHead;
		angleCellRanges = new ArrayList<>(2);
	}

	@Override
	public void run() {
		stopwatch = new Stopwatch();
		Main.log.add(getProperty("thread_start").formatted(getClass()));
		initWorkbook();
		createCellStyle();
		boolean[] headBlockFullness = contentContainer.getHeadBlockFullness();
		String[] rowStrings = contentContainer.getRowStrings();
		int totalExcelRows = baseRowInt + rowStrings.length + 1;
		for (int i = 0; i < totalExcelRows; i++) {
			sheet.createRow(i); // Create all rows
		}
		for (int i = 0; i < contentHeadPlacementBlocks.length; i++) {
			// Blocks with reinforcement
			if (contentHeadPlacementBlocks[i] instanceof RFClass && headBlockFullness[i]) {
				buildBlock(contentContainer.getBlock(i));
			}
			if (contentHeadPlacementBlocks[i] instanceof HotRolledSteelType && headBlockFullness[i]) {
				buildBlock(contentContainer.getHotRolledSteelSummaryBlock(i));
			}
		}
		buildFinallyVerticalSummaryMassColumn();
		buildAngleCellRangeAddress();
		buildUpperHead();
		buildLeftColumn(rowStrings);
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

	private void buildBlock(SummaryBlock block) {
		boolean isInstanceOfAngle = isInstanceOfAngle(block);
		boolean isInstanceOfSheet = isInstanceOfSheet(block);
		int startBlockColumn = columnInt;
		Main.log.add(block.toString());
		for (int j = 0; j < block.getBodyWidth(); j++) {
			rowInt = baseRowInt;
			for (int k = 0; k < block.getBodyHeight(); k++) {
				// Write body
				writeCell(sheet.getRow(rowInt++).createCell(columnInt), block.getValue(k, j));
			}
			writeCell(sheet.getRow(rowInt).createCell(columnInt), block.getHorizontalSummaryMass(j));
			// Write column name
			String columnName;
			if (isInstanceOfAngle || isInstanceOfSheet) {
				columnName = ((HotRolledSteelSummaryBlock) block).getImage(j).toString();
			} else {
				// Reinforcement
				columnName = getProperty("column_name_1") + block.getDiameter(j);
			}
			fillCell(5, columnInt, columnName);
			// Next column
			columnInt++;
		}
		rowInt = baseRowInt;
		// Write vertical summary mass
		for (int j = 0; j < block.getBodyHeight(); j++) {
			writeCell(sheet.getRow(rowInt++).createCell(columnInt), block.getVerticalSummaryMass(j));
		}
		writeCell(sheet.getRow(rowInt).createCell(columnInt), block.getBlockSummaryMass());
		// Write vertical summary mass column name
		fillCell(5, columnInt, getProperty("column_name_2"));
		// Write design code
		String designCode = DesignCode.getProperty("reinforcing_rolled");
		if (isInstanceOfAngle || isInstanceOfSheet) {
			HotRolledSteelType hotRolledSteelType = ((HotRolledSteelSummaryBlock) block).getHotRolledSteelType();
			if (hotRolledSteelType == HotRolledSteelType.EQUAL_LEG_ANGLE) {
				designCode = DesignCode.getProperty("hot_rolled_steel_equal_leg_angles");
			} else if (hotRolledSteelType == HotRolledSteelType.UNEQUAL_LEG_ANGLE) {
				designCode = DesignCode.getProperty("hot_rolled_steel_unequal_leg_angles");
			} else {
				// Sheet
				designCode = DesignCode.getProperty("hot_rolled_steel_sheets");
			}
		}
		fillCell(4, startBlockColumn, designCode);
		// Merge cells
		for (int j = (startBlockColumn + 1); j <= columnInt; j++) {
			createEmptyCell(4, j);
		}
		sheet.addMergedRegion(new CellRangeAddress(4, 4, startBlockColumn, columnInt));
		// Write reinforcement class or hot rolled steel type
		String rfClassOrHrst = RFClass.toString(block.getRFClass());
		if (isInstanceOfAngle) {
			rfClassOrHrst = getProperty("table_head_3");
		}
		if (isInstanceOfSheet) {
			rfClassOrHrst = getProperty("table_head_4");
		}
		fillCell(3, startBlockColumn, rfClassOrHrst);
		// Merge cells
		for (int j = (startBlockColumn + 1); j <= columnInt; j++) {
			createEmptyCell(3, j);
		}
		CellRangeAddress cellAddresses = new CellRangeAddress(3, 3, startBlockColumn, columnInt);
		if (isInstanceOfAngle) {
			// Add to list and will be invoke "buildAngleCellRangeAddress" method
			angleCellRanges.add(cellAddresses);
		} else {
			// Regular
			sheet.addMergedRegion(cellAddresses);
		}
		// Remember last column
		if (!isInstanceOfAngle && !isInstanceOfSheet) {
			// Write last reinforcement block column for manage table head
			lastReinforcementBlockColumn = columnInt;
		}
		// Next column
		columnInt++;
	}

	private boolean isInstanceOfAngle(SummaryBlock block) {
		if (block instanceof HotRolledSteelSummaryBlock) {
			return ((HotRolledSteelSummaryBlock) block).getHotRolledSteelType() != HotRolledSteelType.SHEET;
		}
		return false;
	}

	private boolean isInstanceOfSheet(SummaryBlock block) {
		if (block instanceof HotRolledSteelSummaryBlock) {
			return ((HotRolledSteelSummaryBlock) block).getHotRolledSteelType() == HotRolledSteelType.SHEET;
		}
		return false;
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

	private void buildFinallyVerticalSummaryMassColumn() {
		Double[] mass = contentContainer.getFinallyVerticalSummaryMass();
		rowInt = baseRowInt;
		for (var d : mass) {
			writeCell(sheet.getRow(rowInt++).createCell(columnInt), d);
		}
		// Write column name
		fillCell(5, columnInt, getProperty("column_name_3"));
		// Empty cells above last column name
		for (int i = 1; i <= 4; i++) {
			createEmptyCell(i, columnInt);
		}
		sheet.addMergedRegion(new CellRangeAddress(1, 4, columnInt, columnInt));
	}

	private void buildUpperHead() {
		// Write table name
		sheet.getRow(0).createCell(0).setCellValue(tableHead);
		sheet.getRow(0).getCell(0).setCellStyle(headerCellStyle);
		sheet.getRow(0).setHeight((short) 450);
		for (int i = 1; i <= columnInt; i++) {
			sheet.getRow(0).createCell(i).setCellStyle(headerCellStyle);
		}
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnInt));
		boolean isReinforcementBlocksExist = lastReinforcementBlockColumn - baseColumnInt > 0;
		if (isReinforcementBlocksExist) {
			fillCell(1, 1, getProperty("table_head_1"));
			for (int i = 2; i <= columnInt; i++) {
				createEmptyCell(1, i);
			}
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, lastReinforcementBlockColumn));
			fillCell(2, 1, getProperty("table_head_2"));
			for (int i = 2; i <= columnInt; i++) {
				createEmptyCell(2, i);
			}
			sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, lastReinforcementBlockColumn));
		}
		// Must be greater than 1 because "columnInt" variable incremented in "buildBlock" method
		boolean isHotRolledSteelBlocksExist = columnInt - lastReinforcementBlockColumn > 1;
		if (isHotRolledSteelBlocksExist) {
			int startColumn = lastReinforcementBlockColumn + 1;
			if (!isReinforcementBlocksExist) {
				startColumn = lastReinforcementBlockColumn;
			}
			fillCell(1, startColumn, getProperty("table_head_5"));
			createEmptyCell(2, startColumn);
			for (int i = 1; i <= 2; i++) {
				for (int j = (startColumn + 1); j < columnInt; j++) {
					createEmptyCell(i, j);
				}
			}
			sheet.addMergedRegion(new CellRangeAddress(1, 2, startColumn, columnInt - 1));
		}
	}

	private void buildAngleCellRangeAddress() {
		if (angleCellRanges.size() > 1) {
			// Expected size == 2 and 0 index element placed before 1 index element
			int first = angleCellRanges.get(0).getFirstColumn();
			int last = angleCellRanges.get(1).getLastColumn();
			sheet.addMergedRegion(new CellRangeAddress(3, 3, first, last));
		} else {
			// Expected size == 1
			sheet.addMergedRegion(angleCellRanges.get(0));
		}
	}

	private void buildLeftColumn(String[] rowStrings) {
		fillCell(1, 0, getProperty("left_string_head"));
		sheet.addMergedRegion(new CellRangeAddress(1, 5, 0, 0));
		rowInt = baseRowInt;
		for (var string : rowStrings) {
			fillCell(rowInt++, 0, string);
		}
		fillCell(rowInt, 0, getProperty("left_string_last"));
	}

	private void fillCell(int row, int column, String text) {
		XSSFCell cell = sheet.getRow(row).createCell(column);
		cell.setCellValue(text);
		cell.setCellStyle(textCellStyle);
	}

	private void createEmptyCell(int row, int column) {
		sheet.getRow(row).createCell(column).setCellStyle(textCellStyle);
	}

	private void initWorkbook() {
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet(getProperty("default_list_name"));
		sheet.setZoom(Integer.parseInt(getProperty("sheet_zoom_1")));
		var coreProperties = workbook.getProperties().getCoreProperties();
		coreProperties.setCreator(Main.getAppNameAndVersion());
		coreProperties.setDescription(Main.getProperty("summary_excel_builder_commentary")
				.formatted(Main.getAppNameAndVersion()));
	}
}