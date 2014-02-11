package entities;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;
import parametres.Parametres;

public class Cube2D extends AbstractEntity2D implements Parametres {

	protected float size;
	
	public Cube2D(float x, float y, float size){
		this.x = x;
		this.y = y;
		this.size = size;
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

	public boolean equals(Object o){
		if(o instanceof Cube2D){
			Cube2D temp = (Cube2D)o;
			if(temp.x == this.x && temp.y == this.y){
				return true;
			}
		}
		return false;
	}
	
	public int hashCode() {
		  return 0;
		}
	
	@Override
	public void setUp() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
