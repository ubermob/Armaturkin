package armaturkin.httpserver.handler;

import armaturkin.utils.HttpServerUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Andrey Korneychuk on 19-Dec-21
 * @version 1.0
 */
public class FaviconHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		InputStream resource = getClass().getResourceAsStream("/Icon.png");
		HttpServerUtil.exchangeWorker(exchange, resource.readAllBytes());
	}
}