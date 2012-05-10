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

public class TileCanvas extends GLCanvas
		implements
			GLEventListener,
			TileSelectorListener,
			MouseMotionListener,
			MouseListener {

	private TileMap currentTileMap;
	private Tile activeTile;
	private Tile stashTile;
	private Rectangle stashTilePosition = new Rectangle(0, 0, 0, 0);

	private Rectangle object = new Rectangle(0, 0, 1, 1);
	private Rectangle target = new Rectangle(0, 0, 0, 0);

	public TileCanvas() {

	}

	public TileCanvas(GLCapabilities glC) {
		this.addGLEventListener(this);

		FPSAnimator animator = new FPSAnimator(this, 60);
		animator.start();

		this.addMouseListener(this);
		this.addMouseMotionListener(this);

	}

	public void setTileMap(TileMap tileMap) {
		this.currentTileMap = tileMap;

		this.currentTileMap.setTileSize(new Size(1, 1));
		this.currentTileMap.setViewPortSize(new Size(this.currentTileMap
				.getTileMapSize().getWidth(), this.currentTileMap
				.getTileMapSize().getHeight()));

		this.target = new Rectangle(0, 0, this.currentTileMap.getTileMapSize()
				.getWidth(), this.currentTileMap.getTileMapSize().getHeight());
	}

	@Override
	public void display(GLAutoDrawable arg0) {
		GL2 gl = arg0.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

		if (this.currentTileMap != null) {
			this.object.setX(0);
			this.object.setY(0);
			this.object.setWidth(this.currentTileMap.getTileSize().getWidth()
					* this.currentTileMap.getTileMapSize().getWidth());
			this.object.setHeight(this.currentTileMap.getTileSize().getHeight()
					* this.currentTileMap.getTileMapSize().getHeight());

			this.target.setX(0);
			this.target.setY(0);
			this.target.setWidth(this.currentTileMap.getTileSize().getWidth()
					* this.currentTileMap.getTileMapSize().getWidth());
			this.target.setHeight(this.currentTileMap.getTileSize().getHeight()
					* this.currentTileMap.getTileMapSize().getHeight());

			// gl.glBegin(GL2.GL_QUADS);
			// gl.glColor3f(1.0f, 0.0f, 0.0f);
			//
			// gl.glVertex2f(-0.5f, -0.5f);
			// gl.glVertex2f(-0.5f, 0.5f);
			// gl.glVertex2f(0.5f, 0.5f);
			// gl.glVertex2f(0.5f, -0.5f);
			//
			// gl.glEnd();
			//
			this.currentTileMap.render(this.object, this.target, arg0, 0);
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
	public void init(GLAutoDrawable arg0) {
		GL2 gl = arg0.getGL().getGL2();
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE,
				GL2.GL_MODULATE);
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
	}
	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
	}

	@Override
	public void didSelectTile(Tile selectedTile) {
		this.activeTile = selectedTile;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		if (currentTileMap == null) {
			System.out.println("1");
			return;
		}
		if (activeTile == null) {
			System.out.println("2");
			return;
		}

		int x = (int) (((float) arg0.getPoint().x / (float) getWidth()) * (float) currentTileMap
				.getTileMapSize().getWidth());
		int y = (int) (((float) arg0.getPoint().y / (float) getHeight()) * (float) currentTileMap
				.getTileMapSize().getHeight());

		if (x == this.stashTilePosition.getX()
				&& y == this.stashTilePosition.getY())
			return;

		Tile tmpTile = this.stashTile;
		this.stashTile = currentTileMap.getTileSheet().getTile(
				currentTileMap.getTileTypeFor(x, y));

		this.currentTileMap.setTileTypeFor(x, y, activeTile.getType());
		if (tmpTile != null) {
			this.currentTileMap.setTileTypeFor(this.stashTilePosition.getX(),
					this.stashTilePosition.getY(), tmpTile.getType());
		}
		this.stashTilePosition.setX(x);
		this.stashTilePosition.setY(y);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (currentTileMap == null) {
			System.out.println("1");
			return;
		}
		if (activeTile == null) {
			System.out.println("2");
			return;
		}

		int x = (int) (((float) e.getPoint().x / (float) getWidth()) * (float) currentTileMap
				.getTileMapSize().getWidth());
		int y = (int) (((float) e.getPoint().y / (float) getHeight()) * (float) currentTileMap
				.getTileMapSize().getHeight());

		this.currentTileMap.setTileTypeFor(x, y, activeTile.getType());
		this.stashTile = null;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (currentTileMap == null) {
			System.out.println("1");
			return;
		}
		if (activeTile == null) {
			System.out.println("2");
			return;
		}

		int x = (int) (((float) e.getPoint().x / (float) getWidth()) * (float) currentTileMap
				.getTileMapSize().getWidth());
		int y = (int) (((float) e.getPoint().y / (float) getHeight()) * (float) currentTileMap
				.getTileMapSize().getHeight());

		if (x == this.stashTilePosition.getX()
				&& y == this.stashTilePosition.getY())
			return;

		Tile tmpTile = this.stashTile;
		this.stashTile = null;

		if (tmpTile != null) {
			this.currentTileMap.setTileTypeFor(this.stashTilePosition.getX(),
					this.stashTilePosition.getY(), tmpTile.getType());
		}
		this.stashTilePosition.setX(0);
		this.stashTilePosition.setY(0);
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}
