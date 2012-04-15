package tools;

import static tools.Logger.*;

public class TimerTool {

	private static String label;
	private static long startTime;

	public static void start(String probeName)
	{
		label = probeName;
		startTime = System.nanoTime();
	}

	public static void stop()
	{
		System.out.println(stopToString());
	}
	public static String stopToString()
	{
		return (label + ": " + stopToTimeDiffNanos() / 1000 + "µs ");
	}
	public static long stopToTimeDiffNanos() 
	{
		return (System.nanoTime() - startTime);
	}

}
