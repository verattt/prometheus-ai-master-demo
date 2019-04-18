//package meta;
//
//import Interfaces.SensorInput;
//import Interfaces.Thinking;
//import meta.MapVisualizer;
//import meta.SimulatorInterface.TcpClient;
//import meta.SimulatorInterface.TcpServer;
//import meta.SimulatorInterface.UserInput;
//
//import tags.Recommendation;
//import tags.Tuple;
//
//import java.util.*;
//
//
//public class Main implements Thinking {
//
//    private static META meta = new META();
//
//    public static void main(String[] args) throws Exception {
////        MapVisualizer mv = new MapVisualizer();
////        Node a  = new Node(1,2,"Wall");
////        TreeMap<Node> map =new TreeMap<>();
////        map.setParent(a);
//
//
////        MapVisualizer global = new MapVisualizer();
////        new Thread(mv).start();
////        new Thread(global).start();
////
////        new Thread(meta).start();
//
//
//    }
//    //how to determine whether it is a valid recommendation.
//    //meta is only capable of path finding and setting goal list
//    //possible knowledge for setting up the knn
//
//
//    public static META getMeta() {
//        return meta;
//    }
//
//    public List<Tuple> think(int iterate, List<Tuple> tuples) {
//        Iterator<Tuple> iter = tuples.iterator();
//        List<Tuple> output = new ArrayList<>();
//        while (iter.hasNext()) {
//            Tuple temp = iter.next();
//            if (temp instanceof Recommendation) {
//                //add to goal list? don't know how to verify it.
//            } else if (temp instanceof SensorInput) {
//                //add node
//            }
//        }
//        return output;
//    }
//
//
//    public static void setGoal(String goal) {
//        meta.setGoalList(goal);
//    }
//}