package armaturkin.httpserver.handler;

import armaturkin.utils.HttpServerUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Andrey Korneychuk on 19-Dec-21
 * @version 1.0
 */
public class MainPageHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String string = HttpServerUtil.getString(getClass().getResourceAsStream("/http/index.html"));
		HttpServerUtil.exchangeWorker(exchange, string.getBytes(StandardCharsets.UTF_8));
	}
}