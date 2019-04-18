package meta;

import java.util.ArrayList;
import java.util.List;


public class TreeMap<T> {
    private T node = null;
    private List<TreeMap> children = new ArrayList<>();
    private TreeMap parent = null;
    private List<Node> walls = new ArrayList<>();

    public List<Node> getWalls() {
        return walls;
    }

    public void setWalls(List<Node> walls) {
        this.walls = walls;
    }


    public TreeMap(T node) {
        this.node = node;
    }

    public TreeMap() {
    }


    public void setChildren(List<TreeMap> children) {
        this.children = children;
    }


    public void addChild(TreeMap child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(T data) {
        TreeMap<T> newChild = new TreeMap<>(data);
        newChild.setParent(this);
        children.add(newChild);
    }

    public void addChildren(List<TreeMap> children) {
        for (TreeMap t : children) {
            t.setParent(this);
        }
        this.children.addAll(children);
    }

    public List<TreeMap> getChildren() {
        return children;
    }

    public T getNode() {
        return this.node;
    }

    public void setNode(T node) {
        this.node = node;
    }

    public void setParent(TreeMap parent) {
        this.parent = parent;
    }

    public TreeMap getParent() {
        return parent;
    }

    private static List<Node> a = new ArrayList<>();

    public static List<Node> getAllNodes(TreeMap<Node> map) {

        if (map == null) {
            return a;
        } else {
            a.add(map.getNode());
            for (TreeMap<Node> t : map.getChildren()) {
                getAllNodes(t);
            }
        }
        return a;

    }

    private static List<Node> b = new ArrayList<>();

    public static List<Node> getAllWayPoint(TreeMap<Node> map) {

        if (map == null) {
            return b;
        } else {
            if (map.getNode().getType().equals("WAYPOINT")) {
                b.add(map.getNode());
                for (TreeMap<Node> t : map.getChildren()) {
                    getAllWayPoint(t);
                }
            }

        }
        return b;

    }
}