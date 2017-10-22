package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Avatar {
	private double[] myPos;
	private double[] forward;
	private double angle;
	private Terrain terrain;
	
	private static MyTexture[] myTextures;
    private static String textureFileName1 = "ass2/ass2/textures/gold.jpg";
    private static String textureExt1 = "jpg";

    float matAmb[] = { 0.5f, 0.5f, 0.5f, 1.0f };
    float matDif[] = { 0.5f, 0.5f, 0.5f, 1.0f };
    float matSpec[] = { 0.5f, 0.5f, 0.5f, 1.0f };
    float matShine[] = { 1f };
    
    public Avatar(double x, double y, double z, Terrain t) {
        myPos = new double[3];
        forward = new double[3];
        summonAvatar(x, y, z, 5.5);
        terrain = t;
    }

    public void init(GL2 gl) {
    	myTextures = new MyTexture[1];
    	myTextures[0] = new MyTexture(gl, textureFileName1, textureExt1, true);
    }
    
    public double[] getPosition() {
        return myPos;
    }
    
    //summmons the avatar to the current camera location, facing the same direction
    public void summonAvatar(double x, double y, double z, double rad) {
    	myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
        angle = rad;
        rotate(0);
    }
    
    public void drawAvatar(GL2 gl) {
    	gl.glPushMatrix();
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[0].getTextureId());
    	
    	GLUT glut = new GLUT();
    	gl.glTranslated(myPos[0], myPos[1], myPos[2]);
    	gl.glRotated(Math.toDegrees(angle) - 90, 0, 1, 0);
    	
    	// Material properties.
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, matAmb,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, matDif,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, matSpec,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine,0);
    	
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[0].getTextureId());
    	glut.glutSolidTeapot(0.1);
    	
    	gl.glPopMatrix();
    }
    
    public boolean canMove(double l) {
    	return terrain.withinRange(myPos[0] + l * forward[0], myPos[2] + l * forward[2]);
    }
    
    public boolean canStrafe(double l) {
    	return terrain.withinRange(myPos[0] + l * forward[2], myPos[2] - l * forward[0]);
    }
    
    //moves the avatar forward and back
    public void move(double l) {
    	if (canMove(l)) {
    		myPos[0] += l * forward[0];
    		myPos[2] += l * forward[2];
    		myPos[1] = terrain.altitude(myPos[0], myPos[2]) + 0.25;
    	}
    }
    
    //moves the avatar left and right
    public void strafe(double l) {
    	if (canStrafe(l)) {
    		myPos[0] += l * forward[2];
    		myPos[2] -= l * forward[0];
    		myPos[1] = terrain.altitude(myPos[0], myPos[2]) + 0.25;
    	}
    }
    
    //rotates the avatar to face forward
    public void rotate(double rad) {
    	angle += rad;
    	forward[0] = Math.cos(angle);
    	forward[2] = -Math.sin(angle);
    }
    
    public double[] getPos() {
    	return myPos;
    }
    
    public double getAngle() {
    	return angle;
    }
}
