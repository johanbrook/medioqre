package gui;

import graphics.bitmap.Bitmap;
import graphics.bitmap.BitmapTool;
import graphics.bitmap.font.BitmapFont;
import gui.animation.Actor;
import gui.tilemap.TileMap;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import constants.Direction;

import datamanager.resourceloader.ResourceManager;

import sun.font.FontFamily;
import tools.GraphicalFPSMeter;
import tools.Logger;
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
	private Canvas canvas;
	private Actor player;
	private Actor[] enemies;
	private TileMap gameMap;
	private Bitmap screen;
	private BitmapFont fpsBitmap;
	private BufferedImage screenImage;
		
	private GraphicalFPSMeter fpsmeter;

	public ViewController(KeyListener listener, int screenWidth, int screenHeight) {
		SCREEN_WIDTH = screenWidth;
		SCREEN_HEIGHT = screenHeight;

		screenImage = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		
		screen = new Bitmap(SCREEN_WIDTH, SCREEN_HEIGHT,BitmapTool.getARGBarrayFromDataBuffer(screenImage.getRaster(), SCREEN_WIDTH, SCREEN_HEIGHT));
		
		this.fpsmeter = new GraphicalFPSMeter();
		this.fpsBitmap = new BitmapFont("");
		
		initScene();

		// Creating the frame
		Canvas canvas = new Canvas();
		canvas.setIgnoreRepaint(true);
		canvas.setPreferredSize(new Dimension(screenWidth, screenHeight));

		// Creating the frame
		JFrame frame = new JFrame("Frank The Tank");
		frame.setIgnoreRepaint(true);
		canvas.setFocusable(true);
		canvas.requestFocusInWindow();
		canvas.addKeyListener(listener);
		
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		frame.add(canvas);
		frame.setVisible(true);
		
		// Setup the canvas and frame
		canvas.createBufferStrategy(2);
		this.bufferStrategy = canvas.getBufferStrategy();
		this.canvas = canvas;
		frame.pack();
		frame.setLocationRelativeTo(null);
	}

	private void initScene() {
		this.player = ResourceManager.loadActors()[0];
		
//		int num = 10000;
//		this.enemies = new Actor[num];
//		Random r = new Random();
//		for (int i = 0; i < num; i++) {
//			this.enemies[i] = player.clone();
//			int rand = r.nextInt(8);
//			Direction d = Direction.SOUTH;
//			switch (rand) {
//			case 0 : 
//				d = Direction.SOUTH;
//			break;
//			case 1 : 
//				d = Direction.SOUTH_EAST;
//			break;
//			case 2 : 
//				d = Direction.SOUTH_WEST;
//			break;
//			case 3 : 
//				d = Direction.NORTH;
//			break;
//			case 4 : 
//				d = Direction.NORTH_EAST;
//			break;
//			case 5 : 
//				d = Direction.NORTH_WEST;
//			break;
//			case 6 : 
//				d = Direction.WEST;
//			break;
//			case 7 : 
//				d = Direction.EAST;
//			break;
//			}
//			this.enemies[i].setDirection(d, true);
//		}
		
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
				
				gameMap.blitVisibleTilesToBitmap(screen, new Rectangle((int)player.getPosition().getX(), (int)player.getPosition().getY(), SCREEN_WIDTH, SCREEN_HEIGHT));
				
//				TimerTool.start("DrawEnemies");
//				if (this.enemies != null) {
//					for (int i = 0; i < this.enemies.length; i++) {
//						if (this.enemies[i].getCurrentFrame() != null) {
//							this.enemies[i].update(dt);
//							screen.blit(this.enemies[i].getCurrentFrame(), (i/10)*32, 20+(i%10)*64);
//						}
//					}
//				}
//				TimerTool.stop();
				
				if (player.getCurrentFrame() != null) {
					player.update(dt);			
					screen.blit(player.getCurrentFrame(), SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
				} else 
					System.out.println("Playerimage is null!");
				
				fpsmeter.tick();
				fpsBitmap.setText("fps: " + this.fpsmeter.currentFPS);
				screen.blit(fpsBitmap.getBitmap(), 5, 5);
					
				g.drawImage(screenImage, 0, 0, this.canvas.getWidth(), this.canvas.getHeight(), null);
				
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
