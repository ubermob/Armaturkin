package armaturkin.core;

public class RfHashCode {
	public static synchronized int getHashCode(int diameter, RFClass rfClass) {
		return diameter + RFClass.getIntegerValue(rfClass);
	}
}