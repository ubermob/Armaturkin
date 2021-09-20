package armaturkin.steelcomponent;

import armaturkin.interfaces.AddMass;
import armaturkin.interfaces.LightInfo;

import java.util.Arrays;

// TODO Replace to Image
public class SteelProductLiteInfo implements AddMass, LightInfo {

	private HotRolledSteelType type;
	private int[] dimensions;
	private double mass;

	public SteelProductLiteInfo(HotRolledSteelType type, int[] dimensions, double mass) {
		this.type = type;
		this.dimensions = dimensions;
		this.mass = mass;
	}

	public HotRolledSteelType getHotRolledSteelType() {
		return type;
	}

	public int[] getDimensions() {
		return dimensions;
	}

	public double getMass() {
		return mass;
	}

	@Override
	public void addMass(double mass) {
		this.mass += mass;
	}

	@Override
	public String toString() {
		return "SteelProductLiteInfo{" + "type=" + type + ", dimensions=" + Arrays.toString(dimensions) +
				", mass=" + mass + '}';
	}
}