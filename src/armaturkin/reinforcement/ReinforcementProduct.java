package armaturkin.reinforcement;

public class ReinforcementProduct {

	private final int position, diameter;
	private final RFClass rfClass;
	private final int length;
	private final double mass;

	public ReinforcementProduct(int position, int diameter, RFClass rfClass, int length, double mass) {
		this.position = position;
		this.diameter = diameter;
		this.rfClass = rfClass;
		this.length = length;
		this.mass = mass;
	}

	@Override
	public String toString() {
		return getClass() + ": [position: " + position + "]," +
				"[diameter: " + diameter + "]," +
				"[RFClass: " + rfClass + "]," +
				"[length: " + length + "]," +
				"[mass: " + mass + "]";
	}

	public int getDiameter() {
		return diameter;
	}

	public RFClass getRfClass() {
		return rfClass;
	}

	public int getLength() {
		return length;
	}

	public double getMass() {
		return mass;
	}
}