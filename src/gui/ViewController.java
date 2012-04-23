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

import core.Rectangle;
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

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		frame.add(canvas);
		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
	
		FPSAnimator anim = new FPSAnimator(canvas, 30);
//		Animator anim = new Animator(canvas);
		this.initScene();
		anim.start();	
	}

	private void initScene()
	{
		try {
			this.player = new Actor(new JSONObject("{\"animations\":[{\"duration\":400,\"frames\":[{\"height\":16,\"texture\":\"pokemon\",\"width\":13,\"y\":8,\"x\":53},{\"height\":17,\"texture\":\"pokemon\",\"width\":13,\"y\":7,\"x\":67}],\"name\":\"moveLeft\"}],\"height\":64,\"width\":64,\"name\":\"Derp\",\"y\":0,\"x\":0}"));
			this.player.setCurrentAnimation("moveLeft");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void render(double dt)
	{	
//		fpsmeter.tick();
//		System.out.println("fps: " + this.fpsmeter.currentFPS);

	}

	@Override
	public void onEvent(Event evt)
	{

		if (evt.getProperty() == Event.Property.INIT_MODEL) {
		}

		if (evt.getValue() instanceof Entity) {
		}
	}

	@Override
	public void display(GLAutoDrawable arg0)
	{
		fpsmeter.tick();
		System.out.println("fps: " + this.fpsmeter.currentFPS);
		
		GL2 gl = arg0.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		
		this.player.getCurrentAnimation().update(16);
		this.player.render(this.player.getRectangle(), this.target, arg0);
		System.out.println("R: "+ this.player.getRectangle().getWidth() + ", "+ this.player.getRectangle().getHeight());
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
	public void dispose(GLAutoDrawable arg0) {}
	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {}
}
