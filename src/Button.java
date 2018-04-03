import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Button {
	private float x, y;
	private String text;
	private boolean state;
	private float width, height;
	private static float OFFSET = 0.01f;
	
	public Button(float x, float y) {
		this.x = x;
		this.y = y;
		this.text = "";
		this.state = false;
	}
	
	public Button(float x, float y, float width, float height) {
		this(x, y);
		this.width = width;
		this.height = height;
	}
	
	public Button(float x, float y, float width, float height, String text) {
		this(x, y);
		this.text = text;
		this.width = width;
		this.height = height;
	}
	
	public boolean isOn() {
		return this.state;
	}
	
	public void toggle() {
		if (this.state) {
			this.state = false;
		}
		else {
			this.state = true;
		}
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void draw(GL2 gl) {
		GLUT glut = new GLUT();
		if(this.isOn()) {
			gl.glBegin(GL2.GL_QUADS);
			gl.glColor3fv(Color.BROWN, 0);
			gl.glVertex2f(this.x + OFFSET * 2, this.y - OFFSET * 2);
			gl.glVertex2f(this.x + this.width - OFFSET, this.y - OFFSET * 2);
			gl.glVertex2f(this.x + this.width - OFFSET, this.y - this.height + OFFSET);
			gl.glVertex2f(this.x + OFFSET * 2, this.y - this.height + OFFSET);
			gl.glEnd();
			gl.glColor3fv(Color.WHITE, 0);
			gl.glRasterPos2d(this.x + 0.04, this.y - 0.055);	
		}
		else {
			gl.glBegin(GL2.GL_QUADS);
			gl.glColor3fv(Color.GREY, 0);//shadow
			gl.glVertex2f(this.x + OFFSET * 2, this.y - OFFSET * 2);
			gl.glVertex2f(this.x + this.width - OFFSET, this.y - OFFSET * 2);
			gl.glVertex2f(this.x + this.width - OFFSET, this.y - this.height + OFFSET);
			gl.glVertex2f(this.x + OFFSET * 2, this.y - this.height + OFFSET);
			gl.glColor3fv(Color.ORANGE, 0);
			gl.glVertex2f(this.x + OFFSET, this.y - OFFSET);
			gl.glVertex2f(this.x + this.width - OFFSET * 2, this.y - OFFSET);
			gl.glVertex2f(this.x + this.width - OFFSET * 2, this.y - this.height + OFFSET * 2);
			gl.glVertex2f(this.x + OFFSET, this.y - this.height + OFFSET * 2);
			gl.glEnd();
			gl.glColor3fv(Color.BLACK, 0);
			gl.glRasterPos2d(this.x + 0.03, this.y - 0.05);		
		}
		glut.glutBitmapString(GLUT.BITMAP_9_BY_15, this.text);
	}

	public boolean isPressed(float x, float y) {
		//checks if the mouse press is within the area of the button
		return (x >= this.x && x <= this.x + width && y <= this.y && y >= this.y - height); 
	}
}
