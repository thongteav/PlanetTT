import java.util.ArrayList;

import com.jogamp.opengl.GL2;

public class AsteroidSystem {
	private ArrayList<Asteroid> asteroids;
	private static int INIT_POP = 5;
	private static int MAX_POP = 15;
	private int population = INIT_POP;
	private int time;
	private boolean falling = false;
	
	public AsteroidSystem() {
		asteroids = new ArrayList<>();
		time = 0;
	}
	
	public void init() {
		for(int i = 0; i < INIT_POP; ++i) {
			asteroids.add(new Asteroid());
		}
	}
	
	public void draw(GL2 gl) {
		for(Asteroid as : asteroids) {
			as.draw(gl);
		}
	}
	
	public void update(double time) {
		this.time += time*100;
		if (this.population <= MAX_POP && this.time % 60 == 0) {
			for (int i = 0; i < 3; ++i) {
				asteroids.add(new Asteroid(this.falling));
				this.population++;
			}
		}

		for (Asteroid as : asteroids) {
			as.update(time);
		}
	}
	
	public void setFalling(boolean falling) {
		this.falling = falling;
		for (Asteroid as : asteroids) {
			as.setFalling(falling);
		}
	}
}
