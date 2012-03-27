package graphics.bitmap;

import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;

public class BitmapTool {

	public static int[] getARGBarrayFromDataBuffer(Raster raster, int imageWidth, int imageHeight)
	{
		DataBuffer buffer = raster.getDataBuffer();
		
		if (buffer.getClass() == DataBufferByte.class) {
			System.out.println("Getting pixels from DataBufferByte");
			
			int[] pixels = new int[imageWidth * imageHeight];
			byte[] data = ((DataBufferByte) buffer).getData();
			
			if (data.length == pixels.length * 4) {
				System.out.println("Image has alpha");
				for (int i = 0; i < pixels.length; i++) {
					int a = 0xff000000 & (data[i * 4] << 24);
					int r = 0x00ff0000 & (data[i * 4 + 3] << 16);
					int g = 0x0000ff00 & (data[i * 4 + 2] << 8);
					int b = 0x000000ff & (data[i * 4 + 1]);
					pixels[i] = (a | r | g | b);
				}
			} else if (data.length == pixels.length * 3) {
				System.out.println("Image doesn't have alpha");
				for (int i = 0; i < pixels.length; i++) {
					int a = 0xff000000;
					int r = 0x00ff0000 & (data[i * 3 + 0] << 16);
					int g = 0x0000ff00 & (data[i * 3 + 1] << 8);
					int b = 0x000000ff & (data[i * 3 + 2] << 0);
					
					pixels[i] = (a | r | g | b);
				}
			} else {
				pixels = null;
			}
			return pixels;
		} else if (buffer.getClass() == DataBufferInt.class) {
			System.out.println("Getting pixels from DataBufferInt");
			return ((DataBufferInt) buffer).getData();
		}
		return null;
	} 
	
}
