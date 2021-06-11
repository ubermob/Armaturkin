import javafx.scene.control.Label;

public class LabelWrapper {

	private final Label label;
	private String defaultText;

	public LabelWrapper(Label label) {
		this.label = label;
	}

	public Label getLabel() {
		return label;
	}

	public String getDefaultText() {
		return defaultText;
	}

	public void setDefaultText(String defaultText) {
		this.defaultText = defaultText;
	}

	public void setText(String text) {
		label.setText(text);
	}
}