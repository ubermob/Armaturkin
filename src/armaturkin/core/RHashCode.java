package armaturkin.core;

public class RHashCode {
	public static synchronized int getHashCode(int diameter, RFClass rfClass) {
		return diameter + RFClass.getIntegerValue(rfClass);
	}
}