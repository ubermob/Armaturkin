package armaturkin.summaryoutput;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Andrey Korneychuk on 24-May-22
 * @version 1.0
 */
public class TableHeaderResult {

	private boolean isNumeric = true;
	private boolean isSince1 = true; // Sample: [1, 2, 3, 4, ...]
	private boolean isSince2 = true; // Sample: [2, 3, 4, 5, ...]
	private final List<Double> cellValueList = new LinkedList<>();

	public boolean isNumeric() {
		return isNumeric;
	}

	public void setNumeric(boolean numeric) {
		isNumeric = numeric;
	}

	public boolean isSince1() {
		return isSince1;
	}

	public void setSince1(boolean since1) {
		isSince1 = since1;
	}

	public boolean isSince2() {
		return isSince2;
	}

	public void setSince2(boolean since2) {
		isSince2 = since2;
	}

	public void addCellValueToList(double value) {
		cellValueList.add(value);
	}

	public String getCellValueListAsString() {
		if (cellValueList.size() == 0) {
			return "do not exist";
		}
		String result = "[ %s ]";
		StringBuilder sb = new StringBuilder();
		for (var v : cellValueList) {
			sb.append("%.0f".formatted(v)).append(" | ");
		}
		sb.delete(sb.length() - 3, sb.length()); // delete 3 last chars
		return result.formatted(sb.toString());
	}

	public boolean notExist() {
		return cellValueList.size() == 0;
	}
}