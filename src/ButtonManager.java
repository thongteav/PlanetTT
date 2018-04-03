import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.jogamp.opengl.GL2;

public class ButtonManager {
	private ArrayList<Button> buttons;
	private float xTopLeft, yTopLeft;
	private float xBtmRight, yBtmRight;
	private Atmosphere atmosphere;
	private AsteroidSystem asteroid;
	private Surface surface;
	private Spaceship ship;
	private boolean dismissed;
	
	private ButtonManager() {//only to be accessed locally
		this.buttons = new ArrayList<>();
		this.setPosition(-1f, 1f, 1f, 0.9f);
		this.atmosphere = new Atmosphere(-1f, 0.9f, 1f, 0.9f, -1f, -1f, 1f, -1f);
		this.ship = new Spaceship(-1.3f, 0.7f);
	}
	
	public ButtonManager(int size) {
		this();
		this.createButtons(size);
	}
	
	public ButtonManager(String[] names) {
		this();
		this.createButtons(names);
	}
	
	public void setSurface(Surface surface) {
		this.surface = surface;
	}	

	public void setAsteroidSystem(AsteroidSystem asteroidSys) {
		this.asteroid = asteroidSys;
	}
	
	public ArrayList<Button> getButtons() {
		return buttons;
	}
	
	public void setButtons(ArrayList<Button> buttons) {
		this.buttons = buttons;
	}
	
	public void setPosition(float xTopLeft, float yTopLeft, float xBtmRight, float yBtmRight) {
		this.xTopLeft = xTopLeft;
		this.yTopLeft = yTopLeft;
		this.xBtmRight = xBtmRight;
		this.yBtmRight = yBtmRight;
	}
	
	/**
	 * Get a button at a certain index
	 * 
	 * @param index a position staring at 0
	 * @return button at the specified position
	 */
	public Button getButtonAtIndex(int index) {
		return this.buttons.get(index);
	}
	
	/**
	 * Create the specified number of buttons to fill in a row
	 * 
	 * @param size the number of buttons to be created
	 */
	public void createButtons(int size) {
		this.buttons.clear();//make sure the list is empty
		float width = (xBtmRight - xTopLeft) / size;//calculate the width of each button 
		
		for(int i = 0; i < size; ++i) {
			this.buttons.add(new Button(width * i + xTopLeft, yTopLeft, width, yTopLeft - yBtmRight));
		}
	}
	
	/**
	 * Create buttons with the names provided
	 * 
	 * @param names the names for the buttons
	 */
	public void createButtons(String[] names) {
		this.buttons.clear();
		float width = (xBtmRight - xTopLeft) / names.length;
		
		for(int i = 0; i < names.length; ++i) {
			this.buttons.add(new Button(width * i + xTopLeft, yTopLeft, width, yTopLeft - yBtmRight, names[i]));
		}
	}
	
	public void update(double time) {
		asteroid.update(time);
		
		if(buttons.get(1).isOn()) {
			atmosphere.update(time); //only update the atmosphere when the time button is on
		}
		
		for(Tree tree : surface.getTrees()) {
			tree.update(time);
		}
		
		ship.update(time);
	}
	
	//The order of rendering is important
	public void draw(GL2 gl, double time) {		
		//atmosphere
		if(buttons.get(0).isOn()) {
			atmosphere.draw(gl);
		}
		
		//time
		if(buttons.get(1).isOn()) {
			atmosphere.draw(gl);
		}
		
		//asteroids to be rendered behind the buttons and on top of the atmosphere mask
		asteroid.draw(gl);
		
		//button area
		gl.glColor3fv(Color.LIGHT_GREY, 0);
		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex2f(xTopLeft, yTopLeft - (yTopLeft - yBtmRight));
		gl.glVertex2f(xTopLeft, yTopLeft);
		gl.glVertex2f(xBtmRight, yBtmRight + (yTopLeft - yBtmRight));
		gl.glVertex2f(xBtmRight, yBtmRight);
		gl.glEnd();	

		//draw each button
		for (Button btn : buttons) {
			btn.draw(gl);
		}	
		
		//rotation
		if(buttons.get(4).isOn()) {
			surface.setRotation(true);
			surface.rotate(gl);
		}
		else {
			surface.setRotation(false);
		}
		surface.draw(gl);
		
		//ship
		ship.draw(gl);
	}

	/**
	 * Checks if a button is pressed to toggle between on and off
	 * 
	 * @param x x position of the mouse click
	 * @param y y position of the mouse click
	 */
	public void isPressed(float x, float y) {
		for (Button btn : buttons) {
			if (btn.isPressed(x, y)) {
				btn.toggle();
				
				if(!buttons.get(1).isOn()) {
					atmosphere.resetColor();
				}
				
				asteroid.setFalling(buttons.get(2).isOn());
				surface.setWindOn(buttons.get(5).isOn());
				ship.setFlying(buttons.get(3).isOn());
				
				//show the instruction the first time the rotate button is pressed
				if(buttons.get(4).isOn() && !this.dismissed) {
					String message = "While rotate button is on, press:\n"
								   + "- Up key: increase the speed of rotation\n"
								   + "- Down key: decrease the speed of rotation\n"
								   + "- Left key: rotate to the left\n"
								   + "- Right key: rotate to the right\n";	
					System.out.println(message);
					JOptionPane.showMessageDialog(null, message);
					this.dismissed = true;
				}
			}
		}
	}
	
}
