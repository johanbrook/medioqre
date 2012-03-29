package tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
*	GraphicalFPSMeter
*
*	Add an instance of this class to the screen,
*	and the current FPS will be shown.
*
*	@author Johan
*/

public class GraphicalFPSMeter {

	private String label;
	private boolean shouldLogFPS;
	
	private double timeLastFrame;
	private double timeThisSecond; 
	
	private int framesThisSecond;
	public int currentFPS;
	
	/**
	 * Create a new FPS meter with the label "FPS" and logging enabled.
	 * 
	 */
	public GraphicalFPSMeter(){
		this("FPS", true);
	}
	
	/**
	 * Create a new FPS meter.
	 * 
	 * @param label The label before the frame rate
	 * @param shouldLogFPS If the FPS should print to system out each second
	 */
	public GraphicalFPSMeter(String label, boolean shouldLogFPS) {
		this.label = label;
		this.shouldLogFPS = shouldLogFPS;
		
		this.timeLastFrame = System.nanoTime();
		this.timeThisSecond = 0;
		this.framesThisSecond = 0;
		this.currentFPS = 0;
	}
	
	
	/**
	 * Tell the FPS meter to measure the current FPS for this moment.
	 * 
	 * @param canvas The canvas to draw the string on
	 */
	public void tick(Graphics2D canvas) {
		canvas.setColor(Color.RED);
		canvas.setFont(new Font("Courier", Font.BOLD, 20));
		canvas.drawString(this.label+": "+ this.tick(), 10, 20);
	}
	
	/**
	 * Measure the FPS without drawing anything
	 * 
	 * @return The current FPS
	 */
	public int tick() {
		double timeThisFrame = System.nanoTime();
		double dt = timeThisFrame - timeLastFrame;
		
		if (timeThisSecond > 1000000000.0) {
			this.currentFPS = framesThisSecond;
			framesThisSecond = 0;
			timeThisSecond = 0;
			if (shouldLogFPS) System.out.println(this.label+": "+this.currentFPS);
			
		} else {
			timeThisSecond += dt;
			framesThisSecond++;
		}
		
		timeLastFrame = timeThisFrame;
		
		return this.currentFPS;
	}
	
	
	
}
