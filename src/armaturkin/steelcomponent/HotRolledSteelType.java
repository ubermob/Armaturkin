package armaturkin.steelcomponent;

import armaturkin.core.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum HotRolledSteelType {

	EQUAL_LEG_ANGLE, UNEQUAL_LEG_ANGLE, SHEET;

	public static List<HotRolledSteelType> getAsList() {
		HotRolledSteelType[] array = HotRolledSteelType.values();
		List<HotRolledSteelType> list = new ArrayList<>(array.length);
		list.addAll(Arrays.asList(array));
		return list;
	}

	public static List<HotRolledSteelType> getFirstTwoElementsAsList() {
		List<HotRolledSteelType> list = new ArrayList<>(2);
		list.add(EQUAL_LEG_ANGLE);
		list.add(UNEQUAL_LEG_ANGLE);
		return list;
	}

	public static List<String> getAsStrings() {
		String[] strings = new String[]{
				Main.app.getProperty("hot_rolled_steel_type_ru_0"),
				Main.app.getProperty("hot_rolled_steel_type_ru_1"),
				Main.app.getProperty("hot_rolled_steel_type_ru_2")};
		List<String> list = new ArrayList<>(strings.length);
		list.addAll(Arrays.asList(strings));
		return list;
	}

	public static HotRolledSteelType parseHotRolledSteelType(String string) {
		if (Main.app.getProperty("hot_rolled_steel_type_ru_0").equals(string)) {
			return EQUAL_LEG_ANGLE;
		}
		if (Main.app.getProperty("hot_rolled_steel_type_ru_1").equals(string)) {
			return UNEQUAL_LEG_ANGLE;
		}
		if (Main.app.getProperty("hot_rolled_steel_type_ru_2").equals(string)) {
			return SHEET;
		}
		return null;
	}
}