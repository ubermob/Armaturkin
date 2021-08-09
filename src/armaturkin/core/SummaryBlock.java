package armaturkin.core;

import armaturkin.reinforcement.RFClass;
import armaturkin.utils.MassCounter;

import java.util.Arrays;

public class SummaryBlock {

	private final Double[][] blockBody;
	private final Double[] horizontalSummaryMass;
	private final Double[] verticalSummaryMass;
	private double blockSummaryMass;
	private final int[] diameters;
	private final RFClass rfClass;
	private final int bodyWidth;
	private final int bodyHeight;

	public SummaryBlock(Double[][] blockBody, int[] diameters, RFClass rfClass, int bodyWidth, int bodyHeight) {
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
		String res = "-B-L-O-C-K-\n";
		for (Double[] d : blockBody) {
			res += Arrays.toString(d) + "\n";
		}
		res += Arrays.toString(horizontalSummaryMass) + "\n";
		res += Arrays.toString(verticalSummaryMass) + "\n";
		res += blockSummaryMass + "\n";
		res += Arrays.toString(diameters) + "\n";
		res += rfClass + "\n";
		res += "-----------";
		return res;
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