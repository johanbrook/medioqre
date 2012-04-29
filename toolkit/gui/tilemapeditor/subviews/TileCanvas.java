package gui.tilemapeditor.subviews;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;

import tilemap.TileMap;

import com.jogamp.opengl.util.FPSAnimator;

import core.Rectangle;

public class TileCanvas extends GLCanvas implements GLEventListener {
	
	private TileMap currentTileMap;
	
	Rectangle object = new Rectangle( 0, 0, 100,100);
	Rectangle target = new Rectangle( 0, 0, 100,100);
	
	public TileCanvas()
	{
		
	}
	
	public TileCanvas (GLCapabilities glC)
	{
		this.addGLEventListener(this);
			
		FPSAnimator animator = new FPSAnimator(this, 10);
		animator.start();
	}

	
	public void setTileMap(TileMap tileMap)
	{
		this.currentTileMap = tileMap;
	}
	
	@Override
	public void display(GLAutoDrawable arg0)
	{
		GL2 gl = arg0.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		
		
		if (this.currentTileMap != null) {
			System.out.println(object);
			System.out.println(target);
			this.currentTileMap.render(object, target, arg0);
			
		} else {
			
			gl.glBegin(GL2.GL_QUADS);
			gl.glColor3f(1.0f, 0.0f, 0.0f);
			
			gl.glVertex2f(-0.5f, -0.5f);
			gl.glVertex2f(-0.5f, 0.5f);
			gl.glVertex2f(0.5f, 0.5f);
			gl.glVertex2f(0.5f, -0.5f);
			
			gl.glEnd();
		}
	}

	@Override
	public void init(GLAutoDrawable arg0)
	{
		GL2 gl = arg0.getGL().getGL2();
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	}
	
	@Override
	public void dispose(GLAutoDrawable arg0)
	{}
	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4)
	{}
}
