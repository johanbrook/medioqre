package controller;

import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.util.HashMap;
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

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;



import tools.datamanagement.PreferenceLoader;
import tools.factory.ObjectFactory;
import tools.timer.GraphicalFPSMeter;

import event.Event;
import event.EventBus;
import event.IEventHandler;

import model.CollidableObject;
import model.GameModel;
import model.character.Player;
import model.weapon.Grenade;
import model.weapon.Portal;
import model.weapon.Projectile;

import graphics.opengl.GLScreen;
import graphics.opengl.animation.Actor;
import graphics.opengl.animation.AnimationListener;
import graphics.opengl.animation.Sprite;
import graphics.opengl.bitmapfont.GLBitmapFont;
import graphics.opengl.bitmapfont.GLLetter;
import graphics.opengl.core.GLRenderableObject;
import graphics.opengl.core.Rectangle;
import graphics.opengl.tilemap.TileMap;

/**
 * The ViewController for FrankTheTank.
 * 
 * It handles events from the model and passes them on, adds and removes actors.
 * 
 * @author John Barbero Unenge
 * 
 */
public class ViewController
		implements
			IEventHandler,
			GLEventListener,
			AnimationListener {

	// State
	private boolean doneLoading = false;

	// Screen
	private GLScreen screen;

	// Overlay
	private List<GLRenderableObject> overlayObjects = new LinkedList<GLRenderableObject>();
	private GLBitmapFont hpMeter;
	private GLBitmapFont fpsMeter;
	private GLBitmapFont ammoMeter;
	private GLBitmapFont waveMeter;

	// Map
	private TileMap tilemap;

	// Actors
	private Actor player;
	private Map<CollidableObject, Actor> actors = new IdentityHashMap<CollidableObject, Actor>();;

	// Tools
	private GraphicalFPSMeter fpsmeter;

	// LSD mode
	private float rotationAngle;
	private boolean isInLSDMode;

	/**
	 * Creates the ViewController with a given width and height, and adds the
	 * given KeyLister to itself.
	 * 
	 * @param listener
	 *            The keylistener
	 * @param screenWidth
	 *            The frame width
	 * @param screenHeight
	 *            The frame height
	 */
	public ViewController(KeyListener listener, int screenWidth,
			int screenHeight) {
		EventBus.INSTANCE.register(this);

		this.fpsmeter = new GraphicalFPSMeter();

		this.screen = new GLScreen(0, 0, screenWidth, screenHeight);

		// Creating the frame
		GLProfile glP = GLProfile.getDefault();
		GLCapabilities glC = new GLCapabilities(glP);
		glC.setDoubleBuffered(true);
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

		Map<Character, GLLetter> letters1 = new HashMap<Character, GLLetter>();
		letters1.put('H', new GLLetter(new Sprite("bitmapfont", 7 * 32, 0 * 32,
				32, 32), 'H'));
		letters1.put('p', new GLLetter(new Sprite("bitmapfont", 7 * 32, 5 * 32,
				32, 32), 'p'));
		letters1.put(':', new GLLetter(new Sprite("bitmapfont", 6 * 32, 8 * 32,
				32, 32), ':'));
		letters1.put(' ', new GLLetter(new Sprite("bitmapfont", 7 * 32, 3 * 32,
				32, 32), ' '));
		letters1.put('0', new GLLetter(new Sprite("bitmapfont", 5 * 32, 8 * 32,
				32, 32), '0'));
		letters1.put('1', new GLLetter(new Sprite("bitmapfont", 4 * 32, 7 * 32,
				32, 32), '1'));
		letters1.put('2', new GLLetter(new Sprite("bitmapfont", 5 * 32, 7 * 32,
				32, 32), '2'));
		letters1.put('3', new GLLetter(new Sprite("bitmapfont", 6 * 32, 7 * 32,
				32, 32), '3'));
		letters1.put('4', new GLLetter(new Sprite("bitmapfont", 7 * 32, 7 * 32,
				32, 32), '4'));
		letters1.put('5', new GLLetter(new Sprite("bitmapfont", 0 * 32, 8 * 32,
				32, 32), '5'));
		letters1.put('6', new GLLetter(new Sprite("bitmapfont", 1 * 32, 8 * 32,
				32, 32), '6'));
		letters1.put('7', new GLLetter(new Sprite("bitmapfont", 2 * 32, 8 * 32,
				32, 32), '7'));
		letters1.put('8', new GLLetter(new Sprite("bitmapfont", 3 * 32, 8 * 32,
				32, 32), '8'));
		letters1.put('9', new GLLetter(new Sprite("bitmapfont", 4 * 32, 8 * 32,
				32, 32), '9'));
		Map<Character, GLLetter> letters2 = new HashMap<Character, GLLetter>();
		letters2.put('H', new GLLetter(new Sprite("bitmapfont", 7 * 32, 0 * 32,
				32, 32), 'H'));
		letters2.put('F', new GLLetter(new Sprite("bitmapfont", 5 * 32, 0 * 32,
				32, 32), 'F'));
		letters2.put('P', new GLLetter(new Sprite("bitmapfont", 7 * 32, 1 * 32,
				32, 32), 'P'));
		letters2.put('S', new GLLetter(new Sprite("bitmapfont", 2 * 32, 2 * 32,
				32, 32), 'S'));
		letters2.put('p', new GLLetter(new Sprite("bitmapfont", 7 * 32, 5 * 32,
				32, 32), 'p'));
		letters2.put(':', new GLLetter(new Sprite("bitmapfont", 6 * 32, 8 * 32,
				32, 32), ':'));
		letters2.put(' ', new GLLetter(new Sprite("bitmapfont", 7 * 32, 3 * 32,
				32, 32), ' '));
		letters2.put('0', new GLLetter(new Sprite("bitmapfont", 5 * 32, 8 * 32,
				32, 32), '0'));
		letters2.put('1', new GLLetter(new Sprite("bitmapfont", 4 * 32, 7 * 32,
				32, 32), '1'));
		letters2.put('2', new GLLetter(new Sprite("bitmapfont", 5 * 32, 7 * 32,
				32, 32), '2'));
		letters2.put('3', new GLLetter(new Sprite("bitmapfont", 6 * 32, 7 * 32,
				32, 32), '3'));
		letters2.put('4', new GLLetter(new Sprite("bitmapfont", 7 * 32, 7 * 32,
				32, 32), '4'));
		letters2.put('5', new GLLetter(new Sprite("bitmapfont", 0 * 32, 8 * 32,
				32, 32), '5'));
		letters2.put('6', new GLLetter(new Sprite("bitmapfont", 1 * 32, 8 * 32,
				32, 32), '6'));
		letters2.put('7', new GLLetter(new Sprite("bitmapfont", 2 * 32, 8 * 32,
				32, 32), '7'));
		letters2.put('8', new GLLetter(new Sprite("bitmapfont", 3 * 32, 8 * 32,
				32, 32), '8'));
		letters2.put('9', new GLLetter(new Sprite("bitmapfont", 4 * 32, 8 * 32,
				32, 32), '9'));
		Map<Character, GLLetter> letters3 = new HashMap<Character, GLLetter>();
		letters3.put('A', new GLLetter(new Sprite("bitmapfont", 0 * 32, 0 * 32,
				32, 32), 'A'));
		letters3.put('m', new GLLetter(new Sprite("bitmapfont", 4 * 32, 5 * 32,
				32, 32), 'm'));
		letters3.put('o', new GLLetter(new Sprite("bitmapfont", 6 * 32, 5 * 32,
				32, 32), 'o'));
		letters3.put(':', new GLLetter(new Sprite("bitmapfont", 6 * 32, 8 * 32,
				32, 32), ':'));
		letters3.put(' ', new GLLetter(new Sprite("bitmapfont", 7 * 32, 3 * 32,
				32, 32), ' '));
		letters3.put('0', new GLLetter(new Sprite("bitmapfont", 5 * 32, 8 * 32,
				32, 32), '0'));
		letters3.put('1', new GLLetter(new Sprite("bitmapfont", 4 * 32, 7 * 32,
				32, 32), '1'));
		letters3.put('2', new GLLetter(new Sprite("bitmapfont", 5 * 32, 7 * 32,
				32, 32), '2'));
		letters3.put('3', new GLLetter(new Sprite("bitmapfont", 6 * 32, 7 * 32,
				32, 32), '3'));
		letters3.put('4', new GLLetter(new Sprite("bitmapfont", 7 * 32, 7 * 32,
				32, 32), '4'));
		letters3.put('5', new GLLetter(new Sprite("bitmapfont", 0 * 32, 8 * 32,
				32, 32), '5'));
		letters3.put('6', new GLLetter(new Sprite("bitmapfont", 1 * 32, 8 * 32,
				32, 32), '6'));
		letters3.put('7', new GLLetter(new Sprite("bitmapfont", 2 * 32, 8 * 32,
				32, 32), '7'));
		letters3.put('8', new GLLetter(new Sprite("bitmapfont", 3 * 32, 8 * 32,
				32, 32), '8'));
		letters3.put('9', new GLLetter(new Sprite("bitmapfont", 4 * 32, 8 * 32,
				32, 32), '9'));
		Map<Character, GLLetter> letters4 = new HashMap<Character, GLLetter>();
		letters4.put('W', new GLLetter(new Sprite("bitmapfont", 6 * 32, 2 * 32,
				32, 32), 'W'));
		letters4.put('a', new GLLetter(new Sprite("bitmapfont", 0 * 32, 4 * 32,
				32, 32), 'a'));
		letters4.put('v', new GLLetter(new Sprite("bitmapfont", 5 * 32, 6 * 32,
				32, 32), 'v'));
		letters4.put('e', new GLLetter(new Sprite("bitmapfont", 4 * 32, 4 * 32,
				32, 32), 'e'));
		letters4.put(':', new GLLetter(new Sprite("bitmapfont", 6 * 32, 8 * 32,
				32, 32), ':'));
		letters4.put(' ', new GLLetter(new Sprite("bitmapfont", 7 * 32, 3 * 32,
				32, 32), ' '));
		letters4.put('0', new GLLetter(new Sprite("bitmapfont", 5 * 32, 8 * 32,
				32, 32), '0'));
		letters4.put('1', new GLLetter(new Sprite("bitmapfont", 4 * 32, 7 * 32,
				32, 32), '1'));
		letters4.put('2', new GLLetter(new Sprite("bitmapfont", 5 * 32, 7 * 32,
				32, 32), '2'));
		letters4.put('3', new GLLetter(new Sprite("bitmapfont", 6 * 32, 7 * 32,
				32, 32), '3'));
		letters4.put('4', new GLLetter(new Sprite("bitmapfont", 7 * 32, 7 * 32,
				32, 32), '4'));
		letters4.put('5', new GLLetter(new Sprite("bitmapfont", 0 * 32, 8 * 32,
				32, 32), '5'));
		letters4.put('6', new GLLetter(new Sprite("bitmapfont", 1 * 32, 8 * 32,
				32, 32), '6'));
		letters4.put('7', new GLLetter(new Sprite("bitmapfont", 2 * 32, 8 * 32,
				32, 32), '7'));
		letters4.put('8', new GLLetter(new Sprite("bitmapfont", 3 * 32, 8 * 32,
				32, 32), '8'));
		letters4.put('9', new GLLetter(new Sprite("bitmapfont", 4 * 32, 8 * 32,
				32, 32), '9'));
		this.hpMeter = new GLBitmapFont(("Hp: " + 0), new Rectangle(10, 30, 60,
				10), letters1, 10);
		this.fpsMeter = new GLBitmapFont(("FPS: " + 0), new Rectangle(10, 10, 60,
				10), letters2, 10);
		this.ammoMeter = new GLBitmapFont(("Ammo: " + 0), new Rectangle(10,
				this.screen.getBounds().getHeight() - 20, 60, 10), letters3, 10);
		this.waveMeter = new GLBitmapFont(("Wave: " + 0), new Rectangle(10,
				this.screen.getBounds().getHeight() - 40, 60, 10), letters4, 10);
		this.overlayObjects.add(this.hpMeter);
		this.overlayObjects.add(this.fpsMeter);
		this.overlayObjects.add(this.ammoMeter);
		this.overlayObjects.add(this.waveMeter);
		this.fpsMeter.setColor(1f, 0f, 0f);
		this.hpMeter.setColor(0f, 1f, 0f);
		this.ammoMeter.setColor(0f, 0f, 1f);
		this.waveMeter.setColor(1f, 0f, 1f);

		this.isInLSDMode = PreferenceLoader.getBoolean("LSD_MODE", false);

		FPSAnimator anim = new FPSAnimator(canvas, 60);
		anim.start();
	}

	@Override
	public void onEvent(Event evt) {
		if (evt.getProperty() == Event.Property.INIT_MODEL) {
			GameModel gm = (GameModel) evt.getValue();

			this.tilemap = ObjectFactory.newTileMap();

			this.screen.addDrawableToLayer(this.tilemap, 0);

			this.player = ObjectFactory.newActor(gm.getPlayer());
			this.player.setCurrentAnimation("moveS");
			this.screen.addDrawableToLayer(this.player, 1);

			this.player.setColor(1f, 1f, 1f);
			this.doneLoading = true;
		} else if (evt.getProperty() == Event.Property.NEW_WAVE) {

			for (Actor actor : this.actors.values()) {
				this.screen.removeDrawableFromLayer(actor);
			}

			GameModel gm = (GameModel) evt.getValue();

			if (this.waveMeter != null)
				this.waveMeter.setText("Wave:" + gm.getCurrentWaveCount());
			List<CollidableObject> entities = (List<CollidableObject>) gm.getObjects();

			for (CollidableObject e : entities) {
				if (e instanceof Player)
					continue;

				Actor newA = ObjectFactory.newActor(e);
				this.actors.put(e, newA);
				this.screen.addDrawableToLayer(newA, 1);
			}
		} else if (evt.getProperty() == Event.Property.FIRED_WEAPON_SUCCESS) {
			if (evt.getValue() instanceof CollidableObject) {
				CollidableObject cObj = (CollidableObject) evt.getValue();
				Actor newA = ObjectFactory.newActor(cObj);
				this.actors.put(cObj, newA);
				this.screen.addDrawableToLayer(newA, 1);
				System.out.println("Creating projectile!");
			}
		} else if (evt.getProperty() == Event.Property.PORTAL_CREATED) {
			Portal p = (Portal) evt.getValue();
			Actor newA = ObjectFactory.newActor(p);
			this.actors.put(p, newA);
			this.screen.addDrawableToLayer(newA, 1);
		} else if (evt.getProperty() == Event.Property.WAS_DESTROYED) {
			if (evt.getValue() instanceof CollidableObject) {
				CollidableObject cObj = (CollidableObject) evt.getValue();

				this.screen.removeDrawableFromLayer(this.actors.remove(cObj));
				// this.actors.remove(cObj);

				if (cObj instanceof Projectile && ((Projectile) cObj).getOwner() instanceof Grenade) {
					Actor explosion = ObjectFactory.newActor(null);
					explosion.addAnimationListener(this);
					explosion.setShouldRepeat(false);
					explosion.setCurrentAnimation("default");
					explosion.setX((cObj.getPosition().x-(explosion.getWidth()/2)));
					explosion.setY(cObj.getPosition().y-(explosion.getHeight()/2));
					this.screen.addDrawableToLayer(explosion, 1);
				}
			}
		}
	}

	@Override
	public void display(GLAutoDrawable arg0) {
		fpsmeter.tick();

		GL2 gl = arg0.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		gl.glClear(GL2.GL_ALPHA_BITS);

		// TimerTool.start("GL-Screen");
		if (doneLoading) {
			if (this.isInLSDMode) {
				gl.glPushMatrix();
				this.rotationAngle += 1;
				gl.glRotated(this.rotationAngle, 0, 0, 1);
			}
			this.screen.setViewPort(this.player.getCollidableObject()
					.getPosition());
			this.screen.render(this.screen.getBounds(),
					this.screen.getBounds(), arg0, 0);
			if (this.isInLSDMode)
				gl.glPopMatrix();

			if (this.player != null) {
				Player p = (Player) this.player.getCollidableObject();
				if (p != null && this.hpMeter != null) {
					this.hpMeter.setText("Hp:" + p.getHealth());
					this.ammoMeter.setText("Ammo:"
							+ p.getCurrentWeapon().getCurrentAmmo());
				}
			}

			this.fpsMeter.setText(("FPS:" + this.fpsmeter.currentFPS));
			gl.glPushMatrix();
			gl.glLoadIdentity();
			for (GLRenderableObject ro : this.overlayObjects) {
				ro.render(ro.getBounds(), this.screen.getBounds(), arg0,
						(20 + this.tilemap.getTileMapSize().getHeight()
								* this.tilemap.getTileSize().getHeight()));
			}
			gl.glPopMatrix();
		}
		gl.glFlush();
		// TimerTool.stop();
	}

	@Override
	public void init(GLAutoDrawable arg0) {
		GL2 gl = arg0.getGL().getGL2();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE,
				GL2.GL_MODULATE);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LESS);
		gl.glEnable(GL2.GL_ALPHA_TEST);

		if (this.isInLSDMode)
			gl.glRotated(45, -1, -1, -1);
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
	}

	@Override
	public void animationDoneAnimating(Object value) {
		System.out.println("1");
		if (value instanceof Actor) {
			Actor actor = (Actor) value;
			this.screen.removeDrawableFromLayer(actor);
			this.actors.remove(actor.getCollidableObject());
			System.out.println("2");
		}
	}

}
