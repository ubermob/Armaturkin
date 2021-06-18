public class MassCounter {

	private Double mass = 0.0;

	public void add(Double mass) {
		if (mass != null) {
			this.mass += mass;
		}
	}

	public Double getValue() {
		return mass == 0.0 ? null : mass;
	}

	public void reset() {
		mass = 0.0;
	}
}