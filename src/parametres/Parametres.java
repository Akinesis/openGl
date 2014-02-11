package parametres;

public interface Parametres {

	public float largeur = 800;
	public float hauteur = 600;
	
	public float vitesseX = 0.3f;
	public float vitesseY = 0.3f;
	
	float tailleImage = 128;
	float tailleCarre = 32;
	float vectCarre = tailleCarre/tailleImage;
	
	float walkingSpeed = 5;
	float grideSize = 10;
	float hauteurPlafond = 10;
	float hauteurSol = 0;
	float tailleTuile = 0.20f;
	
	float fov = 68;
	
	float zNear = 0.3f;
	float zFar = 20f;
	
	/** The distance where fog starts appearing. */
    final float fogNear = 9f;
    
    /** The distance where the fog stops appearing (fully black here) */
    final float fogFar = 13f;
	
	
}
