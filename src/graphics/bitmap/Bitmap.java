package graphics.bitmap;

import java.awt.Color;
import java.util.Arrays;

import javax.naming.OperationNotSupportedException;

import tools.Logger;

/**
 * A class representing a bitmap.
 * 
 * @author Barber
 */
public class Bitmap {

	public enum ColorProfile {
		PROFILE_32BIT_ARGB,
	};

	public int width, height;
	public int[] pixels;

	/**
	 * Creates a bitmap with the given width and height. Pixels are ready to be
	 * filled.
	 * 
	 * @param width
	 *            The desired width of the bitmap.
	 * @param height
	 *            The desired height of the bitmap.
	 */
	public Bitmap(int width, int height) {
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
	}

	/**
	 * Creates a bitmap with the given width, height and pixels.
	 * 
	 * @param width
	 *            The desired width of the bitmap.
	 * @param height
	 *            The desired height of the bitmap.
	 * @param pixels
	 *            The pixels.
	 */
	public Bitmap(int width, int height, int[] pixels) {
		this.width = width;
		this.height = height;
		this.pixels = pixels;
	}

	/**
	 * Fill the bitmap with the given color.
	 * 
	 * @param color
	 *            The color to fill the bitmap with.
	 */
	public void clear(Color color) {
		Arrays.fill(this.pixels, color.getRGB());
	}

	/**
	 * Fill the bitmap with the color represented by the given integer.
	 * 
	 * @param color
	 *            The integer representation of the color to use. Eg 0xff00ff00
	 *            would fill it with green.
	 */
	public void clear(int color) {
		Arrays.fill(this.pixels, color);
	}

	/**
	 * Set the pixels of the bitmap.
	 * 
	 * @pre The length has to match the dimension of the bitmap.
	 * @param pixels
	 *            The pixels.
	 * @throws IllegalArgumentException
	 *             If the pixels length does not match the dimension of the
	 *             bitmap.
	 */
	public void setPixels(int[] pixels) {
		if (pixels.length == this.width * this.height) {
			this.pixels = pixels.clone();
		} else {
			Logger.log("Pixels: " + pixels.length + " WxH: "
					+ this.width * this.height);
			throw new IllegalArgumentException(
					"Pixel length does not match bitmap dimension.");
		}
	}

	/**
	 * Sets the pixels from a two dimensional pixel array.
	 * 
	 * @param pixels
	 *            The two dimensional array of pixels to assign the bitmap.
	 * @throws IllegalArgumentException
	 *             If the dimension of the pixels do not match the dimension of
	 *             the bitmap.
	 */
	public void setPixels2D(int[][] pixels) {
		if (pixels == null)
			throw new IllegalArgumentException(
					"Trying to set pixels with a null array.");

		if (pixels.length == this.width && pixels[0].length == this.height
				|| pixels.length == this.height
				&& pixels[0].length == this.width) {
			this.pixels = new int[this.width * this.height];
			for (int h = 0; h < pixels.length; h++) {
				for (int w = 0; w < pixels[h].length; w++) {
					this.pixels[h * this.width + w] = pixels[h][w];
				}
			}
		} else {
			throw new IllegalArgumentException(
					"Pixel length does not match bitmap dimension.");
		}

	}

	/**
	 * Get a the pixels in the bitmap.
	 * 
	 * @return The pixels.
	 */
	public int[] getPixels() {
		return this.pixels;
	}

	/**
	 * Returns the pixel at position determined by the given x and y.
	 * 
	 * @param x
	 *            The x position of the pixel.
	 * @param y
	 *            The y position of the pixel.
	 * @return The pixel at the position determined by the x and y.
	 */
	public int getPixel(int x, int y) {
		return pixels[y * width + x];
	}

	/**
	 * Get the width of the bitmap.
	 * 
	 * @return The width of the bitmap.
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Get the height of the bitmap.
	 * 
	 * @return The height of the bitmap.
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Blit the bitmap with the given bitmap at the given x and y position.
	 * 
	 * @param bitmap
	 *            The bitmap to blit onto this bitmap.
	 * @param x
	 *            The x coordinate to position the bitmap.
	 * @param y
	 *            The y coordinate to position the bitmap.
	 * @throws IllegalArgumentException
	 *             If the bitmap is null.
	 */
	public void blit(Bitmap bitmap, int x, int y) {
		// TODO Add alpha channel
		if (bitmap == null)
			throw new IllegalArgumentException("Trying to blit with null!\n");

		int[] bitmapPixels = bitmap.getPixels();
		int currentPixel;
		for (int h = 0; h < bitmap.height; h++) {
			if (h + y >= height)
				return;
			else if (h + y >= 0) {
				for (int w = 0; w < bitmap.width; w++) {
					if (w + x >= width)
						break;
					else if (w + x >= 0) {
						currentPixel = bitmapPixels[h * bitmap.width + w];

						int alpha = (currentPixel >> 24) & 0xff;
		
						if (alpha == 0xff) {
							pixels[(h + y) * width + (w + x)] = currentPixel;
						} 
						else if (alpha != 0x00) {
//							pixels[(h + y) * width + (w + x)] = mergePixels(
//									pixels[(h + y) * width + (w + x)],
//									currentPixel);
						}
					}
				}
			}
		}
	}

	public Bitmap getSubImage(int x, int y, int width, int height)
	{
		Bitmap bitmap = new Bitmap(width, height);
		
		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				bitmap.pixels[h * bitmap.width + w] = this.pixels[(h + y) * this.width + (w + x)];
			}
		}
		
		return bitmap;
	}
	
	public int mergePixels(int backgroundPixel, int foregroundPixel) {
		int a1 = 0xff000000 & backgroundPixel;
		int r1 = 0x00ff0000 & backgroundPixel;
		int g1 = 0x0000ff00 & backgroundPixel;
		int b1 = 0x000000ff & backgroundPixel;

		int a2 = 0xff000000 & foregroundPixel;
		int r2 = 0x00ff0000 & foregroundPixel;
		int g2 = 0x0000ff00 & foregroundPixel;
		int b2 = 0x000000ff & foregroundPixel;

		int diffBgAlpha = 0xff000000 - a2;
		
		int a = a1;
		int r = (r1 * diffBgAlpha + r2 * a2)/(diffBgAlpha+a2);
		int g = (g1 * diffBgAlpha + g2 * a2)/(diffBgAlpha+a2);
		int b = (b1 * diffBgAlpha + b2 * a2)/(diffBgAlpha+a2);
		
		return (0xff000000 | r | g | b);
	}
}
