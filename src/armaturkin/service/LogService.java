package armaturkin.service;

import armaturkin.core.Log;

import java.util.List;

/**
 * @author Andrey Korneychuk on 06-Jan-22
 * @version 1.0
 */
public interface LogService {

	void add(String string);

	void add(Exception exception);

	List<String> getList();

	void merge(Log log);
}