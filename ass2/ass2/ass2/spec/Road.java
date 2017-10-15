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
    private static final double t = 0.001;
    private static final double texInc = 0.005;
    
    //Texture variables
    private static MyTexture[] myTextures;
    private static String textureFileName1 = "ass2/ass2/textures/road2.jpg";
    private static String textureExt1 = "jpg";
    
    //Material lighting
    float matAmbAndDif2[] = {0.0f, 0.9f, 0.0f, 1.0f};
	float matSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	float matShine[] = { 120.0f };

	// Material property vectors.
	float matAmbAndDif1[] = {1.0f, 1.0f, 1.0f, 1.0f};
    
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
    	
    	// Material properties.
    	/*gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif1,0);
    	gl.glMaterialfv(GL2.GL_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif2,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, matSpec,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine,0);*/
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
    
    public void drawRoad(GL2 gl, Terrain terrain) {
    	gl.glPushMatrix();
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[0].getTextureId());
    	//Only handling flat surfaces for now
    	
    	
    	//Setup of initial cross section
    	/*double d = 0.5*myWidth;
    	System.out.println("Distance is: " + d);
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
		}*/
    	
    	double[][] tMatrix = new double[4][4];
    	
    	//Attempt to transform the cross section
    	//gl.glLineWidth(10.0f);
    	double texCoord = 0;
    	gl.glBegin(GL2.GL_QUAD_STRIP);
    		for (double increment = 0; increment < size(); increment += t) {
    			double[] point = point(increment);
	    		double spineX = point[0];
	    		double spineZ = point[1];
	    		double altitude = terrain.altitude(spineX, spineZ);
	    		
	    		//Add the new point to the transformation matrix
	    		tMatrix[0][3] = spineX;
	    		tMatrix[1][3] = altitude;
	    		tMatrix[2][3] = spineZ;
	    		tMatrix[3][3] = 1;
	    		
	    		//Add the new k vector to the transformation matrix
	    		double[] k = new double[3];
	    		double[] tmp = getTangent(increment);
	    		k[0] = tmp[0];
	    		k[1] = altitude;
	    		k[2] = tmp[1];
	    		k = MathUtil.normalise(k);
	    		
	    		//Point that is perpendicular to curve
	    		double[] i1 = new double[3];
	    		i1[0] = k[2];
	    		i1[1] = altitude;
	    		i1[2] = -k[0];
	    		
	    		//Adding the point onto the normal
	    		//Normalise it to the half the size of the width
	    		i1 = MathUtil.normalise(i1);
	    		i1[0] = 0.5*myWidth*i1[0];
	    		i1[2] = 0.5*myWidth*i1[2];
	    		
	    		i1[0] += spineX;
	    		i1[2] += spineZ;
	    		
	    		//Point that is on the other perpendicular side
	    		double[] i2 = new double[3];
	    		i2[0] = -k[2];
	    		i2[1] = altitude;
	    		i2[2] = k[0];
	    		
	    		//Adding the point onto the normal
	    		i2 = MathUtil.normalise(i2);
	    		i2[0] = 0.5*myWidth*i2[0];
	    		i2[2] = 0.5*myWidth*i2[2];
	    		
	    		i2[0] += spineX;
	    		i2[2] += spineZ;

	    		gl.glTexCoord2d(0, texCoord);
	    		gl.glVertex3d(i1[0], altitude, i1[2]);
	    		gl.glTexCoord2d(1, texCoord);
	    		gl.glVertex3d(i2[0], altitude, i2[2]);
	    		
	    		texCoord += texInc;
	    		
    		}
    		
    		//Last point is a special case
    		double lastX = myPoints.get(myPoints.size()-2);
    		double lastZ = myPoints.get(myPoints.size()-1);
    		/*double[] k = new double[3];
    		double[] tmp = getTangent(increment);
    		k[0] = tmp[0];
    		k[1] = 0;
    		k[2] = tmp[1];
    		k = MathUtil.normalise(k);
    		
    		//Add the new i vector to the transformation matrix
    		double[] i = new double[3];
    		i[0] = k[2];
    		i[1] = 0;
    		i[2] = -k[0];
    		
    		//Adding the point onto the normal
    		i = MathUtil.normalise(i);
    		i[0] = 0.5*myWidth*i[0];
    		i[2] = 0.5*myWidth*i[2];
    		
    		i[0] += spineX;
    		i[2] += spineZ;

    		gl.glVertex3d(i[0], 0, i[2]);
    		gl.glVertex3d(spineX, 0, spineZ);*/
    		
    	gl.glEnd();
    	
    	gl.glPopMatrix();
    }


}
