package ass2.spec;

import com.jogamp.opengl.GL2;

public class Torch {
	private float pos[];
	private float direction[];
	public Torch(double[] p, double[] d) {
		updatePos(p, d);
	}
	
	public void updatePos(double[] p, double[] d) {
		pos = new float[] {(float)p[0], (float)p[1], (float)p[2], 0};
		direction = new float[] {(float)d[0], (float)d[1], (float)d[2], 0};
	}
	
	public void setTorch(GL2 gl) {
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, pos, 0); 
		gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_CUTOFF, 45);
		gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_EXPONENT, 4);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_DIRECTION, direction, 0);
	}
	
	public void drawTorch(GL2 gl) {
		
	}
}

