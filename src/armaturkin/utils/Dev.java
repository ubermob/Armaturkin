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
		return "=== Var state ===\n" +
				"Actual notification: {" + Main.app.getActualNotification() + "}\n" +
				"Actual notification size: " + Main.app.getActualNotification().length() + "\n" +
				"var 'manuallySummaryEntries' size: "
				+ Main.app.getManuallyEntryModel().getManuallySummaryEntries().size() + "\n" +
				"var 'backgroundReinforcementManuallyEntries' size: "
				+ Main.app.getManuallyEntryModel().getBackgroundReinforcementManuallyEntries().size() + "\n" +
				"Specification load from 'Update data': " + DesignCode.isUpdate() + "\n" +
				"=================";
	}
}