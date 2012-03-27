package graphics.bitmap;

import static org.junit.Assert.*;

import graphics.bitmap.Bitmap;

import java.awt.Color;
import java.util.Random;

import javax.naming.OperationNotSupportedException;

import org.junit.Test;


public class TestBitmap {

	public void test() {

	}

	private static int[] generateDummyPixels(int width, int height,
			Bitmap.ColorProfile profile) {
		int a, r, g, b;
		int[] pixels = new int[width * height];
		Random rand = new Random();

		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				a = profile == Bitmap.ColorProfile.PROFILE_32BIT_ARGB ? rand
						.nextInt(0xff) : 0x00;
				r = rand.nextInt(0xff);
				g = rand.nextInt(0xff);
				b = rand.nextInt(0xff);

				pixels[h * width + w] = (a | r | g | b);
			}
		}
		return pixels;
	}

	private static int[][] generateDummyPixels2D() {
		try {
			throw new OperationNotSupportedException("Method not implemented!");
		} catch (OperationNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Test
	public void testWidthHeightConstructor() {
		// Constructor (width, height)
		Bitmap bm1 = new Bitmap(100, 200);

		assertNotNull(bm1);
		assertEquals(100, bm1.getWidth());
		assertEquals(200, bm1.getHeight());
		}

	@Test
	public void testWidthHeightPixelConstructor() {
		// Constructor (width, height, pixels)
		Bitmap bm2 = new Bitmap(100, 200, generateDummyPixels(100, 200, Bitmap.ColorProfile.PROFILE_32BIT_ARGB));
		
		assertNotNull(bm2);
		assertEquals(100, bm2.getWidth());
		assertEquals(200, bm2.getHeight());
		assertNotNull(bm2.getPixels());
		assertEquals(bm2.getWidth() * bm2.getHeight(), bm2.getPixels().length);
	}

	@Test
	public void testClearMethods() {
		// Clear
		Bitmap bm2 = new Bitmap(100, 200, generateDummyPixels(100, 200, Bitmap.ColorProfile.PROFILE_32BIT_ARGB));
		
		int[] pixels1 = bm2.getPixels();
		assertNotNull(pixels1);
		assertTrue((pixels1[4] <= 0xff && pixels1[4] >= 0x00));

		// Clear with Color
		bm2.clear(Color.WHITE);
		boolean s1 = true;
		int[] pixels2 = bm2.getPixels();
		for (int i = 0; i < pixels2.length; i++) {
			if (pixels2[i] != 0xffffffff) s1 = false;
		}
		assertTrue(s1);

		// Clear with integer
		bm2.clear(0xaabbccdd);
		boolean s2 = true;
		int[] pixels3 = bm2.getPixels();
		for (int i = 0; i < pixels3.length; i++) {
			if (pixels3[i] != 0xaabbccdd) s2 = false;
		}
		assertTrue(s2);
	}

	@Test
	public void testSetPixels() {
		// Set pixels
		Bitmap bm1 = new Bitmap(3, 3, generateDummyPixels(3, 3,
				Bitmap.ColorProfile.PROFILE_32BIT_ARGB));

		// Set pixels with one dimensional array
		// First case
		int[] p1 = { 0xaabbccdd, 0x11223344, 0x55667788, 0x88776655,
				0x44332211, 0xddccbbaa, 0x99553311, 0xbbcc8833, 0x33bbcc99,
				0xaa992200 };
		boolean s1 = false;
		try {
			bm1.setPixels(p1);
		} catch (IllegalArgumentException iae) {
			s1 = true;
		}
		assertTrue(s1);

		// Second case
		int[] p2 = { 
				0xaabbccdd, 0x11223344, 0x55667788, 
				0x88776655, 0x44332211, 0xddccbbaa, 
				0x99553311, 0xbbcc8833, 0x33bbcc99 
				};
		bm1.setPixels(p2);
		boolean s2 = true;
		for (int i = 0; i < bm1.getPixels().length; i++) {
			if (bm1.getPixels()[i] != p2[i])
				s2 = false;
		}
		assertTrue(s2);
	}
	@Test
	public void testSetPixels2D() {
		// Set pixels
		Bitmap bm1 = new Bitmap(3, 3, generateDummyPixels(3, 3,
				Bitmap.ColorProfile.PROFILE_32BIT_ARGB));

		// Set up comparison pixels
		int[] p2 = { 
				0xaabbccdd, 0x11223344, 0x55667788, 
				0x88776655, 0x44332211, 0xddccbbaa, 
				0x99553311, 0xbbcc8833, 0x33bbcc99 
				};
		
		// Set pixels with twodimensional array
		int[][] p3 = { 
				{ 0xaabbccdd, 0x11223344, 0x55667788 },
				{ 0x88776655, 0x44332211, 0xddccbbaa },
				{ 0x99553311, 0xbbcc8833, 0x33bbcc99 } 
				};

		bm1.setPixels2D(p3);
		boolean s3 = true;
		for (int i = 0; i < bm1.getPixels().length; i++) {
			if (bm1.getPixels()[i] != p2[i])
				s3 = false;
		}
		
		assertEquals(3, bm1.getWidth());
		assertEquals(3, bm1.getHeight());
		assertEquals(9, bm1.getPixels().length);
		assertEquals(bm1.getWidth() * bm1.getHeight(), bm1.getPixels().length);
		assertTrue(s3);	
	}
	@Test
	public void testGetPixel()
	{
		int[] p = {
				0xffffffff, 0xffffffff, 0xffffffff, 0xffffffff, 0xffffffff, 
				0xffffffff, 0x00442266, 0xffffffff, 0xffffffff, 0xffffffff, 
				0xffffffff, 0xffffffff, 0xffffffff, 0xffffffff, 0xffffffff, 
				0xffffffff, 0xffffffff, 0xffffffff, 0xaabbccdd, 0xffffffff, 
				0xffffffff, 0xaabb9966, 0xffffffff, 0xffffffff, 0xffffffff, 
		};
		Bitmap b = new Bitmap(5,5,p);
		
		assertEquals(0x00442266, b.getPixel(1, 1));
		assertEquals(0xaabbccdd, b.getPixel(3, 3));
		assertEquals(0xaabb9966, b.getPixel(1, 4));
	}
	@Test
	public void testBlit()
	{
		int[] backgroundPixels = {
				0xffaaaaaa, 0xffaaaaaa, 0xffaaaaaa, 0xffaaaaaa, 0xffaaaaaa, 
				0xffaaaaaa, 0xffaaaaaa, 0xffaaaaaa, 0xffaaaaaa, 0xffaaaaaa, 
				0xffaaaaaa, 0xffaaaaaa, 0xffaaaaaa, 0xffaaaaaa, 0xffaaaaaa, 
				0xffaaaaaa, 0xffaaaaaa, 0xffaaaaaa, 0xffaaaaaa, 0xffaaaaaa, 
				0xffaaaaaa, 0xffaaaaaa, 0xffaaaaaa, 0xffaaaaaa, 0xffaaaaaa, 
		};
		
		Bitmap background = new Bitmap(5, 5, backgroundPixels.clone());
		
		// Blit with null
		Bitmap foreground = null;
		boolean s1 = false;
		try {
			background.blit(foreground, 1, 1);
			s1 = false;
		} catch (IllegalArgumentException iae) {
			s1 = true;
		}
		assertTrue(s1);
		
		// First blit
		
		int[] foregroundPixels1 = {
				0xff996633, 0xff996633, 0xff996633,
				0xff996633, 0xff996633, 0xff996633,
				0xff996633, 0xff996633, 0xff996633
		};
		
		Bitmap foreground1 = new Bitmap(3, 3, foregroundPixels1);
		background.blit(foreground1, 1, 1);
		
		assertEquals(0xffaaaaaa, background.getPixel(0, 0));
		assertEquals(0xff996633, background.getPixel(1, 1));
		assertEquals(0xff996633, background.getPixel(3, 3));
		assertEquals(0xffaaaaaa, background.getPixel(4, 4));
		
		// Second blit
		
		int[] foregroundPixels2 = {
				0xffbb4488, 0xffbb4488, 
				0xffbb4488, 0xffbb4488, 
		};
		
		Bitmap foreground2 = new Bitmap(2, 2, foregroundPixels2);
		background.blit(foreground2, 2, 2);
		
		assertEquals(0xffaaaaaa, background.getPixel(0, 0));
		assertEquals(0xffbb4488, background.getPixel(2, 2));
		assertEquals(0xffbb4488, background.getPixel(3, 3));
		assertEquals(0xffaaaaaa, background.getPixel(4, 4));
		
		// Third blit
		
		int[] foregroundPixels3 = {
				0xff663300
		};
		
		Bitmap foreground3 = new Bitmap(1, 1, foregroundPixels3);
		background.blit(foreground3, 2, 4);
		
		assertEquals(0xffaaaaaa, background.getPixel(0, 0));
		assertEquals(0xff663300, background.getPixel(2, 4));
		assertEquals(0xffaaaaaa, background.getPixel(4, 4));
		
		// Fourth blit

		int[] foregroundPixels4 = { 
				0xff996633, 0xff996633, 0xff996633,
				0xff996633, 0xff996633, 0xff996633, 
				0xff996633, 0xff996633,	0xff996633 
				};

		background.setPixels(backgroundPixels.clone());
		
		Bitmap foreground4 = new Bitmap(3, 3, foregroundPixels4);
		boolean s2 = false;
		try {
		background.blit(foreground4, 4, 4);
		s2 = true;
		} catch (ArrayIndexOutOfBoundsException aiobe){
			s2 = false;
		}
		assertTrue(s2);
		assertEquals(0xffaaaaaa, background.getPixel(0, 0));
		assertEquals(0xffaaaaaa, background.getPixel(1, 1));
		assertEquals(0xffaaaaaa, background.getPixel(3, 3));
		assertEquals(0xff996633, background.getPixel(4, 4));
		
		// Fifth blit

		int[] foregroundPixels5 = { 
				0xff996633, 0xff996633, 0xff996633,
				0xff996633, 0xff996633, 0xff996633, 
				0xff996633, 0xff996633, 0xff996633 
				};

		background.setPixels(backgroundPixels.clone());

		Bitmap foreground5 = new Bitmap(3, 3, foregroundPixels5);
		boolean s3 = false;
		try {
			background.blit(foreground5, -2, -2);
			s3 = true;
		} catch (ArrayIndexOutOfBoundsException aiobe) {
			s3 = false;
		}
		assertTrue(s3);
		assertEquals(0xff996633, background.getPixel(0, 0));
		assertEquals(0xffaaaaaa, background.getPixel(1, 1));
		assertEquals(0xffaaaaaa, background.getPixel(3, 3));
		assertEquals(0xffaaaaaa, background.getPixel(4, 4));
	}
}
