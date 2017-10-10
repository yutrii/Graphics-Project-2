package ass2.spec;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

/**
 * COMMENT: Comment Road 
 *
 * @author malcolmr
 */
public class Road {

    private List<Double> myPoints;
    private double myWidth;
    private static final double t = 0.1;
    
    //Texture variables
    private static MyTexture[] myTextures;
    private static String textureFileName1 = "ass2/ass2/textures/road.jpg";
    private static String textureExt1 = "jpg";
    
    
    /** 
     * Create a new road starting at the specified point
     */
    public Road(double width, double x0, double y0) {
        myWidth = width;
        myPoints = new ArrayList<Double>();
        myPoints.add(x0);
        myPoints.add(y0);
    }

    /**
     * Create a new road with the specified spine 
     *
     * @param width
     * @param spine
     */
    public Road(double width, double[] spine) {
        myWidth = width;
        myPoints = new ArrayList<Double>();
        for (int i = 0; i < spine.length; i++) {
            myPoints.add(spine[i]);
        }
    }
    
    public void init(GL2 gl) {
    	// Create texture ids. 
    	myTextures = new MyTexture[1];
    	myTextures[0] = new MyTexture(gl, textureFileName1, textureExt1, true);
    }

    /**
     * The width of the road.
     * 
     * @return
     */
    public double width() {
        return myWidth;
    }

    /**
     * Add a new segment of road, beginning at the last point added and ending at (x3, y3).
     * (x1, z1) and (x2, z2) are interpolated as bezier control points.
     * 
     * @param x1
     * @param z1
     * @param x2
     * @param z2
     * @param x3
     * @param y3
     */
    public void addSegment(double x1, double z1, double x2, double z2, double x3, double y3) {
        myPoints.add(x1);
        myPoints.add(z1);
        myPoints.add(x2);
        myPoints.add(z2);
        myPoints.add(x3);
        myPoints.add(y3);        
    }
    
    /**
     * Get the number of segments in the curve
     * A segment comprises of 3 control points to make 1 bezier curve
     * @return
     */
    public int size() {
        return myPoints.size() / 6;
    }

    /**
     * Get the specified control point.
     * 
     * @param i
     * @return
     */
    public double[] controlPoint(int i) {
        double[] p = new double[2];
        p[0] = myPoints.get(i*2);
        p[1] = myPoints.get(i*2+1);
        return p;
    }
    
    /**
     * Get a point on the spine. The parameter t may vary from 0 to size().
     * Points on the kth segment take have parameters in the range (k, k+1).
     * 
     * @param t
     * @return
     */
    public double[] point(double t) {
        int i = (int)Math.floor(t);
        t = t - i;
        
        i *= 6;
        
        double x0 = myPoints.get(i++);
        double y0 = myPoints.get(i++);
        double x1 = myPoints.get(i++);
        double z1 = myPoints.get(i++);
        double x2 = myPoints.get(i++);
        double z2 = myPoints.get(i++);
        double x3 = myPoints.get(i++);
        double y3 = myPoints.get(i++);
        
        
        double[] p = new double[2];

        p[0] = b(0, t) * x0 + b(1, t) * x1 + b(2, t) * x2 + b(3, t) * x3;
        p[1] = b(0, t) * y0 + b(1, t) * z1 + b(2, t) * z2 + b(3, t) * y3;        
        
        return p;
    }
    
    /**
     * Calculate the Bezier coefficients
     * 
     * @param i
     * @param t
     * @return
     */
    private double b(int i, double t) {
        
        switch(i) {
        
        case 0:
            return (1-t) * (1-t) * (1-t);

        case 1:
            return 3 * (1-t) * (1-t) * t;
            
        case 2:
            return 3 * (1-t) * t * t;

        case 3:
            return t * t * t;
        }
        
        // this should never happen
        throw new IllegalArgumentException("" + i);
    }
    
    //Same as getting the normal point but at a tangent
    public double[] getTangent(double t) {
    	int i = (int)Math.floor(t);
        t = t - i;
        
        i *= 6;
        
        double x0 = myPoints.get(i++);
        double z0 = myPoints.get(i++);
        double x1 = myPoints.get(i++);
        double z1 = myPoints.get(i++);
        double x2 = myPoints.get(i++);
        double z2 = myPoints.get(i++);
        double x3 = myPoints.get(i++);
        double z3 = myPoints.get(i++);
        
        
        double[] p = new double[2];

        /*p[0] = t(0, t) * x0 + t(1, t) * x1 + t(2, t) * x2 + t(3, t) * x3;
        p[1] = t(0, t) * y0 + t(1, t) * z1 + t(2, t) * z2 + t(3, t) * y3; */       
        
        p[0] = t(0, t) * (x1 - x0) + t(1, t) * (x2 - x1) + t(2, t) * (x3 - x2);
        p[1] = t(0, t) * (z1 - z0) + t(1, t) * (z2 - z1) + t(2, t) * (z3 - z2);
        
        return p;
    }
    
    
    //Returns the coefficients for the tangent at the point t
    private double t(int i, double t) {
        //Differentiate the curve for tangent
        switch(i) {
        
        case 0:
            return 3*(1 - t)*(1 - t);

        case 1:
            return 6*t*(1-t);
            
        case 2:
            return 3*t*t;
        }
        
        // this should never happen
        throw new IllegalArgumentException("" + i);
    }
    
