package armaturkin.service;

import java.io.IOException;

/**
 * @author Andrey Korneychuk on 07-Jan-22
 * @version 1.0
 */
public interface StorageService {

	void checkFavoriteDirectory();

	void restoreDirectory(String string) throws IOException;

	void deleteStorage(String path);
}