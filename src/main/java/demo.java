import com.google.inject.Guice;
import es.api.ExpertSystem;
import knn.api.KnowledgeNode;
import knn.api.KnowledgeNodeNetwork;
import meta.*;
import meta.TreeMap;
import nn.*;
import prometheus.api.Prometheus;
import prometheus.guice.PrometheusModule;
import tags.Argument;
import tags.Fact;
import tags.Tag;
import tags.Tuple;

import java.util.*;

import static nn.DroneMain.buildSquareWorld;

public class demo {
    public static int iterations =300;
    public static final boolean LocalMapVisible =true;
    public static int[][] Moveable;
    public static TheWorld world;
    public static Drone testDrone;
    //2 knns are required, ruleknn is for storing all the rules for thinking, which need to be reset every iteration.
    //knn is for us to store any explored knowledge, which will not be reset unless requested
    private static KnowledgeNodeNetwork knn;
    private static KnowledgeNodeNetwork ruleknn;
    private static ExpertSystem es;

    public static TreeMap<Node> globalMap;
    private static NN nn;
    private static HashMap<String, Action> actionMap = new HashMap<>();


    private static Meta meta;


    public static void setup() {

        //setup the knn and es layer
        Prometheus prometheus = Guice.createInjector(new PrometheusModule()).getInstance(Prometheus.class);
        Prometheus prometheus1 = Guice.createInjector(new PrometheusModule()).getInstance(Prometheus.class);
        ruleknn = prometheus1.getKnowledgeNodeNetwork();
        knn = prometheus.getKnowledgeNodeNetwork();

        ruleknn.loadData("drone.txt");
        es = prometheus1.getExpertSystem();

        //setup a  12*12 square world and add object along horizontalLine and Vertical Line according to need.
        world = buildSquareWorld(12);
        drawHorizontalLine(world, new Immovable(), 2, 5, 5);
        drawHorizontalLine(world, new Immovable(), 8, 10, 7);
        drawVerticalLine(world, new Immovable(), 1, 4, 5);
        drawVerticalLine(world, new Immovable(), 5, 7, 8);
        drawVerticalLine(world, new Immovable(), 9, 12, 8);

        //setup the testDrone's sensor and possible action for it to take, and the hash table for easier transfer from action to recommedation
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
        Action wait = new Action("Wait", 0, 0, 0);
        Action[] actions = {fl, f, fr, rl, rr, wait};
        Action[] actionss = {fl, f, fr, l, r, bl, b, br, rl, rr};
        for (Action ac : actions) {
            actionMap.put(ac.getName(), ac);
        }
        Sensor fl_Im = new Basic_Sensor(new Immovable(), -1, 1);
        Sensor f_Im = new Basic_Sensor(new Immovable(), 0, 1);
        Sensor fr_Im = new Basic_Sensor(new Immovable(), 1, 1);
        Sensor fl_m = new Basic_Sensor(new Moveable(), -1, 1);
        Sensor f_m = new Basic_Sensor(new Moveable(), 0, 1);
        Sensor fr_m = new Basic_Sensor(new Moveable(), 1, 1);
        Sensor[] sensors = {fl_Im, f_Im, fr_Im};
        testDrone = new Drone("test", actionss, sensors, world,4, 4, 0);



        //this data is for original nn that trained the drone to make decision, we are not using those functions
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
        testDrone.trainDrone(data);

        //this is to generate an array Movable that stored all position for the movable object to move along with.
        int[] xx = {1,2,3,4,5};
        int[] yy = {6,7,8,9,10};
        Moveable = generatePath(xx,yy);

        //set up the testdrone in world,nn and meta layer.
        world.display();
        nn = new NN(world, testDrone);
        meta = new Meta( knn, actionMap, new Node(testDrone.getX(),testDrone.getY(),"starting Position"), world,testDrone,LocalMapVisible);




    }


