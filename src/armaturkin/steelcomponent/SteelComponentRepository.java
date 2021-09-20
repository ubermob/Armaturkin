package armaturkin.steelcomponent;

import armaturkin.core.Main;
import armaturkin.core.DesignCode;
import armaturkin.interfaces.LightInfo;
import armaturkin.utils.WholeNumber;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SteelComponentRepository {

	static Workbook equalLegAngles;

	public static void load() {
		try {
			equalLegAngles = WorkbookFactory.create(Main.class.getResourceAsStream(
			"/design_codes/%s.xlsx".formatted(DesignCode.getProperty("hot_rolled_steel_equal_leg_angles"))
			));
		} catch (IOException e) {
			Main.log.add(e);
		}
	}

	public static List<Image> getFullEqualAnglesImage() {
		var iterator = equalLegAngles.getSheetAt(0).iterator();
		List<Image> list = new ArrayList<>();
		while (iterator.hasNext()) {
			var next = iterator.next();
			if (next.getRowNum() == 0) {
				continue;
			}
			Number thickness;
			if (WholeNumber.isAWholeNumber(next.getCell(2).getNumericCellValue())) {
				thickness = (int) next.getCell(2).getNumericCellValue();
			} else {
				thickness = next.getCell(2).getNumericCellValue();
			}
			list.add(new Image(
					HotRolledSteelType.EQUAL_LEG_ANGLE,
					(int) next.getCell(1).getNumericCellValue(),
					thickness,
					next.getRowNum()
			));
		}
		return list;
	}

	public static List<Number> getFirstDimension() {
		return getDimension(1);
	}

	public static List<Number> getSecondDimension(int parentValue) {
		return getDependedDimension(parentValue, 1, 2);
	}

	public static int getHashCode(HotRolledSteelType type, int[] dimensions) {
		if (type == HotRolledSteelType.EQUAL_LEG_ANGLE) {
			Iterator<Row> iterator = iterator(equalLegAngles);
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
		Iterator<Row> iterator = iterator(equalLegAngles);
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
		Iterator<Row> iterator = iterator(equalLegAngles);
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

	private static Iterator<Row> iterator(Workbook workbook) {
		return workbook.getSheetAt(0).iterator();
	}

	public static class Image implements LightInfo {

		private final HotRolledSteelType type;
		private final Number a, b, c, rowNumber;
		private double mass;

		public Image(HotRolledSteelType type, Number a, Number b, Number c, Number rowNumber) {
			this.type = type;
			this.a = a;
			this.b = b;
			this.c = c;
			this.rowNumber = rowNumber;
		}

		public Image(HotRolledSteelType type, Number a, Number b, Number rowNumber) {
			this.type = type;
			this.a = a;
			this.b = b;
			c = null;
			this.rowNumber = rowNumber;
		}

		public HotRolledSteelType getType() {
			return type;
		}

		public Number getA() {
			return a;
		}

		public Number getB() {
			return b;
		}

		public Number getC() {
			return c;
		}

		public Number getRowNumber() {
			return rowNumber;
		}

		public double getMass() {
			return mass;
		}

		public void setMass(double mass) {
			this.mass = mass;
		}

		@Override
		public String toString() {
			if (type == HotRolledSteelType.EQUAL_LEG_ANGLE) {
				return "L" + a + "x" + b;
			} else if (type == HotRolledSteelType.UNEQUAL_LEG_ANGLE) {
				return "L" + a + "x" + b + "x" + c;
			} else if (type == HotRolledSteelType.SHEET) {
				return "-" + a + "x" + b;
			}
			return "RAW:" + type + "" + a + "" + b + "" + c + "" + rowNumber;
		}
	}
}