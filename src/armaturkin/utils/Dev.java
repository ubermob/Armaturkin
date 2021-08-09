package armaturkin.utils;

import armaturkin.core.Main;

public class Dev {
	public static boolean isDevMode = false;

	// TODO: complete method
	public static void printVarState() {
		Main.log.add("Var state");
		Main.log.add("Main.manuallySummaryEntries size: " + Main.manuallySummaryEntries.size());
		Main.log.add("Main.backgroundReinforcementManuallyEntries size: " + Main.backgroundReinforcementManuallyEntries.size());
	}
}