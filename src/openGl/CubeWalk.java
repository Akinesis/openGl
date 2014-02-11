package openGl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Vector;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector3f;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import entities.Camera;
import entities.Cube3D;
import entities.WiredCube3D;
import static org.lwjgl.opengl.GL11.*;
import parametres.ColideSystem;
import parametres.Parametres;

public class CubeWalk implements Parametres{


	private int floorTexture;

	private int ceilingDisplayList, SouthWallDisplayList, NorthWallDisplayList, floorDisplayList, WestWallDisplayList, EastWallDisplayList;
	private long lastFrame;
	
	private Camera camera;
	
	private Vector<Cube3D> cubes;
	
	private ColideSystem colide;
	
	private WiredCube3D cursor;


	public CubeWalk(){

		try{
			Display.setResizable(true);
			Display.setDisplayMode(new DisplayMode((int)largeur, (int)hauteur));
			Display.setTitle("La 3D ça roxx !");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}

		lastFrame=getTime();
		
		cubes = new Vector<Cube3D>();

		camera = new Camera(fov, (float)Display.getWidth()/(float)Display.getHeight(), zNear, zFar);
		cubes.add(new Cube3D(2, 0.5f, 2.5f, 1/(10*tailleTuile)));
		cubes.add(new Cube3D(-2.5f, 1, -2.5f, 1/(10*tailleTuile)));
		cubes.add(new Cube3D(-2, 0.5f, -2.5f, 1/(10*tailleTuile)));
		cubes.add(new Cube3D(-3, 2.5f, 2.5f, 1/(10*tailleTuile)));
		cursor = new WiredCube3D(0, 1, 0, 1/(10*tailleTuile));
		colide = new ColideSystem();
		colide.setClone(cubes);


		//création du fog
		{
			//floatBuffer avec la le fog
			FloatBuffer fogColours = BufferUtils.createFloatBuffer(4);
			fogColours.put(new float[]{0.3f, 0.3f, 0.3f, 1f});
			glClearColor(0.4f, 0.4f, 0.4f, 1f);
			fogColours.flip();
			
			//les option du fog
			glFog(GL_FOG_COLOR, fogColours);
			glFogi(GL_FOG_MODE, GL_LINEAR);
			glHint(GL_FOG_HINT, GL_NICEST);
			glFogf(GL_FOG_START, fogNear);
			glFogf(GL_FOG_END, fogFar);
			glFogf(GL_FOG_DENSITY, 0.005f);
		}

		//début du chargement de la texture
		floorTexture = glGenTextures();
		{
			InputStream in = null;
			try {
				//ouverture de la texture
				in = new FileInputStream("res/floor.png");
				PNGDecoder decoder = new PNGDecoder(in);
				ByteBuffer buffer = BufferUtils.createByteBuffer(4 * decoder.getWidth() * decoder.getHeight());
				decoder.decode(buffer, decoder.getWidth() * 4, Format.RGBA);
				buffer.flip();
				glBindTexture(GL_TEXTURE_2D, floorTexture);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
				glBindTexture(GL_TEXTURE_2D, 0);

			} catch (FileNotFoundException ex) {
				System.err.println("Failed to find the texture files.");
				ex.printStackTrace();
				Display.destroy();
				System.exit(1);
			} catch (IOException ex) {
				System.err.println("Failed to load the texture files.");
				ex.printStackTrace();
				Display.destroy();
				System.exit(1);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}


		SouthWallDisplayList = glGenLists(1);
		genWallVertFace(SouthWallDisplayList, grideSize/2, hauteurPlafond, grideSize/2, 1);
		NorthWallDisplayList = glGenLists(1);
		genWallVertFace(NorthWallDisplayList, grideSize/2, hauteurPlafond, -grideSize/2, 2);
		WestWallDisplayList = glGenLists(1);
		genWallVertSide(WestWallDisplayList, grideSize/2, hauteurPlafond, grideSize/2, 2);
		EastWallDisplayList = glGenLists(1);
		genWallVertSide(EastWallDisplayList, -grideSize/2, hauteurPlafond, grideSize/2, 1);

		floorDisplayList = glGenLists(1);
		genWallHori(floorDisplayList, -grideSize/2, hauteurSol, -grideSize/2, 1);

		ceilingDisplayList = glGenLists(1);
		genWallHori(ceilingDisplayList, -grideSize/2, hauteurPlafond, -grideSize/2, 2);
		
		//objectDisplayList = glGenLists(1);

		Mouse.setGrabbed(true);
	

	}

	public void start(){


		while(!Display.isCloseRequested()){
			
			cursor.destroy();
					
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			camera.initProjection();
			
			int delta = getDelta();

			glBindTexture(GL_TEXTURE_2D, floorTexture);

			glEnable(GL_CULL_FACE);


			glCallList(floorDisplayList);
			glCallList(ceilingDisplayList);
			glCallList(SouthWallDisplayList);
			glCallList(NorthWallDisplayList);
			glCallList(WestWallDisplayList);
			glCallList(EastWallDisplayList);
			
			for(Cube3D cube : cubes){
				cube.draw();
			}
			
			glLineWidth(3);
			cursor.setPos(getX(), cursor.getY(), cursor.getZ());
			cursor.draw();
			
			glEnable(GL_DEPTH_TEST);
			glDisable(GL_CULL_FACE);
			glBindTexture(GL_TEXTURE_2D, 0);

			glLineWidth(1);
			genCrosshair();
			
			System.out.println(camera.getPos());
		
			camera.useView();
			

			float mouseDX = Mouse.getDX() * 2 * 0.16f;
			float mouseDY = Mouse.getDY() * 2 * 0.16f;
			if(Mouse.isGrabbed()){
				if (camera.getRot().y + mouseDX >= 360) {
					camera.getRot().y = camera.getRot().y + mouseDX - 360;
				} else if (camera.getRot().y + mouseDX < 0) {
					camera.getRot().y = 360 - camera.getRot().y + mouseDX;
				} else {
					camera.getRot().y += mouseDX;
				}if (camera.getRot().x - mouseDY >= -85 && camera.getRot().x - mouseDY <= 85) {
					camera.getRot().x += -mouseDY;
				} else if (camera.getRot().x - mouseDY < -85) {
					camera.getRot().x = -85;
				} else if (camera.getRot().x - mouseDY > 85) {
					camera.getRot().x = 85;
				}
			}


			boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_Z);
			boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S);
			boolean keyLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_Q);
			boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D);
			boolean flyUp = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
			boolean flyDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
			
			float speed = (camera.isJumping())?0.003f:0.004f;
			
			if(keyUp)
				camera.move(speed*delta,1, colide);
			if(keyDown)
				camera.move(-speed*delta,1, colide);
			if(keyLeft)
				camera.move(speed*delta,0, colide);
			if(keyRight)
				camera.move(-speed*delta,0, colide);
			
			camera.jump(flyUp, delta, colide);
			if (flyDown && !flyUp) {
				double newPositionY = (walkingSpeed * 0.002) * delta;
				camera.getPos().y += newPositionY;
			}
			if (Mouse.isButtonDown(0)) {
				Mouse.setGrabbed(true);
			}
			while (Keyboard.next()) {
				if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
					camera.setPos(new Vector3f(0, -1, 0));
					camera.setRot(new Vector3f(0, 0, 0));
				}
				
				if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)&& Mouse.isGrabbed()) {
					Mouse.setCursorPosition(Display.getWidth()/2, Display.getHeight()/2);
					Mouse.setGrabbed(false);
					
				}
			}
			

			Display.update();
			Display.sync(60);
		}

		glDeleteTextures(floorTexture);
		glDeleteLists(floorDisplayList, 1);
		glDeleteLists(ceilingDisplayList, 1);
		glDeleteLists(SouthWallDisplayList, 1);
		glDeleteLists(NorthWallDisplayList, 1);
		glDeleteLists(WestWallDisplayList, 1);
		glDeleteLists(EastWallDisplayList, 1);
		Display.destroy();
		System.exit(0);
	}

	
	private void genWallHori(int disp, float x, float y, float z, int clef){

		float[] coord = {x,z, x,z+grideSize, x+grideSize,z ,x+grideSize,z ,x,z+grideSize ,x+grideSize,z+grideSize};

		float[] text = {0,0, 0,grideSize * 10 * tailleTuile, grideSize * 10 * tailleTuile,0, grideSize * 10 * tailleTuile,0, 
				0, grideSize * 10 * tailleTuile, grideSize * 10 * tailleTuile,grideSize * 10 * tailleTuile};

		glNewList(disp, GL_COMPILE);
		glBegin(GL_TRIANGLES);

		if(clef == 1){
			for(int i = 0; i<12; i+=2){
				glTexCoord2f(text[i], text[i+1]);
				glVertex3f(coord[i], y, coord[i+1]);
			}
		}

		if(clef == 2){

			for(int i = 11; i>-1; i-=2){
				glTexCoord2f(text[i-1], text[i]);
				glVertex3f(coord[i-1], y, coord[i]);
			}
		}

		glEnd();
		glEndList();

		/*
		glTexCoord2f(0, 0);
		glVertex3f(x, y, z);

		glTexCoord2f(0, grideSize * 10 * tailleTuile);
		glVertex3f(x, y, z+grideSize);

		glTexCoord2f(grideSize * 10 * tailleTuile, 0);
		glVertex3f(x+grideSize, y, z);

		glTexCoord2f(grideSize * 10 * tailleTuile, 0);
		glVertex3f(x+grideSize, y, z);

		glTexCoord2f(0, grideSize * 10 * tailleTuile);
		glVertex3f(x, y, z+grideSize);

		glTexCoord2f(grideSize * 10 * tailleTuile, grideSize * 10 * tailleTuile);
		glVertex3f(x+grideSize, y, z+grideSize);

		glEnd();
		glEndList();*/

	}
	
	private void genWallVertFace(int disp, float x, float y, float z, int clef){
		glNewList(disp, GL_COMPILE);
		glBegin(GL_TRIANGLES);
		
		if(clef==1){
			glTexCoord2f(-grideSize * 10 * tailleTuile, -grideSize * 10 * tailleTuile);
			glVertex3f(x-grideSize, hauteurSol, z);
		}else if(clef==2){
			glTexCoord2f(0, 0);
			glVertex3f(x, y, z);
		}
		
		glTexCoord2f(0, -grideSize * 10 * tailleTuile);
		glVertex3f(x-grideSize, y, z);
		

		glTexCoord2f(-grideSize * 10 * tailleTuile, 0);
		glVertex3f(x, hauteurSol, z);

		
		glTexCoord2f(0, -grideSize * 10 * tailleTuile);
		glVertex3f(x, hauteurSol, z);
		
		glTexCoord2f(-grideSize * 10 * tailleTuile, 0);
		glVertex3f(x-grideSize, y, z);
		
		if(clef==2){
			glTexCoord2f(-grideSize * 10 * tailleTuile, -grideSize * 10 * tailleTuile);
			glVertex3f(x-grideSize, hauteurSol, z);
		}else if(clef==1){
			glTexCoord2f(0, 0);
			glVertex3f(x, y, z);
		}
		
		glEnd();
		glEndList();
	}

	private void genWallVertSide(int disp, float x, float y, float z, int clef){
		glNewList(disp, GL_COMPILE);
		glBegin(GL_TRIANGLES);
		
		if(clef==1){
			glTexCoord2f(-grideSize * 10 * tailleTuile, -grideSize * 10 * tailleTuile);
			glVertex3f(x, hauteurSol, z-grideSize);
		}else if(clef==2){
			glTexCoord2f(0, 0);
			glVertex3f(x, y, z);
		}
		
		glTexCoord2f(0, -grideSize * 10 * tailleTuile);
		glVertex3f(x, y, z-grideSize);
		

		glTexCoord2f(-grideSize * 10 * tailleTuile, 0);
		glVertex3f(x, hauteurSol, z);

		
		glTexCoord2f(0, -grideSize * 10 * tailleTuile);
		glVertex3f(x, hauteurSol, z);
		
		glTexCoord2f(-grideSize * 10 * tailleTuile, 0);
		glVertex3f(x, y, z-grideSize);
		
		if(clef==2){
			glTexCoord2f(-grideSize * 10 * tailleTuile, -grideSize * 10 * tailleTuile);
			glVertex3f(x, hauteurSol, z-grideSize);
		}else if(clef==1){
			glTexCoord2f(0, 0);
			glVertex3f(x, y, z);
		}
		
		glEnd();
		glEndList();
	}
	
	private void genCrosshair(){
		
		load2D();
		
		glBegin(GL_LINES);
		
		glVertex2f(Display.getWidth()/2 +10, Display.getHeight()/2);
		glVertex2f(Display.getWidth()/2 -10, Display.getHeight()/2);
		
		glVertex2f(Display.getWidth()/2, Display.getHeight()/2 +10);
		glVertex2f(Display.getWidth()/2, Display.getHeight()/2 -10);
		
		glEnd();

	}
	
	private void load2D(){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, largeur, hauteur, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
		glLoadIdentity();
		
		glDisable(GL_DEPTH_TEST);
		
	}
	
	
	private float getX(){
		//return temp-(temp%16);
		
		//float tempX = camera.getPos().x + (float)Math.cos(Math.toRadians(camera.getRot().y + 90 ));
		float tempX = (float)(-1*Math.sin(camera.getRot().x))/(float)(Math.sin(camera.getRot().y+90));
		
		return tempX;
	
	}
	
	public void get3DRay(){
		
	}

	
	private static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	private int getDelta() {
		long currentTime = getTime();
		int delta = (int) (currentTime - lastFrame);
		lastFrame = getTime();
		return delta;
	}

	public static void main(String[] args){
		CubeWalk cube = new CubeWalk();
		cube.start();
	}

}
