package entities;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glIsEnabled;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class TexturedCube2D extends Cube2D {
	
	private Texture texture;
	float coordX, coordY;
	
	public TexturedCube2D(float x, float y, float size, String tex, float coX, float coY) {
		super(x, y, size);
		texture = textureLoad(tex);
		coordX = (coX>0)?coX/128:0; // 32 => 0,25
		coordY = (coY>0)?coY/128:0; // 64 => 0,50
		if (!glIsEnabled(GL_TEXTURE_2D)){glEnable(GL_TEXTURE_2D);}
	}

	
	public void draw(){
		texture.bind();
		
		glBegin(GL_TRIANGLES);
		glTexCoord2f(coordX, coordY);
		glVertex2f(x, y);
		glTexCoord2f(coordX, coordY+vectCarre);
		glVertex2f(x, y+size);
		glTexCoord2f(coordX+vectCarre, coordY);
		glVertex2f(x+size, y);
		
		glTexCoord2f(coordX, coordY+vectCarre);
		glVertex2f(x, y+size);
		glTexCoord2f(coordX+vectCarre, coordY);
		glVertex2f(x+size, y);
		glTexCoord2f(coordX+vectCarre, coordY+vectCarre);
		glVertex2f(x+size, y+size);
		glEnd();
	}
	
	public Texture getTexture(){
		return texture;
	}
	private Texture textureLoad(String nom){
		try {
			return TextureLoader.getTexture("PNG", new FileInputStream(new File("res/"+nom+".png")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
