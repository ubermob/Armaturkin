package armaturkin.service;

import armaturkin.core.Log;
import armaturkin.model.ManuallyEntryModel;
import armaturkin.model.SummaryModel;
import armaturkin.summaryoutput.SummaryHub;
import armaturkin.summaryoutput.SummaryThreadPool;
import armaturkin.utils.UnacceptableSymbolReplacer;

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
		if (!summaryPaths.isEmpty() || !manuallySummaryEntries.isEmpty()) {
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
	public void checkSummaryDropSpace(int i) throws InterruptedException {
		var summaryPaths = summaryModel.getSummaryPaths();
		if (!summaryPaths.get(i).isEmpty()) {
			SummaryThreadPool summaryThreadPool = new SummaryThreadPool(i);
			List<Log> logList = summaryThreadPool.getLogList();
			for (Log threadLog : logList) {
				app.getLogService().merge(threadLog);
			}
		}
	}

	@Override
	public void consumeSummaryBuilderFile() {

	}

	public void setSummaryModel(SummaryModel summaryModel) {
		this.summaryModel = summaryModel;
	}

	public void setManuallyEntryModel(ManuallyEntryModel manuallyEntryModel) {
		this.manuallyEntryModel = manuallyEntryModel;
	}
}