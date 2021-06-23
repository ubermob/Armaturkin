import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public interface CellEmptyChecker {
	default boolean isCellEmpty(Cell cell) {
		// https://stackoverflow.com/questions/15764417/how-to-check-if-an-excel-cell-is-empty-using-apache-poi/15779444
		if (cell == null) {
			return true;
		}
		return cell.getCellType() == CellType.BLANK;
	}
}