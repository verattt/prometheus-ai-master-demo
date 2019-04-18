//package meta;
//
//import meta.MapVisualizer;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.ArrayList;
//
//import static org.junit.Assert.*;
//
//public class PlanTest {
//    private META meta;
//    GlobalMap globalMap;
//    ArrayList<Goal> goals;
//    ArrayList<LocalMap> locals;
//    ArrayList<Edge> edges;
//
//    @Before
//    public void setUp() {
//        meta = new META();
//        //MapVisualizer mv = new MapVisualizer();
//        //new Thread(mv).start();
//
//        globalMap = null;
//
//
//        meta.setRootGlobalMap(globalMap);
//
//        ArrayList<LocalMap> AllLocalMaps = new ArrayList<LocalMap>();
//
//        // first global map
//
//        Node o1 = new Node("o1", NodeType.OBJECT);
//        Node o2 = new Node("o2", NodeType.OBJECT);
//        Node o3 = new Node("o3", NodeType.OBJECT);
//        Node o4 = new Node("o4", NodeType.OBJECT);
//
//        LocalMap lo1 = new LocalMap(o1);
//        AllLocalMaps.add(lo1);
//        LocalMap lo2 = new LocalMap(o2);
//        AllLocalMaps.add(lo2);
//        LocalMap lo3 = new LocalMap(o3);
//        AllLocalMaps.add(lo3);
//        LocalMap lo4 = new LocalMap(o4);
//        AllLocalMaps.add(lo4);
//
//        lo1.addChild(10, -10, lo3);
//        lo1.addChild(5, 0, lo2);
//        lo2.addChild(9, 0, lo3);
//        lo2.addChild(3, 0, lo4);
//        lo3.addChild(15, 0, lo4);
//
//        // second global map
//
//        Node o5 = new Node("o5", NodeType.OBJECT);
//        Node o6 = new Node("o6", NodeType.OBJECT);
//        Node o7 = new Node("o7", NodeType.OBJECT);
//
//        LocalMap lo5 = new LocalMap(o5);
//        AllLocalMaps.add(lo5);
//        LocalMap lo6 = new LocalMap(o6);
//        AllLocalMaps.add(lo6);
//        LocalMap lo7 = new LocalMap(o7);
//        AllLocalMaps.add(lo7);
//
//        lo4.addChild(20, 0, lo5);
//        lo5.addChild(8, 0, lo6);
//        lo5.addChild(7, 0, lo7);
//        lo6.addChild(6, 0, lo7);
//
//
//        //third global map
//
//        Node o8 = new Node("o8", NodeType.OBJECT);
//        Node o9 = new Node("o9", NodeType.OBJECT);
//        Node o10 = new Node("o10", NodeType.OBJECT);
//        Node o11 = new Node("o11", NodeType.OBJECT);
//        Node o12 = new Node("o12", NodeType.OBJECT);
//
//        LocalMap lo8 = new LocalMap(o8);
//        AllLocalMaps.add(lo8);
//        LocalMap lo9 = new LocalMap(o9);
//        AllLocalMaps.add(lo9);
//        LocalMap lo10 = new LocalMap(o10);
//        AllLocalMaps.add(lo10);
//        LocalMap lo11 = new LocalMap(o11);
//        AllLocalMaps.add(lo11);
//        LocalMap lo12 = new LocalMap(o12);
//        AllLocalMaps.add(lo12);
//
//        lo7.addChild(25, 0, lo8);
//        lo8.addChild(6, 0, lo9);
//        lo8.addChild(7, 0, lo10);
//        lo10.addChild(5, 0, lo11);
//        lo10.addChild(5, 0, lo12);
//
//
//        //fourth global map
//
//        Node o13 = new Node("o13", NodeType.OBJECT);
//        Node o14 = new Node("o14", NodeType.OBJECT);
//        Node o15 = new Node("o15", NodeType.OBJECT);
//        Node o16 = new Node("o16", NodeType.OBJECT);
//
//        LocalMap lo13 = new LocalMap(o13);
//        AllLocalMaps.add(lo13);
//        LocalMap lo14 = new LocalMap(o14);
//        AllLocalMaps.add(lo14);
//        LocalMap lo15 = new LocalMap(o15);
//        AllLocalMaps.add(lo15);
//        LocalMap lo16 = new LocalMap(o16);
//        AllLocalMaps.add(lo16);
//
//        lo11.addChild(15, 0, lo13);
//        lo11.addChild(10, 0, lo14);
//        lo13.addChild(5, 0, lo14);
//        lo13.addChild(4, 0, lo15);
//        lo15.addChild(8, 0, lo16);
//
//        locals = AllLocalMaps;
//        edges = META_Utility.getAllEdges(AllLocalMaps);
//        meta.setAllLocalMaps(AllLocalMaps);
//
//
//        //set batery stations: oxx.setType(NodeType.BATTERY); only change xx, where xx=1 to 16
//        o2.setType(NodeType.BATTERY);
//
//        //set goals;
//        /**to set a new goal, do the following:
//         *   Goal goaln=new Goal(oxx,priority);
//         *   goals.add(goaln);
//         *   Only change n(nth goal), and xx, where xx=1 to 16
//         */
//
//        goals = new ArrayList<Goal>();
//        Goal goal1 = new Goal(o15, 2);
//        Goal goal2 = new Goal(o10, 1);
//        Goal goal3 = new Goal(o3, 6);
//        Goal goal4 = new Goal(o9, 4);
//        goals.add(goal1);
//        goals.add(goal2);
//        goals.add(goal3);
//        goals.add(goal4);
//        //set all above goals to meta
//        meta.setGoals(goals);
//
//        //set starting position; to change current location, change lo1 in [lo1,lo2,lo3....lo15,lo16]
//        meta.setCurrentPosition(lo1);
//        //set how much battery it costs by traveling 1 unit of distance
//        meta.setDistanceToBatteryRiot(0.5);
//        //set startting battery, battery<=100, where 100=100% battery
//        meta.setBattery(20);
//        //set starting error, by default, set to 0, which represents the robot is new to the testing environment
//        meta.setMeanError(0);
//
//        //Simulate the real distance traveled by traveling 1 unit of distance.
//        //real distance = Base+factor*random(0,1); so for base=0.5,factor=0.8, real distance=0.5+[0,0.8]
//        meta.setRandomDistanceBase(0.5);
//        meta.setRandomDistancefactor(0.8);
//
//    }
//
//
//    // @Test
//    public void testBuildGraph() {
//        setUp();
//        ArrayList<Edge_Compressed> result = meta.BuildGraph(locals, goals);
//        System.out.println("Step1");
//        for (Edge_Compressed e : result) {
//            System.out.println(e.getStart().getNode().getObject() + " to "
//                    + e.getDestination().getNode().getObject() + ": " + e.getWeight());
//        }
//        MapVisualizer mapDraw = new MapVisualizer();
//
//    }
//
//    //@Test
//    public void testGenerateMST() {
//        setUp();
//
//        ArrayList<Edge_Compressed> graphc = meta.BuildGraph(locals, goals);
//        ArrayList<Edge_Compressed> mst = meta.GenerateMST(graphc, goals);
//        System.out.println("Step2");
//        for (Edge_Compressed e : mst) {
//            System.out.println(e.getStart().getNode().getObject() + " to "
//                    + e.getDestination().getNode().getObject() + ": " + e.getWeight());
//            ArrayList<Edge> edges = e.getPath();
//            for (Edge edge : edges) {
//                System.out.println("Start=" + edge.getStart().getNode().getObject() + " End=" + edge.getDestination().getNode().getObject() + " Distance=" + edge.getWeight());
//            }
//        }
//    }
//
//    //@Test
//   public void testGeneratePlan() {
//        setUp();
//
//        ArrayList<Edge_Compressed> graphc = meta.BuildGraph(locals, goals);
//        ArrayList<Edge_Compressed> mst = meta.GenerateMST(graphc, goals);
//        ArrayList<Plan> planlist = meta.GeneratePlan(mst);
//        System.out.println("Step3");
//        System.out.println("======================================");
//        System.out.println("Planlistsize=" + planlist.size());
//        for (Plan plan : planlist) {
//            System.out.println("ToLocalmap:" + plan.getLocal().getNode().getObject() + "  planlistsize= " + plan.getPath().size());
//            ArrayList<Edge> edges = plan.getPath();
//            for (Edge edge : edges) {
//                System.out.println("Start=" + edge.getStart().getNode().getObject() + " End=" + edge.getDestination().getNode().getObject() + " Distance=" + edge.getWeight());
//            }
//        }
//    }
//
//
//    @Test
//   public void testExecute() {
//        setUp();
//        ArrayList<Edge_Compressed> graphc = meta.BuildGraph(locals, goals);
//        ArrayList<Edge_Compressed> mst = meta.GenerateMST(graphc, goals);
//        ArrayList<Plan> planlist = meta.GeneratePlan(mst);
//        System.out.println("Step 4.Execute Plan");
//        System.out.println("======================================");
//        meta.Execute(planlist, false);
//
//
//    }
//}