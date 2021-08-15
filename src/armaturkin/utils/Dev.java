package armaturkin.utils;

import armaturkin.core.Main;
import armaturkin.core.Specification;

public class Dev {

	public static boolean isDevMode = false;

	// TODO: complete method
	public static void printVarState() {
		Main.log.add("=== Var state ===");
		Main.log.add("Main.notification: {" + Main.getNotification() + "}");
		Main.log.add("Main.notification.size: " + Main.getNotification().length());
		Main.log.add("Main.manuallySummaryEntries size: " + Main.manuallySummaryEntries.size());
		Main.log.add("Main.backgroundReinforcementManuallyEntries size: " + Main.backgroundReinforcementManuallyEntries.size());
		Main.log.add("Specification load from \"Update data\": " + Specification.isUpdate());
		Main.log.add("=================");
	}
}