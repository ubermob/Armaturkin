package armaturkin.core;

import utools.propertiestools.UtfProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class DesignCode {

	private static Properties properties = new Properties();
	private static final String FILE_NAME = "Design_code_properties.txt";
	private static boolean isUpdate = false;

	public static void loadProperties() {
		try {
			Path check = Path.of(Root.programRootPath, Root.getProperty("update_data_directory"), FILE_NAME);
			if (Files.exists(check)) {
				properties = UtfProperties.getExternalUtfProperties(check.toString());
				isUpdate = true;
			} else {
				properties = UtfProperties.getInternalUtfProperties("/" + FILE_NAME);
			}
		} catch (IOException e) {
			Main.app.log(e);
		}
	}

	/**
	 * @param key the property key
	 * @return {@link String} the value in this property list with the specified key value.
	 */
	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	/**
	 * @return {@code boolean}. True if properties load from Update folder.
	 * False - default.
	 */
	public static boolean isUpdate() {
		return isUpdate;
	}
}