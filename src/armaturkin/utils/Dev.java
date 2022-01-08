package armaturkin.utils;

import armaturkin.core.DesignCode;
import armaturkin.core.Main;

public class Dev {

	public static boolean isDevMode = false;

	// TODO: complete method
	public static void printVariableStates() {
		Main.app.log(buildVariableStates());
	}

	public static String buildVariableStates() {
		return "=== Var state ===" +
				"Actual notification: {" + Main.app.getActualNotification() + "}" +
				"Actual notification size: " + Main.app.getActualNotification().length() +
				"var 'manuallySummaryEntries' size: " + Main.app.getManuallyEntryModel().getManuallySummaryEntries().size() +
				"var 'backgroundReinforcementManuallyEntries' size: "
				+ Main.app.getManuallyEntryModel().getBackgroundReinforcementManuallyEntries().size() +
				"Specification load from 'Update data': " + DesignCode.isUpdate() +
				"=================";
	}
}