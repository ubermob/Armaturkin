package armaturkin.httpserver.handler;

import armaturkin.core.Main;
import armaturkin.utils.HttpServerUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Andrey Korneychuk on 19-Dec-21
 * @version 1.0
 */
public class NotePageHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		// TODO: replace file and realize cache
		String string = HttpServerUtil.getString(Files.readAllLines(Path.of("D:/VSCode_projects/Armaturkin/html_template.txt")));
		StringBuilder builder = new StringBuilder();
		builder.append("<table border=\"solid\">").append("<tr>");
		builder.append("<td width=\"40%\">").append("<h2 align=\"center\">Короткие уведомления</h2>");

		builder.append("<ul>");
		for (var v : Main.getNotification().split("\n")) {
			builder.append("<li>").append(v).append("</li>");
		}
		builder.append("</ul>");

		builder.append("</td>");

		builder.append("<td width=\"40%\">").append("<h2 align=\"center\">Все уведомления</h2>");

		builder.append("<ul>");
		for (var v : Main.getAllNotifications()) {
			builder.append("<li>").append(v).append("</li>");
		}
		builder.append("</ul>");

		builder.append("</td>");
		builder.append("</tr>").append("</table>");
		string = string.formatted(builder.toString());
		HttpServerUtil.exchangeWorker(exchange, string.getBytes());
	}
}