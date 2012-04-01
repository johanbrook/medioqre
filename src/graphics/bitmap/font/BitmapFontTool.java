package graphics.bitmap.font;

import graphics.bitmap.Bitmap;
import graphics.bitmap.BitmapTool;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BitmapFontTool {
	
	// Static implementation
	private static BitmapFontTool sharedFont;
	
	public static BitmapFontTool getSharedBitmapFontTool()
	{
		if (sharedFont == null) sharedFont = new BitmapFontTool("bmf_fps_meter");
		return sharedFont;
	}
	////////////////////////
	
	
	// Instance implementation
	private String name;
	private String imgURL;
	private Map<String, Bitmap> letters;
	
	private BitmapFontTool(String string) {
		
		letters = new TreeMap<String, Bitmap>();
		
		InputStream inputStream = BitmapFontTool.class.getResourceAsStream("/bitmapfonts/"+string+".json");
		try {
			String jsonString = IOUtils.toString(inputStream);
			JSONObject rootObj = new JSONObject(jsonString);
			
			this.name = rootObj.getString("name");
			this.imgURL = rootObj.getString("image-src");
			
			JSONArray chars = rootObj.getJSONArray("characters");
			BufferedImage bImg = ImageIO.read(new File("res/bitmapfonts/"+string+".png"));
			Bitmap charsBitmap = new Bitmap(bImg.getWidth(), bImg.getHeight(),
					BitmapTool.getARGBarrayFromDataBuffer(bImg.getData(), bImg.getWidth(), bImg.getHeight()));
			
			for (int i = 0; i < chars.length(); i++) {
				JSONObject jsObj = chars.getJSONObject(i);
				String letter = jsObj.getString("letter");
				int x = jsObj.getInt("x");
				int y = jsObj.getInt("y");
				int width = jsObj.getInt("width");
				int height = jsObj.getInt("height");
				
				Bitmap charBitmap = charsBitmap.getSubImage(x, y, width, height);
				
				this.letters.put(letter, charBitmap);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public Bitmap getLetter(String letter)
	{
		//WARNING! Could cause trouble!
		return letters.get(letter);
	}
}
