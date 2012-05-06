package gui.tilemapeditor.subviews;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;

import tilemap.TileMap;

import com.jogamp.opengl.util.FPSAnimator;

import core.Rectangle;
import core.Size;

public class TileCanvas extends GLCanvas implements GLEventListener {
	
	private TileMap currentTileMap;
	
	Rectangle object = new Rectangle( 0, 0, 200,120);
	Rectangle target = new Rectangle( 0, 0, 200,120);
	
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
		
		this.currentTileMap.setTileSize(new Size(1,1));
		this.currentTileMap.setViewPortSize(new Size(this.currentTileMap.getTileMapSize().getWidth(), this.currentTileMap.getTileMapSize().getHeight()));
		
		this.target = new Rectangle(0, 0, this.currentTileMap.getTileMapSize().getWidth(),this.currentTileMap.getTileMapSize().getHeight());
	}
	
	@Override
	public void display(GLAutoDrawable arg0)
	{
		GL2 gl = arg0.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		
		
		if (this.currentTileMap != null) {
			this.currentTileMap.render(this.currentTileMap.getBounds(), target, arg0, 0);
			
		} else {
			
			gl.glBegin(GL2.GL_QUADS);
			gl.glColor3f(1.0f, 0.0f, 0.0f);
			gl.glVertex2f(-0.5f, -0.5f);
			
			gl.glColor3f(1.0f, 0.0f, 1.0f);
			gl.glVertex2f(-0.5f, 0.5f);
			
			gl.glColor3f(0.0f, 1.0f, 0.0f);
			gl.glVertex2f(0.5f, 0.5f);
			
			gl.glColor3f(0.0f, 0.0f, 1.0f);
			gl.glVertex2f(0.5f, -0.5f);
			
			gl.glEnd();
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
	{}
	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4)
	{}
}
