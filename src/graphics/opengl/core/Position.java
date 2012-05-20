package graphics.opengl.core;

public class Position {

	public float x,y;
	
	public Position() {
		this(0,0);
	}
	public Position(float x, float y) {
		this.setX(x);
		this.setY(y);
	}
	
	public void setX(float x) {
		this.x = x;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getX() {
		return this.x;
	}
	public float getY() {
		return this.y;
	}
}
