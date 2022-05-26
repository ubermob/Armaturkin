package armaturkin.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Andrey Korneychuk on 07-Jan-22
 * @version 1.0
 */
public class StorageServiceImpl extends AbstractService implements StorageService {

	@Override
	public void checkFavoriteDirectory() {
		if (config.isFavoritePathNotNull()) {
			controller.setFavoriteDropSpaceText(app.getProperty("favorite_is_on").formatted(config.getFavoritePath()));
			if (Files.notExists(Path.of(config.getFavoritePath()))) {
				try {
					restoreDirectory(config.getFavoritePath());
					app.getNotificationService()
							.addNotification(app.getProperty("favorite_is_restored_1").formatted(config.getFavoritePath()));
					app.log(app.getProperty("favorite_is_restored_2").formatted(getClass()));
				} catch (Exception e) {
					app.getNotificationService()
							.addNotification(app.getProperty("favorite_restore_failed_1").formatted(config.getFavoritePath()));
					app.log(app.getProperty("favorite_restore_failed_2").formatted(getClass()));
					app.log(e);
					controller.setFavoriteDropSpaceText(
							app.getProperty("favorite_restore_failed_1").formatted(config.getFavoritePath())
					);
				}
			}
		} else {
			controller.setFavoriteDropSpaceText(app.getProperty("favorite_is_off"));
		}
	}

	@Override
	public void restoreDirectory(String string) throws IOException {
		String[] branch = string.split("\\\\");
		String path = "";
		for (String directory : branch) {
			path += directory + "\\";
			if (Files.notExists(Path.of(path))) {
				Files.createDirectory(Path.of(path));
			}
		}
	}

	@Override
	public void deleteStorage(String path) {
		try {
			List<Path> collect = Files.list(Path.of(path)).collect(Collectors.toList());
			for (Path file : collect) {
				Files.delete(file);
			}
		} catch (IOException e) {
			app.log(e);
		}
	}
}