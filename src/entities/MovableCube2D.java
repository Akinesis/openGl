package entities;

import static org.lwjgl.opengl.GL11.*;

public class MovableCube2D extends AbstractMovableEntity2D  implements parametres.Parametres{
	
	protected float size;
	
	public MovableCube2D(float x, float y, float size){
		this.x = x;
		this.y = y;
		this.size = size;
		dx = dy = 0;
	}
	
	public MovableCube2D(){
		x = y = size = 1;
		dx = dy = 0;
	}

	@Override
	public void draw() {
		glBegin(GL_TRIANGLES);
		glVertex2f(x, y);
		glVertex2f(x, y+size);
		glVertex2f(x+size, y);
		
		glVertex2f(x, y+size);
		glVertex2f(x+size, y);
		glVertex2f(x+size, y+size);
		glEnd();
	}

	@Override
	public void setUp() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
	
	public void moveX(float delta){	
		this.x += ((dx*delta)+x+size > largeur || (dx*delta)+x < 0)?0:dx*delta;
	}
	
	public void moveY(float delta){
		this.y += ((dy*delta)+y+size > hauteur || (dy*delta)+y < 0)?0:dy*delta;
	}

}
