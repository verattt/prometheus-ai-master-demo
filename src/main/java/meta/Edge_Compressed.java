package meta;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Edge_Compressed {

    private double weight;
    private double distance;
    private LocalMap start;
    private LocalMap destination;
    private GlobalMap globalStart;
    private GlobalMap globalDestination;
    private ArrayList<Edge> path = new ArrayList<>();  //stores the original edges that is compressed into this edge_compressed instance

    public Edge_Compressed(double weight , LocalMap start, LocalMap destination) {
        this.weight=weight;
        this.start = start;
        this.destination = destination;
        this.globalStart = start.getGlobalMap();
        this.globalDestination = destination.getGlobalMap();
    }

    public Edge_Compressed(Edge_Compressed edge_c) {
        this.weight = edge_c.getWeight();
        this.distance=edge_c.getDistance();
        this.start = edge_c.getStart();
        this.destination = edge_c.getDestination();
        this.globalStart = edge_c.getStart().getGlobalMap();
        this.globalDestination = edge_c.getDestination().getGlobalMap();
        this.path=edge_c.getPath();
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public LocalMap getStart() {
        return start;
    }

    public void setStart(LocalMap start) {
        this.start = start;
    }

    public LocalMap getDestination() {
        return destination;
    }

    public void setDestination(LocalMap destination) {
        this.destination = destination;
    }

    public GlobalMap getGlobalStart() {
        return globalStart;
    }

    public void setGlobalStart(GlobalMap globalStart) {
        this.globalStart = globalStart;
    }

    public GlobalMap getGlobalDestination() {
        return globalDestination;
    }

    public void setGlobalDestination(GlobalMap globalDestination) {
        this.globalDestination = globalDestination;
    }

    public ArrayList<Edge> getPath() { return path; }

    public void setPath(ArrayList<Edge> path) {
        this.path = path;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) { this.distance = distance; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge_Compressed)) return false;
        Edge_Compressed that = (Edge_Compressed) o;
        return Double.compare(that.getWeight(), getWeight()) == 0 &&
                Objects.equals(getStart(), that.getStart()) &&
                Objects.equals(getDestination(), that.getDestination());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getWeight(), getStart(), getDestination());
    }
}
