import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SummaryHub implements Runnable, FileNameCreator, Stopwatch {

	private final HashMap<Integer, List<String>> summaryPaths;
	private final String tableHead;
	private String fileName;
	private volatile HashMap<Integer, HashMap<Integer, ReinforcementLiteInfo>> targetHashMap;
	private List<Log> summaryLog;
	private ContentContainer contentContainer;
	private long millis;

	public SummaryHub(HashMap<Integer, List<String>> summaryPaths, String tableHead, String fileName) {
		this.summaryPaths = summaryPaths;
		this.tableHead = tableHead;
		this.fileName = fileName;
		targetHashMap = new HashMap<>();
		summaryLog = new ArrayList<>();
	}

	@Override
	public void run() {
		millis = getStartTime();
		Main.log.add(Main.properties.getProperty("threadStart").formatted(getClass()));
		int[] allowedDropSpaces = {1, 2, 3, 5}; // Work in progress
		boolean[] rawDropSpaces = {false, false, false, true, false, true, true, true};
		Thread[][] allThreads = new Thread[8][];
		for (int i : allowedDropSpaces) {
			List<String> listOfLabel = summaryPaths.get(i);
			targetHashMap.put(i, new HashMap<>());
			if (!rawDropSpaces[i - 1] && summaryPaths.get(i) != null) {
				// Run pretty
				Thread[] subThreads = new Thread[listOfLabel.size()];
				allThreads[i] = subThreads;
				for (int j = 0; j < subThreads.length; j++) {
					Log log = new Log();
					summaryLog.add(log);
					SummaryFileWorker summaryFileWorker = new SummaryFileWorker(listOfLabel.get(j), targetHashMap.get(i), i, log);
					subThreads[j] = new Thread(summaryFileWorker);
					subThreads[j].start();
				}
			} else {
				// Run raw
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
		Main.log.add(Main.properties.getProperty("threadComplete").formatted(getClass(), getStopwatch(millis)));
	}

	void mergeLog() {
		for (Log log : summaryLog) {
			Main.log.merge(log);
		}
	}

	void buildExcel() {
		buildContent();
		SummaryExcelBuilder summaryExcelBuilder = new SummaryExcelBuilder(contentContainer, tableHead, createFileName(fileName));
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
				for (int j = 0; j < 291; j++) {
					if (subMap.containsKey(j)) {
						contentContainer.put(i, j, subMap.get(j).getMass());
						Main.log.add(Main.properties.getProperty("print_target_hash_map").formatted(j, subMap.get(j).toString()));
					}
				}
			}
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