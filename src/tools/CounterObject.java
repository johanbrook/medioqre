package tools;

import static tools.Logger.*;

public class CounterObject {
	
	private String name;
	private int counter;
	private long startTime;
	private long timeSpent;
	
	public CounterObject(String probeName)
	{
		this.name = probeName;
		this.reset();
	}
	public void tick()
	{
		this.startTime = System.nanoTime();
	}
	public int tock()
	{
		this.timeSpent += System.nanoTime() - this.startTime;
		return ++this.counter;
	}

	public int stopToInt()
	{
		return this.counter;
	}
	public void stop()
	{
		if (this.counter == 0) {
			log("Counter didn't run!");
			return;
		}
		log(name + " ran " + this.stopToInt() + " times, which took: "+ (this.timeSpent / 1000) + "µs at an average of: "+ this.timeSpent / (this.counter) + "ns");
	}
	public void reset()
	{
		this.startTime = 0;
		this.counter = 0;
		this.timeSpent = 0;
	}
}
