package tools;

import static tools.Logger.*;

/**
 * A tool for measuring time spend between to points.
 * 
 * @author John Barbero Unenge
 *
 */
public class TimerTool {

	private static String label;
	private static long startTime;

	/**
	 * Start the time taking.
	 * 
	 * @param probeName A name associated with the current time taking 
	 */
	public static void start(String probeName)
	{
		label = probeName;
		startTime = System.nanoTime();
	}

	/**
	 * Stop and log the result.
	 */
	public static void stop()
	{
		log(stopToString());
	}
	
	/**
	 * Stop the time taking.
	 * 
	 * @return A string with the result
	 */
	public static String stopToString()
	{
		return (label + ": " + stopToTimeDiffNanos() / 1000 + "µs ");
	}
	
	/**
	 * Stop and get the time spent.
	 * 
	 * @return The time in nano seconds
	 */
	public static long stopToTimeDiffNanos() 
	{
		return (System.nanoTime() - startTime);
	}
}
