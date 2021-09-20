package armaturkin.manuallyentry;

import armaturkin.reinforcement.RFClass;

public class ManuallyEntryAdaptor {

	private final ManuallyEntry manuallyEntry;

	public ManuallyEntryAdaptor(ManuallyEntry manuallyEntry) {
		this.manuallyEntry = manuallyEntry;
	}

	public double getLength() {
		return manuallyEntry.getMassReinforcement();
	}

	public ManuallyEntry getManuallyEntry() {
		return manuallyEntry;
	}

	public int getDiameter() {
		return manuallyEntry.getDiameter();
	}

	public RFClass getRfClass() {
		return manuallyEntry.getRfClass();
	}
}