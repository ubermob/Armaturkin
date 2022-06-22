package armaturkin.service;

import armaturkin.core.Log;
import armaturkin.model.ManuallyEntryModel;
import armaturkin.model.SummaryModel;
import armaturkin.summaryoutput.SummaryBuilderParser;
import armaturkin.summaryoutput.SummaryHub;
import armaturkin.summaryoutput.SummaryPool;
import armaturkin.utils.UnacceptableSymbolReplacer;
import armaturkin.workers.WorkerException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Andrey Korneychuk on 06-Jan-22
 * @version 1.0
 */
public class SummaryServiceImpl extends AbstractService implements SummaryService {

	private SummaryModel summaryModel;
	private ManuallyEntryModel manuallyEntryModel;

	@Override
	public void downloadSummaryFile() {
		var summaryPaths = summaryModel.getSummaryPaths();
		var manuallySummaryEntries = manuallyEntryModel.getManuallySummaryEntries();
		if (!summaryPaths.isEmpty() || !manuallySummaryEntries.isEmpty() || summaryModel.isSummaryBuilderListNotNull()) {
			String path;
			if (config.isFavoritePathNotNull()) {
				path = config.getFavoritePath();
			} else {
				path = Path.of(config.getPathToSummaryCalculatingFile()).getParent().toString();
			}
			String summaryTableHead = controller.getSummaryTableHead();
			if (summaryTableHead.equals("")) {
				summaryTableHead = app.getProperty("default_table_main_header");
			}
			SummaryHub summaryHub = new SummaryHub(
					manuallySummaryEntries,
					path,
					UnacceptableSymbolReplacer.replace(controller.getSummaryFileName()),
					summaryTableHead
			);
			Thread summaryHubThread = new Thread(summaryHub);
			summaryHubThread.start();
		}
	}

	@Override
	public void checkSummaryDropSpace(int i) {
		var summaryPaths = summaryModel.getSummaryPaths();
		if (!summaryPaths.get(i).isEmpty()) {
			SummaryPool summaryPool = null;
			try {
				summaryPool = new SummaryPool(i);
				summaryPool.runSummaryFileWorkers();
			} catch (WorkerException e) {
				e.printStackTrace();
			}
			assert summaryPool != null;
			List<Log> logList = summaryPool.getLogList();
			for (Log log : logList) {
				app.getLogService().merge(log);
			}
		}
	}

	@Override
	public void consumeSummaryBuilderFile(String path) throws IOException {
		summaryModel.setSummaryBuilderList(SummaryBuilderParser.parse(path));
		summaryModel.setPathToSummaryBuilderFile(path);
	}

	public void setSummaryModel(SummaryModel summaryModel) {
		this.summaryModel = summaryModel;
	}

	public void setManuallyEntryModel(ManuallyEntryModel manuallyEntryModel) {
		this.manuallyEntryModel = manuallyEntryModel;
	}
}