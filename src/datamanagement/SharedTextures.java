package datamanagement;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.media.opengl.GLException;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;


public class SharedTextures {

	// Static
	private static final SharedTextures sharedTextures = new SharedTextures();
	
	public static SharedTextures getSharedTextures() 
	{
		return sharedTextures;
	}
	
	// Instance
	private String spriteBaseURL;
	private Map<String, Texture> textures;
	
	private SharedTextures()
	{
		this.textures = new TreeMap<String, Texture>();
		this.spriteBaseURL = (String) "res/spritesheets/";
	}
	
	public Texture getTexture(String textureName) {
		Texture t = this.textures.get(textureName);
		if (t == null) {
			
			try {
				t = TextureIO.newTexture(new File(spriteBaseURL+textureName+".png"), false);
				if (t != null) {
					this.textures.put(textureName, t);
					System.out.println("Loaded texture: "+textureName);
				} else {
					System.out.println("Texture: "+textureName+" could not be loaded!");
				}
			} catch (GLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		return t;
	}
}
