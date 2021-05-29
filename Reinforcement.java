public class Reinforcement {

	private final int position;
	private final int diameter;
	private final RFClass rfClass;
	private final int length;
	private final int number;
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
		return "";
	}
}