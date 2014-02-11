package openGl;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

public class testou {

	public static void main(String[] args){
		
		float x = 100, y= 100;
		
		
		//bloque de création d'une fenetre
		try {
			Display.setDisplayMode(new DisplayMode(640, 480));
			Display.setTitle("Nom qui roxx !!");
			Display.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
		
		//initialisation de la matrice de projection
		glMatrixMode(GL_PROJECTION);
        glOrtho(0, 640, 480, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
		
		
		//boucle d'update
		while(!Display.isCloseRequested()){
			
			if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
				y-=5;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)&& !deborde(x, y+5)){
				y+=5;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)&& !deborde(x-5, y)){
				x-=5;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)&& !deborde(x+5, y)){
				x+=5;
			}
			
			if(Mouse.isButtonDown(0) && !deborde(Mouse.getX(), Display.getHeight()-Mouse.getY())){
				x=Mouse.getX();
				y= Display.getHeight()-Mouse.getY();
			}
			
			//création du triangle
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			//initilaisation de la création du cavenas
			glBegin(GL_TRIANGLES);
			
			glColor3ub((byte)255, (byte)0, (byte)0);
			glVertex2f(x, y);
			
			glColor3ub((byte)0, (byte)255, (byte)0);
			glVertex2f(x, y+100);
			
			glColor3ub((byte)0, (byte)0, (byte)255);
			glVertex2f(x+100, y);
			
			glEnd();
			//fin de la création
			
			Display.update();
			Display.sync(60);
		}
		
		//destruction de la fenêtre
		Display.destroy();
	}
	
	public static boolean deborde(float x, float y){
		boolean test = x < 0 || x+100 > 640 || y < 0 || y+100 > 480;
		return test;
	}
	
	
}
