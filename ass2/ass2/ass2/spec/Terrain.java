package ass2.spec;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

//TESTING COMMENT FOR GIT SETUP

/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain {
	//Objects on the terrain
    private Dimension mySize;
    private double[][] myAltitude;
    private List<Tree> myTrees;
    private List<Road> myRoads;
    private List<Monster> myMonsters;
    private float[] mySunlight;
    private Camera myCamera;
    
    //Flags for showing things
    private boolean isRaining = false;
    private boolean isBillboard = true;
    private boolean showRainDrop = false;
    private boolean demoBillboard = false;
    private boolean rainPause = false;
    
    //Points as array for normal calculations
    double[] p0 = new double[3];
    double[] p1 = new double[3];
    double[] p2 = new double[3];
    double[] p3 = new double[3];
    
    //Texture variables
    private static MyTexture[] myTextures;
    private static String textureFileName1 = "ass2/ass2/textures/grass1.jpg";
    private static String textureExt1 = "jpg";
    private static String textureFileName2 = "ass2/ass2/textures/rain.jpg";
    private static String textureExt2 = "jpg";
    
    //Material lighting
    float matAmb[] = {0.25f, 0.25f, 0.25f, 1.0f};
    float matDif[] = {0.44f, 0.55f, 0.07f, 1.0f};
	float matSpec[] = { 0.0f, 0.0f, 0.0f, 1.0f };
	float matShine[] = { 5.0f };
	
	//Particle
	private int MAX_PARTICLES;
	private RainParticle[] particles;// = new RainParticle[MAX_PARTICLES];
	
    
    /**
     * Create a new terrain
     *
     * @param width The number of vertices in the x-direction
     * @param depth The number of vertices in the z-direction
     */
    public Terrain(int width, int depth) {
        mySize = new Dimension(width, depth);
        myAltitude = new double[width][depth];
        myTrees = new ArrayList<Tree>();
        myRoads = new ArrayList<Road>();
        myMonsters = new ArrayList<Monster>();
        mySunlight = new float[3];
        MAX_PARTICLES = width*depth*5;
        //MAX_PARTICLES = 100;
        particles = new RainParticle[MAX_PARTICLES];
     
    }
    
    public Terrain(Dimension size) {
        this(size.width, size.height);
    }

    public Dimension size() {
        return mySize;
    }

    public List<Tree> trees() {
        return myTrees;
    }

    public List<Road> roads() {
        return myRoads;
    }

    public float[] getSunlight() {
        return mySunlight;
    }
    
    //Initialisation function for terrain
    public void init(GL2 gl) {
    	// Create texture ids. 
    	myTextures = new MyTexture[2];
    	myTextures[0] = new MyTexture(gl, textureFileName1, textureExt1, true);
    	myTextures[1] = new MyTexture(gl, textureFileName2, textureExt2, true);
    	
    	for (Tree t : myTrees) {
    		t.init(gl);
    	}
    	
    	for (Road r : myRoads) {
    		r.init(gl);
    	}
    	
    	for (Monster m: myMonsters) {
    		m.init(gl);
    	}
    	
    	for (int i = 0; i < MAX_PARTICLES; i++) {
    		particles[i] = new RainParticle(this);
    	}
    }

    /**
     * Set the sunlight direction. 
     * 
     * Note: the sun should be treated as a directional light, without a position
     * 
     * @param dx
     * @param dy
     * @param dz
     */
    public void setSunlightDir(float dx, float dy, float dz) {
        mySunlight[0] = dx;
        mySunlight[1] = dy;
        mySunlight[2] = dz;        
    }
    
    /**
     * Resize the terrain, copying any old altitudes. 
     * 
     * @param width
     * @param height
     */
    public void setSize(int width, int height) {
        mySize = new Dimension(width, height);
        double[][] oldAlt = myAltitude;
        myAltitude = new double[width][height];
        
        for (int i = 0; i < width && i < oldAlt.length; i++) {
            for (int j = 0; j < height && j < oldAlt[i].length; j++) {
                myAltitude[i][j] = oldAlt[i][j];
            }
        }
    }

    /**
     * Get the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public double getGridAltitude(int x, int z) {
        return myAltitude[x][z];
    }

    /**
     * Set the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public void setGridAltitude(int x, int z, double h) {
        myAltitude[x][z] = h;
    }

    /**
     * Get the altitude at an arbitrary point. 
     * Non-integer points should be interpolated from neighbouring grid points
     * 
     * TO BE COMPLETED
     * 
     * @param x
     * @param z
     * @return
     */
    public double altitude(double x, double z) {
        double altitude = 0;
        
        //if point is integer
        if (x == Math.floor(x) && z == Math.floor(z)) {
        	altitude = getGridAltitude((int)x, (int)z);
        } else { //non integer
        
        	/*	 +-----+  
   				 | U  /|  
   				 |  /  |
   				 |/  L |
   				 +-----+
        	 */
        	
        	//determine if it is in upper or lower triangle
        	//4 points are (Starting top left clockwise
        	//floor(x), floor(z) -> p1
        	//ceil(x), floor(z)  -> p2
        	//ceil(x), ceil(z)   -> p3
        	//floor(x), ceil(z)  -> p4
        		
        	//method is to check which side of the diagonal edge the point lies (between p2 and p4)
        	//then interpolate
        	
        	double p2x = Math.ceil(x);
        	double p2z = Math.floor(z);
        	
        	double p4x = Math.floor(x);
        	double p4z = Math.ceil(z);
        	
        	if (x == p4x) {
        		//on left line
        		double p1y = getGridAltitude((int)p2x, (int)p4z);
        		double p4y = getGridAltitude((int)p4x, (int)p4z);
        		double height = p1y - p4y;
        		
        		if (height == 0) { //points are same altitude
        			altitude = getGridAltitude((int)p4x, (int)p4z);
        		} else {
        			double pos;
        			double dist = Math.sqrt(2);
        			
        			if (height > 0) { // p1 is higher
        			//distance of unknown point along line
        				pos = p4z - z;
        			} else { //p1 is higher than p2
        				height = Math.abs(height);
        				pos = z - p2z;
        			}
        			altitude = pos * height / dist;
        		}
        	} else if (z == p2z) {
        		//on top line
        		double p1y = getGridAltitude((int)p4x, (int)p2z);
        		double p2y = getGridAltitude((int)p2x, (int)p2z);
        		double height = p1y - p2y;
        		
        		if (height == 0) { //points are same altitude
        			altitude = getGridAltitude((int)p4x, (int)p4z);
        		} else {
        			double pos;
        			double dist = 1;
        			
        			if (height > 0) { // p1 is higher
        			//distance of unknown point along line
        				pos = p2x - x;
        			} else { //p1 is higher than p2
        				height = Math.abs(height);
        				pos = x - p4x;
        			}
        			altitude = pos * height / dist;
        		}
        	} else {
        	
	        	//double det = ((p4x - p2x)*(z - p2z) - (p4z - p2z)*(x - p2x));
	        	
	        	double det = (p2x - x) - (z - p2z);
	        	
	        	double p2y = getGridAltitude((int)p2x, (int)p2z);
	        	double p4y = getGridAltitude((int)p4x, (int)p4z);
	        	
	        	if (det == 0) {
	        		//it's on the diagonal line, easy
	        		//use p1 and p2 altitude to interpolate
	        		 //length of diagonal
	        		// simplify interpolation by finding difference in altitude
	        		double height = p2y - p4y;
	        		
	        		if (height == 0) { //points are same altitude
	        			altitude = getGridAltitude((int)p4x, (int)p4z);
	        		} else {
	        			double pos;
	        			double dist = Math.sqrt(2);
	        			
	        			if (height < 0) { // p2 is higher than p1
	        			//distance of unknown point along line
	        				pos = Math.sqrt((p2x-x)*(p2x-x) + (z-p2z)*(z-p2z));
	        			} else { //p1 is higher than p2
	        				height = Math.abs(height);
	        				pos = Math.sqrt((x-p4x)*(x-p4x) + (p4z-z)*(p4z-z));
	        			}
	        			altitude = pos * height / dist;
	        		}
	        	} else {
	        		double r1;
	        		double r2;
	        		if (det > 0) {
	        			//it's in upper trangle (i think)
	        			double p1x = p4x;
	                	double p1z = p2z;
	                	double p1y = getGridAltitude((int)p1x, (int)p1z);
	                	
	                	r1 = (z-p1z)*p4y + (p4z-z)*p1y;
	                	r2 = (z-p2z)*p4y + (p4z-z)*p2y;
	                	altitude = ((x-p4x)/(p4z-z))*r2 + ((p4x+p4z-z-x)/(p4z-z))*r1;
	                	
	        		} else {
	        			//it's in lower (i think)
	        			double p3x = p2x;
	                	double p3z = p4z;
	                	double p3y = getGridAltitude((int)p3x, (int)p3z);
	
	                	r1 = (z-p2z)*p4y + (p4z-z)*p2y;
	                	r2 = (z-p2z)*p3y + (p3z-z)*p2y;
	                	altitude = ((x-p2x-p2z+z)/(z-p2z))*r2 + ((p2x-x)/(z-p2z))*r1;
	        		}
	        	}
        	}
        }
        
        return altitude;
    }

    /**
     * Add a tree at the specified (x,z) point. 
     * The tree's y coordinate is calculated from the altitude of the terrain at that point.
     * 
     * @param x
     * @param z
     */
    public void addTree(double x, double z) {
        double y = altitude(x, z);
    	//System.out.println("Tree at (" + x + ", " + z + ") is at altitude " + y);
        Tree tree = new Tree(x, y, z);
        myTrees.add(tree);
    }


    /**
     * Add a road. 
     * 
     * @param x
     * @param z
     */
    public void addRoad(double width, double[] spine) {
        Road road = new Road(width, spine);
        myRoads.add(road);        
    }
    
    /**
     * Add a Monster. 
     * 
     * @param x
     * @param z
     */
    public void addMonster(double x, double z) {
    	Monster monster = new Monster(x, altitude(x, z), z);
        myMonsters.add(monster);        
    }
    
    public boolean withinRange(double x, double z) {
    	if (x < 0 || x > mySize.height - 1 || z < 0 || z > mySize.width - 1) {
    		return false;
    	}
    	
    	return true;
    }
    
    public void drawTerrain(GL2 gl) {
    	int width = mySize.width;
    	int height = mySize.height;
    	int x, z = 0;
    	
    	gl.glPushMatrix();
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[0].getTextureId());
    	
    	// Material properties.
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, matAmb,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, matDif,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, matSpec,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine,0);
    	
    	//Offset the polgyon back to allow the roads to show
    	gl.glEnable(GL2.GL_POLYGON_OFFSET_FILL);
    	gl.glPolygonOffset(1.0f, 1.0f);
    	
    	gl.glBegin(GL2.GL_TRIANGLES);
    		for (z = 0; z < height-1; z++) {
    			for (x = 0; x < width-1; x++) {
    				//First point (0, 0)
    				p0[0] = x;
    				p0[1] = myAltitude[x][z];
    				p0[2] = z;
    				
    				//Second point (0, 1)
    				p1[0] = x;
    				p1[1] = myAltitude[x][z+1];
    				p1[2] = z+1;
    				
    				//Third point (1, 0)
    				p2[0] = x+1;
    				p2[1] = myAltitude[x+1][z];
    				p2[2] = z;
    				
    				//Fourth point (1, 1)
    				p3[0] = x+1;
    				p3[1] = myAltitude[x+1][z+1];
    				p3[2] = z+1;
    				
    				//Setup the vectors to cross for normal
    				double[] v1 = {p1[0] - p0[0], p1[1] - p0[1], p1[2] - p0[2]};
    				double[] v2 = {p2[0] - p0[0], p2[1] - p0[1], p2[2] - p0[2]};
    				double[] v3 = {p1[0] - p3[0], p1[1] - p3[1], p1[2] - p3[2]};
    				double[] v4 = {p2[0] - p3[0], p2[1] - p3[1], p2[2] - p3[2]};
    				
    				double[] n1 = MathUtil.cross(v1, v2);
    				n1 = MathUtil.normalise(n1);
    				
    				double[] n2 = MathUtil.cross(v4, v3);
    				n2 = MathUtil.normalise(n2);
    				
    				//Average the two normals to use on the two joining vertices
    				/*double[] n3 = { n1[0] + n2[0], n1[1] + n2[1], n1[2] + n2[2] };
    				double[] smoothNormal = MathUtil.normalise(n3);*/
    						
    				gl.glNormal3dv(n1, 0);
    				//First triangle
    				gl.glTexCoord2d(0.5, 1);
    				gl.glVertex3d(x, myAltitude[x][z], z);
    				gl.glTexCoord2d(0, 0);
    				gl.glVertex3d(x, myAltitude[x][z+1], z+1);
    				gl.glTexCoord2d(1, 0);
    				gl.glVertex3d(x+1, myAltitude[x+1][z], z);
    				
    				gl.glNormal3dv(n2, 0);
    				//Second triangle
    				gl.glTexCoord2d(0, 0);
    				gl.glVertex3d(x+1, myAltitude[x+1][z], z);
    				gl.glTexCoord2d(1, 0);
    				gl.glVertex3d(x, myAltitude[x][z+1], z+1);
    				gl.glTexCoord2d(0.5, 1);
    				gl.glVertex3d(x+1, myAltitude[x+1][z+1], z+1);
    			}
    		}
    	gl.glEnd();
    	gl.glDisable(GL2.GL_POLYGON_OFFSET_FILL);
		
    	//Draw all trees
		for (Tree t : myTrees) {
			t.drawTree(gl);
		}
    	
		for (Road r : myRoads) {		
			r.drawRoad(gl, this);
		}
		
		for (Monster m: myMonsters) {
			m.draw(gl, myCamera.isTorch());
		}
		
		/*##########################################################
		 * DRAWING RAIN PARTICLES
		 *##########################################################*/
		
		if (this.isRaining) {
			//Material lighting for rain
	        float matAmb[] = {0.53f, 0.81f, 0.92f, 1.0f};
	        float matDif[] = {0.12f, 0.26f, 0.9f, 0.4f};
	    	float matSpec[] = {0.12f, 0.26f, 0.9f, 1.0f};
	    	float matShine[] = { 0.5f };
	    	
	    	gl.glPushMatrix();
	    	
	    	// Material properties.
	    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, matAmb,0);
	    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, matDif,0);
	    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, matSpec,0);
	    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine,0);
	    	
	    	gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[1].getTextureId());
			
			for (int i = 0; i < MAX_PARTICLES; i++) {
				if (particles[i].alive) {
					double px = particles[i].pos[0];
					double py = particles[i].pos[1];
					double pz = particles[i].pos[2];
					
					gl.glPushMatrix();
					gl.glTranslated(px, py, pz);
					gl.glRotated(Math.toDegrees(myCamera.getAngle())+90, 0, 1, 0);
					gl.glBegin(GL2.GL_QUADS);
						gl.glNormal3d(0, 0, 1);			            
			            
						if (demoBillboard) {
							gl.glTexCoord2d(1, 1);
				            gl.glVertex3d(0.5, 0.5, 0); // Top Right
				            gl.glTexCoord2d(0, 1);
				            gl.glVertex3d(0, 0.5, 0); // Left point
				            gl.glTexCoord2d(0, 0);
				            gl.glVertex3d(0, 0, 0); // Bottom point
				            gl.glTexCoord2d(1, 0);
				            gl.glVertex3d(0.5, 0, 0); // Right point
						} else {
							gl.glTexCoord2d(0.2, 1);
				            gl.glVertex3d(0, 0.1, 0); // Top Right
				            gl.glTexCoord2d(0, 1);
				            gl.glVertex3d(-0.003125, 0, 0); // Left point
				            gl.glTexCoord2d(0, 0);
				            gl.glVertex3d(0, -0.03125, 0); // Bottom point
				            gl.glTexCoord2d(0.2, 0);
				            gl.glVertex3d(0.003125, 0, 0); // Right point
						}
					gl.glEnd();
					gl.glPopMatrix();
				}
				
				//Once the particle has reached the ground, re-position it
				// and repeat.
				if (particles[i].pos[1] < 0 || particles[i].pos[1] < altitude(particles[i].pos[0], particles[i].pos[2])) {
					double randX = Math.random()*(mySize.getWidth()-1);
		    		double randZ = Math.random()*(mySize.getHeight()-1);
		    		
		    		particles[i].pos[0] = randX;
					particles[i].pos[1] = particles[i].start_pos;
					particles[i].pos[2] = randZ;
				} else {
					if (!rainPause) {
						//Move particles after drawing them
						double speed = Math.max(particles[i].speed + RainParticle.speedSetting, 0.001);
						particles[i].pos[1] -= speed;
					}
				}
			}
			
			
			if (showRainDrop) {
				gl.glBegin(GL2.GL_QUADS);
					gl.glNormal3d(0, 0, 1);
					
					gl.glTexCoord2d(1, 1);
					gl.glVertex3d(0.0625, 0.125, 0.0625); //Top point
					
					gl.glTexCoord2d(0, 1);
					gl.glVertex3d(0.075, 0.03125, 0.05); //Left point
					
					gl.glTexCoord2d(0, 0);
					gl.glVertex3d(0.0625, 0, 0.0625); //Bottom point
					
					gl.glTexCoord2d(1, 0);
					gl.glVertex3d(0.05, 0.03125, 0.075); //Right point
				gl.glEnd();
			}
			
			gl.glPopMatrix();
			/*##########################################################
			 * 
			 *##########################################################*/
			//gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
		}
		gl.glPopMatrix();
    }
    
    public void setCamera(Camera c ) {
    	this.myCamera = c;
    }
    
    public double getCameraAngle() {
    	return this.myCamera.getAngle();
    }
    
    public void toggleRain() {
    	this.isRaining = !this.isRaining;
    }
    
    public void toggleBillboard() {
    	this.isBillboard = !this.isBillboard;
    }
    
    public void toggleRainDrop() {
    	this.showRainDrop = !this.showRainDrop;
    }
    
    public void rainFast() {
    	RainParticle.speedSetting = Math.min(RainParticle.speedSetting + 0.1, 1);
    }
    
    public void rainSlow() {
    	RainParticle.speedSetting = Math.max(RainParticle.speedSetting - 0.1, -1);
    }
    
    public void toggleDemoBillboard() {
    	this.demoBillboard = !this.demoBillboard;
    }
    
    public void toggleRainPause() {
    	this.rainPause = !this.rainPause;
    }
}
