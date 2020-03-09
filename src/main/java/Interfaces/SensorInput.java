package Interfaces;

import nn.Drone;
import nn.TheWorld;
import tags.Tuple;

import java.util.List;

public interface SensorInput {
    List<Tuple> readSimulator(TheWorld world, Drone testDrone);

    // if the NN works properly, itshould takeinput of raw data from sensor and implements method as down below
    // List<Tuple> readsimulator(int nnID, int nnStruct, int data[]);

}
