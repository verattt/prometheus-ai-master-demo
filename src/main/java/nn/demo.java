package nn;

import apple.laf.JRSUIUtils;
import com.google.inject.Guice;
import es.api.ExpertSystem;
import knn.api.KnowledgeNode;
import knn.api.KnowledgeNodeNetwork;
import knn.api.KnowledgeNodeParseException;
import meta.*;
import meta.TreeMap;
import org.apache.commons.lang3.StringUtils;
import prometheus.api.Prometheus;
import prometheus.guice.PrometheusModule;
import tags.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

import static nn.DroneMain.buildSquareWorld;

import static nn.DroneMain.drawLine;

public class demo {
    public static TheWorld world;
    public static Drone testDrone;
    private static KnowledgeNodeNetwork knn;
    private static KnowledgeNodeNetwork traceknn;
    private static ExpertSystem es;
    public static MapVisualizer mv;
    public static MapVisualizer localmv;
    public static  TreeMap<Node> rootT;
    private static NN nn;
    private static HashMap<String, Action> actionMap = new HashMap<>();
    private static HashMap<Tag,LocalMap> Lmap;

    public static void setup() {

        Prometheus prometheus = Guice.createInjector(new PrometheusModule()).getInstance(Prometheus.class);
        knn = prometheus.getKnowledgeNodeNetwork();
        traceknn = prometheus.getKnowledgeNodeNetwork();
        knn.loadData("drone.txt");
        es = prometheus.getExpertSystem();
        world = buildSquareWorld(12);
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
        Action[] actions = {fl, f, fr, l, r, bl, b, br, rl, rr, wait};
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
        drawHorizontalLine(world, new Immovable(), 2, 5, 5);
        drawHorizontalLine(world, new Immovable(), 8, 10, 7);
        drawVerticalLine(world, new Immovable(), 1, 4, 5);
        drawVerticalLine(world, new Immovable(), 5, 7, 8);
        drawVerticalLine(world, new Immovable(), 9, 12, 8);
        //drawVerticalLine(world, new Moveable(), 2, 2, 2);
        Lmap = new HashMap<>();
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
//        drawLine(world,new Immovable(),2,5,6,5);
//        drawLine(world,new Immovable(),8,7,12,7);
//        drawLine(world,new Immovable(),5,0,5,5);
//        drawLine(world,new Immovable(),8,5,8,7);
//        drawLine(world,new Immovable(),8,9,8,12);


    }


