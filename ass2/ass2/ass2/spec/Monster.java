package ass2.spec;

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

public class Monster {
	private double[] pos; //position of monster
	private double angle; //angle that the monster is facing
	private float[] positions; //VBO vertex data
	private float[] normals; //VBO normal data
	private float[] texCoords; //VBO texture data
	private int shader1; //shader ID

	//Texture variables
    private static MyTexture[] myTextures;
    private static String textureFileName1 = "ass2/ass2/textures/monster1.jpg";
    private static String textureExt1 = "jpg";
    private static String textureFileName2 = "ass2/ass2/textures/monster2.jpg";
    private static String textureExt2 = "jpg";
	
    //material variables
    float matAmb[] = {0.25f, 0.25f, 0.25f, 1.0f};
    float matDif[] = {0.2f, 0.2f, 0.2f, 1.0f};
	float matSpec[] = { 0.0f, 0.0f, 0.0f, 1.0f };
	float matShine[] = { 50.0f };
    
	private int[] bufferIDs;
	
	public Monster(double x, double y, double z) {
		pos = new double[] {x, y, z};
		angle = Math.random() * 360;
	}
	
	public void init(GL2 gl) {
		positions = new float[]{-1, 1, 1, 1, 1, 1, 1, -1, 1, -1, -1, 1,//front
				1, -1, 1, 1, -1, -1, 1, 1, -1, 1, 1, 1,//right
				-1, -1, -1, -1, 1, -1, 1, 1, -1, 1, -1, -1,//back
				-1, -1, 1, -1, 1, 1, -1, 1, -1, -1, -1, -1,//left
				-1, 1, 1, 1, 1, 1, 1, 1, -1, -1, 1, -1,//top
				-1, -1, -1, 1, -1, -1, 1, -1, 1, -1, -1, 1//bottom
		};
		
		normals = new float[]{0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1,
				1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0,
				0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1,
				-1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0,
				0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0,
				0, -1, 0, 0, -1, 0,	0, -1, 0, 0, -1, 0
		};
		
		texCoords = new float[] {
			1, 1, 0, 1, 0, 0, 1, 0,
			1, 1, 0, 1, 0, 0, 1, 0,
			1, 1, 0, 1, 0, 0, 1, 0,
			1, 1, 0, 1, 0, 0, 1, 0,
			1, 1, 0, 1, 0, 0, 1, 0,
			1, 1, 0, 1, 0, 0, 1, 0,
		};
		
		//Shader.java stuff
		String VERTEX_SHADER = "vertexShader.glsl";
		String FRAGMENT_SHADER= "fragmentShader.glsl";
		try {
			shader1 = Shader.initShaders(gl, VERTEX_SHADER, FRAGMENT_SHADER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//set up textures
		myTextures = new MyTexture[2];
    	myTextures[0] = new MyTexture(gl, textureFileName1, textureExt1, true);
    	myTextures[1] = new MyTexture(gl, textureFileName2, textureExt2, true);
		
    	//set data buffers
		FloatBuffer posData = Buffers.newDirectFloatBuffer(positions);
		FloatBuffer normalData = Buffers.newDirectFloatBuffer(normals);
		FloatBuffer texData = Buffers.newDirectFloatBuffer(texCoords);
		
		//load data buffers
		bufferIDs = new int[1];
		gl.glGenBuffers(1, bufferIDs,0);
		
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, bufferIDs[0]);
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, (positions.length + normals.length + texCoords.length)*Float.BYTES, posData, GL2.GL_STATIC_DRAW);
		
		gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 0, positions.length*Float.BYTES, posData);
		gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, positions.length*Float.BYTES, normals.length*Float.BYTES, normalData);
		gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, (positions.length + normals.length) * Float.BYTES, texCoords.length * Float.BYTES, texData);
	}
	
	public void draw(GL2 gl, boolean isTorch) {
		gl.glPushMatrix();
		gl.glTranslated(pos[0], pos[1], pos[2]);
		gl.glRotated(angle, 0, 1, 0);
		gl.glScaled(0.5, 0.5, 0.5);
		{
			//if torch/night mode is on, use immediate mode (shader cannot handle moving light)
			if (isTorch) {
				gl.glEnable(GL2.GL_TEXTURE_2D);
			} else {
				gl.glUseProgram(shader1);
			}
			
			//head
			gl.glPushMatrix();
			gl.glTranslated(0, 2, 0);
			gl.glScaled(0.25, 0.22, 0.25);
			
			drawCube(gl, true);
			
			gl.glPopMatrix();
			
			//body
			gl.glPushMatrix();
			
			gl.glTranslated(0, 1.375, 0);
			gl.glScaled(0.25, 0.375, 0.125);
						
			drawCube(gl, false);
			
			gl.glPopMatrix();
			
			//left arm
			gl.glPushMatrix();
			
			gl.glTranslated(-0.35, 1.05, 0);
			gl.glRotated(-2.5, 0, 0, 1);
			gl.glScaled(0.0625, 0.7, 0.0625);
						
			drawCube(gl, false);
			
			gl.glPopMatrix();
			
			//right arm
			gl.glPushMatrix();
			
			gl.glTranslated(0.35, 1.05, 0);
			gl.glRotated(2.5, 0, 0, 1);
			gl.glScaled(0.0625, 0.7, 0.0625);
						
			drawCube(gl, false);
			
			gl.glPopMatrix();
			
			//left leg
			gl.glPushMatrix();
			
			gl.glTranslated(-0.1, 0.5, 0);
			gl.glScaled(0.0625, 0.5, 0.0625);
						
			drawCube(gl, false);
			
			gl.glPopMatrix();
			
			//right leg
			gl.glPushMatrix();
			
			gl.glTranslated(0.1, 0.5, 0);
			gl.glScaled(0.0625, 0.5, 0.0625);
						
			drawCube(gl, false);
			
			gl.glPopMatrix();
			
		}
		gl.glUseProgram(0);
		gl.glPopMatrix();
	}
	
	//render the cube with the VBO
	private void drawCube(GL2 gl, boolean isHead) {
		int start = 0;
		
		//set up material properties
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, matAmb,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, matDif,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, matSpec,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine,0);
    			
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, bufferIDs[0]);

		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
		gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
		
		gl.glVertexPointer(3,GL.GL_FLOAT,0, 0);
		gl.glNormalPointer(GL.GL_FLOAT,0, normals.length*Float.BYTES );
		gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, (positions.length + normals.length)*Float.BYTES);
		
		//if we are rendering the head, texture it with the monster's face
		if (isHead) {
			gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[1].getTextureId());
			
			gl.glDrawArrays(GL2.GL_QUADS,0,4);
			//set start as 4 to render rest of the cube
			start = 4;
		}
		
		gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[0].getTextureId());
		
		gl.glDrawArrays(GL2.GL_QUADS,start,24);
		
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
		
		gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
		
	}
}
