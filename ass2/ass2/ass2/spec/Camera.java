package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class Camera {
	private Terrain terrain;
	private Avatar aang;
	private Torch torch;
	private double aspectRatio;
	private boolean isTorch;
	private Mode mode;
	private double[] pos;
	private double[] forward; //starting position facing 315deg
	private double angle = 5.5; //starting position 315 degrees in radians
	
	//Moving sun settings
	private boolean isSunMove = false;
	private float[] sunPos = { (float) Math.cos(0), (float) Math.sin(0), 0, 0};
	private double sunTime = 0;
	private double sunIncrement = 0.0087;
	
	// Light property vectors.
	float lightAmb[] = { 0.99f, 0.36f, 0.21f, 1.0f };
	float lightDifAndSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	float globAmb[] = { 1f, 1f, 1f, 1.0f };
	
	// Light property vectors.
	float sunLight[] = { 0.2f, 0.2f, 0.2f, 1.0f };
	
	public static enum Mode {
		FIRST_PERSON,
		THIRD_PERSON,
		FREE_VIEW
	}
	
	public Camera(Terrain t, Avatar a) {
		pos = new double[] {0, 0.25, 0};
		forward = new double[] {0.707, 0, 0.707};
		terrain = t;
		aang = a;
		aspectRatio = 1;
		mode = Mode.FREE_VIEW;
		torch = new Torch(pos, forward);
		isTorch = false;
	}
	
	//updates the camera based on new movements or rotations
	public void updateCamera(GL2 gl) {
		GLU glu = new GLU();
		
		if (isTorch) { //if the torch is on (i.e. night mode)
			//disable sunlight and activate the torch
			gl.glDisable(GL2.GL_LIGHT0);
			gl.glEnable(GL2.GL_LIGHT1);
			gl.glDisable(GL2.GL_LIGHT2);
			gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
			torch.updatePos(pos, forward);
			torch.setTorch(gl);
		} else { //otherwise re-enable sunlight
			gl.glClearColor(0.5f, 0.5f, 1f, 1);
			gl.glDisable(GL2.GL_LIGHT1);
			gl.glEnable(GL2.GL_LIGHT0);
		}
		
		if (mode != Mode.FREE_VIEW) {
			//use terrain altitude if within bounds, otherwise float 0.5 above sea level (0 y ordinate)
			if (terrain.withinRange(pos[0], pos[2])) {
				pos[1] = terrain.altitude(pos[0], pos[2]) + 0.25;
			} else {
				pos[1] = 0.25;
			}
		}
		
		gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        
        //regular camera in first person, follow avatar in third person
        if (mode == Mode.FIRST_PERSON || mode == Mode.FREE_VIEW) {
        	glu.gluLookAt(pos[0], pos[1], pos[2], pos[0] + forward[0], pos[1], pos[2] + forward[2], 0, 1, 0);
        } else if (mode == Mode.THIRD_PERSON) {
        	glu.gluLookAt(pos[0] - forward[0], pos[1], pos[2] - forward[2], pos[0], pos[1], pos[2], 0, 1, 0);
        }
        
        /*############### SUNLIGHT POSITIONING AND MOVEMENT ###############*/
        if (!isSunMove) {
        	if (!isTorch) { gl.glEnable(GL2.GL_LIGHT0); } 
        	gl.glDisable(GL2.GL_LIGHT2);
        	float[] lightPos = new float[4];
            float[] sun = terrain.getSunlight();
            lightPos[0] = sun[0];
            lightPos[1] = sun[1];
            lightPos[2] = sun[2];
            lightPos[3] = 0;
            gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);
            
            // Light property vectors.
        	float lightAmb[] = { 1.0f, 1.0f, 1.0f, 1.0f };
        	float lightDifAndSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f };

        	// Light properties.
        	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb,0);
        	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDifAndSpec,0);
        	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightDifAndSpec,0);
        } else {
        	gl.glDisable(GL2.GL_LIGHT0);
        	gl.glEnable(GL2.GL_LIGHT2);
        	//Set the sunlight
        	gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_AMBIENT, sunLight,0);
        	gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_DIFFUSE, sunLight,0);
        	gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPECULAR, sunLight,0);
        	
        	//Set the sky light
        	gl.glClearColor(lightAmb[0], lightAmb[1], lightAmb[2], 1);
        	
        	//Position the sun
        	gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_POSITION, sunPos, 0);
        	
        	sunTime += sunIncrement;
        	sunPos[0] = (float) Math.cos(sunTime);
        	sunPos[1] = (float) Math.sin(sunTime);
        	
        	if (sunTime < 1.57) { //SUNRISE
        		lightAmb[0] /*= lightDifAndSpec[0]*/ -= 0.0027;
        		lightAmb[1] /*= lightDifAndSpec[1]*/ += 0.00077;
        		lightAmb[2] /*= lightDifAndSpec[2]*/ += 0.0044;
        		
        		sunLight[0] = sunLight[1] = sunLight[2] += 0.0044;
        	} else if (sunTime >= 1.57 && sunTime < 3.14) { //MIDDAY
        		lightAmb[0] /*= lightDifAndSpec[0]*/ += 0.0027;
        		lightAmb[1] /*= lightDifAndSpec[1]*/ -= 0.00077;
        		lightAmb[2] /*= lightDifAndSpec[2]*/ -= 0.0044;
        		
        		sunLight[0] = sunLight[1] = sunLight[2] -= 0.0044;
        	} else if (sunTime >= 3.14 && sunTime < 4.71) { //SUNSET
        		lightAmb[0] /*= lightDifAndSpec[0]*/ -= 0.0055;
        		lightAmb[1] /*= lightDifAndSpec[1]*/ -= 0.002;
        		lightAmb[2] /*= lightDifAndSpec[2]*/ -= 0.0012;
        		
        		sunLight[0] = sunLight[1] = sunLight[2] -= 0.0011;
        	} else if (sunTime >= 4.71 && sunTime < 6.28) { //MIDNIGHT
        		lightAmb[0] /*= lightDifAndSpec[0]*/ += 0.0055;
        		lightAmb[1] /*= lightDifAndSpec[1]*/ += 0.002;
        		lightAmb[2] /*= lightDifAndSpec[2]*/ += 0.0012;
        		
        		sunLight[0] = sunLight[1] = sunLight[2] += 0.0011;
        	} else if (sunTime >= 6.28) { //SUNRISE
        		//RESET THE SUN
        		resetDay();
        	}
        }
        
        /*#################################################################*/
        
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
        //90 degree FOV in free mode, otherwise 60 degrees
        if (mode == Mode.FREE_VIEW) {
        	glu.gluPerspective(90, aspectRatio, 0.1f, 100f);
        } else {
        	glu.gluPerspective(60, aspectRatio, 0.1f, 100f);
        }

        gl.glMatrixMode(GL2.GL_MODELVIEW);
	}
	
	public void resetDay() {
		sunTime = 0;
		lightAmb[0] = lightDifAndSpec[0] = 0.99f;
		lightAmb[1] = lightDifAndSpec[1] = 0.36f;
		lightAmb[2] = lightDifAndSpec[2] = 0.21f;
		sunPos[0] = (float) Math.cos(Math.PI*0);
		sunPos[1] = (float) Math.sin(Math.PI*0);
		sunPos[2] = 0;
		sunLight[0] = sunLight[1] = sunLight[2] = 0.2f;
	}
	
	public void toggleTorch() {
		isTorch = !isTorch;
	}
	
	public void toggleSunMove() {
		isSunMove = !isSunMove;
	}
	
	//moves camera forward or back
	public void move(double l) {
		if (mode == Mode.FREE_VIEW || aang.canMove(l)) {
			pos[0] += l * forward[0];
			pos[2] += l * forward[2];
		}
	}
	
	//moves camera side to side (not rotate)
	public void strafe(double l) {
		if (mode == Mode.FREE_VIEW || aang.canStrafe(l)) {
			pos[0] += l * forward[2];
			pos[2] -= l * forward[0];
		}
	}
	
	//moves camera up and down (free view mode only)
	public void fly(double l) {
		if (mode == Mode.FREE_VIEW) {
			pos[1] += l;
		}
	}
	
	//rotates camera left and right (along y axis)
	public void rotate(double rad) {
		angle += rad;

		forward[0] = Math.cos(angle);
		forward[2] = -Math.sin(angle);
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
	
	public void setAspectRatio(double ar) {
		aspectRatio = ar;
	}
	
	public boolean isTorch() {
		return isTorch;
	}
	
	//switches the mode between first and third person and free view
	public void changeMode() {
		if (mode == Mode.FIRST_PERSON) {
			mode = Mode.FREE_VIEW;
		} else if (mode == Mode.THIRD_PERSON) {
			moveWithinMap();
			mode = Mode.FIRST_PERSON;
		} else {
			moveWithinMap();
			mode = Mode.THIRD_PERSON;
		}
	}
	
	//used to bring the camera back to within bounds of the map
	//if it had been moved beyond in free view mode
	private void moveWithinMap() {
		if (pos[0] < 0) pos[0] = 0;
		if (pos[2] < 0) pos[2] = 0;
		if (pos[0] >= terrain.size().width) pos[0] = terrain.size().width-1;
		if (pos[2] >= terrain.size().height) pos[2] = terrain.size().height-1;
		pos[1] = terrain.altitude(pos[0], pos[2]) +  0.25;
	}
}
