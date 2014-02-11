package openGl;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.util.HashSet;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import entities.Cube2D;
import entities.TexturedCube2D;

public class CarreGrille implements parametres.Parametres{

	private HashSet<Cube2D> cubes;
	private enum couleur{
		ROUGE,
		VERT,
		BLEU,
		HUMAIN;
	}
	private couleur choix;

	public CarreGrille(){
		try {
			Display.setDisplayMode(new DisplayMode((int)largeur, (int)hauteur));
			Display.setTitle("Nom qui roxx !!");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}

		cubes = new HashSet<Cube2D>();
		choix = couleur.ROUGE;

		//initialisation de la matrice de projection
		glMatrixMode(GL_PROJECTION);
		glOrtho(0, largeur, hauteur, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}

	public void start(){
		while(!Display.isCloseRequested()){

			while(Keyboard.next()){
				if(Keyboard.isKeyDown(Keyboard.KEY_1)){
					choix = couleur.ROUGE;
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_2)){
					choix = couleur.VERT;
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_3)){
					choix = couleur.BLEU;
				}
				if(Keyboard.isKeyDown(Keyboard.KEY_4)){
					choix = couleur.HUMAIN;
				}
				
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
				cubes.clear();
			}

			//création du carré
			if(Mouse.isButtonDown(0)){
				float colX = getColX();
				float colY = getColY();
				float x = getX();
				float y = getY();
				
				TexturedCube2D cubeTemp = new TexturedCube2D(x, y, 16, "testMap", colX, colY);
				
				if(cubes.contains(cubeTemp)){
					cubes.remove(cubeTemp);
				}
				cubes.add(cubeTemp);
			}

			//création du triangle
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			//wood.bind();

			for(Cube2D cube : cubes){
				cube.draw();
			}

			Display.update();
			Display.sync(60);
		}

		for(Cube2D cube : cubes){
			cube.destroy();
		}

		Display.destroy();
	}

	private float getX(){
		float temp = Mouse.getX();
		return temp-(temp%16);
	}

	private float getY(){
		float temp = hauteur-Mouse.getY();
		return temp-(temp%16);
	}
	
	private float getColX(){
		switch(choix){
		case ROUGE:
			return 0;
		case VERT:
			return 32;
		case BLEU:
			return 64;
		case HUMAIN:
			return 32;
		default:
			return 0;
		}
	}
	
	private float getColY(){
		switch(choix){
		case ROUGE:
			return 0;
		case VERT:
			return 64;
		case BLEU:
			return 64;
		case HUMAIN:
			return 0;
		default:
			return 64;
		}
	}

	public static void main(String [] args){
		CarreGrille game = new CarreGrille();
		game.start();
	}


}
