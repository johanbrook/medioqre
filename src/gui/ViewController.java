package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class ViewController implements Runnable{

	public static void main(String[] args)
	{
		new ViewController(null, 1280, 760);
	}
	
	// FPS
	private final int FPS = 60;
	
	// Screen
	private final int SCREEN_WIDTH;
	private final int SCREEN_HEIGHT;
	private BufferStrategy bufferStrategy;
	
	public ViewController(KeyListener l, int screenWidth, int screenHeight)
	{
		SCREEN_WIDTH = screenWidth;
		SCREEN_HEIGHT = screenHeight;
		
		Canvas canvas = new Canvas();
		canvas.setIgnoreRepaint(true);
		canvas.setPreferredSize(new Dimension(screenWidth,screenHeight));
		
		// Creating the frame
		JFrame frame = new JFrame("Frank The Tank");
		frame.setIgnoreRepaint(true);
		frame.add(canvas);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		// Create the canvas
		canvas.createBufferStrategy(2);
		this.bufferStrategy = canvas.getBufferStrategy();
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		
		// Starting runLoop
		Thread t = new Thread(this);
		t.start();
	}

	private void render() {
		do {
			do {
				Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
				
				g.setColor(Color.RED);
				g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
				
				g.dispose();
			} while (bufferStrategy.contentsRestored());
				bufferStrategy.show();
		} while (bufferStrategy.contentsLost());
	}
	
	@Override
	public void run() {
		while(!Thread.interrupted()) {
			try {
				Thread.sleep(1000/FPS);
				render();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
