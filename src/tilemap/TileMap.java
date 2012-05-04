package tilemap;

import graphics.tools.PixelCastingTool;

import java.awt.Point;
import java.util.Random;

import javax.media.opengl.GLAutoDrawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import core.GLRenderableObject;
import core.JSONSerializable;
import core.Rectangle;
import core.Size;

public class TileMap implements GLRenderableObject {

	private TileSheet tileSheet;
	private int[][] tiles;
	private Size tileSize = new Size(1, 1);
	private Size tileMapSize;
	private Rectangle tileViewPort;
	
	// Temp
	private Rectangle tileRenderRect;
	
	public TileMap(int rows, int columns, TileSheet tileSheet, int[] pixels)
	{
		this.tiles = pixels == null ? new int[columns][rows] : PixelCastingTool.get2dTileMatrixFromPixelArray(columns, rows, pixels);
		
		this.tileMapSize = new Size(columns, rows);
		
		this.setTileSheet(tileSheet); 
	}

	public void randomizeTileMap()
	{
		int[] diffTiles = new int[this.tileSheet.getTiles().size()];
		int index = 0;
		for (Tile t : this.tileSheet.getTiles()) {
			diffTiles[index] = t.getType();
			index++;
		}
		
		Random r = new Random();
		 
		for (int x = 0; x < this.tiles.length; x++) {
			for (int y = 0; y < this.tiles[x].length; y++) {
				this.tiles[x][y] = diffTiles[r.nextInt(diffTiles.length)];
			}
		}

	}
	public void clearTileMap(int clearTile)
	{	 
		for (int x = 0; x < this.tiles.length; x++) {
			for (int y = 0; y < this.tiles[x].length; y++) {
				this.tiles[x][y] = clearTile;
			}
		}

	}	
	public Size getTileMapSize()
	{
		return this.tileMapSize;
	}

	public void fillPizelArrayWithTiles(int[] arrayToFill)
	{
		int rows = this.tiles == null ? 0 : this.tiles.length;
		int cols = rows > 0 ? this.tiles[0].length : 0;
		
		for (int y = 0; y < cols; y++) {
			for (int x = 0; x < rows; x++) {
				arrayToFill[y * cols + x] = this.tiles[x][y];
			}
		}
	}
	
	public void setTileSheet(TileSheet tileSheet)
	{
		this.tileSheet = tileSheet;
	}
	public void setViewPortSize(Size size)
	{
		if (this.tileViewPort == null) this.tileViewPort = new Rectangle(0, 0, 0, 0);
		
		this.tileViewPort.setWidth(size.getWidth());
		this.tileViewPort.setHeight(size.getHeight());
	}
	public void setTileSize(Size size)
	{
		this.tileSize = size;
	}
	
	
	@Override
	public void render(Rectangle object, Rectangle target, GLAutoDrawable canvas)
	{		
		if (this.tileRenderRect == null) this.tileRenderRect = new Rectangle(0, 0, 0, 0);
		for (int x = 0; x < this.tiles.length; x++) {
			for (int y = 0; y < this.tiles[x].length; y++) {
				tileRenderRect.setX(x * tileSize.getWidth()+object.getX());
				tileRenderRect.setY(y * tileSize.getHeight()+object.getY());
				tileRenderRect.setWidth(tileSize.getWidth());
				tileRenderRect.setHeight(tileSize.getHeight());
				
				this.tileSheet.getTile(this.tiles[x][y]).render(tileRenderRect, target, canvas);
			}
		}
	}

	@Override
	public void update(double dt)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Rectangle getBounds()
	{
		return this.tileViewPort;
	}
}
