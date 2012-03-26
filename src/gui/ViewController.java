package gui;

import graphics.bitmap.Bitmap;
import graphics.bitmap.BitmapTool;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.WindowConstants;


import constants.Direction;

import model.Position;


/**
 * A GUI-Interface in BASIC so we can track the IP-address.
 * 
 * @author Barber
 *
 */
public class ViewController implements PropertyChangeListener {

	// Screen
	private final int SCREEN_WIDTH;
	private final int SCREEN_HEIGHT;
	private BufferStrategy bufferStrategy;
	private Actor player;
	private Bitmap screen;
	private BufferedImage screenImage;

	public ViewController(KeyListener listener, int screenWidth, int screenHeight) {
		SCREEN_WIDTH = screenWidth;
		SCREEN_HEIGHT = screenHeight;

		screenImage = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		
		screen = new Bitmap(SCREEN_WIDTH, SCREEN_HEIGHT);
		screen.pixels = BitmapTool.getARGBarrayFromDataBuffer(screenImage.getRaster().getDataBuffer(), SCREEN_WIDTH, SCREEN_HEIGHT);
		screen.clear(0xff0000ff);
		
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
		frame.addKeyListener(listener);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// Setup the canvas and frame
		canvas.createBufferStrategy(2);
		this.bufferStrategy = canvas.getBufferStrategy();
		frame.pack();
		frame.setLocationRelativeTo(null);
	}

	private void initScene() {
		this.player = new Actor(new Position(SCREEN_WIDTH / 2,
				SCREEN_HEIGHT / 2));
	}

	public void render(double dt) {
		do {
			do {
				Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();

				screen.clear(0xffff00ff);
				
				if (player.getCurrentFrame() != null) {
					screen.blit(player.getCurrentFrame(), (int) player.getPosition().getX(), (int) player.getPosition().getY());
				} else 
					System.out.println("Playerimage is null!");

				g.drawImage(screenImage, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
				
				g.dispose();
			} while (bufferStrategy.contentsRestored());
			bufferStrategy.show();
		} while (bufferStrategy.contentsLost());
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		player.setDirection((Direction) evt.getNewValue());
		System.out.println(evt.getPropertyName() + " " + evt.getNewValue());
	}
}
