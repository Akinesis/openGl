package entities;


public abstract class AbstractMovableEntity2D extends AbstractEntity2D{

	protected float dx, dy;
	
	public void setDx(float dx){
		this.dx = dx;
	}
	
	public void setDy(float dy){
		this.dy = dy;
	}
	
	public abstract void moveX(float delta);
	
	public abstract void moveY(float delta);

}
