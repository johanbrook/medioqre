package tools;

import java.util.HashMap;
import java.util.Map;

/**
 * A class used for keeping track of a bunch of CounterObjecs used for measuring 
 * time and performance.
 * 
 * @author John Barbero Unenge
 *
 */
public class CounterTool {

	private static Map<String, CounterObject>	counters	= new HashMap<String, CounterObject>();

	/**
	 * Get CounterObject for the probe.
	 * 
	 * If it hasn't been used before it created.
	 * 
	 * @param probeName The name of the probe
	 * @return The CounterObject
	 */
	public static CounterObject getCounter(String probeName)
	{
		CounterObject counter = counters.get(probeName);
		if (counter == null) {
			counter = new CounterObject(probeName);
			counters.put(probeName, counter);
		}
		return counter;
	}

}
