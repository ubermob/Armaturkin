package armaturkin.utils;

/**
 * Replace unacceptable Windows file name symbols.
 */
public class UnacceptableSymbolReplacer {

	public static final String[] TARGET = {"\\", "/", ":", "*", "?", "\"", "<", ">", "|"}; // Size == 9
	public static final String REPLACEMENT = "";

	/**
	 * @param input {@link String} File name.
	 * @return replaced {@link String}.
	 */
	public static String replace(String input) {
		for (String s : TARGET) {
			input = input.replace(s, REPLACEMENT);
		}
		return input;
	}
}