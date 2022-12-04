package armaturkin.core;

import armaturkin.controller.Controller;
import armaturkin.httpserver.HttpServer;
import armaturkin.model.*;
import armaturkin.service.*;
import armaturkin.steelcomponent.SteelComponentRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Andrey Korneychuk on 06-Jan-22
 * @version 1.0
 */
public class App {

	private final NotificationService notificationService;
	private final LogService logService;
	private final StorageService storageService;
	private final FirstHarvestingService firstHarvestingService;
	private final SummaryService summaryService;
	private final Properties properties;
	private Controller controller;
	private final Configuration config;
	private final FirstHarvestingModel firstHarvestingModel;
	private final SummaryModel summaryModel;
	private final ManuallyEntryModel manuallyEntryModel;
	private HttpServer server;
	private String workerExceptionMessage;

	public App() throws IOException {
		notificationService = new NotificationServiceImpl();
		logService = new LogServiceImpl();
		storageService = new StorageServiceImpl();
		firstHarvestingService = new FirstHarvestingServiceImpl();
		summaryService = new SummaryServiceImpl();
		server = null;
		properties = new Properties();
		loadMainProperties();
		Root.loadProperties();
		Root.checkDirectories();
		DesignCode.loadProperties();
		config = new Configuration(Root.programRootPath + Root.getProperty("config_file_name")
				, properties, this);
		firstHarvestingModel = new FirstHarvestingModelImpl();
		summaryModel = new SummaryModelImpl();
		manuallyEntryModel = new ManuallyEntryModelImpl();
		SteelComponentRepository.load();
		configInjection();
		appInjection();
		modelInjection();
	}

	public void close() throws IOException {
		if (server != null) {
			server.stop();
		}
		config.saveConfigFile();
		LogManager.saveLog();
		notificationService.saveAllNotifications();
	}

	public void addNotification(String notification) {
		notificationService.addNotification(notification);
	}

	public String getActualNotification() {
		return notificationService.getNotification();
	}

	public NotificationService getNotificationService() {
		return notificationService;
	}

	public void log(String string) {
		logService.add(string);
	}

	public void log(Exception exception) {
		logService.add(exception);
	}

	public LogService getLogService() {
		return logService;
	}

	public StorageService getStorageService() {
		return storageService;
	}

	public FirstHarvestingService getFirstHarvestingService() {
		return firstHarvestingService;
	}

	public SummaryService getSummaryService() {
		return summaryService;
	}

	public Configuration getConfig() {
		return config;
	}

	public FirstHarvestingModel getFirstHarvestingModel() {
		return firstHarvestingModel;
	}

	public SummaryModel getSummaryModel() {
		return summaryModel;
	}

	public ManuallyEntryModel getManuallyEntryModel() {
		return manuallyEntryModel;
	}

	public boolean hasWorkerException() {
		return workerExceptionMessage != null;
	}

	public String getWorkerExceptionMessage() {
		return workerExceptionMessage;
	}

	public void setWorkerExceptionMessage(String message) {
		workerExceptionMessage = message;
	}

	public void offWorkerException() {
		workerExceptionMessage = null;
	}

	public void setController(Controller controller) {
		this.controller = controller;
		controller.injection(
				this
				, config
				, firstHarvestingService
				, firstHarvestingModel
				, summaryService
				, summaryModel
		);
		((NotificationServiceImpl) notificationService).setController(controller);
		((StorageServiceImpl) storageService).setController(controller);
		((FirstHarvestingServiceImpl) firstHarvestingService).setController(controller);
		((SummaryServiceImpl) summaryService).setController(controller);
	}

	public Controller getController() {
		return controller;
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public void setHttpServer(HttpServer server) {
		this.server = server;
	}

	public void startHttpServer() {
		if (server != null) {
			Thread serverThread = new Thread(server);
			serverThread.start();
		}
	}

	private void loadMainProperties() {
		try (InputStream resource = Main.class.getResourceAsStream("/Main_properties.xml")) {
			properties.loadFromXML(resource);
		} catch (Exception e) {
			log(e);
		}
	}

	private void configInjection() {
		((StorageServiceImpl) storageService).setConfig(config);
		((FirstHarvestingServiceImpl) firstHarvestingService).setConfig(config);
		((SummaryServiceImpl) summaryService).setConfig(config);
	}

	private void appInjection() {
		((StorageServiceImpl) storageService).setApp(this);
		((FirstHarvestingServiceImpl) firstHarvestingService).setApp(this);
		((SummaryServiceImpl) summaryService).setApp(this);
	}

	private void modelInjection() {
		((FirstHarvestingServiceImpl) firstHarvestingService).setFirstHarvestingModel(firstHarvestingModel);
		((SummaryServiceImpl) summaryService).setSummaryModel(summaryModel);
		((SummaryServiceImpl) summaryService).setManuallyEntryModel(manuallyEntryModel);
	}
}