package meta;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import java.util.List;
import java.util.Objects;

public class GlobalMap {
    private LocalMap localMapRoot;
    private ArrayList<Edge> edges;

    public GlobalMap(LocalMap localMapRoot) {
        this.localMapRoot = localMapRoot;
    }

    public void addChild(double distance, double angle, LocalMap from, LocalMap to) {
        Edge edge = new Edge(distance, angle, from, to);
        edges.add(edge);
    }

    public LocalMap getLocalMapRoot() {
        return localMapRoot;
    }

    public ArrayList<Edge> getEdges(){
        return this.edges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GlobalMap)) return false;
        GlobalMap globalMap = (GlobalMap) o;
        return Objects.equals(getLocalMapRoot(), globalMap.getLocalMapRoot()) &&
                Objects.equals(getEdges(), globalMap.getEdges());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getLocalMapRoot(), getEdges());
    }
}
