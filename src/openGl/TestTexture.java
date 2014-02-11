package openGl;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import entities.MovableCube2D;
import entities.MovableTexturedCube2D;
import static org.lwjgl.opengl.GL11.*;

public class TestTexture implements parametres.Parametres{
	
	private static long lastFrame;
	
	//private Texture wood;
	
	public void Start(){

		//bloque de création d'une fenetre
		try {
			Display.setDisplayMode(new DisplayMode((int)largeur, (int)hauteur));
			Display.setTitle("Nom qui roxx !!");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
		
		lastFrame = getTime();
		//wood = textureLoad("wood");
		
		MovableCube2D cube = new MovableTexturedCube2D(100, 100, 100, "testMap", 32, 64);
		cube.setDx(vitesseX);
		cube.setDy(vitesseY);
        cube.setUp();

		//initialisation de la matrice de projection
		glMatrixMode(GL_PROJECTION);
		glOrtho(0, largeur, hauteur, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);

		//boucle d'update
		while(!Display.isCloseRequested()){
			
			float delta = (float)getDelta();

			if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
				cube.moveY(-delta);
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
				cube.moveY(delta);
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
				cube.moveX(-delta);
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
				cube.moveX(delta);
			}
			
			//création du triangle
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			//wood.bind();
			
			cube.draw();
			
			Display.update();
			Display.sync(60);
		}
		
		cube.destroy();
		Display.destroy();
		
	}
	
	/*public boolean deborde(float x, float y){
		boolean test = x < 0 || x+100 > 640 || y < 0 || y+100 > 480;
		return test;
	}*/
	
	public static void main(String[] args){
		TestTexture poke = new TestTexture();
		poke.Start();
		
	}
	
	private static long getTime(){
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	private static double getDelta(){
		double currentTime = getTime();
		double delta = currentTime - (double) lastFrame;
		lastFrame = getTime();
		return delta;
		
	}
}

