import java.util.ArrayList;
import java.util.Random;

import com.jogamp.opengl.GL2;

/**
 * The surface of the planet consists of three layers and can rotate.
 * 
 * @author Thong Teav
 *
 */
public class Surface {
	private float x, y;
	private double angle;
	private double rotatingAngle;
	private double rotateDirection;
	private ArrayList<Asteroid> layers;	
	private ArrayList<Tree> trees;
	private boolean rotating = false;
	private int base;
	private static Random rand = new Random(System.currentTimeMillis());
	private final static double MAX_ROTATE_SPEED = 0.25;
	private final static double MIN_ROTATE_SPEED = 0.05;
	
	public Surface(float centerX, float centerY) {
		this.x = centerX;
		this.y = centerY;
		this.rotatingAngle = 0.15;
		this.rotateDirection = 1;
		layers = new ArrayList<>();
		layers.add(new Asteroid(centerX, centerY, 1.94f, Color.BROWN, 72, -0.08, 0.1));
		layers.add(new Asteroid(centerX, centerY, 1.86f, Color.BURNT_ORANGE, 60, -0.04, 0.06));
		layers.add(new Asteroid(centerX, centerY, 1.76f, Color.ORANGE, 45, -0.04, 0.04));
		angle = 0;
		trees = new ArrayList<>();
		for(double[] vertex : layers.get(1).getVertices()) {
			if(rand.nextBoolean()) {
				trees.add(new Tree((float) vertex[0], (float) vertex[1], rand.nextFloat() * 0.1f + 0.05f, rand.nextInt(4) + 3, vertex[2]));
			}
		}
	}
	
	public void draw(GL2 gl) {		
		gl.glPushMatrix();
		gl.glTranslatef(x, y, 0f);		
		gl.glRotated(angle, 0, 0, 1);
		gl.glTranslatef(-x, -y, 0f);	
		
		gl.glCallList(base);
		gl.glCallList(base + 1);
		this.drawTrees(gl); //render the trees behind the top layer and in front of the third layer
		gl.glCallList(base + 2);
		
		gl.glPopMatrix();
	}
	
	public void drawTrees(GL2 gl) {
		for(Tree tree : this.trees) {
			tree.draw(gl);
		}
	}
	
	public void rotate(GL2 gl) {
		if (rotating) {
			angle += rotatingAngle * rotateDirection;
		}		
		if(angle > 360) {
			angle = 0;
		}
	}
	
	public ArrayList<Tree> getTrees() {
		return this.trees;
	}
	
	public void setRotation(boolean rotating) {
		this.rotating = rotating;
	}
	
	public void createSurfaceList(GL2 gl) {
		base = gl.glGenLists(layers.size());
		
		for (int i = 0; i < layers.size(); ++i) {
			gl.glNewList(base + i, GL2.GL_COMPILE);
			layers.get(i).draw(gl);
			gl.glEndList();
		}
	}
	
	public void setWindOn(boolean windOn) {
		for(Tree t: this.trees) {
			t.setWindOn(windOn);
		}
	}
	
	public double getRotatingAngle() {
		return this.rotatingAngle;
	}
	
	public void changeRotateDirection() {
		this.rotateDirection = -this.rotateDirection;
	}
	
	public void rotateLeft() {
		this.rotateDirection = Math.abs(this.rotateDirection);
	}
	
	public void rotateRight() {
		this.rotateDirection = -1 * Math.abs(this.rotateDirection);
	}
	
	public void increaseRotationSpeed() {
		if (this.rotatingAngle < MAX_ROTATE_SPEED) {
			this.rotatingAngle += 0.01;
		}
	}
	
	public void decreaseRotationSpeed() {
		if (this.rotatingAngle > MIN_ROTATE_SPEED) {
			this.rotatingAngle -= 0.01;
		}
	}
}
