package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */
public class Tree {

    private double[] myPos;
    private double radius = 0.1;
    private static final double NUM_QUADS = 32;
    private static final double INC = 2*Math.PI/NUM_QUADS;
    
    //Texture variables
    private static MyTexture[] myTextures;
    private static String textureFileName1 = "ass2/ass2/textures/bark2.jpg";
    private static String textureExt1 = "jpg";
    private static String textureFileName2 = "ass2/ass2/textures/leaves2.jpg";
    
    public Tree(double x, double y, double z) {
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
    }
    
    public double[] getPosition() {
        return myPos;
    }
    
    public void init(GL2 gl) {
    	// Create texture ids. 
    	myTextures = new MyTexture[2];
    	myTextures[0] = new MyTexture(gl, textureFileName1, textureExt1, true);
    	myTextures[1] = new MyTexture(gl, textureFileName2, textureExt1, true);
    }
    
    public void drawTree(GL2 gl) {
    	gl.glPushMatrix();
    	double x = myPos[0];
    	double y = myPos[1];
    	double z = myPos[2];
    	gl.glTranslated(x, y, z);
    	
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[0].getTextureId());
    	
    	//Material lighting for wood
        float matAmb[] = {0.25f, 0.25f, 0.25f, 1.0f};
        float matDif[] = {0.2f, 0.2f, 0.2f, 1.0f};
    	float matSpec[] = { 0.0f, 0.0f, 0.0f, 1.0f };
    	float matShine[] = { 50.0f };
    	
    	// Material properties.
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, matAmb,0);
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, matDif,0);
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpec,0);
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShine,0);
    	
    	double x1, z1;
    	gl.glBegin(GL2.GL_QUAD_STRIP);
	    	for (int i = 0; i <= NUM_QUADS; i++) {
		    	x1 = radius * Math.cos(INC*i);
		    	z1 = radius * Math.sin(INC*i);
		    	
		    	//Since the trunk is a cylinder, the normal is just the x and z
		    	double[] n = {x1, 0, z1};
		    	n = MathUtil.normalise(n);
		    	gl.glNormal3d(n[0], 0, n[2]);
		    	
		    	gl.glTexCoord2d(i/NUM_QUADS, 0);
	    	    gl.glVertex3d(x1, 0, z1);
	    	    
	    	    gl.glTexCoord2d(i/NUM_QUADS, 1);
	    	    gl.glVertex3d(x1, 1, z1);
	
	    	}
    	gl.glEnd();
    	
    	gl.glTranslated(0, 1.3, 0);
    	
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[1].getTextureId());
    	// Material properties.
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, matAmb,0);
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, matDif,0);
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpec,0);
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShine,0);
    	GLUT glut = new GLUT();
    	glut.glutSolidSphere(0.5, 20, 10);
    	//drawSphere(gl);
    	gl.glPopMatrix();
    }
}

/*    
    private void drawSphere(GL2 gl) {
    	gl.glTranslated(0, 1.3, 0);
    	
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[1].getTextureId());
    	//Material lighting for the sphere
        float matAmb[] = {0.25f, 0.25f, 0.25f, 1.0f};
        float matDif[] = {0.12f, 0.58f, 0.18f, 1.0f};
    	float matSpec[] = { 0.0f, 0.0f, 0.0f, 1.0f };
    	float matShine[] = { 50.0f };
    	
    	// Material properties.
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, matAmb,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, matDif,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, matSpec,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine,0);
    	
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
    			x1 = radius * getX(t)*Math.cos((double)ang*2.0*Math.PI/360.0); 
    			x2 = radius * getX(t+deltaT)*Math.cos((double)ang*2.0*Math.PI/360.0); 
    			y1 = radius * getY(t);

    			z1 = radius * getX(t)*Math.sin((double)ang*2.0*Math.PI/360.0);  
    			z2 = radius * getX(t+deltaT)*Math.sin((double)ang*2.0*Math.PI/360.0);  
    			y2 = radius * getY(t+deltaT);

    			double normal[] = {x1,y1,z1};

    			normalize(normal);    

    			double sCoord = j/numSlices;
    			gl.glNormal3dv(normal,0);
    			
    			gl.glTexCoord2d(0d, 0d);
    			gl.glVertex3d(x1,y1,z1);
    			
    			normal[0] = x2;
    			normal[1] = y2;
    			normal[2] = z2;

    			normalize(normal);    
    			gl.glNormal3dv(normal,0);
    			
    			gl.glTexCoord2d(0.3d, 0.3d);
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
*/