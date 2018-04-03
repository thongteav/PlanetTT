import com.jogamp.opengl.GL2;

public class Star implements Particle{
	private float x, y;
	private double color[];
	private float size;
	
	public Star(float posX, float posY, float alpha, float size) {
		this.x = posX;
		this.y = posY;
		this.color = new double[4];
		this.color[0] = 0;
		this.color[1] = 1;
		this.color[2] = 1;
		this.color[3] = alpha;
		this.size = size;
	}
	
	public void draw(GL2 gl) {
		gl.glColor4d(this.color[0], this.color[1], this.color[2], this.color[3]);
		gl.glPointSize(this.size);
		
		gl.glBegin(GL2.GL_POINTS);
		gl.glVertex2f(this.x, this.y);
		gl.glEnd();
	}
	
	public double getAlpha() {
		return this.color[3];
	}

	@Override
	public void update(double time) {
		
	}
}
