package armaturkin.httpserver.handler;

import armaturkin.summaryoutput.ContentContainer;
import armaturkin.utils.HttpServerUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Andrey Korneychuk on 19-Dec-21
 * @version 1.0
 */
public class ContentPageHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String string = HttpServerUtil.getString(getClass().getResourceAsStream("/http/html_template.txt"));
		StringBuilder builder = new StringBuilder();
		builder.append("<table border=\"solid\">");
		String[][] content = ContentContainer.getContentAsArray();
		for (var subArray : content) {
			builder.append("<tr border=\"solid\">");
			for (var element : subArray) {
				builder.append("<td>").append(element).append("</td>");
			}
			builder.append("</tr>");
		}
		builder.append("</table>");
		string = string.formatted(builder.toString());
		HttpServerUtil.exchangeWorker(exchange, string.getBytes(StandardCharsets.UTF_8));
	}
}