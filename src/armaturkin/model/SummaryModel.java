package armaturkin.model;

import armaturkin.summaryoutput.SummaryBuilder;

import java.util.HashMap;
import java.util.List;

/**
 * @author Andrey Korneychuk on 08-Jan-22
 * @version 1.0
 */
public interface SummaryModel {

	HashMap<Integer, List<String>> getSummaryPaths();

	void setSummaryBuilderList(List<SummaryBuilder> summaryBuilderList);

	List<SummaryBuilder> getSummaryBuilderList();

	List<String> getUserContentRowList();

	boolean isSummaryBuilderListNotNull();

	void removeSummaryBuilderList();

	void setPathToSummaryBuilderFile(String path);

	String getPathToSummaryBuilderFile();

	int getSummaryPathsKeyCounters();
}