package ass2.spec;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

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
        	//call them p1 and p2
        	
        	double p1x = Math.ceil(x);
        	double p1z = Math.floor(z);
        	double p2x = Math.floor(x);
        	double p2z = Math.ceil(z);
        	double p3x;
        	double p3z;
        	
        	double det = ((p2x - p1x)*(z - p1z) - (p2z - p1z)*(x - p1x));
        	
        	if (det == 0) {
        		//it's on the diagonal line, easy
        		//use p1 and p2 altitude to interpolate
        		 //length of diagonal
        		// simplifly interpolation by finding difference in altitude
        		double height = (double)(getGridAltitude(p2x, p2z) - getGridAltitude(p1x, p1z));
        		
        		if (height == 0) { //points are same altitude
        			altitude = getGridAltitude((int)p2x, (int)p2z);
        		} else {
        			double pos;
        			double dist = Math.sqrt(2);
        			
        			if (height > 0) { // p2 is higher than p1
        			//distance of unknown point along line
        				pos = Math.sqrt((p1x-x)*(p1x-x) + (z-p1z)*(z-p1z));
        			} else { //p1 is higher than p2
        				height = Math.abs(height);
        				pos = Math.sqrt((x-p2x)*(x-p2x) + (p2z-z)*(p2z-z));
        			}
        			altitude = pos * height / dist;
        		}
        	} else {
        		altitude = 0;
        		if (det > 0) {
        			//it's in upper trangle (i think)
        			p3x = Math.floor(x);
        			p3z = Math.floor(z);
        		} else {
        			//it's in lower (i think)
        			p3x = Math.ceil(x);
        			p3z = Math.ceil(z);
        		}
        		
        		//TODO: interpolate in triangle plane
        	}
        			
        	
        	
        	/*
        	//pretend these are the three triangle points once determined
        	double[] t1 = new double {1,2 3};
        	double[] t2 = new double {1, 2, 3};
        	double[] t3 = new double {1, 2, 3};
        	
        	[ t1x t1z t1a ]   [ x ]
        	[ t2x t2z t2a ] = [ z ]
        	[ t3x t3z t3a ]   [ a ]
        	*/
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


}
