package meta.internal;

import Interfaces.LayerInput;
import meta.Goal;
import meta.MapVisualizer;
import meta.Node;
import meta.SimulatorInterface.SimulatorController;
import meta.TreeMap;
import nn.Action;
import nn.Drone;
import tags.Recommendation;
import tags.Tuple;

import java.util.*;

public class META implements Runnable, LayerInput {

    private int cutOffDistance = 18;
    private int distanceToEdge = 6;
    private int collidisionCirculeRedius = 10;
    private MapVisualizer mv = null;
    private SimulatorController sc = new SimulatorController();
    private TreeMap<Node> root = new TreeMap<Node>();
    private TreeMap<Node> currentTree = new TreeMap<Node>();
    private List<Goal> goalList = new ArrayList<>();

    private boolean isExploringMap = true;

    //TODO: sensor live data see todo in SimulatorController.
    public META() {

    }


    public void setMv(MapVisualizer mv) {
        this.mv = mv;
    }

    @Override
    public void run() {
        System.out.println("Hello, META");


        try {
            Thread.sleep(2000); // Wait for TCP connection

        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("META run exception");
        }

        //set root node as a way point
        root = createWayPoint();
        currentTree = root;
        mv.addTreeNode(root);

        while (isExploringMap) {

            mv.addTreeNode(root); // update map visualizer
            List<Node> nodes = scan();
            for (Node na : nodes) { // add all see to current tree
                if (!na.getType().equals("wall")) {
                    currentTree.addChild(na);
                }

            }

            Node waypointNode = findLongestPossibleDirection(nodes);

            nodes = getOnlyWallNodes(nodes);
            currentTree.setWalls(nodes);

            if (waypointNode != null) { // maxwall distance > 30

                travelToNode(waypointNode, nodes);

                // stopped before target, make a way point of here
                // TODO: if returning to parent, do not create a new way point
                TreeMap<Node> newtree = createWayPoint();
                newtree.setParent(currentTree);
                currentTree.addChild(newtree);
                currentTree = newtree;

            } else {
                // TODO: bug!!!
                // could not find a place to go
                // go back to parent
                System.out.println("back to parent");
                travelToNode((Node) currentTree.getParent().getNode());
                currentTree = currentTree.getParent();
            }

        }
        System.out.println("finished exploring map");
        // Currently hard code a tracking object
        Node cube4 = sc.look();
        boolean isTracked = false;
        sc.rotate(10, true);
        while (!isExploringMap) {
            // TODO:
//            System.out.println("ll");
            Node see = sc.look();
            System.out.println(see.getObject());
            if (!see.getObject().equals(cube4.getObject())) {
//                System.out.println("not same obj");
                isTracked = true;
                if (!sc.check(1)) {
                    sc.rotate(90, false);
                }

//                see = sc.look();

            } else {

                if (isTracked) {
                    sc.stop();
                    isTracked = false;
                }


                double distance = calculateDistance(sc.getLocation(), see.getCoordinate());
                if (distance > 15) {
                    sc.move(distance - 10, false);
                }
            }

//            sc.stop();
            sleep(100);
        }

    }


    private void travelToNode(Node n, List<Node> nodes) {
        System.out.println("traveling to " + n.getCoordinate());
        Double currentHeading = sc.getRotation();
        sc.rotate(n.getAngle() - currentHeading, true);
        int errorThreshold = 0;
        boolean target = false;
        boolean tooClose = false;
        if (n.getType().equals("object")) {
            target = true;
        }
        while (!isArrivedNode(n)) {

            if (!tooClose) {


                if (target) {

                    if (sc.look().getObject().equals(n.getObject())) {
                        errorThreshold = 0;
                        if (!sc.check(0)) {
                            sc.move(calculateDistance(n.getCoordinate(), sc.getLocation()), false);
                        }

                    } else {
                        System.out.println("robot not see target");
                        sleep(500);
                        errorThreshold++;
                        if (errorThreshold > 5) {
                            System.out.println("error threshold greater than 5 ");
                            sc.stop();
                            if (calculateDistance(sc.getLocation(), n.getCoordinate()) <= 20) {
                                return;
                            } else {
                                System.out.println("scan around to find target");
                                sc.stop();
                                List<Node> see = scan();
                                for (Node s : see) {
                                    if (s.getObject().equals(n.getObject())) {
                                        travelToNode(n);
                                        return;
                                    }
                                }
                                // TODO: now robot could not see target, and is 20m away, return will go to next max dis
                                return;
                            }
                        }
                    }
                } else {
//                started
                    if (!sc.check(0)) {
                        sc.move(calculateDistance(n.getCoordinate(), sc.getLocation()), false);
                    }
                    sleep(100);
                }
            } else {
                sc.stop();
                return;
            }
            for (Node na : nodes) {
                // too small will detect close
                if (calculateDistance(na.getCoordinate(), sc.getLocation()) <= 5) {
                    tooClose = true;
                    sc.stop();
                }
            }
        }
        sc.stop();
    }


