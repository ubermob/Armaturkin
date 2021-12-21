package armaturkin.utils;

import armaturkin.core.DesignCode;
import armaturkin.core.Main;

public class Dev {

	public static boolean isDevMode = false;

	// TODO: complete method
	public static void printVariableStates() {
		Main.log.add(buildVariableStates());
	}

	public static String buildVariableStates() {
		return "=== Var state ===" +
				"Main.notification: {" + Main.getNotification() + "}" +
				"Main.notification.size: " + Main.getNotification().length() +
				"Main.manuallySummaryEntries size: " + Main.manuallySummaryEntries.size() +
				"Main.backgroundReinforcementManuallyEntries size: " + Main.backgroundReinforcementManuallyEntries.size() +
				"Specification load from \"Update data\": " + DesignCode.isUpdate() +
				"=================";
	}
}