public class ReinforcementProduct {

	private final int position;
	private final int diameter;
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
}