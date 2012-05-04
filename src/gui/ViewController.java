package gui;

import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.json.JSONException;
import org.json.JSONObject;

import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.FPSAnimator;

import tilemap.TileMap;
import tools.GraphicalFPSMeter;
import tools.TimerTool;

import event.Event;
import event.EventBus;
import event.IEventHandler;
import event.Event.Property;

import model.CollidableObject;
import model.Entity;
import model.GameModel;
import model.character.Enemy;
import model.character.Player;

import core.Rectangle;
import core.Size;
import datamanagement.ResourceLoader;
import event.Event;
import event.EventBus;
import event.IEventHandler;
import graphics.opengl.Actor;

/**
 * A GUI-Interface in BASIC so we can track the IP-address.
 * 
 * @author Barber
 * 
 */
public class ViewController implements IEventHandler, GLEventListener {

	// State
	private boolean				doneLoading	= false;

	// Screen
	private Screen				screen;

	// Map
	private TileMap				tilemap;

	// Actors
	private Actor				player;
	private Map<Entity, Actor>	enemies;
	private Map<Entity, Actor>	projectiles;
	private Map<Entity, Actor>	items;

	// Tools
	private GraphicalFPSMeter	fpsmeter;

	public ViewController(KeyListener listener, int screenWidth,
			int screenHeight)
	{
		EventBus.INSTANCE.register(this);

		this.fpsmeter = new GraphicalFPSMeter();

		this.screen = new Screen(0, 0, screenWidth, screenHeight);

		// Creating the frame
		GLProfile glP = GLProfile.getDefault();
		GLCapabilities glC = new GLCapabilities(glP);
		GLCanvas canvas = new GLCanvas(glC);

		// Creating the frame
		JFrame frame = new JFrame("Frank The Tank");
		canvas.setFocusable(true);
		canvas.requestFocusInWindow();
		canvas.addKeyListener(listener);
		canvas.addGLEventListener(this);
		

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		frame.add(canvas);
		frame.setPreferredSize(new Dimension(screenWidth, screenHeight));
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);

		FPSAnimator anim = new FPSAnimator(canvas, 60);
		anim.start();
	}



	@Override
	public void onEvent(Event evt)
	{
		if (evt.getProperty() == Event.Property.INIT_MODEL) {
			GameModel gm = (GameModel) evt.getValue();
			try {
				this.tilemap = ResourceLoader
						.loadTileMapFromResources("test.png");
				this.tilemap.setTileSheet(ResourceLoader
						.loadTileSheetFromResource("tiles.tilesheet"));
				this.tilemap.setViewPortSize(new Size(48 * 12, 48 * 20));
				this.tilemap.setTileSize(new Size(48, 48));

				this.screen.addDrawableToLayer(this.tilemap, 0);

				this.enemies = new IdentityHashMap<Entity, Actor>();

				List<CollidableObject> entities = ((GameModel) evt.getValue())
						.getObjects();

				for (CollidableObject e : entities) {
					if (e instanceof Enemy) {
						Actor newA = new Actor(
								new JSONObject(
										ResourceLoader
												.loadJSONStringFromResources("walker1.actor")),
								(Entity) e);
						this.enemies.put((Entity) e, newA);
						this.screen.addDrawableToLayer(newA, 1);
					} else if (e instanceof Player) {
						this.player = new Actor(
								new JSONObject(
										ResourceLoader
												.loadJSONStringFromResources("frank.actor")),
								gm.getPlayer());
						this.player.setCurrentAnimation("moveS");
						this.screen.addDrawableToLayer(this.player, 1);
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			this.doneLoading = true;
		}
	}

	@Override
	public void display(GLAutoDrawable arg0)
	{
		fpsmeter.tick();
		// System.out.println("fps: " + this.fpsmeter.currentFPS);

		GL2 gl = arg0.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		// TimerTool.start("GL-Screen");
		if (doneLoading) {
			this.screen.setViewPort(this.player.getEntity().getPosition());
			this.screen.render(this.screen.getBounds(),
					this.screen.getBounds(), arg0);
		}
		// TimerTool.stop();
	}

	@Override
	public void init(GLAutoDrawable arg0)
	{
		GL2 gl = arg0.getGL().getGL2();
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE,
				GL2.GL_MODULATE);
	}

	@Override
	public void dispose(GLAutoDrawable arg0)
	{
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4)
	{
	}
}
