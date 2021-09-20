package armaturkin.utils;

/**
 * @author Andrey Korneychuk on 20-Sep-21
 * @version 1.0
 */
public class WholeNumber {

	/**
	 * @param number checked number
	 * @return true - if floating number is a whole (example: 2.0).
	 * false - if floating number is not a whole (example: 2.25).
	 */
	public static boolean isAWholeNumber(double number) {
		return number - Math.round(number) == 0;
	}

	/**
	 * @param number checked number
	 * @return true - if floating number is a whole (example: 2.0).
	 * false - if floating number is not a whole (example: 2.25).
	 */
	public static boolean isAWholeNumber(float number) {
		return number - Math.round(number) == 0;
	}
}