package armaturkin.summaryoutput;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrey Korneychuk on 05-Jan-22
 * @version 1.0
 */
public class SummaryBuilder {

	private final String title;
	private final List<String> pathToFiles;
	private final List<String> pathToDirectories;
	private int type;
	private int repeat;

	public SummaryBuilder(String title) {
		this.title = title;
		pathToFiles = new ArrayList<>();
		pathToDirectories = new ArrayList<>();
		type = SummaryThreadPool.PRETTY;
		repeat = 1;
	}

	public String getTitle() {
		return title;
	}

	public void addPathToFile(String path) {
		pathToFiles.add(path);
	}

	public List<String> getPathToFiles() {
		return pathToFiles;
	}

	public void addPathToDirectory(String path) {
		pathToDirectories.add(path);
	}

	public List<String> getPathToDirectories() {
		return pathToDirectories;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRepeat() {
		return repeat;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	@Override
	public String toString() {
		return "SummaryBuilder{" +
				"title='" + title + '\'' +
				", pathToFiles=" + pathToFiles +
				", pathToDirectories=" + pathToDirectories +
				", type=" + type +
				", repeat=" + repeat +
				'}';
	}
}