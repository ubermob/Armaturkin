package armaturkin.core;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Properties;

public class Root {

	static Os os = Os.WINDOWS;
	private static final Properties PROPERTIES = new Properties();
	public static String programRootPath;
	static Character diskLetter;
	static String absolutePath;

	public static void loadProperties() {
		try (InputStream resource = Main.class.getResourceAsStream("/Root_structure.xml")) {
			PROPERTIES.loadFromXML(resource);
		} catch (Exception e) {
			Main.app.log(e);
		}
		if (os == Os.WINDOWS) {
			programRootPath = getProperty("program_root_path_windows");
		}
		if (os == Os.LINUX) {
			programRootPath = System.getProperty("user.home") + getProperty("program_root_path_extend");
		}
	}

	public static void checkDirectories() throws IOException {
		if (absolutePath != null) {
			diskLetter = null;
			programRootPath = absolutePath + getProperty("program_root_path_extend");
		}
		if (os == Os.WINDOWS && diskLetter != null) {
			programRootPath = diskLetter + programRootPath.substring(1);
		}
		if (Files.notExists(Path.of(programRootPath))) {
			Files.createDirectory(Path.of(programRootPath));
		}
		Iterator<Object> objectIterator = PROPERTIES.keys().asIterator();
		while (objectIterator.hasNext()) {
			String key = (String) objectIterator.next();
			if (key.contains("directory")) {
				Path path = Path.of(programRootPath, getProperty(key));
				if (Files.notExists(path)) {
					Files.createDirectory(path);
				}
			}
		}
	}

	/**
	 * @param key the property key
	 * @return {@link String} the value in this property list with the specified key value.
	 */
	public static String getProperty(String key) {
		return PROPERTIES.getProperty(key);
	}

	enum Os {
		WINDOWS, LINUX
	}
}