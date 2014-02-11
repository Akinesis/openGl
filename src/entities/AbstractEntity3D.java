package entities;

public abstract class AbstractEntity3D implements Entity3D {
	float x, y,z;

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public float getZ() {
		return z;
	}

	@Override
	public void setX(float x) {
		this.x=x;

	}

	@Override
	public void setY(float y) {
		this.x=y;

	}

	@Override
	public void setz(float z) {
		this.x=z;

	}

	@Override
	public void setPos(float x, float y, float z) {
		this.x=x;
		this.y=y;
		this.z=z;

	}

	@Override
	public abstract void draw();

	@Override
	public abstract void setUp();

	@Override
	public abstract void destroy() ;

}
