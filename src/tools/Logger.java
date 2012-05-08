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
	 * Log an object to the system out.
	 * 
	 * @param msg The object to print
	 * @pre AppController.MODE == AppController.DEBUG
	 */
	public static void log(Object msg) {
		if(AppController.MODE == AppController.DEBUG) {
			System.out.println(msg);
		}
	}
	
	/**
	 * Log an error to the system out.
	 * 
	 * @param msg The object to log as error
	 * @pre AppController.MODE == AppController.DEBUG
	 */
	public static void err(Object msg) {
		if(AppController.MODE == AppController.DEBUG) {
			System.err.println(msg);
		}
	}
}
