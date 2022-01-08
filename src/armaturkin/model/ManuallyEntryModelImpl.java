package armaturkin.model;

import armaturkin.manuallyentry.ManuallyEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * 4th Tab
 *
 * @author Andrey Korneychuk on 08-Jan-22
 * @version 1.0
 */
public class ManuallyEntryModelImpl implements ManuallyEntryModel {

	private List<ManuallyEntry> manuallySummaryEntries;
	private List<ManuallyEntry> backgroundReinforcementManuallyEntries;

	public ManuallyEntryModelImpl() {
		manuallySummaryEntries = new ArrayList<>();
		backgroundReinforcementManuallyEntries = new ArrayList<>();
	}

	@Override
	public List<ManuallyEntry> getManuallySummaryEntries() {
		return manuallySummaryEntries;
	}

	@Override
	public List<ManuallyEntry> getBackgroundReinforcementManuallyEntries() {
		return backgroundReinforcementManuallyEntries;
	}
}