    public static void main(String[] args) {

        setup();
        int iterations = 500;





//        List<Argument> a =fact.getArguments();
//        System.out.println(Integer.parseInt(a.get(0).toString().replaceAll("[^0-9]", "")));

        world.display();
        int[] xx = {1,2,3,4,5};
        int[] yy = {6,7,8,9,10};
        int Moveable[][] = generatePath(xx,yy);

        //MapVisualizer mv = new MapVisualizer();
       // mv.run();
        MapVisualizer localmv = new MapVisualizer();
        localmv.run();
        NN nn = new NN(world, testDrone);
        TreeMap<Node> rootT = new TreeMap<>(new Node(testDrone.getX()*7-50,testDrone.getY()*-7+50,"start"));
//        Node root = new Node(testDrone.getX(), testDrone.getY(), "w");
//        TreeMap<Node> rootTree = new TreeMap<Node>(root);
//        mv.addTreeNode(rootTree);
        parseTest();
        int addit = iterations;
        while (iterations > 0) {
            world.display();
            int[] xy =  world.addMoveable(Moveable[addit%Moveable.length]);

//            mv.update();
            //first Layer NN
            List<Tuple> input = new ArrayList<>();
            List<Tuple> output = nn.thinkG(1, input);
            //Layer KNN and ES
            List<Tuple> tags = knn.think(1, output);
            List<Tuple> rec = es.think(1, tags);

            Set<Action> ac = new HashSet<>();
            for (Tuple rc : rec) {
                if (rc instanceof Recommendation) {
                    ac.add(actionMap.get(rc.getLabel().substring(1)));
                }
            }

            List<Fact> sensorData = new ArrayList<>();
            for(Tuple tag:output){
                if(tag instanceof Fact && tag.getLabel().equals("Sensor")){
                    sensorData.add((Fact) tag);
                }
            }





            List<KnowledgeNode> Toadd = addWaypoint(7,sensorData);
            //System.out.println(Toadd);
//
//                rootT = new TreeMap<Node> (new Node (testDrone.getX()*7-50,testDrone.getY()*-7+50,"root"));
//                localmv.addTreeNode(rootT);






            if(Toadd==null) {

            }


            else{
                for(KnowledgeNode  kn:  Toadd) {
                    TreeMap<Node> root = display(kn, true);
                    rootT.addChild(root);
                    localmv.addTreeNode(rootT);
                    rootT = root;
                    //localmv.addTreeNode(display(Toadd,true));
                }
            }
            //localmv.update();


//            testDrone.takeAction(actionMap.get("fl"));
//            try {
//                    world.step(testDrone.getName(), testDrone.getX(), testDrone.getY(), testDrone.getDirection());
//                    try {
//                        TimeUnit.SECONDS.sleep(1);
//                    } catch (InterruptedException e) {
//                        System.out.println("Wait issue");
//                    }
//                } catch (crashException e) {
//                    System.out.println("CRASH at (" + testDrone.getX() + "," + testDrone.getY() + ")");
//                }

//           System.out.println(tag.iterator().next().simpleToString());

            //todo 这里有bug
            if(iterations<0) {
                // take the action pushed forward
                if (ac.size() == 1) {
                    for (Action act : ac) {
                        testDrone.takeAction(act);
                    }
                    ac.clear();
                    try {
                        world.step(testDrone.getName(), testDrone.getX(), testDrone.getY(), testDrone.getDirection());
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            System.out.println("Wait issue");
                        }
                    } catch (crashException e) {
                        System.out.println("CRASH at (" + testDrone.getX() + "," + testDrone.getY() + ")");
                    }
                }

                // non-uniform action provided recompute
                else {
                    ac.clear();
                    iterations++;
                }
            }


            nn.think(1,input,Moveable[addit%Moveable.length]);







//            Tag xxx = new Fact("WAYPOINT(x4y4)");
//            System.out.println(knn.getKnowledgeNodes());
            if(xy!=null){
                world.removeMoveable(xy);
            }
            else{
                addit++;
            }

            iterations--;
            //System.out.println("Iteration: " +iterations);
            addit--;

        }
        System.out.println(knn.getKnowledgeNodes());
        Collection<KnowledgeNode> trace = traceknn.getKnowledgeNodes();
        Set<Tag> traces = traceknn.getActiveTags();
        Node r = (new Node(testDrone.getX() * 7 - 50, testDrone.getY() * -7 + 50, "root"));
        TreeMap<Node> traceMap = new TreeMap<Node>(r);
        System.out.println(traceknn.getActiveTags());
//        System.out.println(traceknn.getKnowledgeNodes());
        MapVisualizer tracemv =new MapVisualizer();
        tracemv.run();
       if(traces.size()>0) {
            for(Tag tr:traces){
                traceMap.addChild(ParseFact((Fact) tr));
            }
//           for (KnowledgeNode kn : trace) {
//               traceMap.addChild(display(kn, true));


           tracemv.addTreeNode(traceMap);
           tracemv.update();
       }
        for(LocalMap  allMap:  Lmap.values()){
                allMap.update();
        }





    }


    public static boolean samePos(Fact inputTag,  Fact sensor){

        Node temp = ParseFact(sensor);
        Node temp2 = ParseFact(inputTag);
//        if(temp.getCoordinate().equals(temp2.getCoordinate())){
//            System.out.println("same position as waypoint");
//        }
        return(temp.getCoordinate().equals(temp2.getCoordinate()));
    }
    public static boolean checkWP(Fact fact){
        String s = fact.getArguments().toString();
        System.out.println("s" +s);
        if(knn.getKnowledgeNode(new Fact("WAYPOINT" +s))!=null){
            return true;
        }
        return false;
    }
    public static List<KnowledgeNode> addWaypoint(int sizeLocalMap, List<Fact> sensorData) {

        List<KnowledgeNode> lkn = new ArrayList<>();
//        if (sizeLocalMap % 2 == 0) {
//            sizeLocalMap = sizeLocalMap + 1;
//        }
//        MapVisualizer localmv = new MapVisualizer();
//        localmv.run();

        //check waypoint existed or not
        //not exis add new one (DONE)
        //System.out.println("sensordata" + sensorData);
        // System.out.println("found existed" + checkWaypoint(sizeLocalMap));
        for (Tag a : sensorData) {
            int x = ((Fact) a).ParseFactToPos()[0];
            int y = ((Fact) a).ParseFactToPos()[1];
            List<Tag> check = checkWaypoint(sizeLocalMap, x, y);
            if (check.size() == 0) {
                String[] data = new String[1 + sensorData.size()];
                if (!(world.getWorldArray()[y][x] instanceof Immovable)) {
                    //System.out.println("new Waypoint");
                    String value = "WAYPOINT(x" + x + ",y" + y + ")";

                    data[0] = value;
                } else {
                    //System.out.println("new Waypoint");
                    String value = "WAYPOINT(x" + testDrone.getX() + ",y" + testDrone.getY() + ")";

                    data[0] = value;
                }

                for (int i = 1; i < data.length; i++) {
                    data[i] = sensorData.get(i - 1).simpleToString();
                }
                //Fact fact = new Fact(value);
                try {
                    KnowledgeNode kn = new KnowledgeNode(data);
                    //todo change test here
                    knn.addKnowledgeNode(kn);
                    traceknn.addActiveTag(kn.getInputTag());
                    System.out.println("newWaypoint " + knn.getKnowledgeNode(kn.getInputTag()));
                    Lmap.put(kn.getInputTag(), new LocalMap(kn, sizeLocalMap));
                    Lmap.get(kn.getInputTag()).run();


                    lkn.add(kn);
                } catch (KnowledgeNodeParseException e) {
                    System.out.println("Error: invalid form of KnowledgeNode");
                }
            }
        }





//                else{
//                    for (Tag mapInputTag: checkWaypoint(sizeLocalMap,x,y)){
//                        String value = mapInputTag.simpleToString();
//                        Set<Tag> mapinfo = knn.getKnowledgeNode(mapInputTag).getOutputTags();
//                        Set<Tag> newMapinfo = new CopyOnWriteArraySet<>();
//                        newMapinfo.add(a);
//
//
//
//                        if (((Fact)a).getPredicateName().contains("Moveable")) {
//                            traceknn.addActiveTag( a);
//                        }
//                        if (!mapinfo.contains(a)) {
//                            newMapinfo.add(a);
//                        }
//
//
//                        for (Tag fact : mapinfo) {
//                            //List<Argument> a =((Fact) fact).getArguments();
//                            //for (Tag newData : newMapinfo) {
//                            //List<Argument> aNew =((Fact) newData).getArguments();
//                            //if(!a.get(0).toString().equals(aNew.get(0).toString()))
//                            //if (!samePos((Fact) fact, (Fact) newData)) {
//                            newMapinfo.add(fact);
//                            //}
//                            // }
//
//                        }
//
//
//                        Fact abc = new Fact(value);
//                        List<Tag> newMapList = new ArrayList<>(newMapinfo);
//                        String[] data = new String[1 + newMapinfo.size()];
//                        data[0] = value;
//
//                        for (int i = 1; i < data.length; i++) {
//                            data[i] = newMapList.get(i - 1).simpleToString();
//                        }
//                        // System.out.println("data length" + data.length);
//
//                        try {
//                            KnowledgeNode kn = new KnowledgeNode(data);
//                            knn.addKnowledgeNode(kn);
//                            Lmap.get(kn.getInputTag()).updateKnowledgeNode(kn);
//                            for(LocalMap  allMap:  Lmap.values()){
//                                //if(allMap!=Lmap.get(kn.getInputTag()))
//                                allMap.hiding();
//                            }
//                            Lmap.get(kn.getInputTag()).update();
//
//
//                            // System.out.println("KN LENGTH" +  knn.getKnowledgeNode(kn.getInputTag()).getOutputTags().size());
//
//
//                            lkn.add(kn);
//
//                        } catch (KnowledgeNodeParseException e) {
//                            System.out.println("Error: invalid form of KnowledgeNode");
//                        }
//
//                    }
//
//            }
//
//
//
//        }

//        //else a  are  inside existed localmap range
        for (Tag a : sensorData) {
            int x = ((Fact) a).ParseFactToPos()[0];
            int y = ((Fact) a).ParseFactToPos()[1];
            List<Tag> check = checkWaypoint(sizeLocalMap, x, y);
            for (Tag mapInputTag : check) {
                String value = mapInputTag.simpleToString();
                Set<Tag> mapinfo = knn.getKnowledgeNode(mapInputTag).getOutputTags();
                Set<Tag> newMapinfo = new CopyOnWriteArraySet<>();
                newMapinfo.add(a);


                if (((Fact) a).getPredicateName().contains("Moveable")) {
                    traceknn.addActiveTag(a);
                }
//                if (!mapinfo.contains(a)) {
//                    newMapinfo.add(a);
//                }

                Vector va = ParseFact((Fact)a).getCoordinate();
                for (Tag fact : mapinfo) {

                    //List<Argument> a =((Fact) fact).getArguments();
                    //for (Tag newData : newMapinfo) {
                    //List<Argument> aNew =((Fact) newData).getArguments();
                    //if(!a.get(0).toString().equals(aNew.get(0).toString()))
                    //if (!samePos((Fact) fact, (Fact) newData)) {
                    if(!ParseFact((Fact) fact).getCoordinate().equals(va))
                    newMapinfo.add(fact);
                    //}
                    // }

                }


                Fact abc = new Fact(value);
                List<Tag> newMapList = new ArrayList<>(newMapinfo);
                String[] data = new String[1 + newMapinfo.size()];
                data[0] = value;

                for (int i = 1; i < data.length; i++) {
                    data[i] = newMapList.get(i - 1).simpleToString();
                }
                // System.out.println("data length" + data.length);

                try {
                    KnowledgeNode kn = new KnowledgeNode(data);
                    knn.addKnowledgeNode(kn);
                    Lmap.get(kn.getInputTag()).updateKnowledgeNode(kn);
                    for (LocalMap allMap : Lmap.values()) {
                        allMap.hiding();
                    }
                    Lmap.get(kn.getInputTag()).update();
                    // System.out.println("KN LENGTH" +  knn.getKnowledgeNode(kn.getInputTag()).getOutputTags().size());


                    lkn.add(kn);

                } catch (KnowledgeNodeParseException e) {
                    System.out.println("Error: invalid form of KnowledgeNode");
                }
            }
        }


            return lkn;

        }

    public static List<Tag> checkWaypoint(int sizeLocalMap,int currentX,int currentY) {
        List<Tag> list =new ArrayList<>();
        int[] xToCheck = new int[sizeLocalMap];
        int[] yToCheck =  new int[sizeLocalMap];
        for(int i=-sizeLocalMap/2; i<=sizeLocalMap/2; i++){
            xToCheck[i+sizeLocalMap/2] = currentX+i;
            yToCheck[i+sizeLocalMap/2] = currentY+i;
        }
        Set<Tag> localMapInfo = new HashSet<>();
        for(int x: xToCheck){
            for(int y: yToCheck){
                if(x>=0&&x<world.getWorldArray().length&&y>=0&&y<world.getWorldArray().length){
                    Fact temp =  new Fact("WAYPOINT(x"+x+",y"+y+")".replaceAll("\\s+",""));
                    if(knn.getKnowledgeNode(temp)!=null){
                        list.add(temp);
                    }
                }
            }
        }
        return list;
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

//    public static void display() {
//        JFrame mainFrame = new JFrame("AI LocalMAP");
//        mainFrame.setSize(300, 300);
//        mainFrame.setLayout(new GridLayout(1, 1));                                    //Initializing the window
//
//        JPanel grid = new JPanel();                                                    //Initializing the Jpanel
//        grid.setLayout(new GridLayout(3, 3));                                                        //and placing it in the window
//        mainFrame.add(grid);
//        //If this is the first call of the display() method
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {                    //Iterating through the 2D array of objects
//
//                JLabel currentObject = new JLabel("", SwingConstants.CENTER);
//                String currentTokenS = "";
//                if (i == 1 && j == 1) {
//
//                    currentObject.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
//                    currentObject.setText("W");
//                }
//                else if (i == 0 && j == 0) {
//                    currentObject.setText("M");
//                    currentObject.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
//                }
//                else if(i>0){
//                    currentObject.setBackground(Color.black);
//                    //currentObject.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
//                }
//                else{
//
//                    currentObject.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
//                }
//
//
//                grid.add(currentObject);
//
//            }
//
//
//            //Then also place it into the cell of the JPanel corresponding with the cell in the 2D array of objects
//
//
//        }
//
//        mainFrame.setVisible(true);  // Activate window
//    }


public static TreeMap<Node> display(KnowledgeNode kn,boolean r){
        if(!kn.isWaypoint()){
            return null;
        }
        else {

            Tag way = kn.getInputTag();
            List<Argument> b =((Fact) way).getArguments();
            int xw = Integer.parseInt(b.get(0).toString().replaceAll("[^0-9]",""))*7-50;
            int yw = Integer.parseInt(b.get(1).toString().replaceAll("[^0-9]",""))*-7+50;
            Node root = new Node(xw, yw, "WAYPOINT");
           // System.out.println(b);
            TreeMap<Node> rootTree = new TreeMap<>(root);



            Set<Tag> tags = kn.getOutputTags();
            for(Tag cur: tags){
                List<Argument> a =((Fact) cur).getArguments();
//                String si = a.get(0).toString();
//                int x = Integer.parseInt(StringUtils.substringBetween(si, "x", "y"))*7;7
                int x = Integer.parseInt(a.get(0).toString().replaceAll("[^0-9]",""))*7-50;
                int y = Integer.parseInt(a.get(1).toString().replaceAll("[^0-9]",""))*-7+50;
                Node newN = new Node(x,y,((Fact) cur).getPredicateName());
                rootTree.addChild(newN);



            }

            return rootTree;

        }


    }

    public static Node ParseFact(Fact f){
        List<Argument> a =f.getArguments();
        int x = Integer.parseInt(a.get(0).toString().replaceAll("[^0-9]",""))*7-50;
        int y = Integer.parseInt(a.get(1).toString().replaceAll("[^0-9]",""))*-7+50;
        Node newN = new Node(x,y,f.getPredicateName());
        return newN;
    }
    public static void parseTest(){
        Fact temp =  new Fact("WAYPOINT(x"+3+"y"+5+")");
        List<Argument> b =((Fact) temp).getArguments();
        String s = b.get(0).toString();
        int x = Integer.parseInt(StringUtils.substringBetween(s, "x", "y"));
        int y = Integer.parseInt(s.replaceAll("[^0-9]","")) - (x * 10);
        System.out.println(y);
}

    public static int[][] generatePath(int[] xx, int[] yy){
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

    //    public static TreeMap<Node> addWaypoint(int sizeLocalMap, List<Fact> sensorData,TreeMap<Node> tree) {
//        if (sizeLocalMap % 2 == 0) {
//            sizeLocalMap = sizeLocalMap + 1;
//        }
////        MapVisualizer localmv = new MapVisualizer();
////        localmv.run();
//
//        //check waypoint existed or not
//        //not exis add new one (DONE)
//        //System.out.println("sensor" + sensorData);
//       // System.out.println("found existed" + checkWaypoint(sizeLocalMap));
//        if(checkWaypoint(sizeLocalMap)==null){
//            String value  ="WAYPOINT(x"+testDrone.getX()+",y"+testDrone.getY()+")";
//            String[] data = new String[1+sensorData.size()];
//            data[0]=value;
//            for(int i=1;i<data.length;i++){
//                data[i]=sensorData.get(i-1).simpleToString();
//            }
//            //Fact fact = new Fact(value);
//            try{
//                KnowledgeNode kn = new KnowledgeNode(data);
//                knn.addKnowledgeNode(kn);
//                tree.addChild(display(kn,false));
//            }
//            catch (KnowledgeNodeParseException e){
//                System.out.println("Error: invalid form of KnowledgeNode");
//            }
//            System.out.println(Arrays.toString(data));
//            System.out.println("added");
//
//        }
//
////        else{
////            Tag mapInputTag = (checkWaypoint(sizeLocalMap));
////            String value  =mapInputTag.simpleToString();
////            Set<Tag> mapinfo = knn.getKnowledgeNode(mapInputTag).getOutputTags();
////            List<Tag> minfoArr = new ArrayList<>(mapinfo);
//////            if(mapinfo.size()<3)
////            //           knn.resetEmpty();
////            Set<Tag> newMapinfo = new CopyOnWriteArraySet<>();
////            TreeMap<Node> child = new TreeMap<>(ParseFact(((Fact)minfoArr.get(mapinfo.size()-1))));
////
////            for(int i=0;i<sensorData.size();i++) {
////                if(!mapinfo.contains((Tag) sensorData.get(i))){
////                    newMapinfo.add(sensorData.get(i));
////                    child.addChild(ParseFact((Fact)sensorData.get(i)));
////                }
////            }
////            tree.addChild(child);
////
////
////
////            for(Tag fact: mapinfo){
////                List<Argument> a =((Fact) fact).getArguments();
////                for(Tag newData: newMapinfo) {
////                    List<Argument> aNew =((Fact) newData).getArguments();
////                    if(!a.get(0).toString().equals(aNew.get(0).toString()))
////                    {
////                        newMapinfo.add(fact);
////                        tree.addChild(new TreeMap<Node>(ParseFact((Fact)fact)));
////                    }
////                }
////
////            }
////
////            Fact abc = new Fact(value);
////            List<Tag> newMapList = new ArrayList<>(newMapinfo);
////            String[] data = new String[1+newMapList.size()];
////            data[0]=value;
////
////            for(int i=1;i<data.length;i++){
////                data[i]=newMapList.get(i-1).simpleToString();
////            }
////
////            try{
////                KnowledgeNode kn = new KnowledgeNode(data);
////                knn.addKnowledgeNode(kn);
////
////
////
////            }
////            catch (KnowledgeNodeParseException e){
////                System.out.println("Error: invalid form of KnowledgeNode");
////            }
////
////
////        }
//        return tree;
//
//
//
//    }

}
