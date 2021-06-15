import java.util.ArrayList;
import java.util.List;

public class ContentHead {

	private List<ReinforcementLiteInfo> list;

	public ContentHead() {
		list = new ArrayList<>();
		RFClass[] rfClass = {RFClass.A240, RFClass.A400, RFClass.A500, RFClass.A500S, RFClass.A600};
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
}