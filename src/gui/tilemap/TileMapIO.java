package gui.tilemap;

import graphics.bitmap.BitmapTool;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TileMapIO {

	public static int[][] getTileMatrixFromImg(String imageURL) throws IOException {
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
