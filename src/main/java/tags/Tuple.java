package tags;

import java.util.Iterator;
import java.util.List;

public abstract class Tuple {
    // All AI knowledge structures will be based on this class.
// Based on the following format: (label, parameters)
// Where “label” is a string
// Where “parameters” are either strings or integers
// The integers range from -100 to +100 and are a score or
// a measurement
    final double minScore = -1;
    final double maxScore = 1;
    String label; // The “label” from (label, params)
    String sParams[]; // The “parameters” from (label,params)
    public double iParams[]; // Int parameters from (label, params)

    public void setTuple(String label,String[] sParams,double[] iParams){
        this.label=label;
        this.sParams=sParams;
        this.iParams=iParams;

    }
    public void setLabel(String label){
        this.label=label;
    }
    public void setSParams(String[] sParams){
        this.sParams=sParams;
    }
    public void setIParams(double[] IParams){
        this.iParams=IParams;
    }
    //public abstract void setTuple();
    public String getLabel(){
        return this.label;
    }
    public String[] getSParams(){
        return this.sParams;
    }
    public double[] getIParams(){
        return this.iParams;
    }

    /*
     * @return string that in recognizable format for ES and KNN
     * */
    public abstract String simpleToString();



    public static <T> T[] toArray(List<T> list){
        Iterator<T> iterator = list.iterator();
        int len = list.size();
        System.out.println(len);
        T[] arr = (T[])new Object[len];
        for (int i =0; i < list.size(); i++){
            arr[i] = list.get(i);
        }
        return arr;

    }





}