/**
*	Logger.java
*
*	@author Johan
*/

package tools;

public final class Logger {
	
	private static Logger instance;
	private static boolean logging = false;
	
	
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
	 * @pre isLogginEnabled() == true
	 */
	public static void log(String message) {
		if(logging){
			System.out.println(message);
		}
	}
	
	
	/**
	 * Log a formatted message and value to the stdout.
	 * 
	 * @param message The formatted string
	 * @param value The value
	 */
	public static void log(String message, int value) {
		if(logging)
			System.out.printf(message, value);
	}
	
	
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
}
