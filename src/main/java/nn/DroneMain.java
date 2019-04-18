package nn;

import tags.Tuple;

import java.util.ArrayList;
import java.util.List;

public class DroneMain {
	public static void main(String[] args) {
		Action fl = new Action("fl",-1,1,0);
		Action f = new Action("f",0,1,0);
		Action fr = new Action("fr",1,1,0);
		Action l = new Action("l",-1,0,0);
		Action r = new Action("r",1,0,0);
		Action bl = new Action("bl",-1,-1,0);
		Action b = new Action("b",-1,0,0);
		Action br = new Action("br",-1,1,0);
		Action rl = new Action("rl",0,0,-.25);
		Action rr = new Action("rr",0,0,.25);
		//Action[] actions = {fl,f,fr,l,r,bl,b,br,rl,rr};

		Sensor fl_Im = new Basic_Sensor(new Immovable(),-1,1);
		Sensor  f_Im = new Basic_Sensor(new Immovable(), 0,1);
		Sensor fr_Im = new Basic_Sensor(new Immovable(), 1,1);

		//Sensor[] sensors = {fl_Im,f_Im,fr_Im};

		Tuple[] tuples ={fl,f,fr,l,r,bl,b,br,rl,rr,fl_Im,f_Im,fr_Im};
		List<Action> listactions= new ArrayList<Action>();
		List<Sensor> listsensors = new ArrayList<Sensor>();
		for(int i=0;i<tuples.length;i++){
			if(tuples[i] instanceof Action){
				listactions.add((Action) tuples[i]);
			}
			else if(tuples[i] instanceof Sensor){
				listsensors.add((Sensor)tuples[i]);
			}
		}

		Action[] actions = new Action[listactions.size()];
		Sensor[] sensors = new Sensor[listsensors.size()];

		//TODO HANDLE THE ITERATOR OF TUPLE
		for(int i=0;i<listactions.size();i++){
			actions[i]=listactions.get(i);
		}
		for(int j=0;j<listsensors.size();j++){
			sensors[j]=listsensors.get(j);
		}


		TheWorld world = buildSquareWorld(6);
		drawLine(world,new Immovable(),2,2,4,2);

		//drawLine(world,new Moveable(),3,3,3,3);

		/*
		drawLine(world,2,1,2,3);
		drawLine(world,0,5,4,5);
		drawLine(world,2,5,2,6);
		drawLine(world,3,7,3,8);
		drawLine(world,2,3,6,3);
		drawLine(world,6,2,6,5);
		drawLine(world,5,7,5,8);
		*/

		double[][] data = {
				{ 1, 1, 1,	 1, 1, 1,0,0,0,0,0,0,0},
				{-1,-1,-1,	-1,-1,-1,0,0,0,0,0,1,1},
				{ 0, 0, 0,	 0, 0, 0,0,0,0,0,0,0,0},

				{-1,-1, 1,	-1,-1, 1,0,0,0,0,0,0,0},
				{-1, 1, 1,	-1, 1, 1,0,0,0,0,0,0,0},
				{ 1, 1,-1,	 1, 1,-1,0,0,0,0,0,0,0},
				{ 1,-1,-1,	 1,-1,-1,0,0,0,0,0,0,0},
				{-1, 1,-1,	-1, 1,-1,0,0,0,0,0,0,0},
				{ 1,-1, 1,	 1,-1, 1,0,0,0,0,0,0,0},

				{ 0, 0, 1,	 0, 0, 1,0,0,0,0,0,0,0},
				{ 0, 1, 1,	 0, 1, 1,0,0,0,0,0,0,0},
				{ 1, 0, 0,	 1, 0, 0,0,0,0,0,0,0,0},
				{ 1, 1, 0,	 1, 1, 0,0,0,0,0,0,0,0},
				{ 1, 0, 1,	 1, 0, 1,0,0,0,0,0,0,0},
				{ 0, 1, 0,	 0, 1, 0,0,0,0,0,0,0,0},

				{-1,-1, 0,	-1,-1, 0,0,0,0,0,0,0,0},
				{-1, 0, 0,	-1, 0, 0,0,0,0,0,0,0,0},
				{ 0,-1,-1,	 0,-1,-1,0,0,0,0,0,0,0},
				{ 0, 0,-1,	 0, 0,-1,0,0,0,0,0,0,0},
				{-1, 0,-1,	-1, 0,-1,0,0,0,0,0,0,0},
				{ 0,-1, 0,	 0,-1, 0,0,0,0,0,0,0,0}
		};

		Drone testDrone = new Drone("test",actions,sensors, world,3,4,0);
		testDrone.trainDrone(data);
		world.display();

		testDrone.perceptronMatrix.print();
		testDrone.run(100,true);
		//System.exit(0);
	}
	public static TheWorld buildSquareWorld(int size) {
		TheWorld world = new TheWorld(size,size);
		for(int x=0; x<size;x++) {
			for(int y=0; y<size;y++) {
				if(x==0 || x== size-1 || y==0 || y==size-1) {
					world.add(new Immovable(), x, y);
				}
			}
		}
		return world;
	}

    public static void drawLine(TheWorld world, WorldObject obj, int x1, int y1, int x2, int y2) {
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
