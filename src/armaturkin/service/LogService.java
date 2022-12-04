package armaturkin.service;

import armaturkin.core.Log;

import java.util.List;

/**
 * @author Andrey Korneychuk on 06-Jan-22
 * @version 1.0
 */
public interface LogService {

	void add(String string);

	void add(String string, boolean isLoggable);

	void add(Exception exception);

	void add(Exception exception, boolean isLoggable);

	List<String> getList();

	void merge(Log log);
}