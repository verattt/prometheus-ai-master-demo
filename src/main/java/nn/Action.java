package nn;/*
This class is used to give actions to the drone.
Each action has a name assiciated with it, how that action changes the robot's position, and how it changes it's direction.
All values assume that the robot currently has direction=0 (i.e the drone is facing due north)

An example action would be:
name = "Forward Left"
x_delta = "-1"
y_delta = "1"
direction_delta = "0"

Alternatively, an action can throw an exception to the drone saying that it is in a situation that it does not know what to do.
This is used to be able to force the robot to learn.
*/

import tags.Tuple;

import java.text.MessageFormat;

public class Action extends Tuple  {
	private String name;
	private int x_delta = 0;
	private int y_delta = 0;
	private double direction_delta = 0;
	private indecisiveException exception;

	public Action(String name, int x, int y, double dir) { 	//constructor for a normal action
		this.name = name;									// requires name and delta values, but not exception
		this.x_delta = x;
		this.y_delta = y;
		this.direction_delta = dir;
		this.exception = null;
		String label = "Action_"+this.name;
		String[] sParams = {"x_delta","y_delta","direction_delta"};
		double[] iParams = {x,y,dir};
		super.setTuple(label,sParams,iParams);
	}
	public Action(String exception_message){	//constructor for exception action
		this.name = "Exception";				// only requires the message the excpetion delivers to initialize
		this.exception = new indecisiveException(exception_message);
	}

	public void exception() throws indecisiveException { //throws the action's exception
		throw this.exception;
	}
	// getter methods
	public int getXDelta(){
		return x_delta;
	}
	public int getYDelta(){
		return y_delta;
	}
	public double getDirectionDelta(){
		return direction_delta;
	}

	public String getName() {
		return this.name;
	}


	//TODO CORRECT THE FORMAT FOR ALL simpleToString（）
	public String simpleToString(){
		StringBuilder params = new StringBuilder();
		for(int i=0;i<super.getSParams().length;i++){
			params.append(getSParams()[i]);
			params.append(getIParams()[i]);
		}
		return MessageFormat.format(
				"{0}{1}",
				getName(),
				params);
	}

	public boolean equals(Action b){
		return((this.name ==b.name)&&(this.x_delta == b.getXDelta())&&this.y_delta == b.getYDelta());						// requires name and delta values, but not exception

	}

}
