package armaturkin.model;

import armaturkin.reinforcement.Reinforcement;
import armaturkin.reinforcement.ReinforcementProduct;
import armaturkin.utils.ParsedRange;

import java.util.HashMap;

/**
 * @author Andrey Korneychuk on 08-Jan-22
 * @version 1.0
 */
public interface FirstHarvestingModel {

	HashMap<Integer, ReinforcementProduct> getReinforcementProductHashMap();

	HashMap<Integer, Reinforcement> getReinforcementHashMap();

	ParsedRange getReinforcementProductParsedRange();

	ParsedRange getReinforcementParsedRange();

	void resetForReinforcementProduct();

	void resetForReinforcement();

	boolean isReadyForDownload();
}