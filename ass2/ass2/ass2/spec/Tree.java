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
    	
    	gl.glColor4d(0.247, 0.749, 0.247, 1);
    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
    	gl.glBegin(GL2.GL_TRIANGLES);
    	
    	gl.glEnd();
    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    }
    

}
