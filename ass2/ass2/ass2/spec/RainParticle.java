package ass2.spec;

public class RainParticle {
	double[] pos = new double[3];
	double speed;
	boolean alive;
	static double speedSetting = 0.0;
	
	double start_pos = 20;
	
	//private double gradient;
	
	public RainParticle(Terrain t) {
		double randX = Math.random()*(t.size().getWidth()-1);
		double randZ = Math.random()*(t.size().getHeight()-1);
		pos[0] = randX;
		pos[1] = start_pos;
		pos[2] = randZ;
		speed = Math.random()*0.5 + 0.5;
		//speed = 0.005;
		alive = true;
		
	}
}
