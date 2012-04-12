package datamanager.resourceloader;

import graphics.bitmap.Bitmap;
import graphics.bitmap.BitmapTool;
import gui.animation.Actor;
import gui.animation.Animation;

import java.awt.Point;
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

public class ResourceManager {

	public static void main(String[] args)
	{
		new ResourceManager();
	}

	public ResourceManager()
	{
		loadActors();
	}

	public static Actor[] loadActors()
	{
		InputStream inputStream = ResourceManager.class
				.getResourceAsStream("/animations/frank_animation_s.json");
		String inputString = null;
		JSONObject jFather = null;
		try {
			inputString = IOUtils.toString(inputStream);

			jFather = new JSONObject(inputString);
			JSONArray jsonArray = jFather.getJSONArray("actors");
			Actor[] actors = new Actor[jsonArray.length()];

			BufferedImage bi = null;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsObj = jsonArray.getJSONObject(i);
				bi = ImageIO.read(new File(jsObj.getString("image-src")));
				Bitmap spriteSheet = new Bitmap(bi.getWidth(), bi.getHeight(),
						BitmapTool.getARGBarrayFromDataBuffer(bi.getData(),
								bi.getWidth(), bi.getHeight()));

				JSONArray jsAnimations = jsObj.getJSONArray("animations");
				Map<String, Animation> animations = new TreeMap<String, Animation>();

				for (int j = 0; j < jsAnimations.length(); j++) {
					String name;
					double duration;

					name = jsAnimations.getJSONObject(j).getString("name");
					duration = jsAnimations.getJSONObject(j).getDouble(
							"duration");

					JSONArray jsFrames = jsAnimations.getJSONObject(j)
							.getJSONArray("frames");

					Bitmap[] frames = new Bitmap[jsFrames.length()];

					for (int k = 0; k < frames.length; k++) {
						int width, height, x, y;
						width = jsFrames.getJSONObject(k).getInt("width");
						height = jsFrames.getJSONObject(k).getInt("height");
						x = jsFrames.getJSONObject(k).getInt("x");
						y = jsFrames.getJSONObject(k).getInt("y");

						frames[k] = spriteSheet
								.getSubImage(x, y, width, height);
					}
					System.out.println("New animation: " + j);
					System.out.println("Animation name: " + name);
					animations.put(name, new Animation(name, duration, frames));
				}
				actors[i] = new Actor(null, animations);
			}
			return actors;
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
