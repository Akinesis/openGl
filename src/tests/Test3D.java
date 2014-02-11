package tests;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.Random;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class Test3D {

	private float speed;
	private Point[] points;
	
	public Test3D(){
		try {
            Display.setDisplayMode(new DisplayMode(640, 480));
            Display.setTitle("test 3D");
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        }
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective((float)30, 640f/480f, 0.001f, 100);
		glMatrixMode(GL_MODELVIEW);
		
		glEnable(GL_DEPTH_TEST);
		
		points = new Point[1000];
        Random random = new Random();
        // Iterate of every array index
        for (int i = 0; i < points.length; i++) {
            // Set the point at the array index to 
            // x = random between -50 and +50
            // y = random between -50 and +50
            // z = random between  0  and -200
            points[i] = new Point((random.nextFloat() - 0.5f) * 100f, (random.nextFloat() - 0.5f) * 100f,
                    random.nextInt(200) - 200);
        }
        
        speed = 0.0f;
	}
	
	
	public void start(){
		
		while(!Display.isCloseRequested()){
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			glTranslatef(0, 0, speed);
			
			glBegin(GL_POINTS);
			for(Point p: points){
				glVertex3f(p.x, p.y, p.z);
			}
			glEnd();
			
			if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                speed += 0.01f;
            }
            // If we're pressing the "down" key decrease our speed
            if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                speed -= 0.01f;
            }
            
            Display.update();
            Display.sync(60);
			
		}
		
		Display.destroy();
		
	}
	
	
	
	
	public static void main(String[] args) {
		Test3D tes = new Test3D();
		tes.start();
	}
	
	 private static class Point {

	        final float x;
	        final float y;
	        final float z;

	        public Point(float x, float y, float z) {
	            this.x = x;
	            this.y = y;
	            this.z = z;
	        }
	    }
}
