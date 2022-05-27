package armaturkin.utils;

/**
 * String utils
 *
 * @author Andrey Korneychuk
 * @version 1.1
 */
public class StringUtil {

	/**
	 * Replace end {@code "\n"} symbol in the string
	 *
	 * @param input target {@link String}
	 * @return replaced {@link String}
	 */
	public static String replaceNewLine(String input) {
		while (input.endsWith("\n")) {
			input = cutEnd(input, 1);
		}
		return input;
	}

	/**
	 * Remove N symbols from end of the string
	 *
	 * @param input  string for cut
	 * @param number number of symbols remove.
	 *               Must be not negative.
	 *               If number equals zero return same string.
	 * @return cutting string
	 * @throws IllegalArgumentException        if number less or equal zero
	 * @throws StringIndexOutOfBoundsException if number greater than string length
	 */
	public static String cutEnd(String input, int number) throws IllegalArgumentException {
		if (number < 0) {
			throw new IllegalArgumentException("number: \'" + number + "\' less than zero");
		}
		return input.substring(0, input.length() - number);
	}

	/**
	 * Remove the one last char.
	 *
	 * @param input string for cut
	 * @param c     foreign char
	 * @return cutting string
	 */
	public static String cutEndIfLastCharIs(String input, char c) {
		if (input.charAt(input.length() - 1) == c) {
			return cutEnd(input, 1);
		}
		return input;
	}
}