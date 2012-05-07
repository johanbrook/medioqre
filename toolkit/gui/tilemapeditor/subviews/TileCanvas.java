package gui.tilemapeditor.subviews;

import gui.tilemapeditor.util.TileSelectorListener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.awt.GLCanvas;

import tilemap.Tile;
import tilemap.TileMap;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import core.Rectangle;
import core.Size;

public class TileCanvas extends GLCanvas implements GLEventListener, TileSelectorListener {
	
	private TileMap currentTileMap;
	private int activeTile;
	private int stashTile;
	
	Rectangle object = new Rectangle(0,0,1,1);
	Rectangle target = new Rectangle(0,0,0,0);
	
	public TileCanvas()
	{
		
	}
	
	public TileCanvas (GLCapabilities glC)
	{
		this.addGLEventListener(this);
			
		FPSAnimator animator = new FPSAnimator(this, 10);
		animator.start();
		
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0)
			{}
			@Override
			public void mousePressed(MouseEvent arg0)
			{}
			@Override
			public void mouseExited(MouseEvent arg0)
			{}
			@Override
			public void mouseEntered(MouseEvent arg0)
			{}
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				System.out.println("Clicked: "+arg0.getPoint());
			}
		});
		this.addMouseMotionListener(new MouseMotionListener() {
		
		@Override
		public void mouseMoved(MouseEvent arg0)
		{
			if (currentTileMap == null) return;
			
			int x = (int)(((float)arg0.getPoint().x / (float)getWidth()) * (float)currentTileMap.getTileMapSize().getWidth());
			int y = (int)(((float)arg0.getPoint().y / (float)getHeight()) * (float)currentTileMap.getTileMapSize().getHeight());
			
			/*
			stashTile = currentTileMap.getTile(x, y);
			currentTileMap.setTile(x, y, activeTile);
			*/
			
			System.out.println("TilemapSize: " + currentTileMap.getTileMapSize().getWidth()+","+currentTileMap.getTileMapSize().getHeight());
			System.out.println("Mouse at: "+x+","+y);
			System.out.println("Mouse at: "+arg0.getPoint());
		}
		
		@Override
		public void mouseDragged(MouseEvent arg0)
		{	
		}
	});
		
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
			this.object.setX(0);
			this.object.setY(0);
			this.object.setWidth(this.currentTileMap.getTileSize().getWidth() * this.currentTileMap.getTileMapSize().getWidth());
			this.object.setHeight(this.currentTileMap.getTileSize().getHeight() * this.currentTileMap.getTileMapSize().getHeight());
			
			this.target.setX(0);
			this.target.setY(0);
			this.target.setWidth(this.currentTileMap.getTileSize().getWidth() * this.currentTileMap.getTileMapSize().getWidth());
			this.target.setHeight(this.currentTileMap.getTileSize().getHeight() * this.currentTileMap.getTileMapSize().getHeight());
			
//			gl.glBegin(GL2.GL_QUADS);
//			gl.glColor3f(1.0f, 0.0f, 0.0f);
//			
//			gl.glVertex2f(-0.5f, -0.5f);
//			gl.glVertex2f(-0.5f, 0.5f);
//			gl.glVertex2f(0.5f, 0.5f);
//			gl.glVertex2f(0.5f, -0.5f);
//			
//			gl.glEnd();
//			
			System.out.println("Thread: " +Thread.currentThread().getId());
			
			this.currentTileMap.render(this.object, this.target, arg0, 0);
			
			System.out.println("TilemapSize: "+this.currentTileMap.getTileMapSize());
		
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

	@Override
	public void didSelectTile(Tile selectedTile)
	{
		this.activeTile = selectedTile.getType();
	}
}
