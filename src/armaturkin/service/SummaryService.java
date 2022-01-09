package armaturkin.service;

import java.io.IOException;

/**
 * @author Andrey Korneychuk on 06-Jan-22
 * @version 1.0
 */
public interface SummaryService {

	void downloadSummaryFile();

	void checkSummaryDropSpace(int i) throws InterruptedException;

	void consumeSummaryBuilderFile(String path) throws IOException;
}