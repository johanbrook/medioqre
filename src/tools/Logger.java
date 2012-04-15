/**
*	Logger.java
*
*	@author Johan
*/

package tools;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public final class Logger {
	
	public static final int LOG_ALL = 1;
	public static final int LOG_CONTROL = 2;
	public static final int LOG_GUI = 3;
	public static final int LOG_STATS = 4;
	public static final int LOG_EVENTS = 5;
	
	private static Logger instance;
	private static boolean logging = false;
	
	private static List<Integer> logModes = new LinkedList<Integer>();
	
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
	public static void log(Object message) {
		log(message, getLogModes());
	}
	
	public static void log(Object message, int ... modes) {
		
		if(isLogginEnabled()){
			boolean temp = false;
			for(int i = 0; i < modes.length; i++) {
				if(logModes.contains(modes[i]) || modes[i] == LOG_ALL)	
					temp = true;
			}
			
			if(temp)
				System.out.println(message);
		}
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
	
	/**
	 * Add a log mode.
	 * 
	 * @param modes The logging mode
	 */
	public static void addLogMode(int ... modes) {
		logModes.clear();
		for(Integer i : modes) {
			if(!logModes.contains(i))
				logModes.add(i);
		}
	}
	
	
	public static int[] getLogModes() {
		int[] temp = new int[logModes.size()];
		for(int i = 0; i < logModes.size(); i++){
			temp[i] = logModes.get(i);
		}
		
		return temp;
	}
}
