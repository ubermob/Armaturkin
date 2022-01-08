package armaturkin.service;

/**
 * @author Andrey Korneychuk on 07-Jan-22
 * @version 1.0
 */
public interface FirstHarvestingService {

	void loadProductFile();

	void loadCalculatingFile();

	void downloadCalculatedFile();

	void preloadUpperDropSpace();
}