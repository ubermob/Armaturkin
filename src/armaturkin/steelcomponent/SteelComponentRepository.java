package armaturkin.steelcomponent;

import armaturkin.core.Main;
import armaturkin.core.DesignCode;
import armaturkin.core.Reader;
import armaturkin.utils.StringUtil;
import armaturkin.utils.WholeNumber;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SteelComponentRepository {

	private static Sheet equalLegAnglesSheet;
	private static List<Image> equalLegAnglesList;
	private static Sheet unequalLegAnglesSheet;
	private static List<Image> unequalLegAnglesList;
	private static List<Number> sheetThicknessList;

	public static void load() {
		try {
			equalLegAnglesSheet = WorkbookFactory.create(Main.class.getResourceAsStream(
					"/design_codes/%s.xlsx".formatted(DesignCode.getProperty("hot_rolled_steel_equal_leg_angles"))
			)).getSheetAt(0);
			unequalLegAnglesSheet = WorkbookFactory.create(Main.class.getResourceAsStream(
					"/design_codes/%s.xlsx".formatted(DesignCode.getProperty("hot_rolled_steel_unequal_leg_angles"))
			)).getSheetAt(0);
			fillSheetArray();
		} catch (IOException e) {
			Main.log.add(e);
		}
	}

	public static List<Number> getSheetThicknessList() {
		return sheetThicknessList;
	}

	public static List<Image> getFullEqualAnglesImages() {
		if (equalLegAnglesList == null) {
			Iterator<Row> iterator = equalLegAnglesSheet.iterator();
			equalLegAnglesList = new ArrayList<>();
			while (iterator.hasNext()) {
				Row next = iterator.next();
				// Skip first row because it is head
				if (next.getRowNum() == 0) {
					continue;
				}
				Number thickness;
				double thicknessCandidate = next.getCell(2).getNumericCellValue();
				if (WholeNumber.isAWholeNumber(thicknessCandidate)) {
					thickness = (int) thicknessCandidate;
				} else {
					thickness = thicknessCandidate;
				}
				equalLegAnglesList.add(new Image(
						HotRolledSteelType.EQUAL_LEG_ANGLE,
						(int) next.getCell(1).getNumericCellValue(),
						thickness,
						(short) next.getRowNum()
				));
			}
		}
		return equalLegAnglesList;
	}

	public static List<Image> getFullUnequalAnglesImages() {
		if (unequalLegAnglesList == null) {
			Iterator<Row> iterator = unequalLegAnglesSheet.iterator();
			unequalLegAnglesList = new ArrayList<>();
			while (iterator.hasNext()) {
				Row next = iterator.next();
				// Skip first row because it is head
				if (next.getRowNum() == 0) {
					continue;
				}
				Number thickness;
				double thicknessCandidate = next.getCell(3).getNumericCellValue();
				if (WholeNumber.isAWholeNumber(thicknessCandidate)) {
					thickness = (int) thicknessCandidate;
				} else {
					thickness = thicknessCandidate;
				}
				unequalLegAnglesList.add(new Image(
						HotRolledSteelType.UNEQUAL_LEG_ANGLE,
						(int) next.getCell(1).getNumericCellValue(),
						(int) next.getCell(2).getNumericCellValue(),
						thickness,
						(short) next.getRowNum()
				));
			}
		}
		return unequalLegAnglesList;
	}

	public static String getCodeByElement(HotRolledSteelType type, Image image) {
		Sheet sheet = null;
		String header = "";
		if (type == HotRolledSteelType.EQUAL_LEG_ANGLE) {
			sheet = equalLegAnglesSheet;
			header = DesignCode.getProperty("hot_rolled_steel_equal_leg_angles");
		}
		if (type == HotRolledSteelType.UNEQUAL_LEG_ANGLE) {
			sheet = unequalLegAnglesSheet;
			header = DesignCode.getProperty("hot_rolled_steel_unequal_leg_angles") + "\n" +
					Main.getProperty("design_code_attention_1");
		}
		Iterator<Cell> iteratorHeader = sheet.getRow(0).iterator();
		Iterator<Cell> iteratorElement = sheet.getRow(image.getRowNumber()).iterator();
		String result = header + "\n";
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

	public static double getAngleMassPerUnitLength(Image image) {
		if (image.getType() == HotRolledSteelType.EQUAL_LEG_ANGLE) {
			return equalLegAnglesSheet.getRow(image.getRowNumber()).getCell(16).getNumericCellValue();
		} else if (image.getType() == HotRolledSteelType.UNEQUAL_LEG_ANGLE) {
			return unequalLegAnglesSheet.getRow(image.getRowNumber()).getCell(20).getNumericCellValue();
		}
		return 0.0;
	}

	// density (kg / mm^3)
	public static double getSteelDensity() {
		return 7.85E-9;
	}

	// ???
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

	// TODO in progress
	private static void fillSheetArray() throws IOException {
		sheetThicknessList = new ArrayList<>();
		List<String> list = Reader.read(Main.class.getResourceAsStream(
				"/design_codes/%s.txt".formatted(DesignCode.getProperty("hot_rolled_steel_sheets"))
		));
		// Parse list
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals("[width]")) {
				// Skip first line: "[thickness]"
				for (int j = 1; j < i; j++) {
					Number thickness;
					double thicknessCandidate = Double.parseDouble(list.get(j));
					if (WholeNumber.isAWholeNumber(thicknessCandidate)) {
						thickness = (int) thicknessCandidate;
					} else {
						thickness = thicknessCandidate;
					}
					sheetThicknessList.add(thickness);
				}
			}
		}
	}
}