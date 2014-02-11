package entities;

public interface Entity2D {
	public float getX();
	public float getY();
	public void setX(float x);
	public void setY(float y);
	public void setPos(float x, float y);
	
	public void draw();
	public void setUp();
	public void destroy();

}
