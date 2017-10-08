package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class Camera {
	private Terrain terrain;
	
	private double[] pos = new double[]{0, 0.5, 0};
	private double[] forward = new double[] {0.707, 0, 0.707}; //starting position facing 315deg
	private double angle = 5.5; //starting position 315 degrees in radians
	
	public Camera(Terrain t) {
		terrain = t;
	}
		
	public void updateCamera(GL2 gl) {
		
		
		GLU glu = new GLU();
		
		pos[1] = terrain.altitude(pos[0], pos[2]) + 0.5;
		
		gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
		glu.gluLookAt(pos[0], pos[1], pos[2], pos[0] + forward[0], pos[1], pos[2] + forward[2], 0, 1, 0);
        
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
        glu.gluPerspective(90, 1, 0.1f, 100f);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
	}
	
	public void move(double l) {
		pos[0] += l * forward[0];
		pos[2] += l * forward[2];
	}
	
	public void rotate(double deg) {
		angle += deg;
		forward[0] = Math.cos(angle);
		forward[2] = -Math.sin(angle);
	}
	
	
}
