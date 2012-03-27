package gui.tilemap;

import graphics.bitmap.BitmapTool;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class TileLoader {

	private static final Map<String, Tile> tiles = new HashMap<String, Tile>();
	
	public static Tile loadTile(String tileURL) throws IOException {
		if (!tiles.containsKey(tileURL)) {
			BufferedImage tileImg = ImageIO.read(new File(tileURL));
			tiles.put(tileURL, new Tile(tileImg.getWidth(), tileImg.getHeight(), BitmapTool.getARGBarrayFromDataBuffer(tileImg.getRaster(), tileImg.getWidth(), tileImg.getHeight())));
		}
		return tiles.get(tileURL);
	}
}
