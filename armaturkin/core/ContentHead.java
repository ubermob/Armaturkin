package armaturkin.core;

import java.util.ArrayList;
import java.util.List;

public class ContentHead {

	private List<ReinforcementLiteInfo> list;
	private final RFClass[] rfClass = {RFClass.A240, RFClass.A400, RFClass.A500, RFClass.A500S, RFClass.A600};

	public ContentHead() {
		list = new ArrayList<>();
		for (RFClass r : rfClass) {
			for (int i: StandardsRepository.diameter) {
				list.add(new ReinforcementLiteInfo(i, r, 0.0));
			}
		}
	}

	public ReinforcementLiteInfo get(int index) {
		return list.get(index);
	}

	public void newList(List<ReinforcementLiteInfo> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return list.toString();
	}

	public String toPrettyString() {
		String result = "";
		for (ReinforcementLiteInfo rein : list) {
			result += Main.properties.getProperty("content_head_pretty_string").formatted(rein.getDiameter(), rein.getRfClass());
		}
		return result.substring(0, (result.length() - 1));
	}

	public boolean[] getBlockFullness() {
		boolean[] result = new boolean[rfClass.length];
		for (int i = 0; i < result.length; i++) {
			for (ReinforcementLiteInfo r : list) {
				if (r.getRfClass() == rfClass[i]) {
					result[i] = true;
					break;
				}
			}
		}
		return result;
	}

	public RFClass getRFClass(int i) {
		return rfClass[i];
	}

	public int[] getDiameters(RFClass rfClass) {
		int counter = 0;
		for (ReinforcementLiteInfo r : list) {
			if (r.getRfClass() == rfClass) {
				counter++;
			}
		}
		int[] result = new int[counter];
		int resultIndex = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getRfClass() == rfClass) {
				result[resultIndex++] = list.get(i).getDiameter();
			}
		}
		return result;
	}

	public int[] getIndexes(RFClass rfClass) {
		int counter = 0;
		for (ReinforcementLiteInfo r : list) {
			if (r.getRfClass() == rfClass) {
				counter++;
			}
		}
		int[] result = new int[counter];
		int resultIndex = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getRfClass() == rfClass) {
				result[resultIndex++] = i;
			}
		}
		return result;
	}
}