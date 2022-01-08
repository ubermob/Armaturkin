package armaturkin.summaryoutput;

import armaturkin.core.Main;
import armaturkin.interfaces.LightInfo;
import armaturkin.reinforcement.RFClass;
import armaturkin.reinforcement.ReinforcementLiteInfo;
import armaturkin.steelcomponent.Image;

public class ContentHeadEntry {

	private final LightInfo entry;

	public ContentHeadEntry(LightInfo entry) {
		this.entry = entry;
	}

	public String getPrettyString() {
		if (entry instanceof ReinforcementLiteInfo) {
			ReinforcementLiteInfo entry = (ReinforcementLiteInfo) this.entry;
			return Main.app.getProperty("content_head_pretty_string_reinforcement").formatted(entry.getDiameter(), entry.getRfClass());
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

	public boolean isInstanceOfLiteInfo() {
		return entry instanceof ReinforcementLiteInfo;
	}

	public boolean isInstanceOfImage() {
		return entry instanceof Image;
	}

	public RFClass getRfClass() {
		return ((ReinforcementLiteInfo) entry).getRfClass();
	}

	public Image getImage() {
		return (Image) entry;
	}

	public int getDiameter() {
		if (entry instanceof ReinforcementLiteInfo) {
			return ((ReinforcementLiteInfo) entry).getDiameter();
		}
		return -1;
	}
}