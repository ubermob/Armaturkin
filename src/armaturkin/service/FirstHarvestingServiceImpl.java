package armaturkin.service;

import armaturkin.model.FirstHarvestingModel;
import armaturkin.utils.UnacceptableSymbolReplacer;
import armaturkin.workers.CalculatingFileWorker;
import armaturkin.workers.FileWorker;
import armaturkin.workers.ProductFileWorker;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Andrey Korneychuk on 08-Jan-22
 * @version 1.0
 */
public class FirstHarvestingServiceImpl extends AbstractService implements FirstHarvestingService {

	private FirstHarvestingModel model;

	@Override
	public void loadProductFile() {
		var reinforcementProductHashMap = model.getReinforcementProductHashMap();
		reinforcementProductHashMap.clear();
		ProductFileWorker productFileWorker = new ProductFileWorker(
				config.getPathToProductFile(),
				reinforcementProductHashMap
		);
		Thread productFileWorkerThread = new Thread(productFileWorker);
		productFileWorkerThread.start();
	}

	@Override
	public void loadCalculatingFile() {
		var reinforcementHashMap = model.getReinforcementHashMap();
		var reinforcementProductHashMap = model.getReinforcementProductHashMap();
		reinforcementHashMap.clear();
		CalculatingFileWorker calculatingFileWorker = new CalculatingFileWorker(
				config.getPathToCalculatingFile(),
				reinforcementHashMap,
				reinforcementProductHashMap
		);
		Thread calculatingFileWorkerThread = new Thread(calculatingFileWorker);
		calculatingFileWorkerThread.start();
	}

	@Override
	public void downloadCalculatedFile() {
		var reinforcementHashMap = model.getReinforcementHashMap();
		var reinforcementProductHashMap = model.getReinforcementProductHashMap();
		if (!reinforcementHashMap.isEmpty() && !reinforcementProductHashMap.isEmpty()) {
			String path;
			if (config.isFavoritePathNotNull()) {
				path = config.getFavoritePath();
			} else {
				path = Path.of(config.getPathToCalculatingFile()).getParent().toString();
			}
			FileWorker fileWorker = new FileWorker(
					path,
					reinforcementHashMap,
					controller.getTableHead(),
					UnacceptableSymbolReplacer.replace(controller.getFileName())
			);
			Thread fileWorkerThread = new Thread(fileWorker);
			fileWorkerThread.start();
		}
	}

	@Override
	public void preloadUpperDropSpace() {
		if (config.isPathToProductFileNotNull() && config.getAutoParseProductList()) {
			Path path = Path.of(config.getPathToProductFile());
			if (Files.exists(path)) {
				controller.setUpperDropSpaceText(
						app.getProperty("upper_label_text_with_file").formatted(path.getFileName().toString())
				);
				loadProductFile();
			}
		}
	}

	public void setFirstHarvestingModel(FirstHarvestingModel firstHarvestingModel) {
		model = firstHarvestingModel;
	}
}