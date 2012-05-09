package tools;

import static tools.Logger.*;

import java.util.HashMap;
import java.util.Map;

public class CounterTool {

	private static Map<String, CounterObject>	counters	= new HashMap<String, CounterObject>();

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
