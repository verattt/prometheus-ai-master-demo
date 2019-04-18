package meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Edge {

    private double distance;
    private double angle;
    private double weight;
    private LocalMap start;
    private LocalMap destination;
    private GlobalMap globalStart;
    private GlobalMap globalDestination;
    private ArrayList<Double> realweights = new ArrayList<>();

    public Edge(double distance, double angle, LocalMap start, LocalMap destination) {
        this.distance = distance;
        this.angle = angle;
        this.start = start;
        this.destination = destination;
        this.weight=distance;
        this.globalStart = start.getGlobalMap();
        this.globalDestination = destination.getGlobalMap();
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
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

    public void addrealweight(double realweight){ realweights.add(realweight); }

    public ArrayList<Double> getrealweights(){ return this.realweights;}

    public void updateweight(){
        if(!this.realweights.isEmpty()) {
            double sum = 0;
            for (double realweight : realweights) {
                sum += realweight;
            }
            this.weight=this.weight/2 +(sum/realweights.size())/2;
        }
    }


}

