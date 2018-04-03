import com.jogamp.opengl.GL2;

public class Spaceship {
	public float x, y;
	public float dx;
	public float size;
	public boolean flying = false;
	public static float MAX_X = 1.2f;
	public float[] cockpit, rect, triFan, rect2, base;
	
	public Spaceship(float x, float y) {
		this.x = x;
		this.y = y;
		this.generateBody();		
		this.dx = 0f;
	}
	
	/**
	 * Creates the body of the spaceship relative the position of the spaceship
	 */
	public void generateBody() {
		cockpit = new float[] {x, y + 0.01f, 0.05f}; 
		rect = new float[]   {x - 0.06f, y + 0.01f, 
							  x + 0.06f, y + 0.01f, 
							  x + 0.06f, y - 0.01f, 
							  x - 0.06f, y - 0.01f};
		triFan = new float[] {x, y, 
							  x - 0.2f, y - 0.04f, 
							  x - 0.15f, y - 0.04f, 
							  x - 0.1f, y - 0.04f, 
							  x - 0.05f, y - 0.04f, 
							  x + 0.05f, y - 0.04f, 
							  x + 0.1f, y - 0.04f, 
							  x + 0.15f, y - 0.04f, 
							  x + 0.2f, y - 0.04f};
		rect2 = new float[]  {x - 0.2f, y - 0.04f, 
							  x + 0.2f, y - 0.04f, 
							  x + 0.2f, y - 0.05f, 
							  x - 0.2f, y - 0.05f};
		base = new float[]   {x - 0.2f, y - 0.05f, 
							  x + 0.2f, y - 0.05f, 
							  x - 0.06f, y - 0.1f, 
							  x + 0.06f, y - 0.1f, 
							  x - 0.05f, y - 0.11f, 
							  x + 0.05f, y - 0.11f};
	}
	
	/**
	 * The spaceship flies across the screen in x direction and reposition itself after going out of screen
	 * 
	 * @param time
	 */
	public void update(double time) {
		if(this.flying || this.dx > 0) {//check if the button for flying is on or if it's already flying
			this.dx += time/3;
			this.x += this.dx * time;			
			
			//update position for different parts of the ship
			cockpit[0] += this.dx * time;
			this.updatePosition(this.rect, time);
			this.updatePosition(this.triFan, time);
			this.updatePosition(this.rect2, time);
			this.updatePosition(this.base, time);
			
			if (this.x > MAX_X) {
				this.x = -MAX_X;
				this.dx = 0f;
				this.generateBody();
			}
		}
	}
	
	public void draw(GL2 gl) {
		this.drawCockpit(gl, this.cockpit, this.cockpit[2]);
		this.drawRectangle(gl, this.rect, Color.ORANGE);
		this.drawTriangleFan(gl, this.triFan);
		this.drawRectangle(gl, this.rect2, Color.CYAN);
		this.drawBase(gl, this.base);
	}
	
	public void setFlying(boolean flying) {
		this.flying = flying;
	}
	

	private void updatePosition(float[] vertices, double time) {
		for(int i = 0; i < vertices.length; i += 2) {
			vertices[i] += this.dx * time;
		}
	}
	
	//------------------------------------------------------------------
	//Private methods to draw different parts of the spaceship
	//------------------------------------------------------------------
	private void drawTriangleFan(GL2 gl, float[] vertices) {
		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		gl.glColor4fv(Color.WHITE, 0);
		gl.glVertex2fv(vertices, 0);
		gl.glColor3fv(Color.ORANGE, 0);
		gl.glVertex2fv(vertices, 2);
		gl.glColor3fv(Color.BURNT_ORANGE, 0);
		gl.glVertex2fv(vertices, 4);
		gl.glColor3fv(Color.ORANGE, 0);
		gl.glVertex2fv(vertices, 6);
		gl.glColor3fv(Color.BURNT_ORANGE, 0);
		gl.glVertex2fv(vertices, 8);
		gl.glColor3fv(Color.ORANGE, 0);
		gl.glVertex2fv(vertices, 10);
		gl.glColor3fv(Color.BURNT_ORANGE, 0);
		gl.glVertex2fv(vertices, 12);
		gl.glColor3fv(Color.ORANGE, 0);
		gl.glVertex2fv(vertices, 16);
		gl.glEnd();		
	}
	
	private void drawRectangle(GL2 gl, float[] vertices, float[] color) {
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3fv(color, 0);
		gl.glVertex2fv(vertices, 0);
		gl.glVertex2fv(vertices, 2);
		gl.glVertex2fv(vertices, 4);
		gl.glVertex2fv(vertices, 6);
		gl.glEnd();
	}
	
	private void drawCockpit(GL2 gl, float[] vertices, float radius) {
		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		gl.glColor4f(1, 1, 1, 1);
		gl.glVertex2fv(vertices, 0);
		gl.glColor3fv(Color.CYAN, 0);
		for (int deg = 0; deg <= 180; deg += 18) {
			double rad = Math.toRadians(deg);
			double newX = vertices[0] + radius * Math.cos(rad);
			double newY = vertices[1] + radius * Math.sin(rad); 
			gl.glVertex2d(newX, newY);
		}
		gl.glEnd();
	}
	
	private void drawBase(GL2 gl, float[] vertices) {
		gl.glBegin(GL2.GL_QUAD_STRIP);
		gl.glColor3fv(Color.ORANGE, 0);
		gl.glVertex2fv(vertices, 0);
		gl.glVertex2fv(vertices, 2);
		gl.glColor3fv(Color.BURNT_ORANGE, 0);
		gl.glVertex2fv(vertices, 4);
		gl.glVertex2fv(vertices, 6);
		gl.glColor3fv(Color.ORANGE, 0);
		gl.glVertex2fv(vertices, 8);
		gl.glVertex2fv(vertices, 10);
		gl.glEnd();
	}
}
