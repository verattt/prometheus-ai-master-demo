package nn;/*
This class is used to represent sensors the drone is equiped with.
Each sensor has a name associated with it, the position of the sensor relative to the drone,
 what type of WorldObject it is sensitive to, and whether it is a positive or negative sensor.

All x and y values assume that the robot currently has direction=0 (i.e the drone is facing due north)

An example sensor would be:
name = "Forward Left Immovable"
signal = new Immovable()
x = "-1"
y = "1"
positive = true

If the object being sensed is different from the signal type, then a 0 is given to the Drone
If the object being sensed matches the signal type, then 1 is returned if (positive==True) and -1 else

**** TO DO ******
Currently the Drone object does all the work figuring out the value the sensor returns.
This code should be moved here so the sensors themselves work it out and the Drone just asks for it.
*/

import java.text.MessageFormat;

public class Basic_Sensor extends Sensor {

	private Class<? extends WorldObject> signal;
	private int x;
	private int y;
	private int range = 1;
	private boolean positive = true;

	public Basic_Sensor(WorldObject signal, int x, int y, boolean positive) { //constructor for when you want to specify positive/negative
		this.signal = signal.getClass();
		this.x = x;
		this.y = y;
		this.positive = positive;
		String[] sParams = {"x","y"};
		double[] iParams = {this.x,this.y};
		super.setTuple(this.signal.toString(),sParams,iParams);


	}
	public Basic_Sensor(WorldObject signal, int x, int y) { //constructor when you assume a sensor is positive (the normal workflow)
		this.signal = signal.getClass();
		this.x = x;
		this.y = y;
		String[] sParams = {"x","y"};
		double[] iParams = {this.x,this.y};
		super.setTuple(this.signal.toString(),sParams,iParams);

	}

	//setter/getter methods
	public Class getSignal() {
		return this.signal;
	}

	public int[] getX() {
		return new int[]{this.x};
	}

	public int[] getY() {
		return new int[]{this.y};
	}
	public int getRange(){
		return this.range;
	}
	//**TO DO **
	//This function takes a WorldObject and returns the value appropriate with this sensor
	// idk if use int or double makes more sense
	private WorldObject getObject(TheWorld world, int[][] visible_cells){
		WorldObject[][] worldArray = world.getWorldArray();

		WorldObject visible_object = null;

		for(int i=0;i<this.range;i++) {
			int x = visible_cells[i][0];
			int y = visible_cells[i][1];
			if(worldArray[y][x] instanceof WorldObject) {
				visible_object = worldArray[y][x];
			}
			else {
				visible_object = null;
			}
			i++;
		}
		return visible_object;

	}

	public double score(TheWorld world, int[][] visible){
		int weight = -1;
		if(this.positive) weight = 1;
		WorldObject object = getObject(world, visible);

		if(object==null) {
			return 1.0 * weight;
		}
		else if(object.getClass().equals(this.getSignal())) {
			return -1.0 * weight;
		}
		else{
			return 0.0;
		}
	}

	//depreciated, delete when score() is made
	public int weight() {
		if(this.positive) {
			return 1;
		}
		else {
			return 0;
		}
	}


	//TODO double check before integrating
	public String simpleToString(){
		StringBuilder params = new StringBuilder();
		for(int i=0;i<super.getSParams().length;i++){
			params.append(getSParams()[i]);
			params.append(getIParams()[i]);
		}
		return MessageFormat.format(
				"{0}{1}",
				getSignal(),
				params);
	}


}
