package meta;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Plan {
    private ArrayList<Edge> path = new ArrayList<>();
    private double EstimatedBatteryCons;
    private LocalMap ToLocal;

    public Plan(ArrayList<Edge> path,LocalMap ToLocal) {
        this.path = path;
        this.ToLocal=ToLocal;
    }

    public Plan() { }

    public ArrayList<Edge> getPath() {
        return path;
    }

    public void setPath(ArrayList<Edge> path) {
        this.path = path;
    }

    public void addPath(Edge edge){this.path.add(edge);}

    public void addPath(ArrayList<Edge> edges) {this.path.addAll(edges);}

    public double EvaluatePlan(double distancetobatteryriot){
        double distance=0;
        for (Edge edge:this.path){
            distance+=edge.getWeight();
        }
        return distance*distancetobatteryriot;
    }

    public void EvaluateAndUpdate(double distancetobatteryriot){
        double distance=0;
        for (Edge edge:this.path){
            distance+=edge.getWeight();
        }
        this.EstimatedBatteryCons=distance*distancetobatteryriot;
    }

    public double getEstimatedBatteryCons(){ return this.EstimatedBatteryCons;}

    public void setLocal(LocalMap ToLocal) { this.ToLocal = ToLocal; }

    public LocalMap getLocal(){ return this.ToLocal;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Plan)) return false;
        Plan plan = (Plan) o;
        return Objects.equals(getPath(), plan.getPath());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getPath());
    }

    @Override
    public String toString() {
        return "Plan{" +
                "path=" + path +
                ", EstimatedBatteryCons=" + EstimatedBatteryCons +
                ", ToLocal=" + ToLocal +
                '}';
    }
}
