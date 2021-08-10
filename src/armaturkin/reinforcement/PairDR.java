package armaturkin.reinforcement;

/**
 * Class contain pair values:
 * {@code int} diameter and {@link RFClass}
 */
public class PairDR {

	private final int diameter;
	private final RFClass rfClass;

	/**
	 * @param diameter int value of diameter.
	 * @param rfClass value of RFClass.
	 */
	public PairDR(int diameter, RFClass rfClass) {
		this.diameter = diameter;
		this.rfClass = rfClass;
	}

	/**
	 * @return {@code int} value diameter
	 */
	public int getDiameter() {
		return diameter;
	}

	/**
	 * @return value {@link RFClass}
	 */
	public RFClass getRfClass() {
		return rfClass;
	}

	@Override
	public String toString() {
		return diameter + " - " + rfClass;
	}
}