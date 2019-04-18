package nn;

import tags.Fact;

import java.util.concurrent.TimeUnit;


public class Drone {
	public PerceptronMatrix perceptronMatrix;
	private String name;
	private int x;
	private int y;
	private double direction;
	private TheWorld world;
	private Sensor[] sensors;
	private Action[] actions;

	public Drone(String name, Action[] actions, Sensor[] sensors, TheWorld world, int x, int y, int direction) {
		this.name = name;
		this.actions = actions;
		this.sensors = sensors;
		this.world = world;
		this.x=x;
		this.y=y;
		this.direction=direction;

		Autonomous drone = new Autonomous(this.name);

		drone.setDirection(direction);

		this.world.add(drone, this.x, this.y);
		this.perceptronMatrix = new PerceptronMatrix(this.sensors,this.actions);

	}

	//set and get methods
	public String getName() {
		return this.name;
	}
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	public double getDirection() {
		return this.direction;
	}

	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setDirection(float dir) {
		this.direction = dir;
	}

	public void addSensor(Sensor sensor, double[][] training_data){
		Sensor[] sensor_append = new Sensor[this.sensors.length+1];
		int i=0;
		for(Sensor snsr : this.sensors){
			sensor_append[i] = snsr;
			i++;
		}
		sensor_append[sensor_append.length-1] = sensor;
		this.sensors = sensor_append;
		this.perceptronMatrix.addSensor(sensor, training_data);
	}

	public void addAction(Action action, double[][] training_data){
		Action[] action_append = new Action[this.actions.length+1];
		int i=0;
		for(Action actn : this.actions){
			action_append[i] = actn;
			i++;
		}
		action_append[action_append.length-1] = action;
		this.actions = action_append;
		this.perceptronMatrix.addAction(action,training_data);
	}
	//uses training data to train the drone's perceptron matrix
	public void trainDrone(double[][] training_data) {
		this.perceptronMatrix.learnAllWeights(training_data);
	}

	//gets the visible x and y coordinates based on the drone's current orientation
	private int[][] getVisible(Sensor sensor){

		int[][] visible = new int[this.sensors.length][2];
		int i = 0;
		for(int x : sensor.getX()){
			for(int y : sensor.getY()){
				if (this.direction < 0.25) {		//up
					visible[i][0] = this.x + x;
					visible[i][1] = this.y - y;
				}
				else if (this.direction < 0.5) {	//right
					visible[i][0] = this.x + y;
					visible[i][1] = this.y + x;
				}
				else if (this.direction < 0.75) {	//down
					visible[i][0] = this.x - x;
					visible[i][1] = this.y + y;
				}
				else if (this.direction < 1.0) {	//left
					visible[i][0] = this.x - y;
					visible[i][1] = this.y - x;
				}
			}
			i++;
		}
		return visible;


	}
	public int[][] getAllVisible(){
		int[][] visible = new int[this.sensors.length][2];
		int i = 0;
		for(Sensor sensor : this.sensors){
			for(int x : sensor.getX()){
				for(int y : sensor.getY()){
					if (this.direction < 0.25) {		//up
						visible[i][0] = this.x + x;
						visible[i][1] = this.y - y;
					}
					else if (this.direction < 0.5) {	//right
						visible[i][0] = this.x + y;
						visible[i][1] = this.y + x;
					}
					else if (this.direction < 0.75) {	//down
						visible[i][0] = this.x - x;
						visible[i][1] = this.y + y;
					}
					else if (this.direction < 1.0) {	//left
						visible[i][0] = this.x - y;
						visible[i][1] = this.y - x;
					}
				}
			}
			i++;
		}
		return visible;
	}

	//uses the currently visible objects as inputs for the drone's perceptron matrix
	//perceptron matrix then makes a decision and outputs an action for the drone, which the drone then takes
	//if the drone is indecisive it will throw an error that will be caught in the run() function
	public Action makeDecision() throws indecisiveException {
		int i=0;
		double[] inputs = new double[this.sensors.length];
		for(Sensor sensor : this.sensors) {
			int[][] visible = getVisible(sensor);
			inputs[i] = sensor.score(world, visible);
			i++;
		}

		this.perceptronMatrix.setInputs(inputs);
		try {
			Action decision = this.perceptronMatrix.makeDecision();
			takeAction(decision);
			return  decision;
		}
		catch(indecisiveException e) {
			throw e;
		}
	}

