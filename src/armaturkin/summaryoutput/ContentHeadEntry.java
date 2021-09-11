package armaturkin.summaryoutput;

import armaturkin.reinforcement.RFClass;
import armaturkin.steelcomponent.SteelProductLiteInfo;
import armaturkin.reinforcement.ReinforcementLiteInfo;

import static armaturkin.core.Main.getProperty;

public class ContentHeadEntry {

	private final Object entry;

	public ContentHeadEntry(Object entry) {
		if (entry instanceof ReinforcementLiteInfo || entry instanceof SteelProductLiteInfo) {
			this.entry = entry;
		} else {
			throw new ClassFormatError();
		}
	}

	public String getPrettyString() {
		if (entry instanceof ReinforcementLiteInfo) {
			ReinforcementLiteInfo entry = (ReinforcementLiteInfo) this.entry;
			return getProperty("content_head_pretty_string_reinforcement").formatted(entry.getDiameter(), entry.getRfClass());
		} else {
			// TODO
			return null;
		}
	}

	public RFClass getRfClass() {
		if (entry instanceof ReinforcementLiteInfo) {
			return ((ReinforcementLiteInfo) entry).getRfClass();
		}
		return null; // TODO
	}

	public int getDiameter() {
		if (entry instanceof ReinforcementLiteInfo) {
			return ((ReinforcementLiteInfo) entry).getDiameter();
		}
		return -1; // TODO
	}
}