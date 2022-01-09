package armaturkin.view;

import javafx.scene.control.Label;
import javafx.scene.layout.Background;

public class LabelWrapper extends DefaultText {

	private final Label label;

	public LabelWrapper(Label label) {
		this.label = label;
	}

	public LabelWrapper(Label label, String defaultText) {
		super(defaultText);
		this.label = label;
	}

	public Label getLabel() {
		return label;
	}

	public void setText(String text) {
		label.setText(text);
	}

	public void resetTextToDefault() {
		label.setText(defaultText);
	}

	public void setBackground(Background background) {
		label.setBackground(background);
	}
}