    public void drawRoad(GL2 gl, double[][] altitudes) {
    	gl.glPushMatrix();
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[0].getTextureId());
    	//Only handling flat surfaces for now
    	double altitude = altitudes[0][0];
    	
    	//Setup of initial cross section
    	double d = 0.5*myWidth;
    	double[] newPoint1 = new double[4];
    	double[] newPoint2 = new double[4];
    	newPoint1[3] = 1;
    	newPoint2[3] = 1;
    	
    	//Starting point
    	double x1 = newPoint1[0] = newPoint2[0] = myPoints.get(0);//point(0)[0];
    	double z1 = newPoint1[2] = newPoint2[2] = myPoints.get(1);//point(0)[1];
	    	
		double x2 = point(t)[0];
    	double z2 = point(t)[1];

		if (z2 - z1 == 0) { //Means the curve starts horizontally
			newPoint1[2] += d;
			newPoint2[2] -= d;
		} else if (x2 - x1 == 0) {//Means the curve starts vertically
			newPoint1[0] += d;
			newPoint2[0] -= d;
		} else {
			double gradient = (z2 - z1)/(x2 - x1); //z2-z1 / x2-x1
	    	double rightAngle = -1/gradient;
	    	
	    	//Calculating initial cross-section points that are
	    	// perpendicular to the initial points on the curve.
			newPoint1[0] = x1 + d*(1/(Math.sqrt(1+Math.pow(rightAngle, 2))));
			newPoint1[2] = z1 + d*(rightAngle/(Math.sqrt(1+Math.pow(rightAngle, 2))));
			
			newPoint2[0] = x1 - d*(1/(Math.sqrt(1+Math.pow(rightAngle, 2))));
			newPoint2[2] = z1 - d*(rightAngle/(Math.sqrt(1+Math.pow(rightAngle, 2))));
		}
    	
    	double[][] tMatrix = new double[4][4];
    	
    	//Attempt to transform the cross section
    	gl.glLineWidth(10.0f);
    	gl.glBegin(GL2.GL_LINES);
    		gl.glVertex3d(newPoint1[0], altitude, newPoint1[2]);
    		gl.glVertex3d(newPoint2[0], altitude, newPoint2[2]);
    		
    		for (double increment = t; increment < size(); increment += t) {
    			double[] point = point(increment);
	    		double spineX = point[0];
	    		double spineZ = point[1];
	    		
	    		//Add the new point to the transformation matrix
	    		tMatrix[0][3] = spineX;
	    		tMatrix[1][3] = 0;
	    		tMatrix[2][3] = spineZ;
	    		tMatrix[3][3] = 1;
	    		
	    		//Add the new k vector to the transformation matrix
	    		double[] k = new double[3];
	    		double[] tmp = getTangent(increment);
	    		k[0] = tmp[0];
	    		k[1] = 0;
	    		k[2] = tmp[1];
	    		k = MathUtil.normalise(k);
	    		tMatrix[0][2] = k[0];
	    		tMatrix[1][2] = k[1];
	    		tMatrix[2][2] = k[2];
	    		tMatrix[3][2] = 0;
	    		
	    		//Add the new i vector to the transformation matrix
	    		double[] i = new double[3];
	    		i[0] = -k[1];
	    		i[1] = 0;
	    		i[2] = k[0];
	    		
	    		
	    		i[0] += spineX;
	    		i[2] += spineZ;
	    		i = MathUtil.normalise(i);
	    		
	    		gl.glVertex3d(i[0], 0, i[2]);
	    		gl.glVertex3d(spineX, 0, spineZ);
	    		/*//i = MathUtil.normalise(i);
	    		tMatrix[0][0] = i[0];
	    		tMatrix[1][0] = i[1];
	    		tMatrix[2][0] = 0;
	    		tMatrix[3][0] = 0;
	    		
	    		//Add the new j vector to the transformation matrix
	    		double[] j = MathUtil.cross(k, i);
	    		tMatrix[0][1] = j[0];
	    		tMatrix[1][1] = j[1];
	    		tMatrix[2][1] = j[2];
	    		tMatrix[3][1] = 0;*/
	    		
	    		/*newPoint1 = MathUtil.multiply(tMatrix, newPoint1);
	    		newPoint2 = MathUtil.multiply(tMatrix, newPoint2);
	    		
	    		gl.glVertex3d(newPoint1[0], altitude, newPoint1[2]);
	    		gl.glVertex3d(newPoint2[0], altitude, newPoint2[2]);*/
	    		
	    		/*double[] tPoint1 = MathUtil.multiply(tMatrix, newPoint1);
	    		double[] tPoint2 = MathUtil.multiply(tMatrix, newPoint2);
	    		gl.glVertex3d(tPoint1[0], altitude, tPoint1[2]);
	    		gl.glVertex3d(tPoint2[0], altitude, tPoint2[2]);*/
    		}
    	gl.glEnd();
    	
    	
    	gl.glBegin(GL2.GL_LINE_STRIP);
			for (double i = 0 ; i < size(); i += t) {
	    			double x_ = point(i)[0];
	    			double z_ = point(i)[1];
	    			gl.glVertex3d(x_, altitude, z_);
	    		
	    	}
			gl.glVertex3d(myPoints.get(myPoints.size()-2), 0, myPoints.get(myPoints.size()-1));
		gl.glEnd();
    	
    	gl.glPopMatrix();
    }


}
