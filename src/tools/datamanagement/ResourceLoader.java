package tools.datamanagement;

import graphics.opengl.tilemap.TileMap;
import graphics.opengl.tilemap.TileSheet;
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


/**
 * A set of helper methods for loading resources and JSON files.
 * 
 * JSON strings and JSON objects are only loaded from file once and then cached
 * to minimize disk operations.
 * 
 * @author John Barbero Unenge and Johan Brook
 * 
 */
public class ResourceLoader {

	private static Map<String, JSONObject> relativeJSONObjects;
	private static Map<String, String> relativeJSONStrings;
	private static Map<String, String> absoluteJSONStrings;

	/**
	 * Loads and parses a JSON-file from the resources folder.
	 * 
	 * The object is cached for future use.
	 * 
	 * @param pathToJsonFile
	 *            The relative file path to the JSON file.
	 * @return A JSON object. Returns null if resource can't be found.
	 */
	public static JSONObject parseJSONFromPath(String pathToJsonFile) {

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

	/**
	 * Loads a JSON file from an inputstream.
	 * 
	 * @param s
	 *            The inputstream.
	 * @return A JSON string. Returns null if resource can't be found.
	 */
	public static String loadJSONStringFromStream(InputStream s) {
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

	/**
	 * Loads a JSON file from an absolute file path.
	 * 
	 * The string is cached for future use.
	 * 
	 * @param absolutePath
	 *            The absolute path to the JSON file.
	 * @return A JSON string. Returns null if resource can't be found.
	 */
	public static String loadJSONStringFromAbsolutePath(String absolutePath) {
		if (absolutePath == null)
			absoluteJSONStrings = new HashMap<String, String>();

		String jsonString = absoluteJSONStrings.get(absolutePath);
		if (jsonString == null) {

			try {
				jsonString = loadJSONStringFromStream(new FileInputStream(
						new File(absolutePath)));
				absoluteJSONStrings.put(absolutePath, jsonString);
			} catch (FileNotFoundException e) {
				err(e.getMessage());
				e.printStackTrace();
			}
		}

		return jsonString;
	}

	/**
	 * Loads a JSON file from the resource folder.
	 * 
	 * The string is cached for future use.
	 * 
	 * @param resource
	 *            The relative path to the JSON file.
	 * @return A JSON string. Returns null if resource can't be found.
	 */
	public static String loadJSONStringFromResources(String resource) {
		if (relativeJSONStrings == null)
			relativeJSONStrings = new HashMap<String, String>();

		String jsonString = relativeJSONStrings.get(resource);
		if (jsonString == null) {
			jsonString = loadJSONStringFromStream(ClassLoader
					.getSystemResourceAsStream(resource));
			relativeJSONStrings.put(resource, jsonString);
		}

		return jsonString;
	}

	/**
	 * Loads a tilemap from an absolute file path. NOTE: No tilesheet is
	 * selected meaning that it will not render until that has been done.
	 * 
	 * The file to load should be a ARGB .png-file.
	 * 
	 * @param absolutePath
	 *            The absolute file path to the map.
	 * @return A tilemap created from the file. Returns null if resource can't
	 *         be found.
	 */
	public static TileMap loadTileMapFromAbsolutePath(String absolutePath) {
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

	/**
	 * Loads a tilemap the resource folder. NOTE: No tilesheet is selected
	 * meaning that it will not render until that has been done.
	 * 
	 * The file to load should be a ARGB .png-file.
	 * 
	 * @param resource
	 *            The name of the resource to load (will be taken from
	 *            spritesheets/levels/)
	 * @return A tilemap created from the file. Returns null if resource can't
	 *         be found.
	 */
	public static TileMap loadTileMapFromResources(String resource) {
		try {
			BufferedImage img = ImageIO.read(ClassLoader
					.getSystemResource("spritesheets/levels/" + resource));

			int[] pixels = PixelCastingTool.getARGBarrayFromDataBuffer(
					img.getRaster(), img.getWidth(), img.getHeight());

			TileMap tileMap = new TileMap(img.getWidth(), img.getHeight(),
					null, pixels);

			log("Loaded tilemap: " + resource);
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

	/**
	 * Loads a tilesheet from an absolute file path.
	 * 
	 * @param absolutePath
	 *            An absolute file path to the tilesheet.
	 * @return A tilesheet. Returns null if resource can't be found.
	 */
	public static TileSheet loadTileSheetFromAbsolutePath(String absolutePath) {
		try {
			InputStream input = new FileInputStream(new File(absolutePath));

			String load = IOUtils.toString(input);

			log("Loaded tile sheet: " + absolutePath);
			return new TileSheet(new JSONObject(load));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		err("Couldn't load tile sheet: " + absolutePath);
		return null;
	}

	/**
	 * Loads a tilesheet from the resources folder.
	 * 
	 * @param resource
	 *            The name of the resource to load (will be taken from
	 *            spritesheets/json/)
	 * @return A tilesheet. Returns null if resource can't be found.
	 */
	public static TileSheet loadTileSheetFromResource(String resource) {
		try {
			InputStream input = ClassLoader
					.getSystemResourceAsStream("spritesheets/json/" + resource);
			String load = IOUtils.toString(input);

			log("Loaded tile sheet: " + resource);
			return new TileSheet(new JSONObject(load));
		} catch (IOException e) {
			err(e.getMessage());
			e.printStackTrace();
		} catch (JSONException e) {
			err(e.getMessage());
			e.printStackTrace();
		}

		err("Couldn't load tile sheet: " + resource);
		return null;
	}

}
