package armaturkin.steelcomponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum HotRolledSteelType {

	EQUAL_LEG_ANGLE, UNEQUAL_LEG_ANGLE, SHEET;

	public static List<HotRolledSteelType> getAsList() {
		var array = HotRolledSteelType.values();
		List<HotRolledSteelType> list = new ArrayList<>(array.length);
		list.addAll(Arrays.asList(array));
		return list;
	}
}