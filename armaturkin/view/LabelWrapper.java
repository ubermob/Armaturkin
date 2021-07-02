package armaturkin.view;

import javafx.scene.control.Label;

public class LabelWrapper extends DefaultText {

	private final Label label;

	public LabelWrapper(Label label) {
		this.label = label;
	}

	public Label getLabel() {
		return label;
	}

	public String getDefaultText() {
		return super.getDefaultText();
	}

	public void setDefaultText(String defaultText) {
		super.setDefaultText(defaultText);
	}

	public void setText(String text) {
		label.setText(text);
	}

	public void resetTextToDefault() {
		label.setText(super.getDefaultText());
	}
}