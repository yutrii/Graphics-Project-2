package ass2.spec;

import com.jogamp.opengl.GL2;

public class Avatar {
	private double[] myPos;
	private double[] forward;// = new double[] {0.707, 0, 0.707};
	private double angle;
	private Terrain terrain;
    private double radius = 0.1;
    private static final double NUM_QUADS = 32;
    private static final double INC = 2*Math.PI/NUM_QUADS;

    //hello
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
    	/*
    	double x = myPos[0];
    	double y = myPos[1];
    	double z = myPos[2];
    	gl.glTranslated(x, y, z);
    	
    	double x1, z1;
    	gl.glBegin(GL2.GL_QUAD_STRIP);
	    	for (int i = 0; i <= NUM_QUADS; i++) {
		    	x1 = radius * Math.cos(INC*i);
		    	z1 = radius * Math.sin(INC*i);
		    	
		    	gl.glNormal3d(x1, 0, z1);
		    	
		    	gl.glTexCoord2d(i/NUM_QUADS, 0);
	    	    gl.glVertex3d(x1, 0, z1);
	    	    
	    	    gl.glTexCoord2d(i/NUM_QUADS, 1);
	    	    gl.glVertex3d(x1, 1, z1);
	
	    	}
    	gl.glEnd();
    	*/
    	drawSphere(gl);
    	gl.glPopMatrix();
    }
    
    public boolean canMove(double l) {
    	return terrain.withinRange(myPos[0] + l * forward[0], myPos[2] + l * forward[2]);
    }
    
    public void move(double l) {
    	if (canMove(l)) {
    		myPos[0] += l * forward[0];
    		myPos[2] += l * forward[2];
    		myPos[1] = terrain.altitude(myPos[0], myPos[2]) + 0.5;
    	}
    }
    
    public void rotate(double rad) {
    	System.out.println("rotating");
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
    
    private void drawSphere(GL2 gl) {
    	gl.glTranslated(myPos[0], myPos[1], myPos[2]);
    	//gl.glRotated(Math.toDegrees(angle), 0, 1, 0);
    	double deltaT;
    	double radius = 0.25;
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
    	
    	//gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[1].getTextureId());
    	
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
