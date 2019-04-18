package nn;

/*
This class is a case of WorldObject
It represents immovable objects (walls) that currently exist in the world representation
Nothing particuarly special about it
*/
public class Immovable extends WorldObject {
	private String name = "Immovable";
	private char token = 'I';
	private int x, y;

	public Immovable(){

		String[] sp = {"x","y"};
		double[] iParams = new double[2];
		super.setTuple(this.getName(),sp,iParams);

	}

	public void setX(int newX){
		this.x = newX;
		super.iParams[0]=newX;
	}

	public void setY(int newY){	//setting the coordinates
		this.y = newY;
		super.iParams[1]=newY;
	}
	
	public int getX(){
		return this.x;
	}
	public int getY(){			//getting the coordinates
		return this.y;
	}
	public void setDirection(double direction) { //only used in autonomous

	}
	public String getName() {
		return this.name;
	}

	public char getToken(){		//getting the token
		return this.token;
	}

	@Override
	public String simpleToString() {
		return null;
	}


	public Immovable factory(){
		return new Immovable();
	}
}
