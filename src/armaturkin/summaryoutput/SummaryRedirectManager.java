package armaturkin.summaryoutput;

import javafx.scene.control.Label;

public class SummaryRedirectManager {

	static final byte DEFAULT_VALUE = 6;
	static byte redirectTo = DEFAULT_VALUE;
	private static Label currentRedirectLabel; // it is dashed
	private static String previousStyle;

	public static void setRedirectTo(int i, Label label) {
		if (redirectTo != i) {
			// Different label
			redirectTo = (byte) i;
			if (currentRedirectLabel != null) {
				currentRedirectLabel.setStyle(previousStyle);
			}
			previousStyle = label.getStyle();
			label.setStyle(previousStyle + "-fx-border-style: dashed;");
			currentRedirectLabel = label;
		} else {
			// Same label
			redirectTo = DEFAULT_VALUE;
			currentRedirectLabel.setStyle(previousStyle);
			currentRedirectLabel = null;
		}
	}

	public static byte getRedirectTo() {
		return redirectTo;
	}
}