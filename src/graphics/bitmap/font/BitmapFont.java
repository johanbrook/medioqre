package graphics.bitmap.font;

import graphics.bitmap.Bitmap;

public class BitmapFont {

	private Bitmap bitmap;
	private String text;
	
	public BitmapFont(String text)
	{
		this.setText(text);
	}
	public Bitmap getBitmap()
	{
		return this.bitmap;
	}
	public void setText(String text)
	{
		if (this.text == null || this.text != text) {
			this.text = text;
			int width = text.length()*16;
			int height = 16;
			
			this.bitmap = new Bitmap(width, height);
			
			for (int i = 0; i < text.length(); i++) {
				this.bitmap.blit(BitmapFontTool.getSharedBitmapFontTool().getLetter(""+text.charAt(i)), i * 16, 0);
			}
		}
	}
}
