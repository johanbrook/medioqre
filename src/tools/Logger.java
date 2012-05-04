package tools;

/**
 *	Logger class
 * 
 *	@author Johan
 *	@deprecated 2012-05-04 Not used.
 */
public final class Logger {
	
	public static final int LOG_ALL = 1;
	public static final int LOG_CONTROL = 2;
	public static final int LOG_GUI = 3;
	public static final int LOG_STATS = 4;
	public static final int LOG_EVENTS = 5;
	
	private static Logger instance;
	private static boolean logging = false;
	
	private static int logMode = LOG_ALL;
	
	private Logger() {}
	
	/**
	 * Get the Logger instance.
	 * 
	 * @return This instance
	 */
	public Logger getInstance() {
		if(instance == null)
			instance = new Logger();
		
		return instance;
	}
	
	
	/**
	 * Log a message to the stdout.
	 * 
	 * @param message The message
	 * @param type The logging type
	 * @pre isLogginEnabled() == true
	 */
	

	
	
	/**
	 * Enable logging.
	 * 
	 * @param cond Set to 'true' to enable logging to the stdout
	 */
	public static void setLogginEnabled(boolean cond) {
		logging = cond;
	}
	
	
	/**
	 * Get the logging status.
	 * 
	 * @return True if logging is enabled
	 */
	public static boolean isLogginEnabled() {
		return logging;
	}
	
	/**
	 * Add a log mode.
	 * 
	 * @param type The logging type
	 */
	public static void setLogMode(int type) {
		logMode = type;
	}
	
}
