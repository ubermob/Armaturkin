package armaturkin.core;

import armaturkin.reinforcement.RFClass;

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

	public boolean[] getHeadBlockFullness() {
		return contentHead.getBlockFullness();
	}

	public String[] getRowStrings() {
		return contentRow.getRowStrings();
	}

	public SummaryBlock getBlock(int i) {
		RFClass rfClass = contentHead.getRFClass(i);
		int[] diameters = contentHead.getDiameters(rfClass);
		int bodyWidth = diameters.length;
		int[] indexes = contentHead.getIndexes(rfClass);
		int bodyHeight = content.getHeight();
		Double[][] blockBody = new Double[bodyHeight][bodyWidth];
		for (int j = 0; j < indexes.length; j++) {
			for (int k = 0; k < bodyHeight; k++) {
				blockBody[k][j] = content.getCell(k, indexes[j]);
			}
		}
		return new SummaryBlock(
				blockBody,
				diameters,
				rfClass,
				bodyWidth,
				bodyHeight
		);
	}

	public Double[] getFinallyVerticalSummaryMass() {
		return content.getFinallyVerticalSummaryMass();
	}

	public void redirect(int i) {
		content.redirect(i);
	}
}