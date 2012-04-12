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
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import constants.Direction;

import datamanager.resourceloader.ResourceManager;

import sun.font.FontFamily;
import tools.GraphicalFPSMeter;
import tools.Logger;
import tools.TimerTool;

import event.Event;
import event.EventBus;
import event.IEventHandler;
import event.Event.Property;

import model.Entity;
import model.GameModel;
import model.character.Enemy;
import model.character.Player;

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
	private Map<Entity, Actor> enemies;
	private TileMap gameMap;
	private Bitmap screen;
	private BitmapFont fpsBitmap;
	private BufferedImage screenImage;

	private GraphicalFPSMeter fpsmeter;

	public ViewController(KeyListener listener, int screenWidth,
			int screenHeight)
	{
		EventBus.INSTANCE.register(this);

		SCREEN_WIDTH = screenWidth;
		SCREEN_HEIGHT = screenHeight;

		screenImage = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT,
				BufferedImage.TYPE_INT_ARGB);

		screen = new Bitmap(SCREEN_WIDTH, SCREEN_HEIGHT,
				BitmapTool.getARGBarrayFromDataBuffer(screenImage.getRaster(),
						SCREEN_WIDTH, SCREEN_HEIGHT));

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

	private void initScene()
	{
		try {
			this.gameMap = new TileMap("res/images/levels/l2.bmp");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void render(double dt)
	{
		do {
			do {
				Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();

				if (this.player != null) {
					gameMap.blitVisibleTilesToBitmap(screen, new Rectangle(
							(int) player.getPosition().getX() - SCREEN_WIDTH
									/ 2, (int) player.getPosition().getY()
									- SCREEN_HEIGHT / 2, SCREEN_WIDTH,
							SCREEN_HEIGHT));
				}

				if (this.enemies != null) {
					for (int i = 0; i < this.enemies.size(); i++) {
						if (this.enemies.get(i) != null) {
							this.enemies.get(i).update(dt);
							if (this.enemies.get(i).getCurrentFrame() != null) {
								screen.blit(this.enemies.get(i)
										.getCurrentFrame(),
										this.enemies.get(i).getPosition().x
												- this.player.getPosition().x
												+ SCREEN_WIDTH / 2,
										this.enemies.get(i).getPosition().y
												- this.player.getPosition().y
												+ SCREEN_HEIGHT / 2);
							}
						}
					}
				}

				if (player != null) {
					player.update(dt);

					if (player.getCurrentFrame() != null) {
						screen.blit(player.getCurrentFrame(), SCREEN_WIDTH / 2,
								SCREEN_HEIGHT / 2);
					} else
						System.out.println("Playerimage is null!");
				}

				fpsmeter.tick();
				fpsBitmap.setText("fps: " + this.fpsmeter.currentFPS);
				screen.blit(fpsBitmap.getBitmap(), 5, 5);

				g.drawImage(screenImage, 0, 0, this.canvas.getWidth(),
						this.canvas.getHeight(), null);

				g.dispose();
			} while (bufferStrategy.contentsRestored());
			bufferStrategy.show();
		} while (bufferStrategy.contentsLost());
	}

	@Override
	public void onEvent(Event evt)
	{

		if (evt.getProperty() == Event.Property.INIT_MODEL) {
			System.out.println("Run");
			if (evt.getValue() instanceof GameModel) {
				this.enemies = new IdentityHashMap<Entity, Actor>();
				List<Entity> entities = ((GameModel) evt.getValue())
						.getEntities();
				Actor pActor = ResourceManager.loadActors()[0];
				for (Entity e : entities) {
					Actor newA = pActor.clone();
					newA.setEntity(e);
					this.enemies.put(e, newA);
					if (e instanceof Player)
						this.player = newA;
				}
			}
		}

		if (evt.getValue() instanceof Entity) {
			Entity p = (Entity) evt.getValue();

			if (p instanceof model.character.Player) {
				if (this.player == null)
					return;
				if (evt.getProperty() == Event.Property.DID_MOVE) {
					player.startMoving();
				} else if (evt.getProperty() == Event.Property.DID_STOP) {
					player.stopMoving();
				}
			}
			if (p instanceof model.character.Enemy) {
				if (this.enemies == null)
					return;
				if (this.enemies.get(p) == null)
					return;
				if (evt.getProperty() == Event.Property.DID_MOVE) {
					enemies.get(p).startMoving();
				} else if (evt.getProperty() == Event.Property.DID_STOP) {
					enemies.get(p).stopMoving();
				}
			}

		}
	}
}
