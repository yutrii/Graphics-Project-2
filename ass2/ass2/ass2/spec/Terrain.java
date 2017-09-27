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

    private Dimension mySize;
    private double[][] myAltitude;
    private List<Tree> myTrees;
    private List<Road> myRoads;
    private float[] mySunlight;
    
    //Points as array for normal calculations
    double[] p0 = new double[3];
    double[] p1 = new double[3];
    double[] p2 = new double[3];
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
        mySunlight = new float[3];
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
        double altitude;
        
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
        	
        	double det = ((p4x - p2x)*(z - p2z) - (p4z - p2z)*(x - p2x));
        	
        	double p2y = getGridAltitude((int)p2x, (int)p2z);
        	double p4y = getGridAltitude((int)p4x, (int)p4z);
        	
        	if (det == 0) {
        		//it's on the diagonal line, easy
        		//use p1 and p2 altitude to interpolate
        		 //length of diagonal
        		// simplifly interpolation by finding difference in altitude
        		double height = p2y - p4y;
        		
        		if (height == 0) { //points are same altitude
        			altitude = getGridAltitude((int)p4x, (int)p4z);
        		} else {
        			double pos;
        			double dist = Math.sqrt(2);
        			
        			if (height > 0) { // p2 is higher than p1
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
    
    public void drawTerrain(GL2 gl) {
    	int width = mySize.width;
    	int height = mySize.height;
    	int x, z = 0;
    	
    	/*gl.glColor4d(0, 0, 0, 1);
    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);*/
    	
    	gl.glBegin(GL2.GL_TRIANGLES);
    		for (z = 0; z < height-1; z++) {
    			for (x = 0; x < width-1; x++) {
    				p0[0] = x;
    				p0[1] = myAltitude[x][z];
    				p0[2] = z;
    				
    				p1[0] = x;
    				p1[1] = myAltitude[x][z+1];
    				p1[2] = z+1;
    				
    				p2[0] = x+1;
    				p2[1] = myAltitude[x+1][z];
    				p2[2] = z;
    				
    				double[] normal1 = MathUtil.getNormalisedNormal(p0, p1, p2);
    				gl.glNormal3dv(normal1, 0);
    				gl.glVertex3d(x, myAltitude[x][z], z);
    				gl.glVertex3d(x, myAltitude[x][z+1], z+1);
    				gl.glVertex3d(x+1, myAltitude[x+1][z], z);
    				
    				p0[0] = x+1;
    				p0[1] = myAltitude[x+1][z+1];
    				p0[2] = z+1;
    				
    				double[] normal2 = MathUtil.getNormalisedNormal(p0, p1, p2);
    				gl.glNormal3dv(normal2, 0);
    				gl.glVertex3d(x+1, myAltitude[x+1][z], z);
    				gl.glVertex3d(x, myAltitude[x][z+1], z+1);
    				gl.glVertex3d(x+1, myAltitude[x+1][z+1], z+1);
    			}
    		}
    	gl.glEnd();
    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    	
    }
}
