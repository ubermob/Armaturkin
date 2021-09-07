package armaturkin.reinforcement;

public class Reinforcement {

	private final int position, diameter;
	private final RFClass rfClass;
	private final int length, number;
	private final double mass;
	private final boolean linear;

	public Reinforcement(int position, int diameter, RFClass rfClass, int length, double mass) {
		this.position = position;
		this.diameter = diameter;
		this.rfClass = rfClass;
		this.length = length;
		number = 1;
		this.mass = mass;
		linear = true;
	}

	public Reinforcement(int position, int diameter, RFClass rfClass, int length, int number, double mass) {
		this.position = position;
		this.diameter = diameter;
		this.rfClass = rfClass;
		this.length = length;
		this.number = number;
		this.mass = mass;
		linear = false;
	}

	@Override
	public String toString() {
		return getClass() + ": [position: " + position + "]," +
				"[diameter: " + diameter + "]," +
				"[RFClass: " + rfClass + "]," +
				"[length: " + length + "]," +
				"[number: " + number + "]," +
				"[mass: " + mass + "]," +
				"[linear: " + linear + "]";
	}

	public int getPosition() {
		return position;
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

	public int getNumber() {
		return number;
	}

	public double getMass() {
		return mass;
	}

	public boolean isLinear() {
		return linear;
	}
}