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
    	double x = myPos[0];
    	double y = myPos[1];
    	double z = myPos[2];
    	gl.glTranslated(x, y, z);
    	
    	
    	gl.glColor4d(0.6, 0.4, 0.2, 1); //brown
    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    	gl.glBegin(GL2.GL_QUADS);
    	
    	
    	
    	
    	/* drawing trunk as two 2d rectangles
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
    	*/
    	
    	double inc = 360/32;
    	double angle = 0;
    	
    	double x1, z1, x2, z2;
    	gl.glBegin(GL2.GL_QUADS);
    	for (int i = 0; i <= 32; i++, angle += inc) {
    	
	    	x1 = 0.1 * Math.cos(Math.toRadians(angle));
	    	z1 = 0.1 * Math.sin(Math.toRadians(angle));
	    	x2 = 0.1 * Math.cos(Math.toRadians(angle+inc));
	    	z2 = 0.1 * Math.sin(Math.toRadians(angle+inc));
	    	
    	    gl.glVertex3d(x1,0,z1);
    	    gl.glVertex3d(x1,1,z1);
    	    gl.glVertex3d(x2,1,z2);
    	    gl.glVertex3d(x2,0,z2);
    	    
    	    /*gl.glVertex3d(x1+x,y,z1+z);
    	    gl.glVertex3d(x1+x,y+1,z1+z);
    	    gl.glVertex3d(x2+x,y+1,z2+z);
    	    gl.glVertex3d(x2+x,y,z2+z);*/
    	}
    	gl.glEnd();
    	
    	drawSphere(gl);
    	
    	//gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
    	/*
    	angle = 0;
    	inc = 360/128;
    	
    	for (int i = 0; i <= 128; i++, angle += inc) {
    		gl.glRotated(inc, 0, 1, 0);
    		drawSemicircle(gl);
    	}
       	*/
    	
    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    	/*
    	gl.glColor4d(0.247, 0.749, 0.247, 1); //green
    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
    	gl.glBegin(GL2.GL_TRIANGLES);
    	
    	gl.glEnd();
    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    	*/
    	
    	
    }
    
    private void drawSemicircle(GL2 gl) {
    	
    	gl.glColor4d(0.247, 0.749, 0.247, 1); //green
    	
		gl.glBegin(GL2.GL_POLYGON);    	
		
		double angle = 90;
    	double increment = 360/32;
    	for (int i = 0; i <= 16; i++, angle += increment) {
    		
    		double Z = 0.5 * Math.cos(Math.toRadians(angle));
    		double Y = 0.5 * Math.sin(Math.toRadians(angle));
    	
    		gl.glVertex3d(0, Y+1, Z);
    	}
    	
    	gl.glEnd();
    }
    
    private void drawSphere(GL2 gl) {
    	gl.glTranslated(0, 1, 0);
    	double deltaT;
    	double radius = 0.5;
    	int maxStacks = 10;
    	int maxSlices = 20;
    	int numStacks = 10;
    	int numSlices = 20;
    	//We want t to go from t = -0.25 to t = 0.25
    	//as we want to revolve a semi-circle around
    	//the y-axis.
    	deltaT = 0.5/maxStacks;
    	int ang;  
    	int delang = 360/maxSlices;
    	double x1,x2,z1,z2,y1,y2;
    	
    	for (int i = 0; i < numStacks; i++) 
    	{ 
    		double t = -0.25 + i*deltaT;
    		
    		gl.glBegin(GL2.GL_TRIANGLE_STRIP); 
    		for(int j = 0; j <= numSlices; j++)  
    		{  
    			ang = j*delang;
    			x1=radius * getX(t)*Math.cos((double)ang*2.0*Math.PI/360.0); 
    			x2=radius * getX(t+deltaT)*Math.cos((double)ang*2.0*Math.PI/360.0); 
    			y1 = radius * getY(t);

    			z1=radius * getX(t)*Math.sin((double)ang*2.0*Math.PI/360.0);  
    			z2= radius * getX(t+deltaT)*Math.sin((double)ang*2.0*Math.PI/360.0);  
    			y2 = radius * getY(t+deltaT);

    			double normal[] = {x1,y1,z1};


    			normalize(normal);    

    			gl.glNormal3dv(normal,0);         
    			gl.glVertex3d(x1,y1,z1);
    			normal[0] = x2;
    			normal[1] = y2;
    			normal[2] = z2;

    			normalize(normal);    
    			gl.glNormal3dv(normal,0); 
    			gl.glVertex3d(x2,y2,z2); 

    		}; 
    		gl.glEnd();  
    	}
    }
    
    public void normalize(double v[])  
    {  
        double d = Math.sqrt(v[0]*v[0]+v[1]*v[1]+v[2]*v[2]);  
        if (d != 0.0) 
        {  
           v[0]/=d; 
           v[1]/=d;  
           v[2]/=d;  
        }  
    } 
    
    void normCrossProd(double v1[], double v2[], double out[])  
    {  
       out[0] = v1[1]*v2[2] - v1[2]*v2[1];  
       out[1] = v1[2]*v2[0] - v1[0]*v2[2];  
       out[2] = v1[0]*v2[1] - v1[1]*v2[0];  
       normalize(out);  
    } 
    
   
    
    double getX(double t){
    	double x  = Math.cos(2 * Math.PI * t);
        return x;
    }
    
    double getY(double t){
    	
    	double y  = Math.sin(2 * Math.PI * t);
        return y;
    }
    

}
