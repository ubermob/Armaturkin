package armaturkin.reinforcement;

public class ProductReinforcementChecker extends ReinforcementChecker {

	private int diameterIndex;
	private boolean realDiameter;
	private double mass1;
	private boolean correctMass = true;

	public ProductReinforcementChecker(ReinforcementProduct reinforcementProduct) {
		super(reinforcementProduct);
	}

	public void checkDiameter() {
		diameterIndex = super.getDiameterIndex();
		realDiameter = (diameterIndex >= 0);
	}

	public boolean isCorrectDiameter() {
		return realDiameter;
	}

	public int getDiameterIndex() {
		return diameterIndex;
	}

	public void checkMass(double mass) {
		if (realDiameter) {
			int length = getReinforcementProduct().getLength();
			mass1 = length / 1000.0 * StandardsRepository.MASS_3_DIGIT[diameterIndex];
			double mass2 = length / 1000.0 * StandardsRepository.MASS_2_DIGIT_1[diameterIndex];
			double mass3 = length / 1000.0 * StandardsRepository.MASS_2_DIGIT_2[diameterIndex];
			boolean match1 = Math.abs(mass1 - mass) < 0.01;
			boolean match2 = Math.abs(mass2 - mass) < 0.01;
			boolean match3 = Math.abs(mass3 - mass) < 0.01;
			if (!match1 && !match2 && !match3) {
				correctMass = false;
			}
		} else {
			correctMass = false;
		}
	}

	public boolean isCorrectMass() {
		return correctMass;
	}

	public double getCorrectMass() {
		return mass1;
	}
}