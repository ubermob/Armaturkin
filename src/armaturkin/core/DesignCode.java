package armaturkin.core;

import utools.propertiestools.UtfProperties;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class DesignCode {

	private static final Properties PROPERTIES = new Properties();
	private static final String FILE_NAME = "Design_code_properties.txt";
	private static boolean isUpdate = false;

	public static void loadProperties() {
		try {
			InputStream inputStream;
			Path check = Path.of(Root.programRootPath, Root.getProperty("update_data_directory"), FILE_NAME);
			if (Files.exists(check)) {
				inputStream = new FileInputStream(check.toString());
				isUpdate = true;
			} else {
				inputStream = Main.class.getResourceAsStream("/" + FILE_NAME);
			}
			UtfProperties.fill(PROPERTIES, inputStream);
			inputStream.close();
		} catch (Exception e) {
			Main.log.add(e);
		}
	}

	/**
	 * @param key the property key
	 * @return {@link String} the value in this property list with the specified key value.
	 */
	public static String getProperty(String key) {
		return PROPERTIES.getProperty(key);
	}

	/**
	 * @return {@code boolean}. True if properties load from Update folder.
	 * False - default.
	 */
	public static boolean isUpdate() {
		return isUpdate;
	}
}