package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import model.Position;
public class ViewController implements Runnable, PropertyChangeListener {

	
	public static void main(String[] args){
		new ViewController(null, 1280, 760);
		
	}

	// FPS
	private final int FPS = 60;

	// Screen
	private final int SCREEN_WIDTH;
	private final int SCREEN_HEIGHT;
	private BufferStrategy bufferStrategy;
	private Actor player;

	public ViewController(KeyListener l, int screenWidth, int screenHeight) {
		SCREEN_WIDTH = screenWidth;
		SCREEN_HEIGHT = screenHeight;

		initScene();

		// Creating the frame
		Canvas canvas = new Canvas();
		canvas.setIgnoreRepaint(true);
		canvas.setPreferredSize(new Dimension(screenWidth, screenHeight));

		// Creating the frame
		JFrame frame = new JFrame("Frank The Tank");
		frame.setIgnoreRepaint(true);
		frame.add(canvas);
		frame.setVisible(true);
		frame.addKeyListener(l);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// Setup the canvas and frame
		canvas.createBufferStrategy(2);
		this.bufferStrategy = canvas.getBufferStrategy();
		frame.pack();
		frame.setLocationRelativeTo(null);

		// Starting runLoop
		Thread t = new Thread(this);
		t.start();
	}

	private void initScene() {
		this.player = new Actor(new Position(SCREEN_WIDTH / 2,
				SCREEN_HEIGHT / 2));
	}

	private void render() {
		do {
			do {
				Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();

				// Drawing
				g.setColor(Color.RED);
				g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
				g.setColor(Color.GREEN);
				g.fillRect((int) player.getPosition().getX(), (int) player
						.getPosition().getY(), 32, 64);

				g.dispose();
			} while (bufferStrategy.contentsRestored());
			bufferStrategy.show();
		} while (bufferStrategy.contentsLost());
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				Thread.sleep(1000 / FPS);
				render();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println(evt.getPropertyName() + " " + evt.getNewValue());
	}
}
