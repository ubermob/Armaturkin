package armaturkin.utils;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Andrey Korneychuk on 19-Dec-21
 * @version 1.0
 */
public class HttpServerUtil {

	public static synchronized void exchangeWorker(HttpExchange exchange, String code) throws IOException {
		OutputStream outputStream = exchange.getResponseBody();
		exchange.sendResponseHeaders(200, code.length());
		outputStream.write(code.getBytes());
		outputStream.close();
	}

	public static synchronized void exchangeWorker(HttpExchange exchange, byte[] bytes) throws IOException {
		OutputStream outputStream = exchange.getResponseBody();
		exchange.sendResponseHeaders(200, bytes.length);
		outputStream.write(bytes);
		outputStream.close();
	}

	public static synchronized String getString(List<String> list) {
		StringBuilder builder = new StringBuilder();
		for (var v : list) {
			builder.append(v);
		}
		return builder.toString();
	}

	public static synchronized String getString(InputStream inputStream) throws IOException {
		byte[] bytes = inputStream.readAllBytes();
		inputStream.close();
		return new String(bytes, StandardCharsets.UTF_8);
	}
}