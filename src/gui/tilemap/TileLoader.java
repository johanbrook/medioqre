package gui.tilemap;

import graphics.bitmap.BitmapTool;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * A class used for loading the tiles into the game. It makes sure that they are only loaded 
 * into memory once and then reused.
 * @author Barber
 *
 */
public class TileLoader {

	private static final Map<String, Tile> tiles = new HashMap<String, Tile>();
	
	/**
	 * Gets a tile from the given image URL.
	 * @param tileURL The URL where the tile image is stored.
	 * @return A Tile created from the given URL.
	 * @throws IOException Thrown if the file can't be found.
	 */
	public static Tile loadTile(String tileURL) throws IOException {
		if (!tiles.containsKey(tileURL)) {
			BufferedImage tileImg = ImageIO.read(new File(tileURL));
			tiles.put(tileURL, new Tile(tileImg.getWidth(), tileImg.getHeight(), BitmapTool.getARGBarrayFromDataBuffer(tileImg.getRaster(), tileImg.getWidth(), tileImg.getHeight())));
		}
		return tiles.get(tileURL);
	}
}
