package armaturkin.model;

import armaturkin.reinforcement.Reinforcement;
import armaturkin.reinforcement.ReinforcementProduct;
import armaturkin.utils.ParsedRange;

import java.util.HashMap;

/**
 * 1st tab
 *
 * @author Andrey Korneychuk on 08-Jan-22
 * @version 1.0
 */
public class FirstHarvestingModelImpl implements FirstHarvestingModel {

	private final HashMap<Integer, ReinforcementProduct> reinforcementProductHashMap;
	private ParsedRange reinforcementProductParsedRange;
	private final HashMap<Integer, Reinforcement> reinforcementHashMap;
	private ParsedRange reinforcementParsedRange;

	public FirstHarvestingModelImpl() {
		reinforcementProductHashMap = new HashMap<>();
		reinforcementProductParsedRange = new ParsedRange();
		reinforcementHashMap = new HashMap<>();
		reinforcementParsedRange = new ParsedRange();
	}

	@Override
	public HashMap<Integer, ReinforcementProduct> getReinforcementProductHashMap() {
		return reinforcementProductHashMap;
	}

	@Override
	public HashMap<Integer, Reinforcement> getReinforcementHashMap() {
		return reinforcementHashMap;
	}

	@Override
	public ParsedRange getReinforcementProductParsedRange() {
		return reinforcementProductParsedRange;
	}

	public ParsedRange getReinforcementParsedRange() {
		return reinforcementParsedRange;
	}

	@Override
	public void resetForReinforcementProduct() {
		reinforcementProductHashMap.clear();
		reinforcementProductParsedRange = new ParsedRange();
	}

	@Override
	public void resetForReinforcement() {
		reinforcementHashMap.clear();
		reinforcementParsedRange = new ParsedRange();
	}

	@Override
	public boolean isReadyForDownload() {
		return !reinforcementHashMap.isEmpty() && !reinforcementProductHashMap.isEmpty();
	}
}