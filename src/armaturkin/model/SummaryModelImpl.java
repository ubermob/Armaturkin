package armaturkin.model;

import java.util.HashMap;
import java.util.List;

/**
 * 3th Tab
 *
 * @author Andrey Korneychuk on 08-Jan-22
 * @version 1.0
 */
public class SummaryModelImpl implements SummaryModel {

	private volatile HashMap<Integer, List<String>> summaryPaths;

	public SummaryModelImpl() {
		summaryPaths = new HashMap<>();
	}

	@Override
	public HashMap<Integer, List<String>> getSummaryPaths() {
		return summaryPaths;
	}
}