package nn;

/*
This class is a case of WorldObject
It represents the drone(s) that currently exist in the world representation
It differs from other WorldObjects in that it has a setDirection function that sets the objects direction and changes its token to match
*/
public class Autonomous extends WorldObject {
	private String name;
	private char token = '^';
	private int x, y;
	private double direction = 0;

	public Autonomous(String name) {
		this.name = name;
		super.setLabel("autonomous"+name);
	}
	public void setX(int newX){
		this.x = newX;
	}

	public void setY(int newY){	//setting the coordinates
		this.y = newY;
	}

	public int getX(){
		return this.x;
	}

	public int getY(){			//getting the coordinates
		return this.y;
	}

	public char getToken(){		//getting the token
		return this.token;
	}
	public String getName() {
		return this.name;
	}
	public void setDirection(double direction) {
		this.direction = direction;
		if (this.direction < 0.25) {
			this.token = '^';
		}
		else if (this.direction < 0.5) {
			this.token = '>';
		}
		else if (this.direction < 0.75) {
			this.token = 'v';
		}
		else if (this.direction < 1.0) {
			this.token = '<';
		}
	}

	@Override
	public String simpleToString() {
		return null;
	}

	@Override
	public Autonomous factory() {
		return new Autonomous("");
	}
}
