package entities;

import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glDeleteLists;

public class WiredCube3D extends Cube3D {

	public WiredCube3D(float x, float y, float z, float size){
		super(x, y, z, size);
		
		cubeDisplayList = glGenLists(1);
		this.genCube();
	}

	public void draw() {
		genCube();
		glCallList(cubeDisplayList);
	}
	
	public void destroy(){
		glDeleteLists(cubeDisplayList, 1);
	}
	
	private void genCube(){
		glNewList(cubeDisplayList, GL_COMPILE);
		glBegin(GL_LINES);
		
		glColor3f(0, 0, 0);

		{//south face
			glVertex3f(x, y, z);
			glVertex3f(x, y-size, z);
			
			glVertex3f(x, y, z);
			glVertex3f(x+size, y, z);
			
			glVertex3f(x, y-size, z);
			glVertex3f(x+size, y-size, z);
			
			glVertex3f(x+size, y-size, z);
			glVertex3f(x+size, y, z);
		}

		{//north face
			glVertex3f(x, y, z+size);
			glVertex3f(x, y-size, z+size);
			
			glVertex3f(x, y, z+size);
			glVertex3f(x+size, y, z+size);
			
			glVertex3f(x, y-size, z+size);
			glVertex3f(x+size, y-size, z+size);
			
			glVertex3f(x+size, y-size, z+size);
			glVertex3f(x+size, y, z+size);
		}

		{//bottom face
			glVertex3f(x, y-size, z);
			glVertex3f(x, y-size, z+size);
			
			glVertex3f(x+size, y-size, z);
			glVertex3f(x+size, y-size, z+size);
			
		}

		{//top face
			glVertex3f(x, y, z);
			glVertex3f(x, y, z+size);
			
			glVertex3f(x+size, y, z);
			glVertex3f(x+size, y, z+size);
		}

		glColor3f(1, 1, 1);

		glEnd();
		glEndList();
	}

}
