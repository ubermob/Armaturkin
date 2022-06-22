package armaturkin.httpserver.handler;

import armaturkin.core.Main;
import armaturkin.utils.HttpServerUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Andrey Korneychuk on 19-Dec-21
 * @version 1.0
 */
public class LogPageHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String string = HttpServerUtil.getString(getClass().getResourceAsStream("/http/html_template.txt"));
		StringBuilder builder = new StringBuilder();
		builder.append("<ul>");
		for (var v : Main.app.getLogService().getList()) {
			builder.append("<pre>").append(v).append("</pre>");
		}
		builder.append("</ul>");
		string = string.formatted(builder.toString());
		HttpServerUtil.exchangeWorker(exchange, string.getBytes(StandardCharsets.UTF_8));
	}
}