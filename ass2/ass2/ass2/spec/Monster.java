package ass2.spec;

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

public class Monster {
	private double[] pos;
	private float[] positions;
	private float[] normals;
	//private float[] colours;
	
	private int[] bufferIDs;
	
	public Monster(double x, double y, double z) {
		pos = new double[] {x, y, z};
	}
	
	public void init(GL2 gl) {
		positions = new float[]{0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1,//top
				0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1,//bottom
				0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 0,//back
				0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0,//left
				1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0,//right
				0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1//front
		};
		
		normals = new float[] {0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0,
				0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0,
				0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1,
				-1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0,
				1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0,
				0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1
		};
		
		// ~~~~~~~~~~~ vertex and fragment shader stuff ~~~~~~~~~~~~~~~~~~~~~~~~`
		/*
		int vertShader = gl.glCreateShader(GL2.GL_VERTEX_SHADER);
		int fragShader = gl.glCreateShader(GL2.GL_FRAGMENT_SHADER);
		
		gl.glShaderSource(vertShader, vShaderSrc.length, vShaderSrc, vLengths, 0);
		gl.glCompileShader(vertShader);
		
		int[] compiled = new int[1];
		gl.glGetShaderiv(vertShader, GL2.GL_COMPILE_STATUS, compiled, 0);
		if (compiled[0] == 0) {
		// compilation error!
		}
		
		int shaderprogram = gl.glCreateProgram();
		gl.glAttachShader(shaderprogram, vertShader);
		gl.glAttachShader(shaderprogram, fragShader);
		gl.glLinkProgram(shaderprogram);
		gl.glValidateProgram(shaderprogram);
		
		String VERTEX_SHADER = “myV1.glsl”;
		String FRAGMENT_SHADER= “myF1.glsl”;
		int shaderprogram;
		shaderprogram = Shader.initShaders(gl,
		VERTEX_SHADER, FRAGMENT_SHADER);
		*/
		// ~~~~~~~~~~~~ end ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		//colours = new float[]{1,0,0, 0,1,0, 1,1,1, 0,0,0, 0,0,1, 1,1,0};
		FloatBuffer posData = Buffers.newDirectFloatBuffer(positions);
		FloatBuffer normalData = Buffers.newDirectFloatBuffer(normals);
		
		bufferIDs = new int[2];
		gl.glGenBuffers(2, bufferIDs,0);
		
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, bufferIDs[0]);
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, (positions.length + normals.length)*Float.BYTES, posData, GL2.GL_STATIC_DRAW);
		//gl.glBufferData(GL2.GL_ARRAY_BUFFER, positions.length*Float.BYTES + colours.length*Float.BYTES, null, GL2.GL_STATIC_DRAW);
		
		gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 0, positions.length*Float.BYTES, posData);
		gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, positions.length*Float.BYTES, normals.length*Float.BYTES, normalData);
	}
	
	public void draw(GL2 gl) {
		gl.glPushMatrix();
		gl.glTranslated(pos[0], pos[1], pos[2]);
		{
			//head
			gl.glPushMatrix();
			gl.glTranslated(0, 1, 0);
			//gl.glRotated(40, 0, 1, 0);
			gl.glScaled(0.25, 0.22, 0.25);
			
			
			drawCube(gl);
			
			gl.glPopMatrix();
			
			//body
			gl.glPushMatrix();
			
			gl.glTranslated(0, 0.6, 0.0625);
			//gl.glRotated(40, 0, 1, 0);
			gl.glScaled(0.25, 0.375, 0.125);
						
			drawCube(gl);
			
			gl.glPopMatrix();
			
			//left arm
			gl.glPushMatrix();
			
			gl.glTranslated(-0.0925, 0.28, 0.09375);
			gl.glRotated(-2.5, 0, 0, 1);
			gl.glScaled(0.0625, 0.7, 0.0625);
						
			drawCube(gl);
			
			gl.glPopMatrix();
			
			//right arm
			gl.glPushMatrix();
			
			gl.glTranslated(0.28, 0.28, 0.09375);
			gl.glRotated(2.5, 0, 0, 1);
			gl.glScaled(0.0625, 0.7, 0.0625);
						
			drawCube(gl);
			
			gl.glPopMatrix();
			
			//left leg
			gl.glPushMatrix();
			
			gl.glTranslated(0.03125, 0, 0.09375);
			//gl.glRotated(2.5, 0, 0, 1);
			gl.glScaled(0.0625, 0.6, 0.0625);
						
			drawCube(gl);
			
			gl.glPopMatrix();
			
			//right leg
			gl.glPushMatrix();
			
			gl.glTranslated(0.15625, 0, 0.09375);
			//gl.glRotated(2.5, 0, 0, 1);
			gl.glScaled(0.0625, 0.6, 0.0625);
						
			drawCube(gl);
			
			gl.glPopMatrix();
		}
		gl.glPopMatrix();
	}
	
	private void drawCube(GL2 gl) {
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, bufferIDs[0]);
		
		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
		
		gl.glVertexPointer(3,GL.GL_FLOAT,0, 0);
		gl.glNormalPointer(GL.GL_FLOAT,0, normals.length*Float.BYTES );
		
		gl.glDrawArrays(GL2.GL_QUADS,0,24);
		
		gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
		
		
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
	}
}
