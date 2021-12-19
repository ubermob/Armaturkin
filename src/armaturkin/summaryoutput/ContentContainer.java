package armaturkin.summaryoutput;

import armaturkin.manuallyentry.ManuallyEntry;
import armaturkin.reinforcement.RFClass;
import armaturkin.reinforcement.RfHashCode;
import armaturkin.steelcomponent.Image;

import java.util.ArrayList;

public class ContentContainer {

	private static String[][] contentAsArray;

	private final ContentHeadPlacement contentHeadPlacement;
	private final Content content;
	private final ContentHead contentHead;
	private final ContentRow contentRow;

	public ContentContainer(ArrayList<Image> sortedSheets) throws Exception {
		contentHeadPlacement = new ContentHeadPlacement();
		content = new Content(contentHeadPlacement.getHashes());
		contentHead = new ContentHead(contentHeadPlacement.getBlocks(), contentHeadPlacement.getHashes().size(), sortedSheets);
		contentRow = new ContentRow();
	}

	public void put(int labelID, int hashCode, double mass) {
		content.put(labelID, hashCode, mass);
	}

	public void put(ManuallyEntry manuallyEntry) {
		if (manuallyEntry.isReinforcement()) {
			int hashCode = RfHashCode.getHashCode(manuallyEntry.getDiameter(), manuallyEntry.getRfClass());
			put(manuallyEntry.getSummaryLabelID(), hashCode, manuallyEntry.getMassReinforcement());
		} else if (manuallyEntry.isAngle()) {
			Image image = manuallyEntry.getLightInfoCastingToImage();
			put(manuallyEntry.getSummaryLabelID(), image.getHashCode(), image.getMass());
		} else {
			// .isSheet == true
			Image image = manuallyEntry.getLightInfoCastingToImage();
			put(manuallyEntry.getSummaryLabelID(), image.getHashCode(), image.getMass());
		}
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

	public void storeFullContentAsArray() {
		contentAsArray = new String[content.getHeight() + 1][content.getWidth() + 1];
		contentAsArray[0][0] = "";
		String[] rowStrings = contentRow.getRowStrings();
		for (int i = 0; i < rowStrings.length; i++) {
			contentAsArray[i + 1][0] = rowStrings[i];
		}
		for (int i = 1; i < contentAsArray[0].length; i++) {
			contentAsArray[0][i] = contentHead.get(i - 1).getPrettyString();
		}
		for (int i = 1; i < contentAsArray.length; i++) {
			for (int j = 1; j < contentAsArray[0].length; j++) {
				contentAsArray[i][j] = String.valueOf(content.getCell(i - 1, j - 1));
				if (contentAsArray[i][j].equals("null")) {
					contentAsArray[i][j] = "";
				}
			}
		}
	}

	public static String[][] getContentAsArray() {
		return contentAsArray;
	}
}