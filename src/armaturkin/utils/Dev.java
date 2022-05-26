package armaturkin.utils;

import armaturkin.core.DesignCode;
import armaturkin.core.Main;
import armaturkin.summaryoutput.SummaryRedirectManager;

public class Dev {

	public static boolean isDevMode = false;

	// TODO: complete method
	public static void printVariableStates() {
		Main.app.log(buildVariableStates());
	}

	public static String buildVariableStates() {
		return "=== Var state ===\n" +
				"Actual notification: {%s}\n".formatted(Main.app.getActualNotification()) +
				"Actual notification size: %d\n".formatted(Main.app.getActualNotification().length()) +
				"var 'manuallySummaryEntries' size: %d\n".formatted(Main.app.getManuallyEntryModel().getManuallySummaryEntries().size()) +
				"var 'backgroundReinforcementManuallyEntries' size: %d\n".formatted(Main.app.getManuallyEntryModel().getBackgroundReinforcementManuallyEntries().size()) +
				"Specification load from 'Update data': %b\n".formatted(DesignCode.isUpdate()) +
				"Redirect to %d\n".formatted(SummaryRedirectManager.getRedirectTo()) +
				"=================";
	}
}