package gui;

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
		try {
			this.spriteBaseURL = (String) "res/spritesheets/";
			 
			
			this.textures.put("player", TextureIO.newTexture(new File(spriteBaseURL+"animated.png"), false));
			
			this.textures.put("tiles", TextureIO.newTexture(new File(spriteBaseURL+"tiles_sheet.png"), false));
//			this.textures.put("player", TextureIO.newTexture(new File(spriteBaseURL+"tiles_sheet.png"), false));
		} catch (Exception e) {
			System.out.println("Fatal error! Failed to load sprite textures!!\n\n");
			e.printStackTrace();
		}
	}
	public Texture getPlayerTexture(){
		return this.textures.get("player");
	}
	public Texture getTilesTexture(){
		return this.textures.get("tiles");
	}
	public Texture getWalker1Texture(){
		return this.textures.get("player");
	}
	public Texture getTexture(String textureName) {
		Texture t = this.textures.get(textureName);
		if (t == null) {
			
			try {
				t = TextureIO.newTexture(new File(spriteBaseURL+textureName+".png"), false);
				if (t != null) {
					this.textures.put(textureName, t);
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