	//takes an action and updates the drone's position and orientation accordingly
	public void takeAction(Action action) {
		//If the drone is not facing due north then we have to tranlate what delta_x and delta_y would mean
		//Probably could be done cleverly with some linear transformations, but this naive approach works fine

		if(this.direction<0.25) {
			if(this.world.getWorldArray()[y-1][x]instanceof Moveable){
				try{
					TimeUnit.SECONDS.sleep(1);
				}
				catch(InterruptedException e){
					System.out.println("Wait issue");
				}
			}
			else{
			this.x+= action.getXDelta();
			this.y-= action.getYDelta();
				this.direction += action.getDirectionDelta();
				int temp = Math.floorMod((int) (100*this.direction), 100);
				this.direction = temp/100.0;
			}
		}
		else if(this.direction<0.5) {
			if(this.world.getWorldArray()[y][x+1]instanceof Moveable){
				try{
					TimeUnit.SECONDS.sleep(1);
				}
				catch(InterruptedException e){
					System.out.println("Wait issue");
				}
			}
			else{
			this.y+= action.getXDelta();
			this.x+= action.getYDelta();
				this.direction += action.getDirectionDelta();
				int temp = Math.floorMod((int) (100*this.direction), 100);
				this.direction = temp/100.0;
			}
		}
		else if(this.direction<0.75) {
			if(this.world.getWorldArray()[y+1][x]instanceof Moveable){
				try{
					TimeUnit.SECONDS.sleep(1);
				}
				catch(InterruptedException e){
					System.out.println("Wait issue");
				}
			}
			else{
			this.x-= action.getXDelta();
			this.y+= action.getYDelta();
				this.direction += action.getDirectionDelta();
				int temp = Math.floorMod((int) (100*this.direction), 100);
				this.direction = temp/100.0;
			}
		}
		else if(this.direction<1) {
			if (this.world.getWorldArray()[y][x - 1] instanceof Moveable) {
				try{
					TimeUnit.SECONDS.sleep(1);
				}
				catch(InterruptedException e){
					System.out.println("Wait issue");
				}
			} else {
				this.y -= action.getXDelta();
				this.x -= action.getYDelta();
				this.direction += action.getDirectionDelta();
				int temp = Math.floorMod((int) (100 * this.direction), 100);
				this.direction = temp / 100.0;
			}
		}
//		this.direction += action.getDirectionDelta();
//		int temp = Math.floorMod((int) (100*this.direction), 100);
//		this.direction = temp/100.0;
	}

	//places the drone in the world and lets the drone run for a given number of steps
	//if the drone meets an indecisive decision, it runs a simulation to try and learn what to do
	//if the drone crashes, the run ends (hopefully shouldn't happen)
	public void run(int itterations,boolean addMovable) {


		int xy[]=null;
		//while(itterations>0) {
			if(addMovable==true){
				//xy= world.addMoveable();
			}
			//System.out.println(itterations);
			int[][] visible = getAllVisible();
			//System.out.println(Arrays.deepToString(visible));
			//System.out.println(this.direction);
			world.display(this.name, visible);
			//world.display();
			try {
				makeDecision();
			}
			catch(indecisiveException e) {
				runSimulation();
				//break;
			}
			/*try {
				world.step(this.name,this.x,this.y,this.direction);
				try{
					TimeUnit.SECONDS.sleep(1);
				}
				catch(InterruptedException e){
					System.out.println("Wait issue");
				}								//This try/catch delay is just so that the simulation doesn't go too fast
			}
			catch(crashException e) {
				System.out.println("CRASH at (" + this.x + "," + this.y + ")");
				break;
			}
			if(addMovable==true&& xy!=null){
				world.removeMoveable(xy);
			}
			itterations--;
		}*/
	}

	//drone runs a simulated world where the drone learns new actions if it faces a new situation
	//unimplemented currently ***TO DO***
	private void runSimulation() {
		//to fill later
	}



}
