package gui;

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

import tools.GraphicalFPSMeter;
import tools.TimerTool;

import model.Entity;
import model.GameModel;
import model.character.Enemy;
import model.character.Player;

import core.Rectangle;
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
	private boolean doneLoading = false;
	
	// Screen
	private Rectangle target;
	private Screen screen;

	// Actors
	private Actor player;
	private Map<Entity, Actor> enemies;

	// Tools
	private GraphicalFPSMeter fpsmeter;
	private GraphicalFPSMeter repaintFps;
	
	private GLAutoDrawable canvas;

	public ViewController(KeyListener listener, int screenWidth,
			int screenHeight)
	{
		EventBus.INSTANCE.register(this);

		this.fpsmeter = new GraphicalFPSMeter();
		this.repaintFps = new GraphicalFPSMeter();

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
		this.canvas = canvas;

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		frame.add(canvas);
		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
		
		FPSAnimator anim = new FPSAnimator(canvas, 60);
		anim.start();
	}

	private void initScene()
	{

	}

	public void render(double dt)
	{
		// fpsmeter.tick();
		// System.out.println("fps: " + this.fpsmeter.currentFPS);
	}

	@Override
	public void onEvent(Event evt)
	{
		if (evt.getProperty() == Event.Property.INIT_MODEL) {
			GameModel gm = (GameModel) evt.getValue();
			try {
				this.player = new Actor(
						new JSONObject(ResourceLoader.loadStringFromResourceFolder("frank.actor")),
						gm.getPlayer());
				this.player.setCurrentAnimation("moveS");
				this.screen.addDrawableToLayer(this.player, 1);
				
				List<Enemy> en = gm.getEnemies();
				this.enemies = new IdentityHashMap<Entity, Actor>();
				for (Enemy e : en) {
					Actor newA = new Actor(new JSONObject(ResourceLoader.loadStringFromResourceFolder("walker1.actor")),e);
					this.enemies.put(e, newA);
					this.screen.addDrawableToLayer(newA, 0);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.doneLoading = true;
		}

		if (evt.getValue() instanceof Entity) {
			if (evt.getProperty() == Event.Property.DID_MOVE) {
				Entity e = (Entity) evt.getValue();
				if (evt.getValue() instanceof Player) {
				
				}
			}
		}
	}

	@Override
	public void display(GLAutoDrawable arg0)
	{
		fpsmeter.tick();
//		System.out.println("fps: " + this.fpsmeter.currentFPS);

		GL2 gl = arg0.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		TimerTool.start("GL-Screen");
		if (doneLoading)
			this.screen.render(this.screen.getBounds(), this.screen.getBounds(), arg0);
		TimerTool.stop();
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
