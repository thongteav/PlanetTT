import com.jogamp.opengl.GL2;

public class Branch implements Particle{
	private float xHead, yHead;
	private float xTail, yTail;
	private boolean finished; //if branch has already created children branches
	private double angle;
	private float thickness;
	private float colorOffset;
	
	public Branch(float xHead, float yHead, float xTail, float yTail, double angle, float thickness, int level) {
		this.xHead = xHead;
		this.yHead = yHead;
		this.xTail = xTail;
		this.yTail = yTail;
		this.angle = angle;
		this.thickness = thickness;
		this.colorOffset = (level - 1) / 10f;
	}
	
	@Override
	public void draw(GL2 gl) {
		gl.glColor4f(1 - this.colorOffset, 1 - this.colorOffset, 1 - this.colorOffset, 1); //draw at white or darker if it's thinner
		gl.glLineWidth(this.thickness);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex2f(xHead, yHead);
		gl.glVertex2f(xTail, yTail);
		gl.glEnd();
	}
	

	@Override
	public void update(double time) {
				
	}

	public float getxHead() {
		return xHead;
	}

	public void setxHead(float xHead) {
		this.xHead = xHead;
	}

	public float getyHead() {
		return yHead;
	}

	public void setyHead(float yHead) {
		this.yHead = yHead;
	}

	public float getxTail() {
		return xTail;
	}

	public void setxTail(float xTail) {
		this.xTail = xTail;
	}

	public float getyTail() {
		return yTail;
	}

	public void setyTail(float yTail) {
		this.yTail = yTail;
	}
	
	public boolean isFinished() {
		return this.finished;
	}
	
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
	public double getAngle() {
		return this.angle;
	}
	
	public float getThickness() {
		return this.thickness;
	}
}
