public class ContentContainer {

	private Content content;
	private ContentHead contentHead;
	private ContentRow contentRow;

	public ContentContainer() {
		content = new Content();
		contentHead = new ContentHead();
		contentRow = new ContentRow();
	}

	public void put(int labelID, int hashCode, double mass) {
		content.put(labelID, hashCode, mass);
	}

	public String printContent() {
		return content.toString();
	}

	public String printCompact() {
		return content.printCompact();
	}

	public String printBorder() {
		return contentHead.toPrettyString() + "\n" + contentRow.toString();
	}

	public void compress() {
		content.compress(contentHead, contentRow);
	}
}