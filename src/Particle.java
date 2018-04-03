import com.jogamp.opengl.GL2;

public interface Particle {
	public void draw(GL2 gl);
	public void update(double time);
}
