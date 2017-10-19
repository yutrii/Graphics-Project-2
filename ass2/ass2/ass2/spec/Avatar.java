package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Avatar {
	private double[] myPos;
	private double[] forward;// = new double[] {0.707, 0, 0.707};
	private double angle;
	private Terrain terrain;
    private double radius = 0.1;
    private static final double NUM_QUADS = 32;
    private static final double INC = 2*Math.PI/NUM_QUADS;


    public Avatar(double x, double y, double z, Terrain t) {
        myPos = new double[3];
        forward = new double[3];
        summonAvatar(x, y, z, 5.5);
        terrain = t;
        
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
  
    	GLUT glut = new GLUT();
    	gl.glTranslated(myPos[0], myPos[1], myPos[2]);
    	gl.glRotated(Math.toDegrees(angle) + 90, 0, 1, 0);
    	glut.glutSolidTeapot(0.1);
    	
    	gl.glPopMatrix();
    }
    
    public boolean canMove(double l) {
    	return terrain.withinRange(myPos[0] + l * forward[0], myPos[2] + l * forward[2]);
    }
    
    public boolean canStrafe(double l) {
    	return terrain.withinRange(myPos[0] + l * forward[2], myPos[2] - l * forward[0]);
    }
    
    public void move(double l) {
    	if (canMove(l)) {
    		myPos[0] += l * forward[0];
    		myPos[2] += l * forward[2];
    		myPos[1] = terrain.altitude(myPos[0], myPos[2]) + 0.25;
    	}
    }
    
    public void strafe(double l) {
    	if (canStrafe(l)) {
    		myPos[0] += l * forward[2];
    		myPos[2] -= l * forward[0];
    		myPos[1] = terrain.altitude(myPos[0], myPos[2]) + 0.25;
    	}
    }
    
    public void rotate(double rad) {
    	//System.out.println("rotating");
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
