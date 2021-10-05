package armaturkin.steelcomponent;

import armaturkin.interfaces.LightInfo;

/**
 * @author Andrey Korneychuk on 21-Sep-21
 * @version 1.0
 */
public class Image implements LightInfo {

	private final HotRolledSteelType type;
	// Three linear dimensions
	private final Number a, b, c;
	private final short rowNumber;
	private double mass;

	public Image(HotRolledSteelType type, Number a, Number b, Number c, short rowNumber) {
		this.type = type;
		this.a = a;
		this.b = b;
		this.c = c;
		this.rowNumber = rowNumber;
	}

	public Image(HotRolledSteelType type, Number a, Number b, short rowNumber) {
		this.type = type;
		this.a = a;
		this.b = b;
		c = null;
		this.rowNumber = rowNumber;
	}

	public Image(Number a, Number b, Number c, double mass) {
		type = HotRolledSteelType.SHEET;
		this.a = a;
		this.b = b;
		this.c = c;
		this.rowNumber = -1;
		this.mass = mass;
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

	public int getHash() {
		if (type == HotRolledSteelType.EQUAL_LEG_ANGLE) {
			return 300 + rowNumber;
		} else if (type == HotRolledSteelType.UNEQUAL_LEG_ANGLE) {
			return 400 + rowNumber;
		}
		return 0;
	}

	@Override
	public String toString() {
		if (type == HotRolledSteelType.EQUAL_LEG_ANGLE) {
			return "L" + a + "x" + b;
		} else if (type == HotRolledSteelType.UNEQUAL_LEG_ANGLE) {
			return "L" + a + "x" + b + "x" + c;
		} else if (type == HotRolledSteelType.SHEET) {
			return "-" + b + "x" + c;
		}
		return "RAW:" + type + "_" + a + "_" + b + "_" + c + "_" + rowNumber;
	}

	public Image cloneWrapper() throws CloneNotSupportedException {
		return (Image) this.clone();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new Image(type, a, b, c, rowNumber);
	}
}