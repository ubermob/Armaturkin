package armaturkin.summaryoutput;

import armaturkin.core.*;
import armaturkin.interfaces.FileNameCreator;
import armaturkin.manuallyentry.ManuallyEntry;
import armaturkin.reinforcement.ReinforcementLiteInfo;
import utools.stopwatch.Stopwatch;

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
	}

	@Override
	public void run() {
		stopwatch = new Stopwatch();
		Main.log.add(Main.properties.getProperty("thread_start").formatted(getClass()));
		for (int i = 1; i <= 8; i++) {
			SummaryThreadPool summaryThreadPool = null;
			try {
				summaryThreadPool = new SummaryThreadPool(i);
			} catch (InterruptedException e) {
				Main.log.add(e);
			}
			if (summaryThreadPool.isNotNull()) {
				targetHashMap.put(i, summaryThreadPool.getHashMap());
				List<Log> logList = summaryThreadPool.getLogList();
				summaryLog.addAll(logList);
			}
		}
		mergeLog();
		try {
			buildContent();
			buildExcel();
		} catch (Exception e) {
			Main.log.add(e);
		}
		Main.log.add(Main.properties.getProperty("thread_complete").formatted(getClass(), stopwatch.getElapsedTime()));
	}

	private void mergeLog() {
		for (Log log : summaryLog) {
			Main.log.merge(log);
		}
	}

	private void buildContent() throws Exception {
		contentContainer = new ContentContainer(SheetDynamicHashCode.sortAndGetSortedCurrentSheetList(manuallySummaryEntries));
		Main.log.add(Main.properties.getProperty("target_hash_map"));
		// Filling auto tab entries
		for (int i = 1; i <= 8; i++) {
			if (targetHashMap.containsKey(i)) {
				HashMap<Integer, ReinforcementLiteInfo> subMap = targetHashMap.get(i);
				Main.log.add(Main.properties.getProperty("summary_drop_space").formatted(i));
				// RF_hash_code_list.txt range
				for (int j = 0; j <= contentContainer.maxHashCode(); j++) {
					if (subMap.containsKey(j)) {
						contentContainer.put(i, j, subMap.get(j).getMass());
						Main.log.add(Main.properties.getProperty("print_target_hash_map").formatted(j, subMap.get(j).toString()));
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
			Main.log.add(Main.properties.getProperty("redirect").formatted(getClass(),
					SummaryRedirectManager.DEFAULT_VALUE, SummaryRedirectManager.redirectTo
			));
		}
		Main.log.add(Main.properties.getProperty("full_content"));
		Main.log.add(contentContainer.borderToString());
		Main.log.add(contentContainer.contentToString());
		Main.log.add(contentContainer.compactContentToString());
		contentContainer.compress();
		Main.log.add(Main.properties.getProperty("compressed_content"));
		Main.log.add(contentContainer.borderToString());
		Main.log.add(contentContainer.contentToString());
	}

	private void buildExcel() {
		SummaryExcelBuilder summaryExcelBuilder = new SummaryExcelBuilder(contentContainer, path
				, createFileName(fileName), tableHead);
		Thread summaryExcelBuilderThread = new Thread(summaryExcelBuilder);
		summaryExcelBuilderThread.start();
	}
}