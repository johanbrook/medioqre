package tools;

public class TimerTool {

	private static String label;
	private static long startTime;
	
	public static void start(String probeName) {
		label = probeName;
		startTime = System.nanoTime();
	}
	public static void stop() {
		long dt = System.nanoTime() - startTime;
		Logger.log(label + ": " + dt / 1000000 + "ms ");
	}
	
}
