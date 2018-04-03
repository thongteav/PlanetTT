import java.util.ArrayList;
import java.util.Random;

import com.jogamp.opengl.GL2;

public class StarManager {
	private ArrayList<Star> stars;
	private int base;
	
	public StarManager(int size) {
		stars = new ArrayList<>(size);
		Random rand = new Random();
		for (int i = 0; i <= size; ++i) {
			//star(x, y, alpha, size) draw the stars below the button area
			stars.add(new Star(rand.nextFloat() * 2 - 1, rand.nextFloat() * 1.8f - 0.9f, rand.nextFloat() * 0.6f + 0.4f, rand.nextFloat() * 3 + 1));
		}
	}
	
	public void createStarList(GL2 gl) {
		base = gl.glGenLists(stars.size());
		
		for (int i = 0; i < stars.size(); ++i) {
			gl.glNewList(base + i, GL2.GL_COMPILE);
			stars.get(i).draw(gl);
			gl.glEndList();
		}
	}
	
	public void draw(GL2 gl) {
		for (int i = 0; i < stars.size(); ++i) {
			gl.glCallList(base + i);
		}
	}
}
