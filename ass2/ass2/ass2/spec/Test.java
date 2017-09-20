package ass2.spec;

public class Test {
	public static void main(String args[]) {
		double[] p1 = {1,1};
		double[] p2 = {0,0};
		double[] p3 = {1,0};
		double[] p4 = {0,0};
		
		double[] point = {0.5, Math.sqrt(2)*0.5};
		
		//double det = ((p2x - p1x)*(z - p1z) - (p2z - p1z)*(x - p1x));
		double det = ( (p2[0] - p1[0])*(point[1] - p1[1]) - (p2[1] - p1[1])*(point[0] - p1[0]) );
		
		System.out.println("Det is :" + det);
	}
}
