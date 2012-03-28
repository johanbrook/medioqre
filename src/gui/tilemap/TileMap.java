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
	
	/**
	 * Creates a TileMap from the given file located at the given URL.
	 * 
	 * @param mapURL The URL to the map to use.
	 * @throws IOException Thrown if the map-file can't be found.
	 */
	public TileMap(String mapURL) throws IOException {
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
		
		int xOffs = (int) ((double) screenRect.x % (double) tiles[0][0].getWidth());
		int yOffs = (int) ((double) screenRect.y % (double) tiles[0][0].getHeight());
		int xTile = (int) ((double) screenRect.x / (double) tiles[0][0].getWidth());
		int yTile = (int) ((double) screenRect.y / (double) tiles[0][0].getHeight());
		
		
		
		
		for (int x = 0; x < screenRect.width / tiles[0][0].getWidth(); x++) {
			for (int y = 0; y < screenRect.height / tiles[0][0].getHeight(); y++) {
				bitmap.blit(tiles[x+xTile][y+yTile], x*tiles[x+xTile][y+yTile].getWidth(), y*tiles[x+xTile][y+yTile].getHeight());
			}
		}
	}
	
}
