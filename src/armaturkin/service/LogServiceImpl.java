package armaturkin.service;

import armaturkin.core.Log;

import java.util.List;

/**
 * @author Andrey Korneychuk on 06-Jan-22
 * @version 1.0
 */
public class LogServiceImpl implements LogService {

	private final Log log;

	public LogServiceImpl() {
		log = new Log();
	}

	@Override
	public void add(String string) {
		log.add(string);
	}

	@Override
	public void add(String string, boolean isLoggable) {
		log.add(string, isLoggable);
	}

	@Override
	public void add(Exception exception) {
		log.add(exception);
	}

	@Override
	public void add(Exception exception, boolean isLoggable) {
		log.add(exception, isLoggable);
	}

	@Override
	public List<String> getList() {
		return log.getList();
	}

	@Override
	public void merge(Log log) {
		this.log.merge(log);
	}
}