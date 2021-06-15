public interface RHashCode {
	default int getHashCode(int diameter, RFClass rfClass) {
		return diameter + RFClass.getIntegerValue(rfClass);
	}
}