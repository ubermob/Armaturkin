package armaturkin.core;

public abstract class ReinforcementChecker {

	private ReinforcementProduct reinforcementProduct;

	public ReinforcementChecker(ReinforcementProduct reinforcementProduct) {
		this.reinforcementProduct = reinforcementProduct;
	}

	public int getDiameterIndex() {
		for (int i = 0; i < StandardsRepository.diameter.length; i++) {
			if (reinforcementProduct.getDiameter() == StandardsRepository.diameter[i]) {
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
		return 0 < length && length <= StandardsRepository.maxLength;
	}

	public ReinforcementProduct getReinforcementProduct() {
		return reinforcementProduct;
	}
}