package armaturkin.view;

public class ArrowCalculator {

	private final double coordinateX;
	private final double coordinateY;
	private final double angle;
	private final double angleGap = 30;
	private final double lineLength = 40;
	private double endX1;
	private double endY1;
	private double endX2;
	private double endY2;

	public ArrowCalculator(double coordinateX, double coordinateY, double angle) {
		this.coordinateX = coordinateX;
		this.coordinateY = coordinateY;
		this.angle = angle;
		calculate();
	}

	public double getEndX1() {
		return endX1;
	}

	public double getEndY1() {
		return endY1;
	}

	public double getEndX2() {
		return endX2;
	}

	public double getEndY2() {
		return endY2;
	}

	private void calculate() {
		double angle1 = angle + 180 - angleGap / 2;
		double angle2 = angle + 180 + angleGap / 2;
		endX1 = coordinateX + lineLength * Math.cos(Math.toRadians(angle1));
		endY1 = coordinateY + lineLength * Math.sin(Math.toRadians(angle1));
		endX2 = coordinateX + lineLength * Math.cos(Math.toRadians(angle2));
		endY2 = coordinateY + lineLength * Math.sin(Math.toRadians(angle2));
	}
}