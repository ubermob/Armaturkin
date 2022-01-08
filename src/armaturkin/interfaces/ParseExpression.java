package armaturkin.interfaces;

import armaturkin.core.Main;
import org.apache.poi.ss.usermodel.Cell;

public interface ParseExpression {

	default int parseIntFromExpression(Cell cell) throws UnsupportedOperationException {
		String string = cell.getStringCellValue();
		if (string.contains("+")) {
			String[] splitted = Parse.it(string, "\\+");
			int result = 0;
			for (var line : splitted) {
				result += Integer.parseInt(line);
			}
			return result;
		} else if (Condition.multiplicationCondition(string)) {
			String[] splitted = Parse.it(string, Condition.condition.ch);
			if (splitted.length != 2) {
				throw new UnsupportedOperationException(
						Main.app.getProperty("interface_exception_throw").formatted(string));
			}
			return Integer.parseInt(splitted[0]) * Integer.parseInt(splitted[1]);
		} else {
			throw new UnsupportedOperationException(
					Main.app.getProperty("interface_exception_throw").formatted(string));
		}
	}

	class Condition {

		private static Condition condition;

		private String ch;

		private Condition(String string) {
			action(string);
		}

		private void action(String string) {
			String[] chars = {"*", "x", "X", // English letters
					"х", "Х"}; // Russian letters
			for (var c : chars) {
				if (string.contains(c)) {
					condition = this;
					ch = c;
					if (c.equals(chars[0])) {
						ch = "\\*";
					}
					break;
				}
			}
		}

		private static boolean multiplicationCondition(String string) {
			condition = null;
			new Condition(string);
			return Condition.condition != null;
		}
	}

	class Parse {

		private static String[] it(String string, String regex) {
			return string.replace(" ", "").split(regex);
		}
	}
}