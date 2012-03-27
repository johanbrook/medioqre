package gui.tilemap;

import graphics.bitmap.Bitmap;

public class Tile {

	private Bitmap bitmap;
	
	public Tile(int width, int height, int[] pixels) {
		this.bitmap = new Bitmap(width, height, pixels);
	}
	public Tile(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
}
