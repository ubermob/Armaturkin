package armaturkin.utils;

/**
 * Replace end {@code "\n"} symbol
 */
public class NewLineReplacer {
	/**
	 * @param input target {@link String}
	 * @return replaced {@link String}
	 */
	public static String replace(String input) {
		while (input.endsWith("\n")) {
			input = input.substring(0, input.length() - 1);
		}
		return input;
	}
}