package tools;

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
		return (label + ": " + stopToTimeDiff() + "ms ");
	}
	public static long stopToTimeDiff() 
	{
		return (System.nanoTime() - startTime) / 1000000;
	}

}