    private Node findLongestPossibleDirection(List<Node> nodeList) {
        // calculate new way point location and check it around 20 for all nodes

        //1. sort
        Collections.sort(nodeList, new Comparator<Node>() {
            public int compare(Node n1, Node n2) {
                return n1.getDistanceToParent() < n2.getDistanceToParent() ? 0 : -1;
            }
        });

        System.out.println("node size" + nodeList.size());

        for (int i = 0; i < nodeList.size(); i++) {
            Node n = nodeList.get(i);
            // 2. for each node, calculate a waypoint and check if for each has a at lease 10 for all nodes

            Vector<Double> wayPointLocation
                    = lineIntersectCircle(n.getCoordinate(), sc.getLocation(), collidisionCirculeRedius);

            boolean isGoingToCollide = false;
            for (int j = i + 1; j < nodeList.size(); j++) {
                Node n1 = nodeList.get(j);
                double x = n1.getCoordinate().get(0);
                double y = n1.getCoordinate().get(1);
                double x1 = sc.getLocation().get(0);
                double y1 = sc.getLocation().get(1);
                double x2 = wayPointLocation.get(0);
                double y2 = wayPointLocation.get(1);
                if (pointToSegDist(x, y, x1, y1, x2, y2) < distanceToEdge) {
                    isGoingToCollide = true;
                }
            }
            // TODO: 感觉这里是个bug，好像没有always true？？？
            if (!isGoingToCollide) {
                boolean isVisited = false;
                for (Node allNode : TreeMap.getAllWayPoint(root)) {
                    if (calculateDistance(n.getCoordinate(), allNode.getCoordinate()) <= 20) {
                        isVisited = true;
                        break;
                    }
                }
                if (isVisited) {
                    continue;
                } else {
                    Node a = new Node();
                    a.setCoordinate(wayPointLocation);
                    a.setType("w");

                    return n;
                }
            }
        }
        return null;
    }


    private static Vector<Double> lineIntersectCircle(Vector<Double> circle, Vector<Double> line, double r) {
        Vector<Double> result = new Vector<>();
        result.add(0.0);
        result.add(0.0);

        double x1, y1, x2, y2 = 0.0;

        x1 = circle.get(0);
        y1 = circle.get(1);
        x2 = line.get(0);
        y2 = line.get(1);

        double k = (y2 - y1) / (x2 - x1);
        double dx, dy = 0;
        dx = Math.sqrt((r * r) / (1 + k * k));
        dy = Math.sqrt((r * r) / dx * dx);

        result.set(0, (x1 - dx));
        result.set(1, (y1 - dy));
        return result;
    }


