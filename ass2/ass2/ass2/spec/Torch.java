package ass2.spec;

import com.jogamp.opengl.GL2;

public class Torch {
	private float pos[];
	private float direction[];
	
	private float lightAmb[] = { 0.7f, 0.7f, 0.7f, 1 };
	private float lightDiff[] = { 1, 1, 1, 1};
	private float lightSpec[] = { 1, 1, 1, 1 };
	
	public Torch(double[] p, double[] d) {
		updatePos(p, d);
	}
	
	//updates the torch position
	public void updatePos(double[] p, double[] d) {
		pos = new float[] {(float)p[0], (float)p[1], (float)p[2], 1};
		direction = new float[] {(float)d[0], (float)d[1], (float)d[2], 1};
	}
	
	//activates the torch
	public void setTorch(GL2 gl) {
		gl.glPushMatrix();
		
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightAmb,0);
    	gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightDiff,0);
    	gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightSpec,0);
    	gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, pos, 0);
    	
		gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_CUTOFF, 30);
		gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_EXPONENT, 0.2f);
		
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_DIRECTION, direction, 0);
		
		gl.glPopMatrix();
	}
}

