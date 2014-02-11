package entities;

public abstract class AbstractEntity2D implements Entity2D {

	protected float x, y;
	
	@Override
	public float getX() {
		return this.x;
	}

	@Override
	public float getY() {
		return this.y;
	}

	@Override
	public void setX(float x) {
		this.x = x;
	}

	@Override
	public void setY(float y) {
		this.y = y;
	}

	@Override
	public void setPos(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public  abstract void draw();

	@Override
	public abstract void setUp();

	@Override
	public abstract void destroy();

}
