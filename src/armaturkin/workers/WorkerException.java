package armaturkin.workers;

/**
 * @author Andrey Korneychuk on 02-Jun-22
 * @version 1.0
 */
public class WorkerException extends Exception {

	public WorkerException() {
		super();
	}

	public WorkerException(String message) {
		super(message);
	}
}