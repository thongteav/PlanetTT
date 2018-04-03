import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

/**
 * Creates a window to represent the actual planet. 
 * Provides interaction by listening to mouse click to activate different features.
 * 
 * @author Thong Teav
 *
 */
public class Planet implements GLEventListener, MouseListener, KeyListener {
	private static int WIDTH = 1080;
	private static int HEIGHT = 1080;
	private ButtonManager buttons;
	private StarManager stars;
	private AsteroidSystem asteroids;
	private double time, tick, prevTick;
	private Surface surface;
	
	public Planet() {		
		String names[] = {"Atmosphere", "Time", "Asteroids", "Spaceship", "Rotate", "Wind"};
		buttons = new ButtonManager(names);	
		stars = new StarManager(100);
		asteroids = new AsteroidSystem();
		surface = new Surface(0f, -2.3f);
		buttons.setAsteroidSystem(asteroids);	
		buttons.setSurface(surface);
	}
	
	@Override
	public void display(GLAutoDrawable gld) {
		// calculate time since last frame
		tick = System.currentTimeMillis() / 1000.0;
		time = tick - prevTick;
		if (prevTick > 0) {
			buttons.update(time);
		}
		prevTick = tick;
		
		GL2 gl = gld.getGL().getGL2();
		gl.glLoadIdentity();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		stars.draw(gl);		
		buttons.draw(gl, time);
		gl.glFlush();
	}

	@Override
	public void dispose(GLAutoDrawable gld) {

	}

	@Override
	public void init(GLAutoDrawable gld) {
		GL2 gl = gld.getGL().getGL2();
		gl.glEnable(GL2.GL_BLEND);
		gl.glEnable(GL2.GL_LINE_SMOOTH);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		//loads the stars and the planet surface into the graphic card
		stars.createStarList(gl);
		surface.createSurfaceList(gl);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {		
		height = (height <= 0) ? 1: height; //prevent divide by zero
		WIDTH = width;	
		HEIGHT = height;
		
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
	}

	public static void main(String[] args) {
		Frame frame = new Frame("Planet TT23");
		GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);
		GLCanvas canvas = new GLCanvas(capabilities);
		Planet planet = new Planet();	
		
		canvas.addGLEventListener(planet);
		canvas.addMouseListener(planet);
		canvas.addKeyListener(planet);
		frame.add(canvas);
		frame.setSize(WIDTH, HEIGHT);
		
		final Animator animator = new Animator(canvas);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				new Thread(new Runnable() {
					public void run() {
						animator.stop();
						System.exit(0);
					}
				}).start();
			}
		});
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		canvas.requestFocusInWindow();
		animator.start();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		float mouseX = e.getX();
		float mouseY = HEIGHT - e.getY();//flip y
		float openglX = 2f * mouseX / WIDTH - 1f;
		float openglY = 2f * mouseY / HEIGHT - 1f;
		buttons.isPressed(openglX, openglY);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(buttons.getButtonAtIndex(4).isOn()) {
			int key = e.getKeyCode();
			switch (key) {
			case KeyEvent.VK_UP:
				surface.increaseRotationSpeed();
				break;
			case KeyEvent.VK_DOWN:
				surface.decreaseRotationSpeed();
				break;
			case KeyEvent.VK_LEFT:
				surface.rotateLeft();
				break;
			case KeyEvent.VK_RIGHT:
				surface.rotateRight();
				break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

}
