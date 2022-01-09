package armaturkin.model;

import armaturkin.reinforcement.Reinforcement;
import armaturkin.reinforcement.ReinforcementProduct;

import java.util.HashMap;

/**
 * 1st tab
 *
 * @author Andrey Korneychuk on 08-Jan-22
 * @version 1.0
 */
public class FirstHarvestingModelImpl implements FirstHarvestingModel {

	private final HashMap<Integer, ReinforcementProduct> reinforcementProductHashMap;
	private final HashMap<Integer, Reinforcement> reinforcementHashMap;

	public FirstHarvestingModelImpl() {
		reinforcementProductHashMap = new HashMap<>();
		reinforcementHashMap = new HashMap<>();
	}

	@Override
	public HashMap<Integer, ReinforcementProduct> getReinforcementProductHashMap() {
		return reinforcementProductHashMap;
	}

	@Override
	public HashMap<Integer, Reinforcement> getReinforcementHashMap() {
		return reinforcementHashMap;
	}
}