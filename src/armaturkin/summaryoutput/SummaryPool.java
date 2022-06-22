package armaturkin.summaryoutput;

import armaturkin.core.Log;
import armaturkin.core.Main;
import armaturkin.reinforcement.ReinforcementLiteInfo;
import armaturkin.workers.SummaryFileWorker;
import armaturkin.workers.WorkerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SummaryPool {

	public static final byte RAW = 0;
	public static final byte PRETTY = 1;
	public static final byte RAW_STAIRWAY = 2;
	private static final byte[] DROP_SPACE_SET = {PRETTY, PRETTY, PRETTY, RAW, PRETTY, RAW, RAW, RAW_STAIRWAY};

	private final int id;
	private final int set;
	private List<String> labelPaths;
	private HashMap<Integer, ReinforcementLiteInfo> hashMap;
	private List<Log> logList;

	public static int getSet(String type) {
		type = type.toUpperCase();
		return switch (type) {
			case "RAW" -> RAW;
			case "PRETTY" -> PRETTY;
			case "RAW_STAIRWAY" -> RAW_STAIRWAY;
			default -> -1;
		};
	}

	public SummaryPool(int id) throws WorkerException {
		this.id = id;
		set = DROP_SPACE_SET[id - 1];
	}

	public SummaryPool(int id, int set) throws WorkerException {
		this.id = id;
		this.set = set;
	}

	public HashMap<Integer, ReinforcementLiteInfo> getHashMap() {
		return hashMap;
	}

	public List<Log> getLogList() {
		return logList;
	}

	public boolean isNotNull() {
		return labelPaths != null;
	}

	public void runSummaryFileWorkers() throws WorkerException {
		labelPaths = Main.app.getSummaryModel().getSummaryPaths().get(id);
		hashMap = new HashMap<>();
		logList = new ArrayList<>();
		if (labelPaths != null) {
			for (int i = 0; i < labelPaths.size(); i++) {
				Log log = new Log();
				logList.add(log);
				SummaryFileWorker summaryFileWorker = new SummaryFileWorker(labelPaths.get(i), hashMap, id, log, set);
				summaryFileWorker.run();
			}
		}
	}
}