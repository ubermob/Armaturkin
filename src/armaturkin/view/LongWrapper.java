package armaturkin.view;

public class LongWrapper {

	private final long number;
	private String string;

	public LongWrapper(long l) {
		this.number = l;
		calculation();
	}

	@Override
	public String toString() {
		return string;
	}

	private void calculation() {
		string = String.valueOf(number);
		if (string.length() > 3) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(string, 0, string.length() - 3);
			stringBuilder.append(" ");
			stringBuilder.append(string.substring(string.length() - 3));
			string = stringBuilder.toString();
		}
	}
}