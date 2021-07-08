package armaturkin.interfaces;

import org.apache.poi.ss.usermodel.Cell;

public interface ParseInt {
	default int parseIntFromNumber(Cell cell) {
		return (int) Math.round(cell.getNumericCellValue());
	}

	default int parseIntFromString(Cell cell) {
		return Integer.parseInt(cell.getStringCellValue());
	}
}