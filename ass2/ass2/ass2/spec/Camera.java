package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class Camera {
	private Terrain terrain;
	private Avatar aang;
	public static enum Mode {
		FIRST_PERSON,
		THIRD_PERSON
	}
	
	private Mode mode;
	private double[] pos;
	private double[] forward; //starting position facing 315deg
	private double angle = 5.5; //starting position 315 degrees in radians
	
	public Camera(Terrain t, Avatar a) {
		pos = new double[] {0, 0.5, 0};
		forward = new double[] {1, 0, 1};
		terrain = t;
		aang = a;
		mode = Mode.FIRST_PERSON;
	}
		
	public void updateCamera(GL2 gl) {
		
		
		GLU glu = new GLU();
		
		if (terrain.withinRange(pos[0], pos[2])) {
			pos[1] = terrain.altitude(pos[0], pos[2]) + 0.5;
		} else {
			pos[1] = 0.5;
		}
		
		gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        if (mode == Mode.FIRST_PERSON) {
        	glu.gluLookAt(pos[0], pos[1], pos[2], pos[0] + forward[0], pos[1], pos[2] + forward[2], 0, 1, 0);
        } else if (mode == Mode.THIRD_PERSON) {
        	glu.gluLookAt(pos[0] - forward[0], pos[1], pos[2] - forward[2], pos[0], pos[1], pos[2], 0, 1, 0);
        }
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
        glu.gluPerspective(90, 1, 0.1f, 100f);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
	}
	
	public void move(double l) {
		if (mode == Mode.FIRST_PERSON || aang.canMove(l)) {
			pos[0] += l * forward[0];
			pos[2] += l * forward[2];
		}
	}
	
	public void rotate(double rad) {
		angle += rad;
		//if (mode == Mode.THIRD_PERSON) {
			
		//} else if (mode == Mode.FIRST_PERSON) {
			forward[0] = Math.cos(angle);
			forward[2] = -Math.sin(angle);
		//}
	}
	
	public Mode getMode() {
		return mode;
	}
	
	public double[] getPos () {
		return pos;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public void changeMode() {
		if (mode == Mode.FIRST_PERSON) {
			mode = Mode.THIRD_PERSON;
		} else if (mode == Mode.THIRD_PERSON) {
			mode = Mode.FIRST_PERSON;
		}
	}
}