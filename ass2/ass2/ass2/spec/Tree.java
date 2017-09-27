package ass2.spec;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */
public class Tree {

    private double[] myPos;
    
    public Tree(double x, double y, double z) {
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
    }
    
    public double[] getPosition() {
        return myPos;
    }
    
    public void drawTree(GL2 gl) {
    	
    	gl.glColor4d(0.6, 0.4, 0.2, 1); //brown
    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    	gl.glBegin(GL2.GL_QUADS);
    	
    	
    	
    	double x = myPos[0];
    	double y = myPos[1];
    	double z = myPos[2];
    	
    	gl.glVertex3d(x + 0.1, y, z);
    	gl.glVertex3d(x - 0.1, y, z);
    	gl.glVertex3d(x - 0.1, y + 1.5, z);
    	gl.glVertex3d(x + 0.1, y + 1.5, z);
    	
    	gl.glVertex3d(x, y, z + 0.1);
    	gl.glVertex3d(x, y, z - 0.1);
    	gl.glVertex3d(x, y + 1.5, z - 0.1);
    	gl.glVertex3d(x, y + 1.5, z + 0.1);
    	
    	gl.glEnd();
    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    	
    	
    	gl.glColor4d(0.247, 0.749, 0.247, 1); //green
		
		gl.glBegin(GL2.GL_POLYGON);    	
		
		double angle = 0;
    	double increment = 360/32;
    	for (int i = 0; i <= 32; i++, angle += increment) {
    		
    		double Z = 0.5 * Math.cos(Math.toRadians(angle));
    		double Y = 0.5 * Math.sin(Math.toRadians(angle));
    	
    		gl.glVertex3d(x, y + 1 + Y, z + Z);
    	}
    	
    	gl.glEnd();
    	//hi
    	gl.glBegin(GL2.GL_POLYGON);
    	
    	for (int i = 0; i <= 32; i++, angle += increment) {
    		
    		double X = 0.5 * Math.cos(Math.toRadians(angle));
    		double Y = 0.5 * Math.sin(Math.toRadians(angle));
    	
    		gl.glVertex3d(x + X, y + 1 + Y, z);
    	}
    	
    	gl.glEnd();
    	
    	
    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    	/*
    	gl.glColor4d(0.247, 0.749, 0.247, 1); //green
    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
    	gl.glBegin(GL2.GL_TRIANGLES);
    	
    	gl.glEnd();
    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    	*/
    }
    

}
