package application;

import com.kuka.roboticsAPI.applicationModel.tasks.ITaskLogger;

public class Logger {
	
	private static ITaskLogger logger;
	
	Logger(ITaskLogger _logger) {
		logger = _logger;
	}
	
	public static void log(String msg) {
		logger.info(msg);
	}

}
