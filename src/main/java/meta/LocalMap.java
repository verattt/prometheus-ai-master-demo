package meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocalMap {
    private Node node;
    private GlobalMap globalMap;
    private ArrayList<Edge> edges=new ArrayList<Edge>();
    private ArrayList<Edge_Compressed> edges_c=new ArrayList<Edge_Compressed>();
    private Goal associatedGoal;

    public LocalMap(Node node) {
        this.node = node;
    }
    //for clone
    public LocalMap(LocalMap localmap){
        this.node=localmap.getNode();
        this.globalMap=localmap.getGlobalMap();
        this.edges=localmap.getEdges();
        this.edges_c=localmap.getEdges_c();
        this.associatedGoal=localmap.associatedGoal;
    }
    public void addChild(double distance, double angle, LocalMap child) {
        Edge edge = new Edge(distance, angle, this, child);
        this.edges.add(edge);
        child.addEdge(edge);
    }

    public GlobalMap getGlobalMap() {
        return globalMap;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void setGlobalMap(GlobalMap globalMap) {
        this.globalMap = globalMap;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public ArrayList<Edge_Compressed> getEdges_c() { return edges_c; }

    public void setEdges_c(ArrayList<Edge_Compressed> edges_c) { this.edges_c = edges_c; }

    public void addEdges_c(Edge_Compressed edges_c) { this.edges_c.add(edges_c); }

    public void clearAllEdges_c (){this.edges_c.clear();}

    public void removeEdges_c(Edge_Compressed edge_c){this.edges_c.remove(edge_c); }

    public void setAssociatedGoal(Goal goal){this.associatedGoal=goal;}

    public Goal getAssoicatedGoal(){return this.associatedGoal;}

    public void addEdge( Edge edge){
        this.edges.add(edge);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocalMap)) return false;
        LocalMap localMap = (LocalMap) o;
        return this.getNode().equals(localMap.getNode());
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.getNode());
    }

    @Override
    public String toString() {
        return "LocalMap{" +
                "node=" + node +
                ", globalMap=" + globalMap +
                ", edges=" + edges +
                ", edges_c=" + edges_c +
                ", associatedGoal=" + associatedGoal +
                '}';
    }
}
