package nn;/*
This is the main method to test the WorldObject alone
Depreciated by DroneMain, since it is strictly more useful
The code here may be nice to refernce though, which is why it hasn't been deleted
*/

import meta.MapVisualizer;
import meta.Node;
import meta.TreeMap;
import tags.Tuple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static nn.DroneMain.buildSquareWorld;


public class Main {
	public static void setup()
	{

//		if(iterations==200) {
//			// System.out.println(Toadd.getInputTag().simpleToString());
//			rootT = new TreeMap<Node> (new Node (testDrone.getX()*7-50,testDrone.getY()*-7+50,"root"));
//			rootT.addChild(new TreeMap<Node> (new Node (testDrone.getX()*7-50,testDrone.getY()*-7+57,"WAYPOINT")));
//			localmv.addTreeNode(rootT);
//		}
//
//		//TODO 不让覆盖重新覆盖waypoint
//		rootT = addWaypoint(7,sensorData,rootT);
//		localmv.addTreeNode(rootT);

	}

	public static void main(String[] args) {


		MapVisualizer mv  = new MapVisualizer();



		//Initialize the world
		Tuple fl_Im = new Basic_Sensor(new Immovable(),-1,1);
		Tuple f_Im = new Basic_Sensor(new Immovable(), 0,1);
		Tuple fr_Im = new Basic_Sensor(new Immovable(), 1,1);
		Tuple fl_m = new Basic_Sensor(new Moveable(),-1,1);
		Tuple f_m = new Basic_Sensor(new Moveable(), 0,1);
		Tuple fr_m = new Basic_Sensor(new Moveable(), 1,1);
		ArrayList<Tuple> in =  new ArrayList<>();
		in.add(fl_Im); in.add(f_Im); in.add(fr_Im);
//		NN a =  new NN();
		List<Tuple> output = new ArrayList<>();
		output.add(fl_m);
		output.add(f_m);
		output.add(fr_m);
		Iterator<Tuple>oi = output.iterator();
		Iterator<Tuple>oicopy = output.iterator();
		TreeMap<Node> root = new TreeMap<Node>(new Node(0,14,"w"));
		int count=3;
		while(count>0){
			//if(oi.next() instanceof Node){
				System.out.println(root.getChildren().size());
				root.addChild(new TreeMap<Node>(new Node(14,0,"w")));
count--;
			}
	//}
			mv.addTreeNode(root);
		mv.run();
		TreeMap<Node> newMap = new TreeMap<Node>(new Node(11,14,"w"));
		root.addChild(newMap);
		mv.addTreeNode(root);
	}


		/*int iterations = 100;					//Set the number of iterations we want of step (100 for now)
		boolean continueSimulation = true;		//Boolean deciding whether the program runs through another simulation

		while(continueSimulation){

			for(int i = 0; i< iterations; i++){
				world.display();				//Show the current state of the world
				world.step();					//Run a step throughout the world
				try{
					TimeUnit.SECONDS.sleep(1);
				}
				catch(InterruptedException e){
					System.out.println("Wait issue");
				}								//This try/catch delay is just so that the simulation doesn't go too fast
			}
			int userChoice = JOptionPane.showConfirmDialog(null, "Would you like to run the simulation again?", "Simulation Over", JOptionPane.YES_NO_OPTION);
			//User's choice (yes or no) in the option pane is saved as an integer
			if (userChoice== JOptionPane.YES_OPTION){
												//If yes, then we allow the loop to go again
			}
			else if(userChoice == JOptionPane.NO_OPTION){
				continueSimulation = false;		//otherwise, we end the simulation
			}
		}
		System.exit(0);							//End of program (use the syscall to close the window at the end as well)
		*/


	private static void drawLine(TheWorld world, Immovable immovable, int i, int i1, int i2, int i3) {

	}
	/*
	private static TheWorld buildWorld(int rows, int columns){

		TheWorld w = new TheWorld(rows, columns);//Actually creating the world with the specified sizes


		Immovable i1 = new Immovable();
		Immovable i2 = new Immovable();
		Immovable i3 = new Immovable();
		Immovable i4 = new Immovable();
		Immovable i5 = new Immovable();			//Creating 5 immovable objects

		Moveable m1 = new Moveable();
		Moveable m2 = new Moveable();
		Moveable m3 = new Moveable();			//Creating 3 moveable objects
		Autonomous a2 = new Autonomous();


		Autonomous a1 = new Autonomous();		//Creating 2 autonomous objects


		w.add(a1, 4, 4);

		w.add(a2, 6, 7);

		w.add(m1, 3, 4);
		w.add(m2, 6, 3);
		w.add(m3, 1, 2);

		w.add(i1, 2, 1);
		w.add(i2, 9, 8);
		w.add(i3, 5, 6);
		w.add(i4, 7, 9);
		w.add(i5, 0, 0);						//Assign the objects to the world at different locations

		return w;

	}
	*/
	private static void drawLine(TheWorld world, WorldObject obj, int x1, int y1, int x2, int y2) {
		// delta of exact value and rounded value of the dependent variable
		int d = 0;

		int dx = Math.abs(x2 - x1);
		int dy = Math.abs(y2 - y1);

		int dx2 = 2 * dx; // slope scaling factors to
		int dy2 = 2 * dy; // avoid floating point

		int ix = x1 < x2 ? 1 : -1; // increment direction
		int iy = y1 < y2 ? 1 : -1;

		int x = x1;
		int y = y1;

		if (dx >= dy) {
			while (true) {
				plot(world, obj, x, y);
				if (x == x2)
					break;
				x += ix;
				d += dy2;
				if (d > dx) {
					y += iy;
					d -= dx2;
				}
			}
		} else {
			while (true) {
				plot(world, obj, x, y);
				if (y == y2)
					break;
				y += iy;
				d += dx2;
				if (d > dy) {
					x += ix;
					d -= dy2;
				}
			}
		}
	}

	public static void plot(TheWorld world, WorldObject obj, int x, int y) {
		int x_max = world.getWorldArray()[0].length;
		int y_max = world.getWorldArray().length;
		if(x < x_max && y < y_max && world.getWorldArray()[y][x] ==null) {
			world.add(obj, x, y);
		}

	}
	}

