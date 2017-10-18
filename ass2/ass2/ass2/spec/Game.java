package ass2.spec;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.FileNotFoundException;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;

import javax.swing.JFrame;
import com.jogamp.opengl.util.FPSAnimator;

import ass2.spec.Camera.Mode;




/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener, MouseMotionListener, MouseWheelListener, KeyListener{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Terrain myTerrain;
    private Avatar aang;
	private Camera camera;
	
    private double rotateX = 0;
    private double rotateY = 0;
    private Point myMousePoint = null;
    private static final int ROTATION_SCALE = 1;
    private static double zoom = 1;
    private static double translateX = 0;
    private static double translateY = 0;
    private static double translateZ = 0;
    
    private boolean[] keyStates;
    
    private static MyTexture[] myTextures;
    private String textureFileName1 = "ass2/ass2/textures/grass1.jpg";
    private String textureExt1 = "jpg";

    public Game(Terrain terrain) {
    	super("Assignment 2");
        myTerrain = terrain;
        aang = new Avatar(0, 0.5, 0 ,terrain);
        camera = new Camera(terrain, aang);
        keyStates = new boolean[256];
    }
    
    /** 
     * Run the game.
     *
     */
    public void run() {
    	  GLProfile glp = GLProfile.getDefault();
          GLCapabilities caps = new GLCapabilities(glp);
          GLJPanel panel = new GLJPanel();
          panel.addGLEventListener(this);
          panel.addMouseMotionListener(this);
          panel.addMouseWheelListener(this);
          panel.addKeyListener(this);
          panel.setFocusable(true);
 
          // Add an animator to call 'display' at 60fps        
          FPSAnimator animator = new FPSAnimator(60);
          animator.add(panel);
          animator.start();

          getContentPane().add(panel);
          setSize(800, 600);        
          setVisible(true);
          setDefaultCloseOperation(EXIT_ON_CLOSE);
    
    }
    
    /**
     * Load a level file and display it.
     * 
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
    	//Terrain terrain = LevelIO.load(new File(args[0]));
    	//Terrain terrain = LevelIO.load(new File("ass2/ass2/spec/testb.json"));
    	//Terrain terrain = LevelIO.load(new File("ass2/ass2/spec/largeTerrain.json"));
    	//Terrain terrain = LevelIO.load(new File("ass2/ass2/spec/testRoads3.json"));
    	//Terrain terrain = LevelIO.load(new File("ass2/ass2/spec/testHill.json"));
    	Terrain terrain = LevelIO.load(new File("ass2/ass2/spec/testWorld.json"));
    	//Terrain terrain = LevelIO.load(new File("ass2/ass2/spec/testLight.json"));
        Game game = new Game(terrain);
        game.run();
    }

	@Override
	public void display(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		GL2 gl = drawable.getGL().getGL2();
		//GLU glu = new GLU();
		gl.glClearColor(0.5f, 0.5f, 1, 1);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT 
				| GL2.GL_DEPTH_BUFFER_BIT);
		
		
		keyOperations();
		
		camera.updateCamera(gl);
		if (camera.getMode() == Mode.THIRD_PERSON) {
			aang.drawAvatar(gl);
		}
		myTerrain.drawTerrain(gl);
		
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void init(GLAutoDrawable arg0) {
		GL2 gl = arg0.getGL().getGL2();
		//gl.glPushMatrix();
		
		gl.glEnable(GL2.GL_DEPTH_TEST);
		
		/*// Enable Blending 
	    gl.glEnable(GL2.GL_BLEND);      
	    //Creates an additive blend, which looks spectacular on a black background
	    gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE);*/
		
		lightingInit(gl);
		textureInit(gl);
		
		myTerrain.init(gl);
		
		//gl.glPopMatrix();
	}
	
	//Does lighting initialisations.
	private void lightingInit(GL2 gl) {
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_NORMALIZE);
		
		// Light property vectors.
		//float[] sun = myTerrain.getSunlight();
    	float lightAmb[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    	float lightDifAndSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    	//float lightPos[] = { sun[0], sun[1], sun[2], 0.0f };
    	float globAmb[] = { 1f, 1f, 1f, 1.0f };

    	// Light properties.
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb,0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDifAndSpec,0);
    	gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightDifAndSpec,0);
    	//gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos,0);

    	gl.glEnable(GL2.GL_LIGHT0); // Enable particular light source.
    	gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb,0); // Global ambient light.
    	gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE); // Enable two-sided lighting.
    	gl.glLightModeli(GL2.GL_LIGHT_MODEL_COLOR_CONTROL, GL2.GL_SEPARATE_SPECULAR_COLOR);

    	/*
    	// Material property vectors.

    	float matAmbAndDif2[] = {0.0f, 0.9f, 0.0f, 1.0f};
    	float matSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    	float matShine[] = { 50.0f };

    	// Material property vectors.
    	float matAmbAndDif1[] = {1.0f, 1.0f, 1.0f, 1.0f};

    	// Material properties.
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif1,0);
    	gl.glMaterialfv(GL2.GL_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif2,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, matSpec,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine,0);*/
	}
	
	//Does the texture initialisations.
	private void textureInit(GL2 gl) {
    	// Create texture ids. 
    	myTextures = new MyTexture[1];
    	//Bind texture and enable mipmaps with them
    	myTextures[0] = new MyTexture(gl,textureFileName1,textureExt1,true);

    	// Specify how texture values combine with current surface color values.
    	gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE); 

    	// Turn on OpenGL texturing.
    	gl.glEnable(GL2.GL_TEXTURE_2D); 
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int x, int y, int w, int h) {
        camera.setAspectRatio((float)w/(float)h);
	}
	

	@Override
	public void mouseDragged(MouseEvent e) {
		Point p = e.getPoint();

        if (myMousePoint != null) {
            int dx = p.x - myMousePoint.x;
            int dy = p.y - myMousePoint.y;

            // Note: dragging in the x dir rotates about y
            //       dragging in the y dir rotates about x
            rotateY += dx * ROTATION_SCALE;
            
            //camera.rotate(rotateY/10000);
            rotateX += dy * ROTATION_SCALE;

        }
        
        myMousePoint = p;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		myMousePoint = e.getPoint();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		if (arg0.getWheelRotation() < 0) {
			zoom = Math.min(10, zoom + 0.1);
		} else {
			zoom = Math.max(0.01, zoom - 0.1);
		}
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			keyStates[1] = true;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			keyStates[2] = true;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			keyStates[3] = true;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			keyStates[4] = true;
		} else {
		
			char key = e.getKeyChar();
			keyStates[key] = true;
			
			if (key == 'c') {
				camera.changeMode();
				if (camera.getMode() == Mode.THIRD_PERSON) {
					 aang.summonAvatar(camera.getPos()[0], camera.getPos()[1], camera.getPos()[2], camera.getAngle());
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			keyStates[1] = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			keyStates[2] = false;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			keyStates[3] = false;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			keyStates[4] = false;
		} else {
			keyStates[e.getKeyChar()] = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	void keyOperations() {
		if (keyStates[1]) {
			camera.move(0.01);
			if (camera.getMode() == Mode.THIRD_PERSON) {
				aang.move(0.01);
			}
		}
		
		if (keyStates[2]) {
			camera.move(-0.01);
			if (camera.getMode() == Mode.THIRD_PERSON) {
				aang.move(-0.01);
			}
		}
		
		if (keyStates[4]) {
			camera.rotate(-0.02);
			if (camera.getMode() ==  Mode.THIRD_PERSON) {
				aang.rotate(-0.02);
			}
		}
		
		if (keyStates[3]) {
			camera.rotate(0.02);
			if (camera.getMode() == Mode.THIRD_PERSON) {
				aang.rotate(0.02);
			}
		}
		
		if (keyStates['w']) {
			camera.move(0.01);
			if (camera.getMode() == Mode.THIRD_PERSON) {
				aang.move(0.01);
			}
		}
		
		if (keyStates['s']) {
			camera.move(-0.01);
			if (camera.getMode() == Mode.THIRD_PERSON) {
				aang.move(-0.01);
			}
		}
		
		if (keyStates['a']) {
			camera.strafe(0.01);
			if (camera.getMode() == Mode.THIRD_PERSON) {
				aang.strafe(0.01);
			}
		}
		
		if (keyStates['d']) {
			camera.strafe(-0.01);
			if (camera.getMode() == Mode.THIRD_PERSON) {
				aang.strafe(-0.01);
			}
		}
	}
}
