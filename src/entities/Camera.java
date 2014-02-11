package entities;

import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FOG;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector3f;

import parametres.ColideSystem;

public class Camera {

	private float fov;
	private float aspect;
	private float near;
	private float far;
	private float hauteurSaut;
	private long lasteJump=0;
	
	private Vector3f position;
	private Vector3f rotation;
	
	private boolean jumping, onGround, falling;
	
	public Camera(float fov, float aspect, float near, float far){
		
		
		this.fov=fov;
		this.aspect=aspect;
		this.near=near;
		this.far=far;
		
		hauteurSaut = 0;
		
		jumping = falling = false;
		onGround = true;
		
		position = new Vector3f(0, -1, 0);

		rotation = new Vector3f(0, 0, 0);
		
		initProjection();
		enable();
			
	}
	
	public void initProjection(){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(fov, aspect, near, far);
		//glMatrixMode(GL_MODELVIEW);	
		glEnable(GL_DEPTH_TEST);

		
	}
	
	private void enable(){
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glEnable(GL_ALPHA_TEST);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_FOG);
	}
	
	public Vector3f getPos(){
		return position;
	}
	
	public Vector3f getRot(){
		return rotation;
	}
	
	public void setPos(Vector3f pos){
		position=pos;
	}
	
	public void setRot(Vector3f rot){
		rotation=rot;
	}
	
	public void useView(){
		glRotatef(rotation.x, 1, 0, 0);
		glRotatef(rotation.y, 0, 1, 0);
		glRotatef(rotation.z, 0, 0, 1);
		glTranslatef(position.x, position.y, position.z);
	}
	
	public void move(float amt, float dir, ColideSystem colide){
		double tempZ = amt * Math.sin(Math.toRadians(rotation.y + 90 * dir));
		double tempX = amt * Math.cos(Math.toRadians(rotation.y + 90 * dir));
		
		position.z += colide.colideZ((float)tempZ, position);
		position.x += colide.colideX((float)tempX,position);
	}
	
	public void jump(boolean space, float delta, ColideSystem colide){
		//System.out.println(getTime() +" "+ lasteJump);
		if(space && !jumping && getTime()-lasteJump > 700){
			jumping=true;
			hauteurSaut= (-colide.ground(position))-2;
			lasteJump = getTime();
		}
		
		//System.out.println(hauteurSaut);	
		
		if(jumping || falling){
			if(position.y > hauteurSaut && !falling){
				position.y -= 0.0040f*delta;
			}else if(!onGround && falling){
				position.y += 0.0040f*delta;
			}else if (position.y <= hauteurSaut){
				falling = true;
			}
		}else{
			falling = position.y < colide.ground(position)-1;
		}

		onGround = position.y >= -(colide.ground(position))-1;
		
		if(onGround){
			jumping=false;
			falling = false;
		}
	}
	
	public boolean isJumping(){
		return jumping;
	}
	
	private static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
}
