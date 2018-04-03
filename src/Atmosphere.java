import com.jogamp.opengl.GL2;

public class Atmosphere {
	private float[] topLeft, topRight, btmLeft, btmRight;
	private double[] topColor;
	private double[] bottomColor;
	private double time;
	private static double MAX_TIME = 40;
	
	public Atmosphere() {
		float[] posTopLeft = {-1f, 1f};
		float[] posTopRight = {1f, 1f};
		float[] posBtmLeft = {-1f, -1f};
		float[] posBtmRight = {1f, -1f};
		topLeft = posTopLeft;
		topRight = posTopRight;
		btmLeft = posBtmLeft;
		btmRight = posBtmRight;
		time = 0;
	}	

	public Atmosphere(float[] topLeft, float[] topRight, float[] btmLeft, float[] btmRight) {
		this.topLeft = topLeft;
		this.topRight = topRight;
		this.btmLeft = btmLeft;
		this.btmRight = btmRight;
		time = 0;
	}
	
	public Atmosphere(float xTopLeft, float yTopLeft, float xTopRight, float yTopRight, float xBtmLeft, float yBtmLeft, float xBtmRight, float yBtmRight) {
		float[] posTopLeft = {xTopLeft, yTopLeft};
		float[] posTopRight = {xTopRight, yTopRight};
		float[] posBtmLeft = {xBtmLeft, yBtmLeft};
		float[] posBtmRight = {xBtmRight, yBtmRight};
		topLeft = posTopLeft;
		topRight = posTopRight;
		btmLeft = posBtmLeft;
		btmRight = posBtmRight;
		time = 0;
	}	
	
	public void draw(GL2 gl) {
		gl.glColor4dv(this.topColor, 0);			
		gl.glBegin(GL2.GL_QUADS);			
		gl.glVertex2fv(topLeft, 0);
		gl.glVertex2fv(topRight, 0);
		gl.glColor4dv(this.bottomColor, 0);
		gl.glVertex2fv(btmRight, 0);		
		gl.glVertex2fv(btmLeft, 0);	
		gl.glEnd();
	}
	
	public void update(double time) {
		this.time += time;
		
		if(this.time > MAX_TIME) {
			this.time = 0;
		}
		
		//interpolate the color from day to dawn to night, back to dawn and then day cycle
		if(this.time >= 0 && this.time < 10) {
			this.topColor = this.lerp(Color.DAY_COLOR_TOP, Color.DAWN_COLOR_TOP, this.time / 10);
			this.bottomColor = this.lerp(Color.DAY_COLOR_BTM, Color.DAWN_COLOR_BTM, this.time / 10);
		}
		else if(this.time >= 10 && this.time < 20) {
			this.topColor = this.lerp(Color.DAWN_COLOR_TOP, Color.NIGHT_COLOR_TOP, (this.time - 10) / 10);
			this.bottomColor = this.lerp(Color.DAWN_COLOR_BTM, Color.NIGHT_COLOR_BTM, (this.time - 10) / 10);
		}
		else if(this.time >= 20 && this.time < 30) {
			this.topColor = this.lerp(Color.NIGHT_COLOR_TOP, Color.DAWN_COLOR_TOP, (this.time - 20) / 10);
			this.bottomColor = this.lerp(Color.NIGHT_COLOR_BTM, Color.DAWN_COLOR_BTM, (this.time - 20) / 10);
		}
		else {
			this.topColor = this.lerp(Color.DAWN_COLOR_TOP, Color.DAY_COLOR_TOP, (this.time - 30) / 10);
			this.bottomColor = this.lerp(Color.DAWN_COLOR_BTM, Color.DAY_COLOR_BTM, (this.time - 30) / 10);
		}
	}
	
	public double[] lerp(double[] start, double[] end, double time) {
//		formula from wiki: (1 - time) * start + time * end;
		double[] current = new double[start.length];
		
		for(int i = 0; i < start.length; ++i) {
			current[i] = (1 - time) * start[i] + time * end[i];
		}
		
		return current;
	}

	public void resetColor() {
		time = 0;
		this.topColor = Color.NIGHT_COLOR_TOP;
		this.bottomColor = Color.NIGHT_COLOR_BTM;
	}
}
