package nn;/*
This class is a case of WorldObject
It represents movable objects that currently exist in the world representation
Nothing particuarly special about it
*/

import tags.Tuple;

public class Moveable extends WorldObject{
		//mplements WorldObject{
	private String name = "Moveable";
	private char token = 'M';
	private int x, y;
	public Moveable(){
		super.setLabel(name);
		String[] sp = {"x","y"};
		super.setSParams(sp);
		super.iParams = new double[2];

	}


	public void setX(int newX){
		this.x = newX;
		this.iParams[0]=newX;
	}

	public void setY(int newY){	//setting the coordinates
		this.y = newY;
		this.iParams[1]=newY;
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
	public String simpleToString(){
		return null;
	}

	@Override
	public Moveable factory() {
		return new Moveable();
	}
}
