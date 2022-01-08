package armaturkin.httpserver;

/**
 * @author Andrey Korneychuk on 08-Jan-22
 * @version 1.0
 */
public class HttpServerWrapper {

	private HttpServer server;

	public HttpServer getServer() {
		return server;
	}

	public void setServer(HttpServer server) {
		this.server = server;
	}
}