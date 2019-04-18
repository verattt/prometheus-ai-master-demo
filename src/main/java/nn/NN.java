package nn;

import Interfaces.SensorInput;
import com.lowagie.text.Meta;
import meta.*;
import tags.Fact;
import tags.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static nn.DroneMain.buildSquareWorld;
import static nn.DroneMain.drawLine;

public class NN  {
    List<Action> listactions= new ArrayList<Action>();
    List<Sensor> listsensors = new ArrayList<Sensor>();
    Drone testDrone;
    TheWorld world;

    public NN(TheWorld world, Drone testDrone){
        this.testDrone=testDrone;
        this.world=world;
    }


//    public void setup(List<Tuple> tuples){
//        for(int i=0;i<tuples.size();i++){
//            if(tuples.get(i) instanceof Action){
//                this.listactions.add((Action) tuples.get(i));
//            }
//            else if(tuples.get(i) instanceof Sensor){
//                this.listsensors.add((Sensor)tuples.get(i));
//            }
//        }
//        double[][] data = {
//                { 1, 1, 1,	 1, 1, 1,0,0,0,0,0,0,0},
//                {-1,-1,-1,	-1,-1,-1,0,0,0,0,0,1,1},
//                { 0, 0, 0,	 0, 0, 0,0,0,0,0,0,0,0},
//
//                {-1,-1, 1,	-1,-1, 1,0,0,0,0,0,0,0},
//                {-1, 1, 1,	-1, 1, 1,0,0,0,0,0,0,0},
//                { 1, 1,-1,	 1, 1,-1,0,0,0,0,0,0,0},
//                { 1,-1,-1,	 1,-1,-1,0,0,0,0,0,0,0},
//                {-1, 1,-1,	-1, 1,-1,0,0,0,0,0,0,0},
//                { 1,-1, 1,	 1,-1, 1,0,0,0,0,0,0,0},
//
//                { 0, 0, 1,	 0, 0, 1,0,0,0,0,0,0,0},
//                { 0, 1, 1,	 0, 1, 1,0,0,0,0,0,0,0},
//                { 1, 0, 0,	 1, 0, 0,0,0,0,0,0,0,0},
//                { 1, 1, 0,	 1, 1, 0,0,0,0,0,0,0,0},
//                { 1, 0, 1,	 1, 0, 1,0,0,0,0,0,0,0},
//                { 0, 1, 0,	 0, 1, 0,0,0,0,0,0,0,0},
//
//                {-1,-1, 0,	-1,-1, 0,0,0,0,0,0,0,0},
//                {-1, 0, 0,	-1, 0, 0,0,0,0,0,0,0,0},
//                { 0,-1,-1,	 0,-1,-1,0,0,0,0,0,0,0},
//                { 0, 0,-1,	 0, 0,-1,0,0,0,0,0,0,0},
//                {-1, 0,-1,	-1, 0,-1,0,0,0,0,0,0,0},
//                { 0,-1, 0,	 0,-1, 0,0,0,0,0,0,0,0}
//        };
//        Action[] actions = new Action[listactions.size()];
//        Sensor[] sensors = new Sensor[listsensors.size()];
//
//        //TODO HANDLE THE ITERATOR OF TUPLE
//        for(int i=0;i<listactions.size();i++){
//            actions[i]=listactions.get(i);
//        }
//        for(int j=0;j<listsensors.size();j++){
//            sensors[j]=listsensors.get(j);
//        }
//        this.world = buildSquareWorld(12);
//        drawLine(world,new Immovable(),2,5,6,5);
//        drawLine(world,new Immovable(),8,7,12,7);
//        drawLine(world,new Immovable(),5,0,5,5);
//        drawLine(world,new Immovable(),8,5,8,7);
//        drawLine(world,new Immovable(),8,9,8,12);
//
//        //test
//        drawLine(world,new Immovable(),2,3,5,3);
//
//        world.display();
//
//        this.testDrone = new Drone("test",actions,sensors, world,3,4,0);
//       // this.testDrone.trainDrone(data);
//
//
//    }

    public List<Tuple> thinkG(int iterations,List<Tuple> input){
       // setup(input);
        List<Tuple> output =new ArrayList<>();

        int[][] visible =  testDrone.getAllVisible();
        this.world.display(testDrone.getName(),visible);

        if(world.getWorldArray()[visible[0][1]][visible[0][0]]!=null){
               Tuple left = world.getWorldArray()[visible[0][1]][visible[0][0]];
                Fact sensor =(new Fact(left.getLabel()+"(x"+visible[0][0]+",y"+visible[0][1]+")"));
                sensor.setLabel("Sensor");
                output.add(sensor);
               output.add(new Fact("left"+left.getLabel()+"()"));

               //System.out.println(left.getLabel()+Arrays.toString(left.getSParams())+Arrays.toString(left.getIParams()));

        }
        else{Fact sensor = new Fact("Nothing(x"+visible[0][0]+",y"+visible[0][1]+")");
            sensor.setLabel("Sensor");
                output.add(sensor);
        }

        if(world.getWorldArray()[visible[1][1]][visible[1][0]]!=null){
            Tuple front = world.getWorldArray()[visible[1][1]][visible[1][0]];
            Fact sensor =(new Fact(front.getLabel()+"(x"+visible[1][0]+",y"+visible[1][1]+")"));
            sensor.setLabel("Sensor");
            output.add(sensor);
            //System.out.println(front.getLabel()+Arrays.toString(front.getSParams())+Arrays.toString(front.getIParams()));
            output.add(new Fact("front"+front.getLabel()+"()"));
        }
        else{Fact sensor = new Fact("Nothing(x"+visible[1][0]+",y"+visible[1][1]+")");
            sensor.setLabel("Sensor");
            output.add(sensor);}

        if(world.getWorldArray()[visible[2][1]][visible[2][0]]!=null){
            Tuple right = world.getWorldArray()[visible[2][1]][visible[2][0]];
            Fact sensor = new Fact(right.getLabel()+"(x"+visible[2][0]+",y"+visible[2][1]+")");
            sensor.setLabel("Sensor");
            output.add(sensor);
            //System.out.println(right.getLabel()+Arrays.toString(right.getSParams())+Arrays.toString(right.getIParams()));
            output.add(new Fact("right"+right.getLabel()+"()"));



        }
        else{
            Fact sensor = new Fact("Nothing(x"+visible[2][0]+",y"+visible[2][1]+")");
            sensor.setLabel("Sensor");
            output.add(sensor);
        }

        return output;



    }

