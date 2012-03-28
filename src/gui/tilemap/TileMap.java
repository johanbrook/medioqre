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
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				bitmap.blit(tiles[x][y], x*tiles[x][y].getWidth(), y*tiles[x][y].getHeight());
			}
		}
	}
	
}
