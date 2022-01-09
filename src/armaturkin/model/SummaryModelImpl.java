package armaturkin.model;

import armaturkin.summaryoutput.SummaryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 3th Tab
 *
 * @author Andrey Korneychuk on 08-Jan-22
 * @version 1.0
 */
public class SummaryModelImpl implements SummaryModel {

	private final HashMap<Integer, List<String>> summaryPaths;
	private List<SummaryBuilder> summaryBuilderList;
	private List<String> userContentRowList;
	private String pathToSummaryBuilderFile;

	public SummaryModelImpl() {
		summaryPaths = new HashMap<>();
	}

	@Override
	public HashMap<Integer, List<String>> getSummaryPaths() {
		return summaryPaths;
	}

	@Override
	public void setSummaryBuilderList(List<SummaryBuilder> summaryBuilderList) {
		this.summaryBuilderList = summaryBuilderList;
		userContentRowList = new ArrayList<>(summaryBuilderList.size());
		for (var v : summaryBuilderList) {
			userContentRowList.add(v.getTitle());
		}
	}

	@Override
	public List<SummaryBuilder> getSummaryBuilderList() {
		return summaryBuilderList;
	}

	@Override
	public List<String> getUserContentRowList() {
		return userContentRowList;
	}

	@Override
	public boolean isSummaryBuilderListNotNull() {
		return summaryBuilderList != null;
	}

	@Override
	public void removeSummaryBuilderList() {
		summaryBuilderList = null;
		userContentRowList = null;
		pathToSummaryBuilderFile = null;
	}

	@Override
	public void setPathToSummaryBuilderFile(String path) {
		pathToSummaryBuilderFile = path;
	}

	@Override
	public String getPathToSummaryBuilderFile() {
		return pathToSummaryBuilderFile;
	}

	@Override
	public int getSummaryPathsKeyCounters() {
		if (isSummaryBuilderListNotNull()) {
			return summaryBuilderList.size();
		}
		// Default value
		return 8;
	}
}