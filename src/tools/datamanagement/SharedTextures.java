package tools.datamanagement;

import java.io.File;
import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.TreeMap;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLException;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

/**
 * A helper class to load and cache textures.
 * 
 * The class stores one set of textures per OpenGL context that tries to use
 * them.
 * 
 * @author John Barbero Unenge
 * 
 */
public class SharedTextures {

	// Static
	private static final SharedTextures sharedTextures = new SharedTextures();

	/**
	 * Gets a singleton instance of SharedTexture.
	 * 
	 * @return A SharedTexture
	 */
	public static SharedTextures getSharedTextures() {
		return sharedTextures;
	}

	// Instance
	private String spriteBaseURL;
	private IdentityHashMap<GLAutoDrawable, TreeMap<String, Texture>> textures;

	private SharedTextures() {
		this.textures = new IdentityHashMap<GLAutoDrawable, TreeMap<String, Texture>>();
		this.spriteBaseURL = (String) "res/spritesheets/images/";
	}

	/**
	 * Binds a texture with the provided name to the OpenGL context.
	 * 
	 * If the texture can't be loaded correct behavior isn't guaranteed.
	 * 
	 * @param textureName
	 *            The texture to use
	 * @param arg0
	 *            The OpenGL context to bind it to
	 * @return The texture that was loaded.
	 */
	public Texture bindTexture(String textureName, GLAutoDrawable arg0) {
		if (this.textures == null) {
			this.textures = new IdentityHashMap<GLAutoDrawable, TreeMap<String, Texture>>();
		}

		TreeMap<String, Texture> threadTextures = this.textures.get(arg0);

		if (threadTextures == null) {
			threadTextures = new TreeMap<String, Texture>();
			this.textures.put(arg0, threadTextures);
		}

		Texture t = threadTextures.get(textureName);

		if (t == null) {
			try {
				t = TextureIO.newTexture(new File(spriteBaseURL + textureName
						+ ".png"), false);
				threadTextures.put(textureName, t);
			} catch (GLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		t.bind(arg0.getGL());

		return t;
	}
}
