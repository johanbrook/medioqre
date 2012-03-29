package gui.tilemap;

import graphics.bitmap.Bitmap;

import java.awt.Rectangle;
import java.io.IOException;

import model.Position;

/**
 * A class used for the visual representation of the gameworld.
 * @author Barber
 *
 */
public class TileMap {

	private Tile[][] tiles;
	private Tile outsideTile;
	
	/**
	 * Creates a TileMap from the given file located at the given URL.
	 * 
	 * @param mapURL The URL to the map to use.
	 * @throws IOException Thrown if the map-file can't be found.
	 */
	public TileMap(String mapURL) throws IOException {
		outsideTile = TileLoader.loadTile("rec/images/tiles/ffff0000.png");
		
		int[][] p = TileMapIO.getPixelMatrixFromImg(mapURL);
		this.tiles = new Tile[p.length][p[0].length];
		for (int x = 0; x < p.length; x++) {
			for (int y = 0; y < p[x].length; y++) {
				this.tiles[x][y] = TileLoader.loadTile("rec/images/tiles/"+Integer.toHexString(p[x][y])+".png");
			}
			System.out.print("\n");
		}
	}
	public void blitVisibleTilesToBitmap(Bitmap bitmap, Rectangle screenRect) {
		
		double xOffs = (int) ((double) screenRect.x % (double) tiles[0][0].getWidth());
		double yOffs = (int) ((double) screenRect.y % (double) tiles[0][0].getHeight());
		int xTile = (int) ((double) screenRect.x / (double) tiles[0][0].getWidth());
		int yTile = (int) ((double) screenRect.y / (double) tiles[0][0].getHeight());	
		
		for (int x = -1; x < (screenRect.width / tiles[0][0].getWidth()) + 2; x++) {
			for (int y = -1; y < (screenRect.height / tiles[0][0].getHeight()) + 2; y++) {
				if (x+xTile >= tiles.length || x+xTile < 0 || y+yTile >= tiles[0].length || y+yTile < 0) {
					bitmap.blit(outsideTile, (int)((double)x * (double)outsideTile.getWidth() - xOffs), (int)((double)y * (double)outsideTile.getHeight() 
							- yOffs));
				} else {
					bitmap.blit(tiles[x + xTile][y + yTile], (int)(((double)x * (double)tiles[x + xTile][y + yTile].getWidth()) 
							- xOffs), (int) (((double)y * (double)tiles[x + xTile][y + yTile].getHeight()) - yOffs));
				}
			}
		}
	}
	
}
