package armaturkin.httpserver.handler;

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
public class MainPageHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		// TODO: replace file and realize cache
		var file = Files.readAllLines(Path.of("D:/VSCode_projects/Armaturkin/index.html"), StandardCharsets.UTF_8);
		HttpServerUtil.exchangeWorker(exchange, HttpServerUtil.getString(file).getBytes());
	}
}