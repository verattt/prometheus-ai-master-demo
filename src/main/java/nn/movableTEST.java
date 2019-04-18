package nn;

import scala.util.regexp.Base;
import tags.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static nn.DroneMain.buildSquareWorld;
import static nn.DroneMain.drawLine;
import static nn.demo.generatePath;

public class movableTEST {
   public static TheWorld world;
   public static Drone testDrone;

    public static List<Tuple> setup() {



        Action fl = new Action("fl", -1, 1, 0);
        Action f = new Action("f", 0, 1, 0);
        Action fr = new Action("fr", 1, 1, 0);
        Action l = new Action("l", -1, 0, 0);
        Action r = new Action("r", 1, 0, 0);
        Action bl = new Action("bl", -1, -1, 0);
        Action b = new Action("b", -1, 0, 0);
        Action br = new Action("br", -1, 1, 0);
        Action rl = new Action("rl", 0, 0, -.25);
        Action rr = new Action("rr", 0, 0, .25);
        Action[] actions = {fl,f,fr,l,r,bl,b,br,rl,rr};

        Sensor fl_Im = new Basic_Sensor(new Immovable(), -1, 1);
        Sensor f_Im = new Basic_Sensor(new Immovable(), 0, 1);
        Sensor fr_Im = new Basic_Sensor(new Immovable(), 1, 1);
        Sensor fl_m = new Basic_Sensor(new Moveable(), -1, 1);
        Sensor f_m = new Basic_Sensor(new Moveable(), 0, 1);
        Sensor fr_m = new Basic_Sensor(new Moveable(), 1, 1);

        Sensor[] sensors = {fl_Im,f_Im,fr_Im};
        world = buildSquareWorld(12);
        drawLine(world,new Immovable(),2,5,6,5);
        drawLine(world,new Immovable(),8,7,12,7);
        drawLine(world,new Immovable(),5,0,5,5);
        drawLine(world,new Immovable(),8,5,8,7);
        drawLine(world,new Immovable(),8,9,8,12);
        List<Tuple> tuples = Arrays.asList(fl, f, fr, l, r, bl, b, br, rl, rr, fl_Im, f_Im, fr_Im);
        testDrone = new Drone("test",actions,sensors, world,3,4,0);

        //Tuple[] tuples ={fl,f,fr,l,r,bl,b,br,rl,rr,fl_Im,f_Im,fr_Im};
        return tuples;

    }


    public static void main(String[] args) {
        List<Tuple> tuples = setup();
        Iterator<Tuple> it = tuples.iterator();
        while(it.hasNext()){
            Tuple a = it.next();
            if(a instanceof Immovable){

            }
        }
        NN nn = new NN(world,testDrone);
        int[] xx = {1,2,3,4,5,6};
        int[] yy = {6,7,8,9,10};
        int Moveable[][] = generatePath(xx,yy);
        nn.think(10, tuples,Moveable[10%Moveable.length]);



    }
}
