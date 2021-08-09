package armaturkin.reinforcement;

import armaturkin.core.Main;

public class ReinforcementLiteInfo {

	private final int diameter;
	private final RFClass rfClass;
	private double mass;

	public ReinforcementLiteInfo(int diameter, RFClass rfClass, double mass) {
		this.diameter = diameter;
		this.rfClass = rfClass;
		this.mass = mass;
	}

	public int getDiameter() {
		return diameter;
	}

	public RFClass getRfClass() {
		return rfClass;
	}

	public double getMass() {
		return mass;
	}

	public void addMass(double mass) {
		this.mass += mass;
	}

	@Override
	public String toString() {
		return Main.properties.getProperty("reinforcement_lite_info_to_string").formatted(diameter, rfClass, mass);
	}
}