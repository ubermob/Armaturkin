import javafx.scene.text.Text;

public class TextWrapper extends DefaultText {

	private final Text text;

	public TextWrapper(Text text) {
		super(text.getText());
		this.text = text;
	}

	public Text getLabel() {
		return text;
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

	public void resetTextToDefault() {
		text.setText(super.getDefaultText());
	}
}