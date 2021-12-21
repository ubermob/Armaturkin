package armaturkin.summaryoutput;

import armaturkin.reinforcement.RFClass;
import armaturkin.utils.MassCounter;

import java.util.Arrays;

public class SummaryBlock {

	protected final int id;
	protected final Double[][] blockBody;
	protected final Double[] horizontalSummaryMass;
	protected final Double[] verticalSummaryMass;
	private double blockSummaryMass;
	private final int[] diameters;
	private final RFClass rfClass;
	private final int bodyWidth;
	private final int bodyHeight;

	public SummaryBlock(int id, Double[][] blockBody, int[] diameters, RFClass rfClass, int bodyWidth, int bodyHeight) {
		this.id = id;
		this.blockBody = blockBody;
		this.diameters = diameters;
		this.rfClass = rfClass;
		this.bodyWidth = bodyWidth;
		this.bodyHeight = bodyHeight;
		horizontalSummaryMass = new Double[bodyWidth];
		verticalSummaryMass = new Double[bodyHeight];
		calculateMass();
	}

	public Double getValue(int height, int width) {
		return blockBody[height][width];
	}

	public Double getHorizontalSummaryMass(int i) {
		return horizontalSummaryMass[i];
	}

	public Double getVerticalSummaryMass(int i) {
		return verticalSummaryMass[i];
	}

	public Double getBlockSummaryMass() {
		return blockSummaryMass;
	}

	public int getDiameter(int i) {
		return diameters[i];
	}

	public RFClass getRFClass() {
		return rfClass;
	}

	public int getBodyWidth() {
		return bodyWidth;
	}

	public int getBodyHeight() {
		return bodyHeight;
	}

	@Override
	public String toString() {
		String result = "--B-L-O-C-K--{id=%d}--\n".formatted(id);
		for (Double[] d : blockBody) {
			result += Arrays.toString(d) + "\n";
		}
		result += "Horizontal summary mass: " + Arrays.toString(horizontalSummaryMass) + "\n";
		result += "Vertical summary mass: " + Arrays.toString(verticalSummaryMass) + "\n";
		result += "Block summary mass: " + blockSummaryMass + "\n";
		result += "Diameters: " + Arrays.toString(diameters) + "\n";
		result += "RFClass: " + rfClass + "\n";
		result += "----------------------";
		return result;
	}

	private void calculateMass() {
		// Calculate horizontal
		MassCounter mass = new MassCounter();
		for (int i = 0; i < bodyWidth; i++) {
			for (int j = 0; j < bodyHeight; j++) {
				mass.add(blockBody[j][i]);
			}
			horizontalSummaryMass[i] = mass.getValue();
			mass.reset();
		}
		// Calculate vertical
		mass.reset();
		for (int i = 0; i < bodyHeight; i++) {
			for (int j = 0; j < bodyWidth; j++) {
				mass.add(blockBody[i][j]);
			}
			verticalSummaryMass[i] = mass.getValue();
			mass.reset();
		}
		// Calculate block summary mass
		mass.reset();
		for (Double summaryMass : verticalSummaryMass) {
			mass.add(summaryMass);
		}
		blockSummaryMass = mass.getValue();
	}
}