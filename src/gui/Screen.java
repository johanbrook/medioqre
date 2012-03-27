package gui;

import graphics.bitmap.Bitmap;

public class Screen extends Bitmap{

	public Screen(int width, int height, int[] pixels) {
		super(width, height, pixels);
		this.clear();
	}
	public void clear()
	{
		this.clear(0xffffffff);
	}
}
