package tests;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import entities.MovableCube2D;

public class plop implements Param {

	private static long lastFrame;
	MovableCube2D cube = new MovableCube2D(100, 100, 100);

	public void start() {
		try {
			Display.setDisplayMode(new DisplayMode((int)largeur, (int)hauteur));
			Display.setTitle("Super titre of death");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		initGL();
		
		lastFrame = getTime();
		
		cube.setDx(vitesseX);
		cube.setDy(vitesseY);
		
		while (!Display.isCloseRequested()) {
			
			float delta=(float)getDelta();
			
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
				cube.moveX(-delta);
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
				cube.moveX(delta);
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_UP)){
				cube.moveY(-delta);
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
				cube.moveY(delta);
			}
				
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

			/*
			GL11.glBegin(GL11.GL_TRIANGLES);
				GL11.glVertex2f(230, 100);
				GL11.glColor3ub((byte)0, (byte)0, (byte)255);
				GL11.glVertex2f(100, 100);
				GL11.glColor3ub((byte)255, (byte)0, (byte)0);
				GL11.glVertex2f(100, 230);
				GL11.glColor3ub((byte)0, (byte)255, (byte)0);
			GL11.glEnd();*/
				
			cube.draw();
			
			Display.update();
			Display.sync(60); // cap fps to 60fps
		}

		Display.destroy();
	}
	
	private void initGL(){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity(); //reset
		GL11.glOrtho(0, 800, 600, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
	}
	
	private static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	private static double getDelta() {
		long currentTime = getTime();
		double delta = (double) currentTime - (double) lastFrame;
		lastFrame = getTime();
		return delta;
	}
	
	
	public static void main(String[] argv) {
		plop displayExample = new plop();
		displayExample.start();
	}
}