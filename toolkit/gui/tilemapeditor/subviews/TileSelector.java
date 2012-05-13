package gui.tilemapeditor.subviews;

import gui.tilemapeditor.util.TileSelectorListener;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.jogamp.gluegen.runtime.opengl.GLProcAddressResolver;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.packrect.Rect;

import core.Rectangle;

import tilemap.Tile;
import tilemap.TileSheet;

public class TileSelector extends GLCanvas
		implements
			GLEventListener,
			TileSelectorListener {

	private TileSheet tileSheet;
	private Rectangle object = new Rectangle(0, 0, 0, 0);
	private Rectangle target = new Rectangle(0, 0, 0, 0);
	private int tileWidth;
	private int tileHeight;
	private final int margin = 2;
	private Tile activeTile;
	private ArrayList<Tile> tiles;
	private int colSize;

	private TileSelectorListener listener;

	public TileSelector(GLCapabilities glC) {
		super(glC);
		this.addGLEventListener(this);

		FPSAnimator animator = new FPSAnimator(this, 30);
		animator.start();

		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (tileWidth == 0 || tileHeight == 0)
					return;

				int x = arg0.getPoint().x / (tileHeight + margin);
				int y = arg0.getPoint().y / (tileWidth + margin);
				if (tiles.size() > y * colSize + x) {
					activeTile = tiles.get(y * colSize + x);
					listener.didSelectTile(activeTile);
				}
			}
		});
		this.addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent arg0) {
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				Component cmp = (Component) arg0.getSource();
				setPreferredSize(new Dimension(cmp.getWidth(), 100));
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
			}

			@Override
			public void componentHidden(ComponentEvent arg0) {
			}
		});
	}

	public void setTileSheet(TileSheet tileSheet) {
		this.tileSheet = tileSheet;

		if (this.tileSheet == null)
			return;

		this.tileWidth = 32;
		this.tileHeight = 32;
		colSize = this.getWidth() / this.tileWidth;

		this.tiles = new ArrayList<Tile>(tileSheet.getTiles().size());
		for (Tile t : this.tileSheet.getTiles()) {
			this.tiles.add(t);
		}
		Collections.sort(this.tiles);

		this.updateGui();
	}

	public void addTileSelectorListener(TileSelectorListener listener) {
		this.listener = listener;
	}

	public Tile getSelectedTile() {
		return this.activeTile;
	}

	private void updateGui() {
	}

	@Override
	public void display(GLAutoDrawable arg0) {
		GL2 gl = arg0.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		this.target.setX(0);
		this.target.setY(0);
		this.target.setWidth(this.getWidth());
		this.target.setHeight(this.getHeight());

		if (this.tiles == null)
			return;
		this.colSize = this.getWidth() / this.tileWidth;

		for (int y = 0; y <= this.tiles.size() / this.colSize; y++) {
			for (int x = 0; x < this.colSize; x++) {
				if (y * this.colSize + x < this.tiles.size()) {
					this.object.setX(x * (this.tileWidth + this.margin));
					this.object.setY(y * (this.tileHeight + this.margin));
					this.object.setWidth(this.tileWidth);
					this.object.setHeight(this.tileWidth);

					this.tiles.get(y * this.colSize + x).render(object, target,
							this, 0);
				}
			}
		}

	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

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
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
		// TODO Auto-generated method stub

	}

	@Override
	public void didSelectTile(Tile selectedTile) {
		this.activeTile = selectedTile;
	}
}
