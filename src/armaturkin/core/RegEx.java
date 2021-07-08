package armaturkin.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegEx {
	public static synchronized int parseNumberOfElements(String input) {
		try {
			Pattern pattern = Pattern.compile("\\(\\d++ шт\\.\\)", Pattern.UNICODE_CASE);
			Matcher matcher = pattern.matcher(input);
			matcher.find();
			String string = input.substring(matcher.start(), matcher.end());
			pattern = Pattern.compile("\\d++", Pattern.UNICODE_CASE);
			matcher = pattern.matcher(string);
			matcher.find();
			return Integer.parseInt(string.substring(matcher.start(), matcher.end()));
		} catch (Exception e) {
			Main.log.add(e);
			return 0;
		}
	}
}