package armaturkin.core;

import armaturkin.view.AddonViews;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;

public class SummaryRedirectManager {

	static final int DEFAULT_VALUE = 6;
	static int redirectTo = DEFAULT_VALUE;
	static Label[] labels = Main.controller.getSummaryLabels();

	public static void setRedirectTo(int i) {
		if (redirectTo != i) {
			redirectTo = i;
			setLine(i);
			Main.controller.setRedirectLineOpacity(1);
			Main.controller.setArrowOpacity(1);
		} else {
			redirectTo = DEFAULT_VALUE;
			Main.controller.setRedirectLineOpacity(0);
			Main.controller.setArrowOpacity(0);
		}
	}

	static void setLine(int i) {
		Line line = AddonViews.redirectLine;
		line.setStartX(labels[5].getLayoutX() + labels[5].getWidth() / 2);
		line.setStartY(labels[5].getLayoutY() + labels[5].getHeight() / 2);
		line.setEndX(labels[i - 1].getLayoutX() + labels[i - 1].getWidth() / 2);
		line.setEndY(labels[i - 1].getLayoutY() + labels[i - 1].getHeight() / 2);
		AddonViews.arrow.refresh(line);
	}
}