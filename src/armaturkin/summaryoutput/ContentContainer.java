package armaturkin.summaryoutput;

import armaturkin.manuallyentry.ManuallyEntry;
import armaturkin.reinforcement.RFClass;
import armaturkin.reinforcement.RfHashCode;
import armaturkin.steelcomponent.HotRolledSteelType;
import armaturkin.steelcomponent.Image;

import java.util.ArrayList;
import java.util.List;

public class ContentContainer {

	private static String[][] contentAsArray;

	private final ContentHeadPlacement contentHeadPlacement;
	private final ContentHead contentHead;
	private final ContentRow contentRow;
	private final Content content;

	public ContentContainer(ArrayList<Image> sortedSheets) throws Exception {
		contentHeadPlacement = new ContentHeadPlacement();
		contentHead = new ContentHead(contentHeadPlacement.getBlocks(), contentHeadPlacement.getHashes().size(), sortedSheets);
		contentRow = new ContentRow();
		content = new Content(contentHeadPlacement.getHashes(), contentRow.getSize());
	}

	public ContentContainer(ArrayList<Image> sortedSheets, List<String> rowList) throws Exception {
		contentHeadPlacement = new ContentHeadPlacement();
		contentHead = new ContentHead(contentHeadPlacement.getBlocks(), contentHeadPlacement.getHashes().size(), sortedSheets);
		contentRow = new ContentRow(rowList);
		content = new Content(contentHeadPlacement.getHashes(), contentRow.getSize());
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
		// Indexes relative full compressed content
		//  |---|---|---|---|---| <- indexes [2, 3] of 5
		//  |   |   |xxx|xxx|   |
		//  |---|---|---|---|---|
		//  |   |   |xxx|xxx|   |
		//  |---|---|---|---|---|
		int[] indexes = contentHead.getIndexes(rfClass);
		int bodyHeight = content.getHeight();
		Double[][] blockBody = new Double[bodyHeight][bodyWidth];
		for (int j = 0; j < indexes.length; j++) {
			for (int k = 0; k < bodyHeight; k++) {
				blockBody[k][j] = content.getCell(k, indexes[j]);
			}
		}
		return new SummaryBlock(
				i,
				blockBody,
				diameters,
				rfClass,
				bodyWidth,
				bodyHeight
		);
	}

	public HotRolledSteelSummaryBlock getHotRolledSteelSummaryBlock(int i) {
		HotRolledSteelType hotRolledSteelType = contentHead.getHotRolledSteelType(i);
		List<Image> images = contentHead.getImages(hotRolledSteelType);
		int bodyWidth = images.size();
		// Indexes relative full compressed content
		//  |---|---|---|---|---| <- indexes [2, 3] of 5
		//  |   |   |xxx|xxx|   |
		//  |---|---|---|---|---|
		//  |   |   |xxx|xxx|   |
		//  |---|---|---|---|---|
		int[] indexes = contentHead.getIndexes(hotRolledSteelType);
		int bodyHeight = content.getHeight();
		Double[][] blockBody = new Double[bodyHeight][bodyWidth];
		for (int j = 0; j < indexes.length; j++) {
			for (int k = 0; k < bodyHeight; k++) {
				blockBody[k][j] = content.getCell(k, indexes[j]);
			}
		}
		return new HotRolledSteelSummaryBlock(
				i,
				blockBody,
				images,
				hotRolledSteelType,
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

	public Object[] getContentHeadPlacementBlocks() {
		return contentHeadPlacement.getBlocks();
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