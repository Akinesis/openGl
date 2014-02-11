package parametres;

import java.util.Vector;

import org.lwjgl.util.vector.Vector3f;

import entities.Cube3D;

public class ColideSystem {

	private Vector<Cube3D> cloneCubes;

	public ColideSystem(){
		cloneCubes = new Vector<Cube3D>();
	}

	public void setClone(Vector<Cube3D> clone){
		cloneCubes = clone;
	}

	public float ground(Vector3f pos){
		float tempGround=0;

		float tempX, tempZ, tempCamX, tempCamZ;
		tempCamX = getPosTemp(pos.x)+((pos.x < 0)?0:0.5f);
		tempCamZ = getPosTemp(pos.z)-((pos.z < 0)?0.5f:0);
		for(Cube3D cube : cloneCubes){
			tempX = getPosTemp(-cube.getX());
			tempZ = getPosTemp(-cube.getZ());
			if(tempCamX==tempX && tempCamZ==tempZ){
				if(pos.y < tempGround && pos.y < -cube.getY()){
					tempGround=cube.getY();
				}
				//must set falling to true if pos.y >= cube.y
			}
		}
		return tempGround;
	}

	private float getPosTemp(float var){
		float temp;
		if(var>0){
			temp = var-(var%0.5f)+0.5f;
		}else{
			temp = var-(var%0.5f);
		}

		return temp;
	}

	public float colideX(float fac, Vector3f pos){
		for(Cube3D cube : cloneCubes){
			if(pos.z > -cube.getZ() && pos.z < -cube.getZ()+0.5f && //z position test
					pos.x+fac < -cube.getX() && pos.x+fac > -cube.getX()-0.5f && //x position test
					((pos.y > -cube.getY() &&  pos.y < -cube.getY()+0.5f) || //y.head position test
					(pos.y+0.5 >= -cube.getY() &&  pos.y+0.5 <= -cube.getY()+0.5f))){ //y.feet position test
				fac = 0;
			}
		}
		return fac;

	}

	public float colideZ(float fac, Vector3f pos){
		for(Cube3D cube : cloneCubes){
			if(pos.z+fac > -cube.getZ() && pos.z+fac < -cube.getZ()+0.5f && //z position test
					pos.x < -cube.getX() && pos.x > -cube.getX()-0.5f && //x position test
					((pos.y > -cube.getY() &&  pos.y < -cube.getY()+0.5f) || //y.head position test
					(pos.y+0.5 >= -cube.getY() &&  pos.y+0.5 <= -cube.getY()+0.5f))){ //y.feet position test
				fac = 0;
			}
		}
		return fac;
	}
}
