package armaturkin.utils;

import armaturkin.core.Reader;

import java.io.IOException;
import java.util.List;

/**
 * @author Andrey Korneychuk on 26-May-22
 * @version 1.0
 */
public class InAppHelpArray {

	private static String[] help;

	public static void load() throws IOException {
		List<String> strings = Reader.readFromInternalSource("/In_app_help_array.txt");
		StringBuilder sb = new StringBuilder();
		for (var v : strings) {
			sb.append(v).append("\n");
		}
		sb.deleteCharAt(sb.length() - 1); // delete last char '\n'

		String delimiter = "<<--==-->>";
		help = sb.toString().split(delimiter);

		for (int i = 0; i < help.length; i++) {
			if (help[i].charAt(0) == '\n') {
				help[i] = help[i].substring(1);
			}
		}
	}

	public static String getString(int i) {
		try {
			return help[i - 1];
		} catch (Exception ignored) {
		}
		return "";
	}
}