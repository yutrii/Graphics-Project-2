package ass2.spec;

import com.jogamp.opengl.GL2;

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
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, matAmb,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, matDif,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, matSpec,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine,0);
    	
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
    	drawCube(gl);
    	gl.glPopMatrix();
    }
    
    //draws a cube (for the leaves)
    private void drawCube(GL2 gl) {
    	//points to draw
    	float[] p = new float[]{-1, 1, 1, 1, 1, 1, 1, -1, 1, -1, -1, 1,//front
				1, -1, 1, 1, -1, -1, 1, 1, -1, 1, 1, 1,//right
				-1, -1, -1, -1, 1, -1, 1, 1, -1, 1, -1, -1,//back
				-1, -1, 1, -1, 1, 1, -1, 1, -1, -1, -1, -1,//left
				-1, 1, 1, 1, 1, 1, 1, 1, -1, -1, 1, -1,//top
				-1, -1, -1, 1, -1, -1, 1, -1, 1, -1, -1, 1//bottom
		};
    	
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
    	
    	gl.glTranslated(0, 1.3, 0);
    	gl.glScaled(0.35, 0.35, 0.35);
    	
    	gl.glBegin(GL2.GL_QUADS); 
    	
    	int[][] norm = new int[][] {
    		{0, 0, -1},
			{1, 0, 0},
			{0, 0, -1},
			{-1, 0, 0},
			{0, 1, 0},
			{0, -1, 0}
    	};
    	
    	for (int i = 0, j = 0; i < p.length; j++) {
    		gl.glNormal3d(norm[j][0], norm[j][1], norm[j][2]);
    		gl.glTexCoord2d(1, 1);
    		gl.glVertex3d(p[i++], p[i++], p[i++]);
    		gl.glTexCoord2d(0, 1);
    		gl.glVertex3d(p[i++], p[i++], p[i++]);
    		gl.glTexCoord2d(0, 0);
    		gl.glVertex3d(p[i++], p[i++], p[i++]);
    		gl.glTexCoord2d(1, 0);
    		gl.glVertex3d(p[i++], p[i++], p[i++]);
    	}
    	
    	gl.glEnd();
    }
}