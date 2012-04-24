package gui;

import java.awt.event.KeyListener;
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

	// Screen
	private Rectangle target;

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

		this.target = new Rectangle(0, 0, screenWidth, screenHeight);

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
		
		FPSAnimator anim = new FPSAnimator(canvas, 30);
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
						new JSONObject(ResourceLoader.loadStringFromAbsolutePath("/Users/Barber/Programmering/EclipseProjects/medioqre/res/spritesheets/frank.actor")),
						gm.getPlayer());
				this.player.setCurrentAnimation("moveS");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (evt.getValue() instanceof Entity) {
			if (evt.getProperty() == Event.Property.DID_MOVE) {
				Entity e = (Entity) evt.getValue();
				if (evt.getValue() instanceof Player) {
					System.out.println("Derp");
					switch (e.getDirection()) {
					case SOUTH:
						this.player.setCurrentAnimation("moveS");
						break;
					case SOUTH_WEST:
						this.player.setCurrentAnimation("moveSW");
						break;
					case WEST:
						this.player.setCurrentAnimation("moveW");
						break;
					case NORTH_WEST:
						this.player.setCurrentAnimation("moveNW");
						break;
					case NORTH:
						this.player.setCurrentAnimation("moveN");
						break;
					case NORTH_EAST:
						this.player.setCurrentAnimation("moveNE");
						break;
					case EAST:
						this.player.setCurrentAnimation("moveE");
						break;
					case SOUTH_EAST:
						this.player.setCurrentAnimation("moveSE");
						break;
					}
				}
			}
		}
	}

	@Override
	public void display(GLAutoDrawable arg0)
	{
		fpsmeter.tick();
		System.out.println("fps: " + this.fpsmeter.currentFPS);

		GL2 gl = arg0.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		if (this.player != null && this.player.getCurrentAnimation() != null) {
			this.player.getCurrentAnimation().update(16);
			this.player.render(this.player.getRectangle(), this.target, arg0);
			
//			System.out.println("R: " + this.player.getRectangle().getWidth() + ", "
//					+ this.player.getRectangle().getHeight());
		}
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
