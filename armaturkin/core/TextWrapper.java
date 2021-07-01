package armaturkin.core;

import javafx.scene.text.Text;

public class TextWrapper extends DefaultText {

	private final Text text;

	public TextWrapper(Text text) {
		super(text.getText());
		this.text = text;
	}

	public String getDefaultText() {
		return super.getDefaultText();
	}

	public void setDefaultText(String defaultText) {
		super.setDefaultText(defaultText);
	}

	public void setText(String text) {
		this.text.setText(text);
	}
}