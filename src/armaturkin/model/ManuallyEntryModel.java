package armaturkin.model;

import armaturkin.manuallyentry.ManuallyEntry;

import java.util.List;

/**
 * @author Andrey Korneychuk on 08-Jan-22
 * @version 1.0
 */
public interface ManuallyEntryModel {

	List<ManuallyEntry> getManuallySummaryEntries();

	List<ManuallyEntry> getBackgroundReinforcementManuallyEntries();
}