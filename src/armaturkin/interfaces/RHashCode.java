package armaturkin.interfaces;

import armaturkin.core.RFClass;

public interface RHashCode {
	default int getHashCode(int diameter, RFClass rfClass) {
		return diameter + RFClass.getIntegerValue(rfClass);
	}
}