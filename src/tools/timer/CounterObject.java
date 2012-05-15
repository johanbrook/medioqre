package tools.timer;

import static tools.Logger.*;

/**
 * An object used for measuring time and performance.
 * 
 * @author John Barbero Unenge
 * 
 */
public class CounterObject {

	private String name;
	private int counter;
	private long startTime;
	private long timeSpent;

	/**
	 * Creates a CounterObject with the given probename.
	 * 
	 * @param probeName
	 *            The probename
	 */
	public CounterObject(String probeName) {
		this.name = probeName;
		this.reset();
	}
	/**
	 * Start timing.
	 */
	public void tick() {
		this.startTime = System.nanoTime();
	}
	/**
	 * Stop timing.
	 * 
	 * @return
	 */
	public int tock() {
		this.timeSpent += System.nanoTime() - this.startTime;
		return ++this.counter;
	}

	/**
	 * Get the number of times tick and tock have been called.
	 * 
	 * @return The number of times
	 */
	public int stopToInt() {
		return this.counter;
	}

	/**
	 * Stop the CounterObjec and log the results.
	 * 
	 * You'll get somthing like: RenderingProbe ran 10 times, which took: 20µs
	 * at an average of: 2000ns
	 */
	public void stop() {
		if (this.counter == 0) {
			log("Counter didn't run!");
			return;
		}
		log(name + " ran " + this.stopToInt() + " times, which took: "
				+ (this.timeSpent / 1000) + "µs at an average of: "
				+ this.timeSpent / (this.counter) + "ns");
	}

	/**
	 * Reset the counter.
	 */
	public void reset() {
		this.startTime = 0;
		this.counter = 0;
		this.timeSpent = 0;
	}
}
