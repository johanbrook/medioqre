package datamanagement;

import graphics.tools.PixelCastingTool;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static tools.Logger.*;
import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import tilemap.TileMap;
import tilemap.TileSheet;

/**
 * A set of helper methods for loading resources and JSON files
 * 
 * @author Barber and Johan
 *
 */
public class ResourceLoader {

	private static Map<String, JSONObject>	relativeJSONObjects;
	private static Map<String, String>		relativeJSONStrings;
	private static Map<String, String>		absoluteJSONStrings;

	public static JSONObject parseJSONFromPath(String pathToJsonFile)
	{

		if (relativeJSONObjects == null)
			relativeJSONObjects = new HashMap<String, JSONObject>();

		JSONObject jsonObject = relativeJSONObjects.get(pathToJsonFile);
		if (jsonObject == null) {
			try {
				String json = loadJSONStringFromResources(pathToJsonFile);
				jsonObject = new JSONObject(json);

				relativeJSONObjects.put(pathToJsonFile, jsonObject);

				return jsonObject;
			} catch (JSONException e) {
				err("Couldn't parse JSON from file! " + e.getMessage());
				e.printStackTrace();
			}
		}

		return jsonObject;
	}

	public static String loadJSONStringFromStream(InputStream s)
	{
		if (s == null) {
			throw new IllegalArgumentException("Input stream can't be null");
		}

		try {
			return IOUtils.toString(s);

		} catch (IOException e) {
			err(e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	public static String loadJSONStringFromAbsolutePath(String absolutePath)
	{
		if (absolutePath == null)
			absoluteJSONStrings = new HashMap<String, String>();

		String jsonString = absoluteJSONStrings.get(absolutePath);
		if (jsonString == null) {

			try {
				jsonString = loadJSONStringFromStream(new FileInputStream(new File(
						absolutePath)));
				absoluteJSONStrings.put(absolutePath, jsonString);
			} catch (FileNotFoundException e) {
				err(e.getMessage());
				e.printStackTrace();
			}
		}

		return jsonString;
	}

	public static String loadJSONStringFromResources(String resource)
	{
		if (relativeJSONStrings == null) relativeJSONStrings = new HashMap<String, String>();
		
		String jsonString = relativeJSONStrings.get(resource);
		if (jsonString == null) {
			jsonString = loadJSONStringFromStream(ClassLoader
					.getSystemResourceAsStream(resource));
			relativeJSONStrings.put(resource, jsonString);
		}
		
		return jsonString;
	}

	public static TileMap loadTileMapFromAbsolutePath(String absolutePath)
	{
		try {
			BufferedImage img = ImageIO.read(new FileInputStream(new File(
					absolutePath)));

			int[] pixels = PixelCastingTool.getARGBarrayFromDataBuffer(
					img.getRaster(), img.getWidth(), img.getHeight());

			TileMap tileMap = new TileMap(img.getWidth(), img.getHeight(),
					null, pixels);

			log("Loaded tilemap: " + absolutePath);
			
			return tileMap;
		} catch (FileNotFoundException e) {
			err(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			err(e.getMessage());
			e.printStackTrace();
		}
		err("Could not load: " + absolutePath);

		return null;
	}

	public static TileMap loadTileMapFromResources(String resource)
	{
		try {
			BufferedImage img = ImageIO.read(ClassLoader
					.getSystemResource("spritesheets/levels/" + resource));

			int[] pixels = PixelCastingTool.getARGBarrayFromDataBuffer(
					img.getRaster(), img.getWidth(), img.getHeight());

			TileMap tileMap = new TileMap(img.getWidth(), img.getHeight(),
					null, pixels);

			log("Loaded tilemap: "+ resource);
			return tileMap;
			
		} catch (FileNotFoundException e) {
			err(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			err(e.getMessage());
			e.printStackTrace();
		}
		err("Could not load: " + resource);

		return null;
	}

	public static TileSheet loadTileSheetFromAbsolutePath(String absolutePath)
	{
		try {
			InputStream input = new FileInputStream(new File(absolutePath));

			String load = IOUtils.toString(input);
			
			log ("Loaded tile sheet: "+absolutePath);
			return new TileSheet(new JSONObject(load));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		err("Couldn't load tile sheet: "+absolutePath);
		return null;
	}

	public static TileSheet loadTileSheetFromResource(String resource)
	{
		try {
			InputStream input = ClassLoader
					.getSystemResourceAsStream("spritesheets/json/" + resource);
			String load = IOUtils.toString(input);
			
			log("Loaded tile sheet: "+resource);
			return new TileSheet(new JSONObject(load));
		} catch (IOException e) {
			err(e.getMessage());
			e.printStackTrace();
		} catch (JSONException e) {
			err(e.getMessage());
			e.printStackTrace();
		}
		
		err("Couldn't load tile sheet: "+resource);
		return null;
	}

}
