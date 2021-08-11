package armaturkin.core;

import armaturkin.reinforcement.ReinforcementLiteInfo;
import armaturkin.workers.SummaryFileWorker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SummaryThreadStarter {

	public static final int RAW = 0;
	public static final int PRETTY = 1;
	public static final int RAW_STAIRWAY = 2;
	private static final int[] DROP_SPACE_SET = {PRETTY, PRETTY, PRETTY, RAW, PRETTY, RAW, RAW, RAW_STAIRWAY};

	private final int id;
	private final int set;
	private List<String> labelPaths;
	private HashMap<Integer, ReinforcementLiteInfo> hashMap;
	private Thread[] subThreads;
	private List<Log> logList;

	public SummaryThreadStarter(int id) {
		this.id = id;
		set = DROP_SPACE_SET[id - 1];
		createThreads();
	}

	public HashMap<Integer, ReinforcementLiteInfo> getHashMap() {
		return hashMap;
	}

	public Thread[] getSubThreads() {
		return subThreads;
	}

	public List<Log> getLogList() {
		return logList;
	}

	public boolean isNullable() {
		return labelPaths == null;
	}

	private void createThreads() {
		labelPaths = Main.summaryPaths.get(id);
		hashMap = new HashMap<>();
		logList = new ArrayList<>();
		if (labelPaths != null) {
			subThreads = new Thread[labelPaths.size()];
			for (int j = 0; j < subThreads.length; j++) {
				Log log = new Log();
				logList.add(log);
				SummaryFileWorker summaryFileWorker = new SummaryFileWorker(labelPaths.get(j), hashMap, id, log, set);
				subThreads[j] = new Thread(summaryFileWorker);
				subThreads[j].start();
			}
		}
	}
}