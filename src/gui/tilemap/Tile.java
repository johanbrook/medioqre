package gui.tilemap;

import graphics.bitmap.Bitmap;

/**
 * A class used for the visual representation of a tile.
 * @author Barber
 *
 */
public class Tile {

	private Bitmap bitmap;
	
	/**
	 * Creates a Tile.
	 * @param width The width of the tile.
	 * @param height The height of the tile.
	 * @param pixels The pixels of the tile.
	 */
	public Tile(int width, int height, int[] pixels) {
		this.bitmap = new Bitmap(width, height, pixels);
	}
	/**
	 * Creates a Tile from an existing Bitmap.
	 * @param bitmap The Bitmap to use.
	 */
	public Tile(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
}
