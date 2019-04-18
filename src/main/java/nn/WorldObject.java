package nn;

import tags.Tuple;

/*
	Interface for all WorldObjects

	token is used to represent the object visually in the world GUI
	x and y are coordinates of the object in the world
	direction largely only used for the Autonomous WorldObject
*/
public abstract class WorldObject extends Tuple {	//Interface that all 3 objects in the world will implement
	public abstract char getToken();
	public abstract void setX(int newX);
	public abstract void setY(int newY);
	public abstract String getName();
	public abstract void setDirection(double direction);
	public abstract int getX();
	public abstract int getY();			//initializing all of the necessary methods
	public abstract WorldObject factory();
}
