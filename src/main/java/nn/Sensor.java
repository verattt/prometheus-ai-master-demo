package nn;

import tags.Tuple;

public abstract class Sensor extends Tuple {
    public abstract Class getSignal();
    public abstract int[] getX();
    public abstract int[] getY();
    public abstract int getRange();
    public abstract double score(TheWorld world, int[][] visible);

}
