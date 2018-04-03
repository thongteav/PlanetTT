import java.util.Random;

import com.jogamp.opengl.GL2;

public class Leaf implements Particle{
	private float x, y;
	private float xInit, yInit;
	private double radius;
	private float age;
	private double angle;
	private float distance = 0f;
	private boolean windOn = false;
	private static float AGE_MAX = 3f;
	private static float AGE_OLD = 1f;
	private static Random rand = new Random(System.currentTimeMillis());
	
	public Leaf(float x, float y, double radius, double angle) {
		this.x = x;
		this.y = y;
		this.xInit = x;
		this.yInit = y;
		this.age = 0;
		this.radius = radius;
		this.angle = angle;
	}
	
	public void draw(GL2 gl) {
		gl.glColor4fv(Color.LEAF_GREEN, 0);
		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		for (int deg = 0; deg <= 360; deg += 36) {
			double rad = Math.toRadians(deg);
			double x = this.x + this.radius * Math.cos(rad) * Math.min(this.age, AGE_OLD);
			double y = this.y + this.radius * Math.sin(rad) * Math.min(this.age, AGE_OLD);
			gl.glVertex2d(x, y);
		}
		gl.glEnd();
	}
	
	/**
	 * The leaf should grow from 0 to its max age, it falls from the branch when it reaches the max age, and if the wind is on,
	 * the leaf should shake on the branch between the time it's old and the max age.
	 */
	public void update(double time) {
		if (this.age < AGE_MAX) {
			this.age += time;
			if(this.age > AGE_OLD) {
				if(windOn) {
					this.x = this.xInit + rand.nextFloat() * 0.006f - 0.003f;
				}
			}
		} 
		else {
			if (windOn || this.distance > 0) {
				this.distance += time * 0.5f;
				double rad = Math.toRadians(180 + this.angle);
				this.x = (float) (this.xInit + Math.cos(rad) * this.distance);
				this.y = (float) (this.yInit + Math.sin(rad) * this.distance);
				if (this.distance >= 1f) { //checking if it's within some radius of the circle
					this.distance = 0;
					this.x = this.xInit; //reset the position of the leaf on the branch
					this.y = this.yInit;
					this.age = 0;
				}
			}
		}
	}
	
	/**
	 * Turn wind on or off
	 * @param windOn
	 */
	public void setWindOn(boolean windOn) {
		this.windOn = windOn;
	}
}
