package datamanagement;

import graphics.tools.PixelCastingTool;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import tilemap.TileMap;
import tilemap.TileSheet;

public class ResourceLoader {
	
	
	public static String loadJSONStringFromStream(InputStream s) {
		if(s == null) {
			throw new IllegalArgumentException("Input stream can't be null");
		}
		
		try {
			return IOUtils.toString(s);

		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String loadJSONStringFromAbsolutePath(String absolutePath) {
		
		try {
			return loadJSONStringFromStream(new FileInputStream(new File(absolutePath)));
			
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public static String loadJSONStringFromResources(String resource) {
		
		return loadJSONStringFromStream(ClassLoader.getSystemResourceAsStream(resource));		
	}
	
	
	public static TileMap loadTileMapFromAbsolutePath(String absolutePath)
	{
		try {
			System.out.println(absolutePath);
			BufferedImage img = ImageIO.read(new FileInputStream(new File(absolutePath)));
			
			int[] pixels = PixelCastingTool.getARGBarrayFromDataBuffer(img.getRaster(), img.getWidth(), img.getHeight());
			
			TileMap tileMap = new TileMap(img.getWidth(), img.getHeight(), null, pixels);
			
			return tileMap;
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		System.out.println("Could not load: " + absolutePath);
		
		return null;
	}
	
	
	public static TileMap loadTileMapFromResources(String resource)
	{
		try {			
			BufferedImage img = ImageIO.read(ClassLoader.getSystemResource("spritesheets/levels/"+resource));
			
			int[] pixels = PixelCastingTool.getARGBarrayFromDataBuffer(img.getRaster(), img.getWidth(), img.getHeight());
			
			TileMap tileMap = new TileMap(img.getWidth(), img.getHeight(), null, pixels);
			
			return tileMap;
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		System.out.println("Could not load: " + resource);
		
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
			return new TileSheet(new JSONObject(load));
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch (JSONException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
}
