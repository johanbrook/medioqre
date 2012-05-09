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

import controller.AppController;

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
import model.item.AmmoCrate;
import model.item.ICollectableItem;
import model.item.MedPack;
import model.weapon.MachineGun;
import model.weapon.Portal;
import model.weapon.PortalGun;
import model.weapon.Projectile;

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
	private boolean							doneLoading	= false;

	// Screen
	private Screen							screen;

	// Map
	private TileMap							tilemap;

	// Actors
	private Actor							player;
	private Map<CollidableObject, Actor>	enemies  = new IdentityHashMap<CollidableObject, Actor>();;
	private Map<CollidableObject, Actor>	projectiles  = new IdentityHashMap<CollidableObject, Actor>();;
	private Map<CollidableObject, Actor>	items  = new IdentityHashMap<CollidableObject, Actor>();;

	// Tools
	private GraphicalFPSMeter				fpsmeter;

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
						.loadTileMapFromResources("test_lvl.png");
				this.tilemap.setTileSheet(ResourceLoader
						.loadTileSheetFromResource("barberset.tilesheet"));
				this.tilemap.setViewPortSize(new Size(32 * 12, 32 * 20));
				this.tilemap.setTileSize(new Size(32, 32));

				this.screen.addDrawableToLayer(this.tilemap, 0);

				List<CollidableObject> entities = ((GameModel) evt.getValue())
						.getObjects();

				this.player = new Actor(new JSONObject(
						ResourceLoader
								.loadJSONStringFromResources("spritesheets/json/frank.actor")),
						gm.getPlayer());
				this.player.setCurrentAnimation("moveS");
				this.screen.addDrawableToLayer(this.player, 1);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			this.doneLoading = true;
		} else if (evt.getProperty() == Event.Property.NEW_WAVE) {

			for (Actor actor : this.projectiles.values()) {
				this.screen.removeDrawableFromLayer(actor);
			}
			for (Actor actor : this.enemies.values()) {
				this.screen.removeDrawableFromLayer(actor);
			}
			for (Actor actor : this.items.values()) {
				this.screen.removeDrawableFromLayer(actor);
			}
			
			List<CollidableObject> entities = (List) ((GameModel) evt
					.getValue()).getObjects();
			for (CollidableObject e : entities) {
				if (e instanceof Enemy) {
					Actor newA;
					try {
						newA = new Actor(
								new JSONObject(
										ResourceLoader
												.loadJSONStringFromResources("spritesheets/json/walker1.actor")),
								(Entity) e);
						this.enemies.put(e, newA);
						this.screen.addDrawableToLayer(newA, 1);
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
				} else if (e instanceof ICollectableItem) {
					Actor newA;
					try {
						newA = new Actor(
								new JSONObject(
										ResourceLoader
												.loadJSONStringFromResources("spritesheets/json/gameitems.actor")),
								e);
						this.items.put(e, newA);
						this.screen.addDrawableToLayer(newA, 1);
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
				}
			}
		} else if (evt.getProperty() == Event.Property.FIRED_WEAPON_SUCCESS) {
			if (evt.getValue() instanceof Projectile) {
				Projectile p = (Projectile) evt.getValue();

				if(true) {
//				if (p.getOwner() instanceof MachineGun) {
					Actor newA;
					try {
						newA = new Actor(
								new JSONObject(
										ResourceLoader
												.loadJSONStringFromResources("spritesheets/json/machinegun_projectile.actor")),
								(CollidableObject) evt.getValue());

						this.projectiles.put((CollidableObject) evt.getValue(),
								newA);
						this.screen.addDrawableToLayer(newA, 1);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		} else if (evt.getProperty() == Event.Property.PORTAL_CREATED) {
			Portal p = (Portal) evt.getValue();
			Actor newA;
			try {
				newA = new Actor(new JSONObject(
						ResourceLoader
								.loadJSONStringFromResources("spritesheets/json/portal.actor")),
						(CollidableObject) p);
				this.items.put((CollidableObject) p, newA);
				this.screen.addDrawableToLayer(newA, 1);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (evt.getProperty() == Event.Property.WAS_DESTROYED) {
			if (evt.getValue() instanceof CollidableObject) {
				CollidableObject o = (CollidableObject) evt.getValue();

				this.screen.removeDrawableFromLayer(this.enemies.remove(o));
				this.screen.removeDrawableFromLayer(this.projectiles.remove(o));
				this.screen.removeDrawableFromLayer(this.items.remove(o));
			}
		}
	}

	@Override
	public void display(GLAutoDrawable arg0)
	{
		fpsmeter.tick();

		GL2 gl = arg0.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		gl.glClear(GL2.GL_ALPHA_BITS);

//		 TimerTool.start("GL-Screen");
		if (doneLoading) {
			this.screen.setViewPort(this.player.getCollidableObject()
					.getPosition());
			this.screen.render(this.screen.getBounds(),
					this.screen.getBounds(), arg0, 0);
		}
//		 TimerTool.stop();
	}

	@Override
	public void init(GLAutoDrawable arg0)
	{
		GL2 gl = arg0.getGL().getGL2();
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE,
				GL2.GL_MODULATE);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LESS);
		gl.glEnable(GL2.GL_ALPHA_TEST);
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
