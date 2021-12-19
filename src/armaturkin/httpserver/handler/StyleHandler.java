package armaturkin.httpserver.handler;

import armaturkin.core.Main;
import armaturkin.utils.HttpServerUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Andrey Korneychuk on 19-Dec-21
 * @version 1.0
 */
public class StyleHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		// TODO: replace file and realize cache
		var file = Files.readAllLines(Path.of("D:/VSCode_projects/Armaturkin/css_template.txt"));
		String string = HttpServerUtil.getString(file).formatted(Main.config.getBackgroundColor(), Main.config.getTextColor());
		HttpServerUtil.exchangeWorker(exchange, string.getBytes(StandardCharsets.UTF_8));
	}
}