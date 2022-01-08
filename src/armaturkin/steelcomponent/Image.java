package armaturkin.steelcomponent;

import armaturkin.core.Main;
import armaturkin.interfaces.AddMass;
import armaturkin.interfaces.LightInfo;
import armaturkin.summaryoutput.SheetDynamicHashCode;

/**
 * @author Andrey Korneychuk on 21-Sep-21
 * @version 1.0
 */
public class Image implements LightInfo, AddMass, Comparable {

	private final HotRolledSteelType type;
	// Three linear dimensions
	private final Number a, b, c;
	private final short rowNumber;
	private double mass;

	// constructor unequal angle
	public Image(HotRolledSteelType type, Number a, Number b, Number c, short rowNumber) {
		this.type = type;
		this.a = a;
		this.b = b;
		this.c = c;
		this.rowNumber = rowNumber;
	}

	// constructor equal angle
	public Image(HotRolledSteelType type, Number a, Number b, short rowNumber) {
		this.type = type;
		this.a = a;
		this.b = b;
		c = null;
		this.rowNumber = rowNumber;
	}

	// constructor for sheet
	public Image(Number a, Number b, Number c, double mass) {
		type = HotRolledSteelType.SHEET;
		this.a = a;
		this.b = b;
		this.c = c;
		this.rowNumber = -1;
		this.mass = mass;
	}

	public HotRolledSteelType getHotRolledSteelType() {
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

	public int getHashCode() {
		if (type == HotRolledSteelType.EQUAL_LEG_ANGLE) {
			return 300 + rowNumber;
		} else if (type == HotRolledSteelType.UNEQUAL_LEG_ANGLE) {
			return 400 + rowNumber;
		} else if (type == HotRolledSteelType.SHEET) {
			return SheetDynamicHashCode.getDynamicHashCode(this);
		}
		return 0;
	}

	public boolean sameImageOfSheet(Image image) {
		assert c != null;
		return image.getHotRolledSteelType() == HotRolledSteelType.SHEET
				&& b.equals(image.getB()) && c.equals(image.getC());
	}

	@Override
	public void addMass(double mass) {
		this.mass += mass;
	}

	@Override
	public String toString() {
		String sign;
		if (type == HotRolledSteelType.EQUAL_LEG_ANGLE) {
			sign = Main.app.getProperty("angle_sign");
			return sign + a + "x" + b;
		} else if (type == HotRolledSteelType.UNEQUAL_LEG_ANGLE) {
			sign = Main.app.getProperty("angle_sign");
			return sign + a + "x" + b + "x" + c;
		} else if (type == HotRolledSteelType.SHEET) {
			sign = Main.app.getProperty("sheet_sign");
			return sign + b + "x" + c;
		}
		return "RAW(type, a, b, c, rowNumber):" + type + "_" + a + "_" + b + "_" + c + "_" + rowNumber;
	}

	@Override
	public int compareTo(Object o) {
		Image some = (Image) o;
		int bCondition = (int) Math.round(b.doubleValue() - some.getB().doubleValue());
		int cCondition = (int) Math.round(c.doubleValue() - some.getC().doubleValue());
		return bCondition * 10000 + cCondition * 10;
	}

	public Image getClone() {
		return new Image(type, a, b, c, rowNumber);
	}
}