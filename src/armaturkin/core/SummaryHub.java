package armaturkin.core;

import armaturkin.interfaces.FileNameCreator;
import armaturkin.interfaces.Stopwatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SummaryHub implements Runnable, FileNameCreator, Stopwatch {

	private final HashMap<Integer, List<String>> summaryPaths;
	private final String path;
	private final String fileName;
	private final String tableHead;
	private final HashMap<Integer, HashMap<Integer, ReinforcementLiteInfo>> targetHashMap;
	private Thread[][] allThreads;
	private final List<Log> summaryLog;
	private ContentContainer contentContainer;
	private long millis;

	public SummaryHub(HashMap<Integer, List<String>> summaryPaths, String path, String fileName, String tableHead) {
		this.summaryPaths = summaryPaths;
		this.path = path;
		this.fileName = fileName;
		this.tableHead = tableHead;
		targetHashMap = new HashMap<>();
		summaryLog = new ArrayList<>();
	}

	@Override
	public void run() {
		millis = getStartTime();
		Main.log.add(Main.properties.getProperty("thread_start").formatted(getClass()));
		allThreads = new Thread[8][];
		for (int i = 1; i <= 8; i++) {
			SummaryThreadStarter summaryThreadStarter = new SummaryThreadStarter(i);
			if(!summaryThreadStarter.isNullable()) {
				targetHashMap.put(i, summaryThreadStarter.getHashMap());
				allThreads[i - 1] = summaryThreadStarter.getSubThreads();
				List<Log> logList = summaryThreadStarter.getLogList();
				summaryLog.addAll(logList);
			}
		}
		for (Thread[] threadArray : allThreads) {
			if (threadArray != null) {
				for (Thread thread : threadArray) {
					try {
						thread.join();
					} catch (InterruptedException e) {
						Main.log.add(e);
					}
				}
			}
		}
		mergeLog();
		buildExcel();
		Main.log.add(Main.properties.getProperty("thread_complete").formatted(getClass(), getStopwatch(millis)));
	}

	void mergeLog() {
		for (Log log : summaryLog) {
			Main.log.merge(log);
		}
	}

	void buildExcel() {
		buildContent();
		SummaryExcelBuilder summaryExcelBuilder = new SummaryExcelBuilder(contentContainer, path, createFileName(fileName), tableHead);
		Thread summaryExcelBuilderThread = new Thread(summaryExcelBuilder);
		summaryExcelBuilderThread.start();
	}

	void buildContent() {
		contentContainer = new ContentContainer();
		Main.log.add(Main.properties.getProperty("target_hash_map"));
		for (int i = 1; i < 9; i++) {
			if (targetHashMap.containsKey(i)) {
				HashMap<Integer, ReinforcementLiteInfo> subMap = targetHashMap.get(i);
				Main.log.add(Main.properties.getProperty("summary_drop_space").formatted(i));
				// RHashCodeList.txt
				for (int j = 0; j < 291; j++) {
					if (subMap.containsKey(j)) {
						contentContainer.put(i, j, subMap.get(j).getMass());
						Main.log.add(Main.properties.getProperty("print_target_hash_map").formatted(j, subMap.get(j).toString()));
					}
				}
			}
		}
		if (SummaryRedirectManager.redirectTo != SummaryRedirectManager.DEFAULT_VALUE) {
			contentContainer.redirect(SummaryRedirectManager.redirectTo);
			Main.log.add(Main.properties.getProperty("redirect").formatted(getClass(),
					SummaryRedirectManager.DEFAULT_VALUE, SummaryRedirectManager.redirectTo
			));
		}
		Main.log.add(Main.properties.getProperty("full_content"));
		Main.log.add(contentContainer.printBorder());
		Main.log.add(contentContainer.printContent());
		Main.log.add(contentContainer.printCompact());
		contentContainer.compress();
		Main.log.add(Main.properties.getProperty("compressed_content"));
		Main.log.add(contentContainer.printBorder());
		Main.log.add(contentContainer.printContent());
	}
}