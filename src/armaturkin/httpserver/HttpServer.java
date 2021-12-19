package armaturkin.httpserver;

import armaturkin.core.Main;
import armaturkin.httpserver.handler.*;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author Andrey Korneychuk on 19-Dec-21
 * @version 1.0
 */
public class HttpServer implements Runnable {

	private final com.sun.net.httpserver.HttpServer server;
	private final String hostname;
	private final int port;

	public HttpServer(String hostname, int port) throws IOException {
		this.hostname = hostname;
		this.port = port;
		server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(hostname, port), 0);
		server.createContext("/", new MainPageHandler());
		server.createContext("/note", new NotePageHandler());
		server.createContext("/content", new ContentPageHandler());
		server.createContext("/favicon.png", new FaviconHandler());
		server.createContext("/style.css", new StyleHandler());
	}

	@Override
	public void run() {
		server.start();
		Main.log.add("HTTP Server start, hostname: " + hostname + ", port: " + port);
	}

	public void stop() {
		server.stop(0);
	}
}