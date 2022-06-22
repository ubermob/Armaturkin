package armaturkin.workers;

/**
 * @author Andrey Korneychuk on 02-Jun-22
 * @version 1.0
 */
public abstract class AbstractWorker {

	public abstract void run() throws WorkerException;
}