    // point x,y
    // line x1,y1,x2,y2
    private static double pointToSegDist(double x, double y, double x1, double y1, double x2, double y2) {

        double cross = (x2 - x1) * (x - x1) + (y2 - y1) * (y - y1);

        if (cross <= 0) return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));

        double d2 = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);

        if (cross >= d2) return Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2));

        double r = cross / d2;

        double px = x1 + (x2 - x1) * r;

        double py = y1 + (y2 - y1) * r;

        return Math.sqrt((x - px) * (x - px) + (py - y) * (py - y));

    }

    private static double calculateDistance(Vector<Double> a, Vector<Double> b) {
        double aX = a.get(0);
        double aZ = a.get(1);
        double bX = b.get(0);
        double bZ = b.get(1);
        return Math.sqrt(Math.pow(aX - bX, 2) + Math.pow(aZ - bZ, 2));
    }

    private boolean isArrivedNode(Node n) {
        try {
            double nodeX = n.getCoordinate().get(0);
            double nodeZ = n.getCoordinate().get(1);
            double currentX = sc.getLocation().get(0);
            double currentZ = sc.getLocation().get(1);
            double distance = Math.sqrt(Math.pow(nodeX - currentX, 2) + Math.pow(nodeZ - currentZ, 2));
            return distance <= cutOffDistance;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    //0-180 clockwise, 181-360(179-0) counter-clockwise
    // this is not an accurate description
    // sometimes at 180 & 179 it rotates unexpectly
    private List<Node> scan() {
        List<Node> nodelist = new ArrayList<Node>();
        sc.rotate(179, false);
        while (sc.check(1)) {
            Node tempnode = sc.look();
            nodelist.add(tempnode);
        }
        sc.rotate(179, false);
        while (sc.check(1)) {
            Node tempnode = sc.look();
            nodelist.add(tempnode);
        }

        return nodelist;
    }

    private TreeMap<Node> createWayPoint() {
        TreeMap<Node> treeNode = new TreeMap<>(new Node());
        Node node = new Node();
        node.setAngle(0);
        node.setCoordinate(sc.getLocation());
        if (currentTree.getNode() == null) {
            node.setDistanceToParent(0);

        } else {
            node.setDistanceToParent(calculateDistance(currentTree.getNode().getCoordinate(), sc.getLocation()));

        }
        node.setType("w");
        treeNode.setNode(node);
        treeNode.setParent(currentTree);
        return treeNode;
    }

    private void travelToNode(Node n) {
        travelToNode(n, new ArrayList<Node>());
    }

    private List<Node> getOnlyWallNodes(List<Node> nodeList) {
        List<Node> onlyWall = new ArrayList<>();
        for (Node n : nodeList) {
            if (n.getType().equals("wall")) {
                onlyWall.add(n);
            }
        }
        return onlyWall;
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setGoalList(String goals) {
        Vector<String> goalList = new Vector<>(2);
        String[] temp = goals.split(";");
        try {
            for (int i = 0; i < temp.length; i++) {
                goalList.add(i, temp[i]);
                System.out.print(temp[i] + "\n");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error occured while passing goal list.");
        }
    }

    private boolean isTooClose(Node maxDistance) {
        return maxDistance.getDistanceToParent() <= 30 || maxDistance.getDistanceToParent() == -1;
    }

    private boolean isDuplcateNode(Node n) {
        if (n == null) {
            return false;
        }

        for (Node no : TreeMap.getAllNodes(root)) {
            if (no.getObject().equals(n.getObject()))
                return true;
        }
        return false;
    }

    public static TreeMap<Node> think(int iteration, List<Tuple> input, Drone testDrone){
        Action rl = new Action("rl", 0, 0, -.25);
        Action rr = new Action("rr", 0, 0, .25);

        Action action=null;
        TreeMap<Node> a=new TreeMap<>();
        //move to think in meta
        for(int i=0;i<input.size();i++){
            if(input.get(i) instanceof Action) {
                action = (Action) input.get(i);


                if (equals(action, rl) || equals(action, rr)) {
                    System.out.println("add waypoint");
                    if(a!=null){
                        a.addChild(new Node(testDrone.getX() * 10, testDrone.getY() * 10, "w"));}
                        else{
                            TreeMap<Node> b  = new TreeMap<Node>(new Node(testDrone.getX()* 10, testDrone.getY() * 10, "w"));
                            return b;
                    }



                }
            }
            }
            return a;
        }
    public static boolean equals(Action a,Action b){
        return((a.getName() ==b.getName())&&(a.getXDelta() == b.getXDelta())&&a.getYDelta() == b.getYDelta());						// requires name and delta values, but not exception

    }

    @Override
    public void recieveDataStream(List<Tuple> x) {
        for(int i=0;i<x.size();i++){
            if(x.get(i) instanceof Recommendation){
                if(x.get(i).getSParams()[0].equals("@leftForwarding")){
                    
                }
            }
        }
    }
}


// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//    public static Vector<Double> lineInterCircle(
//            Vector<Double> ptStart, // 线段起点
//            Vector<Double> ptEnd, // 线段终点
//            Vector<Double> ptCenter, // 圆心坐标
//            float Radius) {
//
//        Vector<Double> ptInter1 = new Vector<>(2);
//        Vector<Double> ptInter2 = new Vector<>(2);
//        ptInter1.add(65536.0);
//        ptInter2.add(65536.0);
//        ptInter1.add(65536.0);
//        ptInter2.add(65536.0);
//
//
//        double fDis = Math.sqrt((ptEnd.get(0) - ptStart.get(0)) * (ptEnd.get(0) - ptStart.get(0)) +
//                (ptEnd.get(1) - ptStart.get(1)) * (ptEnd.get(1) - ptStart.get(1)));
//
//
//        Vector<Double> d = new Vector<>();
//        d.add(0.0);
//        d.add(0.0);
//
//        d.set(0, (ptEnd.get(0) - ptStart.get(0)) / fDis);
//        d.set(1, (ptEnd.get(1) - ptStart.get(1)) / fDis);
//
//        Vector<Double> E = new Vector<>();
//        E.add(0.0);
//        E.add(0.0);
//
//        E.set(0, ptCenter.get(0) - ptStart.get(0));
//        E.set(0, ptCenter.get(1) - ptStart.get(1));
//
//        double a = E.get(0) * d.get(0) + E.get(1) * d.get(1);
//        double a2 = a * a;
//
//        double e2 = E.get(0) * E.get(0) + E.get(1) * E.get(1);
//
//        float r2 = Radius * Radius;
//
//        if ((r2 - e2 + a2) < 0) {
//            return null;
//        } else {
//            double f = Math.sqrt(r2 - e2 + a2);
//
//            double t = a - f;
//
//            if (((t - 0.0) > -0.00001) && (t - fDis) < 0.00001) {
//                ptInter1.set(0, ptStart.get(0) + t * d.get(0));
//                ptInter1.set(0, ptStart.get(1) + t * d.get(1));
//            }
//
//            t = a + f;
//
//            if (((t - 0.0) > -0.00001) && (t - fDis) < 0.00001) {
//                ptInter2.set(0, ptStart.get(0) + t * d.get(0));
//                ptInter2.set(1, ptStart.get(1) + t * d.get(1));
//            }
//            if (calculateDistance(ptInter1, ptStart) < calculateDistance(ptInter2, ptStart)) {
//                return ptInter1;
//            } else {
//                return ptInter2;
//            }
//
//        }
//
//    }
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// travel to node
//
//        Node maxDistance = new Node();
//        Node second = new Node();
//        second.setDistanceToParent(-1);
//        maxDistance.setDistanceToParent(-1);
//        for (Node n : nodeList) {
//            if (n.getDistanceToParent() > maxDistance.getDistanceToParent()) {
//                boolean isVisited = false;
//                for (Node allNode : TreeMap.getAllNodes(root)) {
//                    if (calculateDistance(n.getCoordinate(), allNode.getCoordinate()) <= 20) {
//                        isVisited = true;
//                        break;
//                    }
//                }
//                if (!isVisited) {
//                    second = maxDistance;
//                    maxDistance = n;
//
//                }
//            }
//            if (n.getType().equals("object")) {
//                if (!isDuplcateNode(n)) {
//                    TreeMap<Node> newTreeNode = new TreeMap<>(n);
//                    currentTree.addChild(newTreeNode);
//                    mv.addTreeNode(root);
//                }
//            }
//        }
//
//        if (isTooClose(second)) {
//            System.out.println("could not find a max distance target");
////            if (!isTooClose(maxDistance)) {
////                return maxDistance;
////            }
//            this.isExploringMap = false;
//            return null;
//        }
//        return second;
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
