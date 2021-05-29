import org.apache.poi.ss.usermodel.Row;

public interface RowEmptyChecker {
	default boolean isRowEmpty (Row row) {
		if (row == null) {
			return true;
		}
		return false;
	}
}