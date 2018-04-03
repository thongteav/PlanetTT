import java.util.ArrayList;
import java.util.Random;

import com.jogamp.opengl.GL2;

public class Asteroid implements Particle {
	private float x, y;
	private float dy;
	private float radius;
	private float colour[];
	private static Random rand = new Random(System.currentTimeMillis());
	private static final float MIN = -1f;
	private ArrayList<double[]> vertices;
	private boolean falling = false;
	
	public Asteroid() {
		this.x = rand.nextFloat() * 2 - 1;
		this.dy = rand.nextFloat() * 0.02f + 0.01f;
		this.radius = rand.nextFloat() * 0.07f + 0.025f;
		this.y = 1 + rand.nextFloat() + this.radius;
		float rgb = rand.nextFloat() * 0.35f + 0.2f;
		float color[] = {rgb, rgb, rgb}; //getting different shades of grey
		this.colour = color;
		this.vertices = new ArrayList<>();
		this.generateVertices(10, -0.2, 0.2);
	}	
	
	public Asteroid(boolean falling) {
		this();
		this.falling = falling;
	}
	
	public Asteroid(float x, float y, float radius, float[] colour, int numOfVertices, double rangeMin, double rangeMax) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.colour = colour;
		this.vertices = new ArrayList<>();
		this.generateVertices(numOfVertices, rangeMin, rangeMax);
	}
	
	/**
	 * Create new vertices around the point at x, y.
	 * 
	 * @param slices the number of slices
	 * @param rangeMin the minimum distance from the center
	 * @param rangeMax the maximum distance from the center
	 */
	private void generateVertices(int slices, double rangeMin, double rangeMax) {
		//clear the existing vertices of a recycled asteroid so that it will have a different shape
		if (!this.vertices.isEmpty()) {
			this.vertices.clear();
		}
		
		int degree = 360 / slices;
		double range = rangeMax - rangeMin;
		for(int deg = 0; deg < 360; deg += degree) { //this leaves a gap from the last point to the first point
			double rad = Math.toRadians(deg);
			double offset = 1 + (rand.nextDouble() * range + rangeMin);
			double x = this.x + this.radius * offset * Math.cos(rad);
			double y = this.y + this.radius * offset * Math.sin(rad);
			double[] vertex = {x, y, deg};
			vertices.add(vertex);
		}
	}

	@Override
	public void draw(GL2 gl) {
		gl.glColor3fv(colour, 0);
		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		gl.glVertex2d(this.x, this.y);
		for(double[] vertex : vertices) {
			gl.glVertex2dv(vertex, 0);
		}
		gl.glVertex2dv(vertices.get(0), 0);//to fill the gap of the circle with the first vertex
		gl.glEnd();
	}

	@Override
	public void update(double time) {
		if(falling || y < 1) { //check if it should be falling and if it's already dropping from the top
			if (y < MIN - radius) { // check if the asteroid is completely out of the screen
				y = 1 + rand.nextFloat() * 0.5f;
				x = rand.nextFloat() * 2 - 1;
				this.generateVertices(vertices.size(), -0.2, 0.2);
				dy = rand.nextFloat() * 0.05f + 0.01f;
			}
			
			dy -= time;
			y += dy * time; // decrease y position of the center point
	
			for (double[] vertex : vertices) { // decrease y positions of all the vertices
				vertex[1] += dy * time;
			}
		}
	}
	
	public ArrayList<double[]> getVertices() {
		return this.vertices;
	}
	
	public void setFalling(boolean falling) {
		this.falling = falling;
	}
}
