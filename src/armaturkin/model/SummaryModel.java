package armaturkin.model;

import java.util.HashMap;
import java.util.List;

/**
 * @author Andrey Korneychuk on 08-Jan-22
 * @version 1.0
 */
public interface SummaryModel {

	HashMap<Integer, List<String>> getSummaryPaths();
}