package armaturkin.steelcomponent;

import armaturkin.interfaces.LightInfo;

/**
 * @author Andrey Korneychuk on 21-Sep-21
 * @version 1.0
 */
public class Image implements LightInfo {

	private final HotRolledSteelType type;
	private final Number a, b, c;
	private int rowNumber;
	private double mass;

	public Image(HotRolledSteelType type, Number a, Number b, Number c, int rowNumber) {
		this.type = type;
		this.a = a;
		this.b = b;
		this.c = c;
		this.rowNumber = rowNumber;
	}

	public Image(HotRolledSteelType type, Number a, Number b, int rowNumber) {
		this.type = type;
		this.a = a;
		this.b = b;
		c = null;
		this.rowNumber = rowNumber;
	}

	public HotRolledSteelType getType() {
		return type;
	}

	public Number getA() {
		return a;
	}

	public Number getB() {
		return b;
	}

	public Number getC() {
		return c;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	@Override
	public String toString() {
		if (type == HotRolledSteelType.EQUAL_LEG_ANGLE) {
			return "L" + a + "x" + b;
		} else if (type == HotRolledSteelType.UNEQUAL_LEG_ANGLE) {
			return "L" + a + "x" + b + "x" + c;
		} else if (type == HotRolledSteelType.SHEET) {
			return "-" + a + "x" + b;
		}
		return "RAW:" + type + "" + a + "" + b + "" + c + "" + rowNumber;
	}
}