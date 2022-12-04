package armaturkin.summaryoutput;

import armaturkin.core.Log;
import armaturkin.core.Main;
import armaturkin.interfaces.FileNameCreator;
import armaturkin.manuallyentry.ManuallyEntry;
import armaturkin.model.SummaryModel;
import armaturkin.reinforcement.ReinforcementLiteInfo;
import armaturkin.workers.WorkerException;
import utools.stopwatch.Stopwatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SummaryHub implements Runnable, FileNameCreator {

	private final List<ManuallyEntry> manuallySummaryEntries;
	private final String path;
	private final String fileName;
	private final String tableHead;
	private final HashMap<Integer, HashMap<Integer, ReinforcementLiteInfo>> targetHashMap;
	private final List<Log> summaryLog;
	private final boolean isDefaultSummaryRowList;
	private final int summaryPathsKeyCounters;
	private ContentContainer contentContainer;
	private Stopwatch stopwatch;

	public SummaryHub(List<ManuallyEntry> manuallySummaryEntries,
	                  String path,
	                  String fileName,
	                  String tableHead) {
		this.manuallySummaryEntries = manuallySummaryEntries;
		this.path = path;
		this.fileName = fileName;
		this.tableHead = tableHead;
		targetHashMap = new HashMap<>();
		summaryLog = new ArrayList<>();
		isDefaultSummaryRowList = !Main.app.getSummaryModel().isSummaryBuilderListNotNull();
		summaryPathsKeyCounters = Main.app.getSummaryModel().getSummaryPathsKeyCounters();
	}

	@Override
	public void run() {
		stopwatch = new Stopwatch();
		boolean isRunAfterSummaryPool = true;
		Main.app.log(Main.app.getProperty("thread_start").formatted(getClass()));
		SummaryModel summaryModel = Main.app.getSummaryModel();
		if (!isDefaultSummaryRowList) {
			summaryModel.getSummaryPaths().clear();
			try {
				SummaryBuilderParser.realize(Main.app.getSummaryModel().getSummaryBuilderList());
			} catch (IOException e) {
				Main.app.log(e);
			}
		}
		for (int i = 1; i <= summaryPathsKeyCounters; i++) {
			SummaryPool summaryPool;
			try {
				if (isDefaultSummaryRowList) {
					summaryPool = new SummaryPool(i);
				} else {
					int type = summaryModel.getSummaryBuilderList().get(i - 1).getType();
					summaryPool = new SummaryPool(i, type);
				}
				summaryPool.runSummaryFileWorkers();
			} catch (WorkerException we) {
				isRunAfterSummaryPool = false;
				Main.app.log(we);
				Main.app.setWorkerExceptionMessage(we.getMessage());
				break;
			}
			if (summaryPool.isNotNull()) {
				targetHashMap.put(i, summaryPool.getHashMap());
				List<Log> logList = summaryPool.getLogList();
				summaryLog.addAll(logList);
			}
		}
		assert isRunAfterSummaryPool;
		mergeLog();
		try {
			buildContent();
			buildExcel();
		} catch (Exception e) {
			Main.app.log(e);
		}
		Main.app.log(Main.app.getProperty("thread_complete").formatted(getClass(), stopwatch.getElapsedTime()));
	}

	private void mergeLog() {
		for (Log log : summaryLog) {
			Main.app.getLogService().merge(log);
		}
	}

	private void buildContent() throws Exception {
		if (isDefaultSummaryRowList) {
			contentContainer = new ContentContainer(
					SheetDynamicHashCode.sortAndGetSortedCurrentSheetList(manuallySummaryEntries)
			);
		} else {
			contentContainer = new ContentContainer(
					SheetDynamicHashCode.sortAndGetSortedCurrentSheetList(manuallySummaryEntries)
					, Main.app.getSummaryModel().getUserContentRowList()
			);
		}
		Main.app.log(Main.app.getProperty("target_hash_map"));
		// Filling auto tab entries
		for (int i = 1; i <= summaryPathsKeyCounters; i++) {
			if (targetHashMap.containsKey(i)) {
				HashMap<Integer, ReinforcementLiteInfo> subMap = targetHashMap.get(i);
				Main.app.log(Main.app.getProperty("summary_drop_space").formatted(i));
				// RF_hash_code_list.txt range
				for (int j = 0; j <= contentContainer.maxHashCode(); j++) {
					if (subMap.containsKey(j)) {
						contentContainer.put(i, j, subMap.get(j).getMass());
						Main.app.log(Main.app.getProperty("print_target_hash_map").formatted(j, subMap.get(j).toString()));
					}
				}
			}
		}
		// Filling manually tab entries
		for (var entry : manuallySummaryEntries) {
			contentContainer.put(entry);
		}
		// Redirect
		if (SummaryRedirectManager.redirectTo != SummaryRedirectManager.DEFAULT_VALUE) {
			contentContainer.redirect(SummaryRedirectManager.redirectTo);
			Main.app.log(Main.app.getProperty("redirect").formatted(getClass(),
					SummaryRedirectManager.DEFAULT_VALUE, SummaryRedirectManager.redirectTo
			));
		}
		Main.app.log(Main.app.getProperty("full_content"));
		Main.app.getLogService().add(contentContainer.borderToString()
				, Main.app.getConfig().getContentContainerBorderLoggable());
		Main.app.getLogService().add(contentContainer.contentToString()
				, Main.app.getConfig().getContentContainerLoggable());
		Main.app.getLogService().add(contentContainer.compactContentToString()
				, Main.app.getConfig().getContentContainerCompactLoggable());
		contentContainer.storeFullContentAsArray();
		contentContainer.compress();
		Main.app.log(Main.app.getProperty("compressed_content"));
		Main.app.log(contentContainer.borderToString());
		Main.app.log(contentContainer.contentToString());
	}

	private void buildExcel() throws InterruptedException {
		SummaryExcelBuilder summaryExcelBuilder = new SummaryExcelBuilder(
				contentContainer,
				path,
				createFileName(fileName, ".xlsx"),
				tableHead
		);
		Thread summaryExcelBuilderThread = new Thread(summaryExcelBuilder);
		summaryExcelBuilderThread.start();
		summaryExcelBuilderThread.join();
	}
}