package tags;

import java.util.Iterator;
import java.util.List;

public class IterateTuple implements Iterable<Tuple> {
    private List<Tuple> a;
    private List<Tuple> copy;
    public IterateTuple(List<Tuple> a){
        this.a =a;
        this.copy=a;
    }


    @Override
    public Iterator<Tuple> iterator() {
        return a.iterator();
    }
    public Iterator<Tuple> copyIterator(){
        return copy.iterator();
    }

    Tuple currentTuple(){
       if(iterator().hasNext()){
           copyIterator().next();
           return iterator().next();
       }
       return null;
    } // Returns NULL when list is empty
    Tuple nextTuple(){
        if(iterator().hasNext()){
            copyIterator().next();
            return iterator().next();
        }
        return null;
    } // Returns NULL when at end of list
    boolean isEmptyTuple(){
        return (nextTuple()==null);
    }// False when nextTuple() is NULL
}
