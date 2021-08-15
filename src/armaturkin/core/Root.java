package armaturkin.core;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Properties;

public class Root {

	private static final Properties PROPERTIES = new Properties();
	public static String programRootPath;
	static Character diskLetter;

	public static void loadProperties() {
		try {
			InputStream resource = Main.class.getResourceAsStream("/Root_structure.xml");
			PROPERTIES.loadFromXML(resource);
			resource.close();
		} catch (Exception e) {
			Main.log.add(e);
		}
		programRootPath = PROPERTIES.getProperty("program_root_path");
	}

	public static void checkDirectories() throws IOException {
		if (diskLetter != null) {
			programRootPath = diskLetter + programRootPath.substring(1);
		}
		if (Files.notExists(Path.of(programRootPath))) {
			Files.createDirectory(Path.of(programRootPath));
		}
		Iterator<Object> objectIterator = PROPERTIES.keys().asIterator();
		while (objectIterator.hasNext()) {
			String key = (String) objectIterator.next();
			if (key.contains("directory")) {
				Path path = Path.of(programRootPath, get(key));
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
	public static String get(String key) {
		return PROPERTIES.getProperty(key);
	}
}