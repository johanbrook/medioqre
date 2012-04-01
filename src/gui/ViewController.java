package gui;

import graphics.bitmap.Bitmap;
import graphics.bitmap.BitmapTool;
import gui.animation.Actor;
import gui.tilemap.TileMap;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import datamanager.resourceloader.ResourceManager;

import tools.GraphicalFPSMeter;
import tools.TimerTool;

import event.Event;
import event.IEventHandler;

import model.Entity;

/**
 * A GUI-Interface in BASIC so we can track the IP-address.
 * 
 * @author Barber
 *
 */
public class ViewController implements IEventHandler {

	// Screen
	private final int SCREEN_WIDTH;
	private final int SCREEN_HEIGHT;
	private BufferStrategy bufferStrategy;
	private Actor player;
	private TileMap gameMap;
	private Bitmap screen;
	private BufferedImage screenImage;
	
	private GraphicalFPSMeter fpsmeter;

	public ViewController(KeyListener listener, int screenWidth, int screenHeight) {
		SCREEN_WIDTH = screenWidth;
		SCREEN_HEIGHT = screenHeight;

		screenImage = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		
		screen = new Bitmap(SCREEN_WIDTH, SCREEN_HEIGHT,BitmapTool.getARGBarrayFromDataBuffer(screenImage.getRaster(), SCREEN_WIDTH, SCREEN_HEIGHT));
		
		this.fpsmeter = new GraphicalFPSMeter();
		
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
		this.player = ResourceManager.loadActors()[0];
		try {
			this.gameMap = new TileMap("res/images/levels/l2.bmp");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void render(double dt) {
		do {
			do {
				Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
				
				TimerTool.start("BlitVisible");
				gameMap.blitVisibleTilesToBitmap(screen, new Rectangle((int)player.getPosition().getX(), (int)player.getPosition().getY(), SCREEN_WIDTH, SCREEN_HEIGHT));
				TimerTool.stop();
				
				TimerTool.start("Player");
				if (player.getCurrentFrame() != null) {
					player.update(dt);
					screen.blit(player.getCurrentFrame(), SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
				} else 
					System.out.println("Playerimage is null!");
				TimerTool.stop();
				
				TimerTool.start("Draw image");
				g.drawImage(screenImage, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
				TimerTool.stop();
				
				this.fpsmeter.tick(g);
				
				g.dispose();
			} while (bufferStrategy.contentsRestored());
			bufferStrategy.show();
		} while (bufferStrategy.contentsLost());
	}


	@Override
	public void onEvent(Event evt) {
		if (evt.getProperty() == Event.Property.DID_MOVE) {
			Entity p = (Entity) evt.getValue();
			player.setDirection(p.getDirection(), true);
			player.setPosition(p.getPosition());
			System.out.println(evt.getProperty() + " " + p.getDirection());
		} else if (evt.getProperty() == Event.Property.DID_STOP) {
			Entity p = (Entity) evt.getValue();
			player.setDirection(p.getDirection(), false);
			player.setPosition(p.getPosition());
			System.out.println(evt.getProperty() + " " + p.getDirection());
		}
	}
}
