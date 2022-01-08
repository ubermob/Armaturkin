package armaturkin.service;

import armaturkin.controller.Controller;
import armaturkin.core.App;
import armaturkin.core.Configuration;

/**
 * @author Andrey Korneychuk on 08-Jan-22
 * @version 1.0
 */
public abstract class AbstractService {

	protected App app;
	protected Configuration config;
	protected Controller controller;

	public void setApp(App app) {
		this.app = app;
	}

	public void setConfig(Configuration config) {
		this.config = config;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}
}