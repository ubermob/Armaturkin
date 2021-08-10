package armaturkin.core;

import armaturkin.reinforcement.RFClass;

public class ManuallyEntryAdaptor {

	private final ManuallyEntry manuallyEntry;

	public ManuallyEntryAdaptor(ManuallyEntry manuallyEntry) {
		this.manuallyEntry = manuallyEntry;
	}

	public double getLength() {
		return manuallyEntry.getMass();
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