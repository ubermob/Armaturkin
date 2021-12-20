package armaturkin.summaryoutput;

import armaturkin.steelcomponent.HotRolledSteelType;
import armaturkin.steelcomponent.Image;

import java.util.Arrays;
import java.util.List;

/**
 * @author Andrey Korneychuk on 20-Dec-21
 * @version 1.0
 */
public class HotRolledSteelSummaryBlock extends SummaryBlock {

	private List<Image> images;
	private HotRolledSteelType hotRolledSteelType;

	public HotRolledSteelSummaryBlock(Double[][] blockBody, List<Image> images
			, HotRolledSteelType hotRolledSteelType, int bodyWidth, int bodyHeight) {
		super(blockBody, new int[]{}, null, bodyWidth, bodyHeight);
		this.images = images;
		this.hotRolledSteelType = hotRolledSteelType;
	}

	@Override
	public String toString() {
		String result = "-B-L-O-C-K-\n";
		for (var v : blockBody) {
			result += Arrays.toString(v) + "\n";
		}
		result += "Horizontal summary mass: " + Arrays.toString(horizontalSummaryMass) + "\n";
		result += "Vertical summary mass: " + Arrays.toString(verticalSummaryMass) + "\n";
		result += "Block summary mass: " + super.getBlockSummaryMass() + "\n";
		result += "Image: " + images + "\n";
		result += "Hot rolled steel type: " + hotRolledSteelType + "\n";
		result += "-----------";
		return result;
	}
}