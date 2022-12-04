package armaturkin.reinforcement;

public abstract class ReinforcementChecker {

	private final ReinforcementProduct reinforcementProduct;

	public ReinforcementChecker(ReinforcementProduct reinforcementProduct) {
		this.reinforcementProduct = reinforcementProduct;
	}

	public int getDiameterIndex() {
		for (int i = 0; i < StandardsRepository.DIAMETERS.length; i++) {
			if (reinforcementProduct.getDiameter() == StandardsRepository.DIAMETERS[i]) {
				return i;
			}
		}
		return -1;
	}

	public boolean isCorrectRFClass() {
		return reinforcementProduct.getRfClass() != RFClass.UNKNOWN;
	}

	public boolean isCorrectLength() {
		int length = reinforcementProduct.getLength();
		return 0 < length && length <= StandardsRepository.MAX_LENGTH;
	}

	public ReinforcementProduct getReinforcementProduct() {
		return reinforcementProduct;
	}
}