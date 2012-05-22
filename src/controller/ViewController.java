package controller;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

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

import org.json.JSONException;
import org.json.JSONObject;

import com.jogamp.opengl.util.FPSAnimator;

import sun.java2d.d3d.D3DGraphicsDevice;
import tools.datamanagement.PreferenceLoader;
import tools.datamanagement.ResourceLoader;
import tools.factory.ObjectFactory;
import tools.timer.GraphicalFPSMeter;

import event.Event;
import event.EventBus;
import event.IEventHandler;

import model.CollidableObject;
import model.GameModel;
import model.character.Player;
import model.weapon.AbstractWeapon;
import model.weapon.Grenade;
import model.weapon.MachineGun;
import model.weapon.Melee;
import model.weapon.Portal;
import model.weapon.PortalGun;
import model.weapon.Projectile;

import graphics.opengl.GLScreen;
import graphics.opengl.animation.Actor;
import graphics.opengl.animation.AnimationListener;
import graphics.opengl.bitmapfont.GLBitmapFont;
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
	private boolean gamePaused = false;

	// Screen
	private GLScreen screen;

	// Overlay
	private List<GLRenderableObject> overlayObjects = new LinkedList<GLRenderableObject>();
	private GLBitmapFont ammoMeter;
	private GLBitmapFont scoreMeter;
	
	private GLBitmapFont hpMeter;
	private GLBitmapFont fpsMeter;
	
	private GLBitmapFont waveMeter;
	private Actor statusHud;

	private GLBitmapFont gamePausedText;
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
	public ViewController(KeyListener listener, Dimension size) {
		
		EventBus.INSTANCE.register(this);

		this.fpsmeter = new GraphicalFPSMeter();

		this.screen = new GLScreen(0, 0, size.width, size.height);

		// Creating the frame
		GLProfile glP = GLProfile.getDefault();
		GLCapabilities glC = new GLCapabilities(glP);
		glC.setDoubleBuffered(true);
		GLCanvas canvas = new GLCanvas(glC);

		// Creating the frame
		canvas.setFocusable(true);
		
		canvas.addKeyListener(listener);
		canvas.addGLEventListener(this);
		
			JSONObject jsonFont = ResourceLoader.parseJSONFromPath("spritesheets/json/font.bmf");
			
			this.hpMeter = new GLBitmapFont(jsonFont);
			this.hpMeter.setBounds(new Rectangle(10, this.screen.getBounds().getHeight() - 80, 250, 20));
			this.hpMeter.setLetterWidth(10);
			
			this.fpsMeter = new GLBitmapFont(jsonFont);
			this.fpsMeter.setBounds(new Rectangle(10, 10, 100,10));
			this.fpsMeter.setLetterWidth(10);
			
			this.waveMeter = new GLBitmapFont(jsonFont);
			this.waveMeter.setBounds(new Rectangle(10, this.screen.getBounds().getHeight() - 40, 60, 10));
			this.waveMeter.setLetterWidth(10);
			
			this.ammoMeter = new GLBitmapFont(jsonFont);
			this.ammoMeter.setBounds(new Rectangle(this.screen.getBounds().getWidth()-70, this.screen.getBounds().getHeight() - 50, 60, 40));
			this.ammoMeter.setLetterWidth(20);
			
			this.scoreMeter = new GLBitmapFont(jsonFont);
			this.scoreMeter.setBounds(new Rectangle(this.screen.getBounds().getWidth()-70, this.screen.getBounds().getHeight() - 87, 60, 20));
			this.scoreMeter.setLetterWidth(10);
			
			this.gamePausedText = new GLBitmapFont(jsonFont);
			int width = 300;
			int height = 80;
			this.gamePausedText.setBounds(new Rectangle(this.screen.getBounds().getWidth() / 2 - width / 2, this.screen.getBounds().getHeight() / 2 - 100, width, height));
			this.gamePausedText.setLetterWidth(width / 11);
			
			this.statusHud = ObjectFactory.newActor(1);
			this.statusHud.setX(this.screen.getBounds().getWidth()-this.statusHud.getWidth());
			this.statusHud.setY(this.screen.getBounds().getHeight()-this.statusHud.getHeight());
			
			this.overlayObjects.add(this.hpMeter);
			this.overlayObjects.add(this.fpsMeter);
			this.overlayObjects.add(this.waveMeter);
			this.overlayObjects.add(this.scoreMeter);
			this.overlayObjects.add(this.ammoMeter);
			this.overlayObjects.add(this.gamePausedText);
			this.overlayObjects.add(this.statusHud);
			
			this.fpsMeter.setColor(1f, 0f, 1f);
			this.hpMeter.setColor(1f, 0f, 0.1f);
			this.waveMeter.setColor(1f, 0f, 0f);
			this.ammoMeter.setColor(0.8f, 0.8f, 0.8f);
			this.gamePausedText.setColor(1f, 1f, 1f);
			
		
		this.isInLSDMode = PreferenceLoader.getBoolean("LSD_MODE", false);

		JFrame f = new JFrame();
		
		f.getContentPane().add(canvas);		
		
		if (AppController.isDebugMode()) {
			f.setPreferredSize(new Dimension(size.width, size.height));
			f.pack();
		} else {
			f.setUndecorated(true);
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(f);
		}
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BufferedImage i = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	    Cursor noCursor = java.awt.Toolkit.getDefaultToolkit().createCustomCursor(i, new java.awt.Point(0,0), "none");
		canvas.requestFocusInWindow();
		f.setCursor(noCursor);
		f.setVisible(true);
		
		FPSAnimator anim = new FPSAnimator(canvas, 60);
		anim.start();
	}

	@Override
	public void onEvent(Event evt) {
		if (evt.getProperty() == Event.Property.NEW_GAME) {
			GameModel gm = (GameModel) evt.getValue();

			this.actors.clear();
			this.screen.removeAll();
			
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

			if (this.waveMeter != null){
				this.waveMeter.setText("Wave:" + gm.getCurrentWaveCount());
			}
				
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
					Actor explosion = ObjectFactory.newActor(0);
					explosion.addAnimationListener(this);
					explosion.setShouldRepeat(false);
					explosion.setCurrentAnimation("default");
					explosion.setX((cObj.getPosition().x-(explosion.getWidth()/2)));
					explosion.setY(cObj.getPosition().y-(explosion.getHeight()/2));
					this.screen.addDrawableToLayer(explosion, 1);
				}
			}
		} else if (evt.getProperty() == Event.Property.PAUSE_GAME) {
			this.gamePaused = true;
			this.gamePausedText.setText("Game Paused");
		} else if (evt.getProperty() == Event.Property.UNPAUSE_GAME) {
			this.gamePaused = false;
			this.gamePausedText.setText(" ");
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
			if (!this.gamePaused) {
				this.screen.update(0);
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
					this.hpMeter.setText("HP: " + p.getHealth());
					this.ammoMeter.setText(""
							+ p.getCurrentWeapon().getCurrentAmmo());
				}
				if (p != null) {
					AbstractWeapon aw = p.getCurrentWeapon();
					
					if (aw instanceof MachineGun)
						this.statusHud.setCurrentAnimation("machinegun");
					else if (aw instanceof Grenade)
						this.statusHud.setCurrentAnimation("grenade");
					else if (aw instanceof Melee)
						this.statusHud.setCurrentAnimation("sword");
					else if (aw instanceof PortalGun) {
						switch (((PortalGun) aw).getMode()) {
							case BLUE :
								this.statusHud.setCurrentAnimation("portal_blue");
								break;
							case ORANGE :
								this.statusHud.setCurrentAnimation("portal_orange");
								break;
						}
					}
					
					this.scoreMeter.setText(""+1337);
				}
			}

			this.fpsMeter.setText(("FPS:" + this.fpsmeter.currentFPS));
			gl.glPushMatrix();
			gl.glLoadIdentity();
			for (GLRenderableObject ro : this.overlayObjects) {
				ro.update(16);
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
