package armaturkin.utils;

/**
 * @author Andrey Korneychuk on 27-May-22
 * @version 1.0
 */
public class ParsedRange {

	private Integer startValue;
	private int lastValue;

	public void write(int value) {
		if (startValue == null) {
			startValue = value;
		}
		lastValue = value;
	}

	public int getStartValue() {
		return startValue;
	}

	public int getLastValue() {
		return lastValue;
	}

	public String rangeAsIndexes() {
		return "[" + startValue + "-" + lastValue + "]";
	}

	public String rangeAsExcelRowNumbers() {
		return "[" + (startValue + 1) + "-" + (lastValue + 1) + "]";
	}
}