public class SummaryExcelBuilder implements Runnable, Stopwatch {

	private final ContentContainer contentContainer;
	private final String tableHead;
	private final String fileName;

	private long millis;

	public SummaryExcelBuilder(ContentContainer contentContainer, String tableHead, String fileName) {
		this.contentContainer = contentContainer;
		this.tableHead = tableHead;
		this.fileName = fileName;
	}

	@Override
	public void run() {
		millis = getStartTime();
		Main.log.add(Main.properties.getProperty("threadStart").formatted(getClass()));
		//
		Main.log.add(Main.properties.getProperty("threadComplete").formatted(getClass(), getStopwatch(millis)));
	}
}