package core;

import javax.media.opengl.GLAutoDrawable;

public interface GLRenderableObject {

	/**
	 * Render the GLRenderableObject.
	 * @param object The rectangle representing where the object should be drawn.
	 * @param target The rectangle representing the surface to draw to. 
	 * @param canvas The canvas to draw to.
	 */
	public void render(Rectangle object, Rectangle target, GLAutoDrawable canvas);
	
}
