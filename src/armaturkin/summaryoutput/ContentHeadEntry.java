package armaturkin.summaryoutput;

import armaturkin.interfaces.LightInfo;
import armaturkin.reinforcement.RFClass;
import armaturkin.steelcomponent.Image;
import armaturkin.reinforcement.ReinforcementLiteInfo;

import static armaturkin.core.Main.getProperty;

public class ContentHeadEntry {

	private final LightInfo entry;

	public ContentHeadEntry(LightInfo entry) {
		this.entry = entry;
	}

	public String getPrettyString() {
		if (entry instanceof ReinforcementLiteInfo) {
			ReinforcementLiteInfo entry = (ReinforcementLiteInfo) this.entry;
			return getProperty("content_head_pretty_string_reinforcement").formatted(entry.getDiameter(), entry.getRfClass());
		} else {
			// instanceof Image
			Image entry = (Image) this.entry;
			if (entry == null) {
				return "-";
			}
			return entry.toString();
		}
	}

	public Object getType() {
		if (entry instanceof ReinforcementLiteInfo) {
			return ((ReinforcementLiteInfo) entry).getRfClass();
		} else {
			// instanceof Image
			return ((Image) entry).getHotRolledSteelType();
		}
	}

	public RFClass getRfClass() {
		return ((ReinforcementLiteInfo) entry).getRfClass();
	}

	public int getDiameter() {
		if (entry instanceof ReinforcementLiteInfo) {
			return ((ReinforcementLiteInfo) entry).getDiameter();
		}
		return -1; // TODO ???
	}
}