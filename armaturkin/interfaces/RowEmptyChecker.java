package armaturkin.interfaces;

import org.apache.poi.ss.usermodel.Row;

public interface RowEmptyChecker {
	default boolean isRowEmpty (Row row) {
		return row == null;
	}
}