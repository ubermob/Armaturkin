package armaturkin.steelcomponent;

import armaturkin.core.Main;
import armaturkin.core.DesignCode;
import armaturkin.utils.StringUtil;
import armaturkin.utils.WholeNumber;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SteelComponentRepository {

	static Sheet equalLegAnglesSheet;
	static List<Image> equalLegAnglesList;

	public static void load() {
		try {
			equalLegAnglesSheet = WorkbookFactory.create(Main.class.getResourceAsStream(
					"/design_codes/%s.xlsx".formatted(DesignCode.getProperty("hot_rolled_steel_equal_leg_angles"))
			)).getSheetAt(0);
		} catch (IOException e) {
			Main.log.add(e);
		}
	}

	public static List<Image> getFullEqualAnglesImage() {
		if (equalLegAnglesList == null) {
			Iterator<Row> iterator = equalLegAnglesSheet.iterator();
			equalLegAnglesList = new ArrayList<>();
			while (iterator.hasNext()) {
				Row next = iterator.next();
				if (next.getRowNum() == 0) {
					continue;
				}
				Number thickness;
				if (WholeNumber.isAWholeNumber(next.getCell(2).getNumericCellValue())) {
					thickness = (int) next.getCell(2).getNumericCellValue();
				} else {
					thickness = next.getCell(2).getNumericCellValue();
				}
				equalLegAnglesList.add(new Image(
						HotRolledSteelType.EQUAL_LEG_ANGLE,
						(int) next.getCell(1).getNumericCellValue(),
						thickness,
						next.getRowNum()
				));
			}
		}
		return equalLegAnglesList;
	}

	public static String getCodeByElement(HotRolledSteelType type, Image image) {
		Sheet sheet = null;
		if (type == HotRolledSteelType.EQUAL_LEG_ANGLE) {
			sheet = equalLegAnglesSheet;
		}
		Iterator<Cell> iteratorHeader = sheet.getRow(0).iterator();
		Iterator<Cell> iteratorElement = sheet.getRow(image.getRowNumber()).iterator();
		String result = "";
		while (iteratorHeader.hasNext()) {
			result += getCellAsString(iteratorHeader.next()) + ": " + getCellAsString(iteratorElement.next()) + "\n";
		}
		return StringUtil.replaceNewLine(result);
	}

	public static String getCellAsString(Cell cell) {
		if (cell.getCellType() == CellType.STRING) {
			return cell.getStringCellValue();
		}
		if (cell.getCellType() == CellType.NUMERIC) {
			if (WholeNumber.isAWholeNumber(cell.getNumericCellValue())) {
				return "" + (int) cell.getNumericCellValue();
			}
			return "" + cell.getNumericCellValue();
		}
		return null;
	}

	public static List<Number> getFirstDimension() {
		return getDimension(1);
	}

	public static List<Number> getSecondDimension(int parentValue) {
		return getDependedDimension(parentValue, 1, 2);
	}

	public static int getHashCode(HotRolledSteelType type, int[] dimensions) {
		if (type == HotRolledSteelType.EQUAL_LEG_ANGLE) {
			Iterator<Row> iterator = equalLegAnglesSheet.iterator();
			while (iterator.hasNext()) {
				Row next = iterator.next();
				if (next.getCell(0).getCellType() == CellType.STRING) {
					continue;
				}
				//if (next.getCell(1).getNumericCellValue())
			}
		}
		throw new IllegalArgumentException(type + " " + Arrays.toString(dimensions));
	}

	private static List<Number> getDependedDimension(int parentValue, int parentCellNumber, int dependedCellNumber) {
		List<Number> result = new ArrayList<>();
		Iterator<Row> iterator = equalLegAnglesSheet.iterator();
		int unique = -1;
		while (iterator.hasNext()) {
			Row next = iterator.next();
			if (next.getCell(dependedCellNumber).getCellType() == CellType.STRING) {
				continue;
			}
			int dimension = (int) next.getCell(dependedCellNumber).getNumericCellValue();
			if (dimension != unique
					&& parentValue == (int) next.getCell(parentCellNumber).getNumericCellValue()) {
				result.add(dimension);
				unique = dimension;
			}
		}
		return result;
	}

	private static List<Number> getDimension(int cellNumber) {
		List<Number> result = new ArrayList<>();
		Iterator<Row> iterator = equalLegAnglesSheet.iterator();
		int unique = -1;
		while (iterator.hasNext()) {
			Cell cellValue = iterator.next().getCell(cellNumber);
			if (cellValue.getCellType() == CellType.STRING) {
				continue;
			}
			int dimension = (int) cellValue.getNumericCellValue();
			if (dimension != unique) {
				result.add(dimension);
				unique = dimension;
			}
		}
		return result;
	}
}