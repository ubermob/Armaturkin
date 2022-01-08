package armaturkin.summaryoutput;

import armaturkin.core.Main;
import armaturkin.core.Reader;
import armaturkin.reinforcement.RFClass;
import armaturkin.steelcomponent.HotRolledSteelType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContentHeadPlacement {

	private final List<Integer> hashes = new ArrayList<>();
	private final List<Object> blocks = new ArrayList<>();

	public ContentHeadPlacement() {
		try {
			for (var line : Reader.read(this.getClass().getResourceAsStream("/RF_hash_code_list.txt"))) {
				if (line.length() != 0 && !line.startsWith("//")) {
					if (!line.startsWith("[") && !line.startsWith("from")) {
						hashes.add(Integer.parseInt(line));
					}
					if (line.startsWith("from")) {
						String[] splitted = line.substring(5).split(" to ");
						// "to" value included to range
						for (int i = Integer.parseInt(splitted[0]); i <= Integer.parseInt(splitted[1]); i++) {
							hashes.add(i);
						}
					}
					if (line.startsWith("[")) {
						String value = line.substring((line.indexOf(":") + 2), line.indexOf("]"));
						switch (value) {
							case "A240" -> blocks.add(RFClass.A240);
							case "A400" -> blocks.add(RFClass.A400);
							case "A500" -> blocks.add(RFClass.A500);
							case "A500S" -> blocks.add(RFClass.A500S);
							case "A600" -> blocks.add(RFClass.A600);
							case "equal-leg angle" -> blocks.add(HotRolledSteelType.EQUAL_LEG_ANGLE);
							case "unequal-leg angle" -> blocks.add(HotRolledSteelType.UNEQUAL_LEG_ANGLE);
							case "sheet" -> blocks.add(HotRolledSteelType.SHEET);
						}
					}
				}
			}
		} catch (IOException e) {
			Main.app.log(e);
		}
	}

	public List<Integer> getHashes() {
		return hashes;
	}

	public Object[] getBlocks() {
		return getBlocksAsList().toArray();
	}

	private List<Object> getBlocksAsList() {
		return blocks;
	}
}