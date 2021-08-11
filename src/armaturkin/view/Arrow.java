package armaturkin.view;

import javafx.scene.shape.Line;

public class Arrow {

	private final Line line1;
	private final Line line2;

	public Arrow(Line line1, Line line2) {
		this.line1 = line1;
		this.line2 = line2;
	}

	public void refresh(Line line) {
		double startX = line.getStartX();
		double startY = line.getStartY();
		double endX = line.getEndX();
		double endY = line.getEndY();
		// triangle
		double a = endX - startX;
		double b = -(endY - startY);
		double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
		double angle = Math.toDegrees(Math.acos(a / c));
		if (b > 0) {
			// For III and IV coordinate quarters
			angle = angle + 2 * (180 - angle);
		}
		line1.setStartX(endX);
		line2.setStartX(endX);
		line1.setStartY(endY);
		line2.setStartY(endY);
		ArrowCalculator calculator = new ArrowCalculator(endX, endY, angle);
		line1.setEndX(calculator.getEndX1());
		line1.setEndY(calculator.getEndY1());
		line2.setEndX(calculator.getEndX2());
		line2.setEndY(calculator.getEndY2());
	}

	public void setOpacity(int i) {
		line1.setOpacity(i);
		line2.setOpacity(i);
	}
}