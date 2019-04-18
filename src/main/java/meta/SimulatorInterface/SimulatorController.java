package meta.SimulatorInterface;

import meta.Node;
import meta.SimulatorInterface.InterfaceToMETA;

import java.util.Vector;

public class SimulatorController implements Runnable {
    private final int SLEEP = 500;// Choose 300 to avoid tcp client 20mm refresh rate
    private final int ISMOVING = 0;
    private final int ISROTATING = 1;

    public SimulatorController() {

    }

    public String sendQuery(String query) {
        return "@result";
    }

    @Override
    public void run() {

    }

    // option 1 = check is rotating, 0 = check is forward
    //true:still moving;false:finished moving
    public boolean check(int option) {
        String lastVar = "";
        String currentVar = "";
        try {
            switch (option) {
                case ISROTATING:
                    lastVar = InterfaceToMETA.getRotationRobot();
                    Thread.sleep(SLEEP); // To avoid check rotation immediately
                    currentVar = InterfaceToMETA.getRotationRobot();
                    break;
                case ISMOVING:
                    lastVar = InterfaceToMETA.getPosition();
                    Thread.sleep(SLEEP);
                    currentVar = InterfaceToMETA.getPosition();
                    break;
            }
            if (currentVar.equals(lastVar)) {
                return false;
            }

        } catch (Exception e) {
            System.out.println("Simulator Controller isRotate thread sleep exception");
        }

        return true;
    }


    public double rotate(double degree, boolean wait) {
        try {

            while (check(ISROTATING) || check(ISMOVING)) {
                Thread.sleep(SLEEP);
            }

            InterfaceToMETA.rotate(degree);
            if (wait) {
                while (check(ISROTATING)) {
                    Thread.sleep(SLEEP);
                }
            }

        } catch (Exception e) {
            System.out.println("Simulator Controller rotate thread sleep exception");
        }
        return 0.0;
    }

    public void rotate360(boolean wait) {
        rotate(180, wait);
        rotate(180, wait);
    }

    public double move(double distance, boolean wait) {
        try {

            while (check(ISROTATING) || check(ISMOVING)) {
                Thread.sleep(SLEEP);
            }

            InterfaceToMETA.goForward(distance);
            if (wait) {
                while (check(ISMOVING)) {
                    Thread.sleep(SLEEP);
                }
            }

        } catch (Exception e) {
            System.out.println("Simulator Controller rotate thread sleep exception");
        }
        return 0.0;
    }

    public Vector<Double> getLocation() {
        String query = InterfaceToMETA.getPosition();
        Vector<Double> location = new Vector<>(2);
        String[] s = query.split(";");

        try {
            location.add(0, Double.parseDouble(s[0]));//x
            location.add(1, Double.parseDouble(s[2]));//z
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("Simulator Controller getLocation Parse Double Exception");
        }


        return location;
    }

    public double getRotation() {
        String query = InterfaceToMETA.getRotationRobot();
        try {
            return Double.parseDouble(query);
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("Simulator Controller getRotation Parse Double Exception");
        }
        return 0.0;
    }

    public void stop() {
        // TODO: stop is not immediate.
        // TODO: issue could be threads not synced, also possible InterfaceToMETA bug
        InterfaceToMETA.stop();
    }

//    private String prevObj = "";

    public Node look() {
        // 31.70194;88.16097;(-36.9, 0.4, -47.9);Brick
//        distance(object detected), theta (object detected), xyz, obj name
        String curObj = InterfaceToMETA.objectDetected();
        if (curObj.equals("none")) {
            Node n = new Node();
            Vector<Double> v = new Vector<>(2);
            v.add(0.0);
            v.add(0.0);
            n.setCoordinate(v);
            n.setDistanceToParent(-1.0);
            n.setType("wall");
            n.setObject("Brick");
            return n;
        }
        Vector<Double> obj = new Vector<>();

//        prevObj = curObj;
        String[] s = curObj.split(";");

        Node node = new Node();
        node.setDistanceToParent(Double.parseDouble(s[0]));
        node.setAngle(Double.parseDouble(s[1]));

        switch (s[3].substring(0, 3)) {
            case "Bri":
                node.setType("wall");
                break;
            case "Box":
                node.setType("error");
                break;
            default:
                node.setType("object");
                break;
        }

        node.setObject(s[3]);
        String c = s[2];
        c = c.substring(1, c.length() - 1);
        String[] a = c.split(",");
        Vector<Double> corrd = new Vector<>(2);
        corrd.add(0, Double.parseDouble(a[0]));
        corrd.add(1, Double.parseDouble(a[2]));
        node.setCoordinate(corrd);
//        System.out.println(node.getObject());
        return node;
//        }
    }
}