    public static void main(String[] args) {

        setup();
        int addit = iterations;


        //start the thread of painting the global map
        MapVisualizer mv = new MapVisualizer();
        mv.run();
        // initiate  the root as the starting position for global map in the mapvisualizer in main thread and globalMap in meta
        Node startPos =  new Node(testDrone.getX(),testDrone.getY(),"start");
        TreeMap<Node> rootT = new TreeMap<>(startPos);
        globalMap = rootT;

        while (iterations > 0) {

            //move the movable object along generated path that saved in Array Movable and return the last poition of the object
            //update world display
            int[] xy =  world.addMoveable(Moveable[addit%Moveable.length]);
            world.display(testDrone.getName(),testDrone.getAllVisible());

            //information pass from nn to es that get sensor information in output and recommendation in rec
            List<Tuple> output = nn.think(1, nn.readSimulator(world,testDrone));
            List<Tuple> rec =new ArrayList<>(es.think(1,ruleknn.think(1,output)));
            //fed both recommendation list and sensor data to meta
            List<Tuple> al= new ArrayList<Tuple>();
            al.addAll(output);
            al.addAll(rec);
            //meta think that move the testdrone and determine new information to update in mapvisualizer
            List<KnowledgeNode>  Toadd = meta.think(1,al);
            if(Toadd!=null){
                for(KnowledgeNode  kn:  Toadd) {
                    TreeMap<Node> root = display(kn, true);
                   mv.addTreeNode(root);
                }

            }
            mv.addTreeNode(meta.getGlobalMap());

            //after meta moved testDrone, update the testdrone position and world
            world = meta.sendDataStream();
            testDrone = meta.getTestDrone();
            world.display(testDrone.getName(),testDrone.getAllVisible());
            //if the Movable is not added successfully means there is obstacle in its moving path, movable will stay same position
            addit =addit +world.removeMoveable(xy);
            world.display(testDrone.getName(),testDrone.getAllVisible());
            mv.update();
            iterations--;


        }


        //after fixed iterations are over
        //show up all important information including movable object found position and waypoint position
        System.out.println(meta.getTraces());
        //show all LocalMapVisualization after iterations are over
        if(LocalMapVisible) {
            for (LocalMapVisualizer allMap : meta.getAllLocalMapVisualizing().values()) {
                allMap.update();
            }
        }





    }




    public static void drawHorizontalLine(TheWorld world, WorldObject obj, int x1, int x2, int y) {

        if(x1>x2){
            int temp = x2;
            x2=x1;
            x1=temp;
        }


        for(int i=x1;i<=x2;i++){
            WorldObject copy =obj.factory();
            plot(world,copy,i,y);
        }

    }
    public static void drawVerticalLine(TheWorld world, WorldObject obj, int y1, int y2, int x) {
        if(y1>y2){
            int temp = y2;
            y2=y1;
            y1=temp;
        }
        for(int i=y1;i<=y2;i++){
            WorldObject copy =obj.factory();
            plot(world,copy,x,i);
        }

    }

    public static void plot(TheWorld world, WorldObject obj, int x, int y) {
        int x_max = world.getWorldArray()[0].length;
        int y_max = world.getWorldArray().length;
        if(x < x_max && y < y_max && world.getWorldArray()[y][x] ==null) {
            world.add(obj, x, y);
        }

    }



// transform new information from meta from KnowledgeNodeinto Mapvisualizer form
public static TreeMap<Node> display(KnowledgeNode kn,boolean r){
        if(!kn.isWaypoint()){
            return null;
        }
        else {
            Tag way = kn.getInputTag();
            List<Argument> b =((Fact) way).getArguments();
            int xw = Integer.parseInt(b.get(0).toString().replaceAll("[^0-9]",""));//*7-50;
            int yw = Integer.parseInt(b.get(1).toString().replaceAll("[^0-9]",""));//*-7+50;
            Node root = new Node(xw, yw, "WAYPOINT");
            TreeMap<Node> rootTree = new TreeMap<>(root);



            Set<Tag> tags = kn.getOutputTags();
            for(Tag cur: tags){
                List<Argument> a =((Fact) cur).getArguments();
                int x = Integer.parseInt(a.get(0).toString().replaceAll("[^0-9]",""));//*7-50;
                int y = Integer.parseInt(a.get(1).toString().replaceAll("[^0-9]",""));//*-7+50;
                Node newN = new Node(x,y,((Fact) cur).getPredicateName());

                    rootTree.addChild(newN);


//            TreeMap<Node> traceMap = new TreeMap<Node>(startPos);
//            if(traces.size()>0) {
//                for(Tag tr:traces){
//                    traceMap.addChild(ParseFact((Fact) tr));
//                }



            }
            Set<Tag> traces = meta.getTraces();

            if(traces!=null) {
                for (Tag tr : traces) {
                    Node importantNodes = ParseFact((Fact) tr);
                    rootTree.addChild(importantNodes);
                }
            }

            return rootTree;

        }


    }
//transform fact that contains a point  position to a Node
    public static Node ParseFact(Fact f){
        List<Argument> a =f.getArguments();
        int x = Integer.parseInt(a.get(0).toString().replaceAll("[^0-9]",""));//*7-50;
        int y = Integer.parseInt(a.get(1).toString().replaceAll("[^0-9]",""));//*-7+50;
        Node newN = new Node(x,y,f.getPredicateName());
        return newN;
    }

//generate  a square-like path according to
    public static int[][] generatePath(int[] xx, int[] yy){
       // int[][] Moveable ={{10,1},{10,6},{7,6},{7,1}};



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
