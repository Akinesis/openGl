package entities;

import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glColor3f;

import parametres.Parametres;

public class Cube3D extends AbstractEntity3D implements Parametres{
	
	protected float size;
	protected int cubeDisplayList;
	
	public Cube3D(float x, float y, float z, float size){
		setPos(-x, y, -z);
		this.size=size;
		
		cubeDisplayList = glGenLists(1);
		genCube();
	}

	@Override
	public void draw() {
		glCallList(cubeDisplayList);
	}

	@Override
	public void setUp() {
	}

	@Override
	public void destroy() {
	}
	
	public boolean equals(Object o){
		if(o instanceof Cube3D){
			Cube3D temp = (Cube3D)o;
			return (temp.x==x && temp.y == y && temp.z == z);
		}
		return false;
	}
	
	private void genCube(){
		glNewList(cubeDisplayList, GL_COMPILE);
		glBegin(GL_TRIANGLES);
		
		
		{//south face
			glColor3f(1, 0, 0);
			glVertex3f(x, y, z);
			glVertex3f(x, y-size, z);
			glVertex3f(x+size, y, z);

			glVertex3f(x+size, y, z);
			glVertex3f(x, y-size, z);
			glVertex3f(x+size, y-size, z);
		}
		
		{//north face
			glVertex3f(x, y, z-size);
			glVertex3f(x+size, y, z-size);
			glVertex3f(x, y-size, z-size);
			
			glVertex3f(x, y-size, z-size);
			glVertex3f(x+size, y, z-size);
			glVertex3f(x+size, y-size, z-size);
		}
		
		{//bottom face
			glVertex3f(x, y-size, z);
			glVertex3f(x, y-size, z-size);
			glVertex3f(x+size, y-size, z);

			glVertex3f(x+size, y-size, z);
			glVertex3f(x, y-size, z-size);
			glVertex3f(x+size, y-size, z-size);
		}
		
		{//top face;
			glVertex3f(x, y, z);
			glVertex3f(x+size, y, z);
			glVertex3f(x, y, z-size);
			
			glVertex3f(x, y, z-size);
			glVertex3f(x+size, y, z);
			glVertex3f(x+size, y, z-size);		
		}
		
		{//east face
			glVertex3f(x+size, y, z);
			glVertex3f(x+size, y-size, z);
			glVertex3f(x+size, y, z-size);

			glVertex3f(x+size, y, z-size);
			glVertex3f(x+size, y-size, z);
			glVertex3f(x+size, y-size, z-size);
		}
		
		{//west face
			glVertex3f(x, y, z);
			glVertex3f(x, y, z-size);
			glVertex3f(x, y-size, z);

			glVertex3f(x, y-size, z);
			glVertex3f(x, y, z-size);
			glVertex3f(x, y-size, z-size);
		}
		
		glColor3f(1, 1, 1);
		
		glEnd();
		glEndList();


	}

}