    public List<Tuple> think(int iterations, List<Tuple> input,int[] addMoveable) {

       // MapVisualizer mv =new MapVisualizer();
        //setup(input);
        int xy[]=null;
        Action rl = new Action("rl", 0, 0, -.25);
        Action rr = new Action("rr", 0, 0, .25);
        // TODO FINISH BUIDLING GRAPH
        //mv.run();
        //TreeMap<Node> root = new TreeMap<Node>(new Node(this.testDrone.getX()*5,this.testDrone.getY()*-5,"w"));
        //mv.addTreeNode(root);
        int[] xx = {1,2,3,4,5,6};
        int[] yy = {6,7,8,9,10};
        int Moveable[][] = generatePath(xx,yy);

        while(iterations>0) {


            List<Tuple> output = new ArrayList<Tuple>();
            xy= this.world.addMoveable(addMoveable);
            int[][] visible = testDrone.getAllVisible();
            this.world.display(testDrone.getName(), visible);


            // this.world.addMoveable();
            for (int i = 0; i < input.size(); i++) {
                if (input.get(i) instanceof Sensor) {
                    output.add(input.get(i));
                }
            }

            try {
                Action action = this.testDrone.makeDecision();
                output.add(action);
                //TreeMap<Node> b= META.think(1,output,testDrone);
                //move to think in meta
                if(equals(action,rl)||equals(action,rr)){

                TreeMap<Node> a = new TreeMap(new Node(this.testDrone.getX()*5,this.testDrone.getY()*-5,"w"));
          //      root.addChild(a);
            //    mv.addTreeNode(root);
             //   mv.update();
               // root=a;
            }

                output.add(action);
            } catch (indecisiveException e) {

            }
            //int[][] visible = testDrone.getAllVisible();
            //System.out.println(Arrays.deepToString(visible));
            //System.out.println(this.direction);
            //this.world.display(testDrone.getName(), visible);
            try{world.step(testDrone.getName(),testDrone.getX(),testDrone.getY(),testDrone.getDirection());
                try{
                    TimeUnit.SECONDS.sleep(1);
                }
                catch(InterruptedException e){
                    System.out.println("Wait issue");
                }	}
            catch(crashException e) {
                System.out.println("CRASH at (" + testDrone.getX() + "," + testDrone.getY() + ")");
                break;
            }

            if(xy!=null){
                world.removeMoveable(xy);
            }



           iterations--;
        }


//        //testDrone.perceptronMatrix.print();
//        MapVisualizer mv  = new MapVisualizer();
//        //List<Tuple> output=  new ArrayList<>();
//        List<Tuple> copy  = input;
//        Iterator<Tuple> it = input.iterator();
//        Iterator<Tuple> itcopy = copy.iterator();
//        TreeMap<Node> map = new TreeMap<>();
//        while(it.hasNext()){
//            if(it.next() instanceof Basic_Sensor){
//                double[] temp = itcopy.next().getIParams();
//                Node a  = new Node(temp[0],temp[1],"w");
//                output.add(a);


//            }


//        }
        List<Tuple> s = new ArrayList<Tuple>();
        return s;

    }

    public static boolean equals(Action a,Action b){
        return((a.getName() ==b.getName())&&(a.getXDelta() == b.getXDelta())&&a.getYDelta() == b.getYDelta());						// requires name and delta values, but not exception

    }


    public int[][] generatePath(int[] xx, int[] yy){
        int[][] Moveable ={{10,1},{10,6},{7,6},{7,1}};



        int[][] path = new int [2*(xx.length+yy.length)-2][2];
        for(int i=0;i<xx.length;i++){
            path[i][0] = yy[0];
            path[i][1] = xx[i];
        }
        for(int i=xx.length;i<xx.length+yy.length-1;i++){
            path[i][0]  =yy[i-xx.length+1];
            path[i][1] = xx[xx.length-1];
        }
        int j =xx.length-1;
        for(int i=xx.length+yy.length-1;i<2*xx.length+yy.length-1;i++){
            path[i][0]  =yy[yy.length-1];
            path[i][1] = xx[j];
            j--;

        }
        j=yy.length-2;
        for(int i=2*xx.length+yy.length-1;i<2*xx.length+2*yy.length-2;i++){
            path[i][0]  =yy[j];
            path[i][1] = xx[0];
            j--;

        }
        return path;
    }



}
