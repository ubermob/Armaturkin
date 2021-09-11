package armaturkin.summaryoutput;

import armaturkin.reinforcement.RFClass;

public class ContentContainer {

	private final ContentHeadPlacement contentHeadPlacement;
	private final Content content;
	private final ContentHead contentHead;
	private final ContentRow contentRow;

	public ContentContainer() {
		contentHeadPlacement = new ContentHeadPlacement();
		content = new Content(contentHeadPlacement.getHashes());
		contentHead = new ContentHead(contentHeadPlacement.getBlocks());
		contentRow = new ContentRow();
	}

	public void put(int labelID, int hashCode, double mass) {
		content.put(labelID, hashCode, mass);
	}

	public String contentToString() {
		return content.toString();
	}

	public String compactContentToString() {
		return content.compactContentToString();
	}

	public String borderToString() {
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

	public int maxHashCode() {
		return content.maxHashCode();
	}
}