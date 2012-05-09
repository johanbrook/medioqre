package tilemap;

import graphics.tools.PixelCastingTool;

import java.util.Random;

import javax.media.opengl.GLAutoDrawable;

import core.GLRenderableObject;
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

	public void fillPixelArrayWithTiles(int[] arrayToFill)
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
	public TileSheet getTileSheet()
	{
		return this.tileSheet;
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
		
		this.tileRenderRect = new Rectangle(0, 0, this.tileSize.getWidth(), this.tileSize.getHeight());
	}
	
	public void setTileTypeFor(int xPos, int yPos, int tileType)
	{
		if (this.tiles != null && this.tiles.length > xPos) {
			if (this.tiles[xPos] != null && this.tiles[xPos].length > yPos) {
				this.tiles[xPos][yPos] = tileType;
			}
		}
	}

	public int getTileTypeFor(int xPos, int yPos)
	{
		if (this.tiles != null && this.tiles.length > xPos) {
			if (this.tiles[xPos] != null && this.tiles[xPos].length > yPos) {
				return this.tiles[xPos][yPos];
			}
		}
		throw new ArrayIndexOutOfBoundsException();
	}
	
	public Size getTileSize()
	{
		return this.tileSize;
	}
	
	public boolean[][] getCollidables()
	{
		boolean[][] collidables = new boolean[this.tileMapSize.getWidth()][this.tileMapSize.getHeight()];
		for (int y = 0; y < collidables.length; y++) {
			for (int x = 0; x < collidables[y].length;x++) {
				collidables[x][y] = this.tileSheet.getTile(this.tiles[x][y]).isCollidable();
			}
		}
		return collidables;
	}
	
	@Override
	public void render(Rectangle object, Rectangle target, GLAutoDrawable canvas, int zIndex)
	{		
		if (this.tileRenderRect == null) this.tileRenderRect = new Rectangle(0, 0, 0, 0);

		if (this.tileSheet == null) return;
		
		for (int x = 0; x < this.tiles.length; x++) {
			for (int y = 0; y < this.tiles[x].length; y++) {
				tileRenderRect.setX(x * tileSize.getWidth()+object.getX());
				tileRenderRect.setY(y * tileSize.getHeight()+object.getY());
				
				if (this.tileSheet.getTile(this.tiles[x][y]) == null) {
					System.out.println("Trying to render a tile that is null!: "+this.tiles[x][y]);
					return;
				}

				this.tileSheet.getTile(this.tiles[x][y]).render(tileRenderRect, target, canvas, 0);
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
