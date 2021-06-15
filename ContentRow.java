import java.util.Arrays;
import java.util.List;

public class ContentRow {

	private List<String> list;

	public ContentRow() {
		try {
			list = Arrays.asList(Main.properties.getProperty("content_row").split("-"));
		} catch (Exception e) {
			Main.log.add(e);
		}
	}

	public String get(int index) {
		return list.get(index);
	}

	public void newList(List<String> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return list.toString();
	}
}