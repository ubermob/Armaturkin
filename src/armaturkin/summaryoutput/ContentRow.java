package armaturkin.summaryoutput;

import armaturkin.core.Main;

import java.util.Arrays;
import java.util.List;

public class ContentRow {

	private List<String> list;

	public ContentRow() {
		try {
			list = Arrays.asList(Main.app.getProperty("default_content_row").split("-"));
		} catch (Exception e) {
			Main.app.log(e);
		}
	}

	public ContentRow(List<String> list) {
		this.list = list;
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

	public String[] getRowStrings() {
		String[] result = new String[list.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = list.get(i);
		}
		return result;
	}

	public int getSize() {
		return list.size();
	}
}