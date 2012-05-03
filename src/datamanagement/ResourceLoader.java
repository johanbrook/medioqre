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
	
	public static String loadJSONStringFromAbsolutePath(String absolutePath)
	{
		try {
			InputStream input = new FileInputStream(new File(absolutePath));
			String load = IOUtils.toString(input);
			return load;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String loadJSONStringFromResources(String resource)
	{
		try {
			InputStream input = ClassLoader.getSystemResourceAsStream("spritesheets/json/"+resource);
			String load = IOUtils.toString(input);
			return load;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
