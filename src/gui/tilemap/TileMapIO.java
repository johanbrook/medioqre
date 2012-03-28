package gui.tilemap;

import graphics.bitmap.BitmapTool;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A class used for loading a Tilemap.
 * @author Barber
 *
 */
public class TileMapIO {

	/**
	 * Gets a 2 dimensional representation of the tilemap. The objects at each slot are int
	 * values representing ARGB. Meaning that you can easily access every individual channel 
	 * of every pixel in the map file like so:<br />
	 * <br />
	 * <code>
	 * int[][] p = getTileMatrixFromImg("example.png");<br />
	 * int a 	= (0xff000000 & p[1][2]) >> 24;<br />
	 * int r 	= (0x00ff0000 & p[1][2]) >> 16;<br />
	 * int g 	= (0x0000ff00 & p[1][2]) >> 8;<br />
	 * int b 	= 0x000000ff & p[1][2]; 
	 * </code>
	 * <br /><br />
	 * This would give:<br />
	 * 0 <= a <= 255<br />
	 * 0 <= r <= 255<br />
	 * 0 <= g <= 255<br />
	 * 0 <= b <= 255<br />
	 * 
	 * @param imageURL The URL to the map to load from.
	 * @return An two dimensional int array containing the ARGB for each pixel.
	 * @throws IOException Thrown if the file can't be found.
	 */
	public static int[][] getPixelMatrixFromImg(String imageURL) throws IOException {
		BufferedImage image = ImageIO.read(new File(imageURL));
		int w = image.getWidth();
		int h = image.getHeight();
		int[][] tileMatrix = new int[w][h];
		int[] pixels = BitmapTool.getARGBarrayFromDataBuffer(image.getRaster(), w, h);
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				tileMatrix[x][y] = pixels[w * y + x];
			}
		}
		return tileMatrix;
	}
}
