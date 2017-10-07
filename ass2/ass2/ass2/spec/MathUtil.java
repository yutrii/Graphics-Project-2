package ass2.spec;


/**
 * A collection of useful math methods 
 *
 * TODO: The methods you need to complete are at the bottom of the class
 *
 * @author malcolmr
 */
public class MathUtil {

    /**
     * Normalise an angle to the range [-180, 180)
     * 
     * @param angle 
     * @return
     */
    static public double normaliseAngle(double angle) {
        return ((angle + 180.0) % 360.0 + 360.0) % 360.0 - 180.0;
    }

    /**
     * Clamp a value to the given range
     * 
     * @param value
     * @param min
     * @param max
     * @return
     */

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * Multiply two matrices
     * 
     * @param p A 3x3 matrix
     * @param q A 3x3 matrix
     * @return
     */
    public static double[][] multiply(double[][] p, double[][] q) {

        double[][] m = new double[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m[i][j] = 0;
                for (int k = 0; k < 3; k++) {
                   m[i][j] += p[i][k] * q[k][j]; 
                }
            }
        }

        return m;
    }

    /**
     * Multiply a vector by a matrix
     * 
     * @param m A 3x3 matrix
     * @param v A 3x1 vector
     * @return
     */
    public static double[] multiply(double[][] m, double[] v) {

        double[] u = new double[3];

        for (int i = 0; i < 3; i++) {
            u[i] = 0;
            for (int j = 0; j < 3; j++) {
                u[i] += m[i][j] * v[j];
            }
        }

        return u;
    }



    // ===========================================
    // COMPLETE THE METHODS BELOW
    // ===========================================
    

    /**
     * TODO: A 2D translation matrix for the given offset vector
     * 
     * @param pos
     * @return
     */
    public static double[][] translationMatrix(double[] v) {
    	double[][] matrix = new double[3][3];
    	matrix[0][0] = matrix[1][1] = matrix[2][2] = 1;
    	matrix[0][2] = v[0];
    	matrix[1][2] = v[1];
        return matrix;
    }

    /**
     * TODO: A 2D rotation matrix for the given angle
     * 
     * @param angle in degrees
     * @return
     */
    public static double[][] rotationMatrix(double angle) {
    	double[][] matrix = new double[3][3];
    	double radians = Math.toRadians(normaliseAngle(angle));
    	matrix[0][0] = matrix[1][1] = Math.cos(radians);
    	matrix[0][1] = Math.sin(radians)*-1;
    	matrix[1][0] = Math.sin(radians);
    	matrix[2][2] = 1;
        return matrix;
    }

    /**
     * TODO: A 2D scale matrix that scales both axes by the same factor
     * 
     * @param scale
     * @return
     */
    public static double[][] scaleMatrix(double scale) {
    	double[][] matrix = new double[3][3];
    	matrix[0][0] = matrix[1][1] = scale;
    	matrix[2][2] = 1;
        return matrix;
    }

    public static void printMatrix(double[][] matrix) {
    	System.out.println("[ " + matrix[0][0] + "  " +
    							  matrix[0][1] + "  " +
    							  matrix[0][2] + "  ");
    	System.out.println("  " + matrix[1][0] + "  " +
				  				  matrix[1][1] + "  " +
				  				  matrix[1][2] + "  ");
    	System.out.println("  " + matrix[2][0] + "  " +
				  				  matrix[2][1] + "  " +
				  				  matrix[2][2] + " ]");
    }
    
    
    /* 
     * Some maths utility functions
     * NORMAL CALCULATION FUNCTIONS
     */

    public static double getMagnitude(double [] n){
    	double mag = n[0]*n[0] + n[1]*n[1] + n[2]*n[2];
    	mag = Math.sqrt(mag);
    	return mag;
    }
    
    public static double [] normalise(double [] n){
    	double  mag = getMagnitude(n);
    	double norm[] = {n[0]/mag,n[1]/mag,n[2]/mag};
    	return norm;
    }
    
    public static double [] cross(double u [], double v[]){
    	double crossProduct[] = new double[3];
    	crossProduct[0] = u[1]*v[2] - u[2]*v[1];
    	crossProduct[1] = u[2]*v[0] - u[0]*v[2];
    	crossProduct[2] = u[0]*v[1] - u[1]*v[0];
    	//System.out.println("CP " + crossProduct[0] + " " +  crossProduct[1] + " " +  crossProduct[2]);
    	return crossProduct;
    }
    
    public static double [] getNormal(double[] p0, double[] p1, double[] p2){
    	double u[] = {p1[0] - p0[0], p1[1] - p0[1], p1[2] - p0[2]};
    	double v[] = {p2[0] - p0[0], p2[1] - p0[1], p2[2] - p0[2]};
    	
    	return cross(u,v);
    	
    }
    
    public static double [] getNormalisedNormal(double[] p0, double[] p1, double[] p2){
    	return normalise(getNormal(p0, p1, p2));	
    }
    
    public static int getCombination(int n, int k) {
    	if (k == 0 || k == n) {
    		return 1;
    	} else {
    		return factorial(n)/(factorial(k)*factorial(n-k));
    	}
    }
    
    public static int factorial(int f) {
    	if (f > 1) {
    		return f*factorial(f-1);
    	}
    	return f;
    }
    
}
