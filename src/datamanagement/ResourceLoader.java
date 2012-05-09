package datamanagement;

import graphics.tools.PixelCastingTool;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
	
	/**
	 * Parses and returns a JSONObject from a file on the given path.
	 * 
	 * @param pathToJsonFile The path to the JSON file
	 * @return A JSONObject on successful parse, otherwise null
	 */
	public static JSONObject parseJSONFromPath(String pathToJsonFile) {
		
		try {
			String json = loadJSONStringFromResources(pathToJsonFile);
			return new JSONObject(json);
		}
		catch(JSONException e) {
			err("Couldn't parse JSON from file! "+e);
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Reads a stream.
	 * 
	 * @param s An InputStream with data
	 * @return A String from the stream
	 * @throws FileNotFoundException If stream is null
	 */
	public static String loadJSONStringFromStream(InputStream s) throws FileNotFoundException {
		if(s == null) {
			throw new FileNotFoundException("Input stream is null, resource couldn't be found");
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
	 * Read a JSON file from an absolute path
	 * 
	 * @param absolutePath The path
	 * @return A String with the JSON contents, or null if the file doesn't exists
	 */
	public static String loadJSONStringFromAbsolutePath(String absolutePath) {
		
		try {
			return loadJSONStringFromStream(new FileInputStream(new File(absolutePath)));
			
		} catch (FileNotFoundException e) {
			err(e.getMessage());
		}
		
		return null;
	}
	
	
	/**
	 * Read a JSON file from a resource
	 * 
	 * @param resource The resource (path to the file)
	 * @return A String with the JSON contents, or null if the resource doesn't exists
	 */
	public static String loadJSONStringFromResources(String resource) {
		try {
			return loadJSONStringFromStream(ClassLoader.getSystemResourceAsStream(resource));
			
		} catch (FileNotFoundException e) {
			err(e.getMessage());
		}
		
		return null;
	}
	
	
	public static TileMap loadTileMapFromAbsolutePath(String absolutePath)
	{
		try {
			BufferedImage img = ImageIO.read(new FileInputStream(new File(absolutePath)));
			
			int[] pixels = PixelCastingTool.getARGBarrayFromDataBuffer(img.getRaster(), img.getWidth(), img.getHeight());
			
			TileMap tileMap = new TileMap(img.getWidth(), img.getHeight(), null, pixels);
			
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
	
	
	public static TileMap loadTileMapFromResources(String resource) {
		try {			
			BufferedImage img = ImageIO.read(ClassLoader.getSystemResource("spritesheets/levels/"+resource));
			int[] pixels = PixelCastingTool.getARGBarrayFromDataBuffer(img.getRaster(), img.getWidth(), img.getHeight());
			TileMap tileMap = new TileMap(img.getWidth(), img.getHeight(), null, pixels);
			
			log("Initialized tilemap from spritesheets/levels/"+resource);
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
			return new TileSheet(new JSONObject(load));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static TileSheet loadTileSheetFromResource(String resource)
	{
		try {
			InputStream input = ClassLoader.getSystemResourceAsStream("spritesheets/json/"+resource);
			String load = IOUtils.toString(input);
			
			log("Initialized tilesheet from spritesheets/json/"+resource);
			return new TileSheet(new JSONObject(load));
		} catch (IOException e) {
			err(e.getMessage());
			e.printStackTrace();
		} catch (JSONException e) {
			err(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
}
