package tools;

import controller.AppController;

/**
 *	Logger class
 *
 *	<p>Only logs messages to the system out if app is in debug mode</p>
 * 
 *	@author Johan
 */
public final class Logger {

	private static Logger instance;
	private static String logFormat = "H:m:s";

	private Logger() {
	}

	public void setTimestampFormat(String format) {
		logFormat = format;
	}

	/**
	 * Get the Logger instance.
	 * 
	 * @return This instance
	 */
	public static Logger getInstance() {
		if (instance == null)
			instance = new Logger();

		return instance;
	}
	
	/**
	 * Add a timestamp to a message.
	 * 
	 * @param msg The message
	 * @return The message prefixed with the current timestamp
	 */
	private static Object addTimestamp(Object msg) {
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(calendar
				.getTime().getTime());
		String stamp = new java.text.SimpleDateFormat(logFormat)
				.format(currentTimestamp);

		return "[" + stamp + "] " + msg;
	}

	/**
	 * Log an object to the system out.
	 * 
	 * @param msg The object to print
	 * @pre AppController.MODE == AppController.DEBUG
	 */
	public static void log(Object msg) {
		if (AppController.isDebugMode()) {
			System.out.println(addTimestamp(msg));
		}
	}

	/**
	 * Log an error to the system out.
	 * 
	 * @param msg The object to log as error
	 * @pre AppController.MODE == AppController.DEBUG
	 */
	public static void err(Object msg) {
		if (AppController.isDebugMode()) {
			System.err.println(addTimestamp(msg));
		}
	}
}
