package knn.internal;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

import Interfaces.LayerInput;
import Interfaces.LayerOutput;
import Interfaces.Thinking;
import com.google.inject.assistedinject.Assisted;
import knn.api.KnowledgeNode;
import knn.api.KnowledgeNodeNetwork;
import tags.*;

/**
 * Implementation of the KNN.
 */
class KnowledgeNodeNetworkImpl implements KnowledgeNodeNetwork, Thinking, LayerInput, LayerOutput {
    private final Map<Tag, KnowledgeNode> mapKN;
    private final Set<Tag> activeTags;
    private final TreeSet<KnowledgeNode> ageSortedKNs;

    private final DirectSearcher directSearcher;
    private final ForwardSearcher forwardSearcher;
    private final BackwardSearcher backwardSearcher;
    private final LambdaSearcher lambdaSearcher;
//    private List<Tuple> input;
//    private List<Tuple> output;

    @Inject
    KnowledgeNodeNetworkImpl(
            @Assisted("mapKN") final Map<Tag, KnowledgeNode> mapKN,
            @Assisted("activeTags") final Set<Tag> activeTags,
            @Assisted("ageSortedKNs") final TreeSet<KnowledgeNode> ageSortedKNs,
            @Assisted("backwardSearchMatchRatio") final
            double backwardSearchMatchRatio,
            @Assisted("backwardSearchAgeLimit")
            final long backwardSearchAgeLimit,
            final DirectSearcherFactory directSearcherFactory,
            final ForwardSearcherFactory forwardSearcherFactory,
            final BackwardSearcherFactory backwardSearcherFactory,
            final LambdaSearcherFactory lambdaSearcherFactory) {
        this.mapKN = mapKN;
        this.activeTags = activeTags;
        this.ageSortedKNs = ageSortedKNs;
        this.directSearcher =
                directSearcherFactory.create(mapKN, activeTags, ageSortedKNs);
        this.forwardSearcher = forwardSearcherFactory.create(directSearcher);
        this.backwardSearcher = backwardSearcherFactory.create(
                activeTags, ageSortedKNs, backwardSearchMatchRatio,
                backwardSearchAgeLimit);
        this.lambdaSearcher =
                lambdaSearcherFactory.create(forwardSearcher, backwardSearcher);
    }

    @Override
    public void resetEmpty() {
        mapKN.clear();
        activeTags.clear();
        ageSortedKNs.clear();
    }

    @Override
    public void clearActiveTags() {
        activeTags.clear();
    }

    @Override
    public void addKnowledgeNode(final KnowledgeNode kn) {
        mapKN.put(kn.getInputTag(), kn);
       // ageSortedKNs.add(kn);
    }

    @Override
    public void deleteExpiredKnowledgeNodes() {
//        final Set<Tag> tagsToDelete = new HashSet<>();
//        for (final KnowledgeNode kn : mapKN.values()) {
//            if (kn.isExpired()) {
//                tagsToDelete.add(kn.getInputTag());
//                ageSortedKNs.remove(kn);
//            }
//        }
//        for (final Tag t : tagsToDelete) {
//            mapKN.remove(t);
//            activeTags.remove(t);
//        }
    }

    @Override
    public void deleteKnowledgeNode(final Tag tag) {
        mapKN.remove(tag);
    }

    @Override
    public void addActiveTag(final Tag tag) {
        activeTags.add(tag);
    }

    @Override
    public void addActiveTags(final Tag... tags) {
        activeTags.addAll(Arrays.asList(tags));
    }

    @Override
    public Set<Tag> getActiveTags() {
        return Collections.unmodifiableSet(activeTags);
    }

    @Override
    public KnowledgeNode getKnowledgeNode(final Tag tag) {
        return mapKN.get(tag);
    }

    @Override
    public Collection<KnowledgeNode> getKnowledgeNodes() {
       return  mapKN.values();

    }


    @Override
    public Set<Tag> directSearch(final Tag inputTag) {
        return directSearcher.search(inputTag);
    }

    @Override
    public Set<Tag> forwardSearch(final Set<Tag> inputTags, final int ply) {
        return forwardSearcher.search(inputTags, ply);
    }

    @Override
    public Set<Tag> forwardThink(final int ply) {
        return forwardSearcher.search(activeTags, ply);
    }

    @Override
    public Set<Tag> backwardSearch(final Set<Tag> inputTags, final int ply) {
        return backwardSearcher.search(inputTags, ply);
    }

    @Override
    public Set<Tag> backwardThink(final int ply) {
        return backwardSearcher.search(activeTags, ply);
    }

    @Override
    public void setBackwardSearchMatchRatio(final double ratio) {
        backwardSearcher.setPartialMatchRatio(ratio);
    }

    @Override
    public Set<Tag> lambdaSearch(final Set<Tag> inputTags, final int ply) {
        return lambdaSearcher.search(inputTags, ply);
    }

    @Override
    public Set<Tag> lambdaThink(final int ply) {
        return lambdaSearcher.search(activeTags, ply);
    }

    @Override
    public List<KnowledgeNode> loadData(final String filename) {
        final List<KnowledgeNode> knowledgeNodes = new ArrayList<>();
        resetEmpty();
        try {
            final BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(filename),
                            "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                final String[] info = line.split(";\\s+");
                final KnowledgeNode kn = new KnowledgeNode(info);
                knowledgeNodes.add(kn);
            }
            br.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        for (final KnowledgeNode knowledgeNode : knowledgeNodes) {
            addKnowledgeNode(knowledgeNode);
        }
        return knowledgeNodes;
    }

    @Override
    public void reset(final String dbFilename) {
    }

    @Override
    public void save(final String dbFilename) {
    }

    @Override
    public void recieveDataStream(List<Tuple> x) {

    }

    @Override
    public void sendDataStream(List<Tuple> x) {

    }



//    public Iterator<Tuple> iterator()
//    {
//        return input.iterator();
//    }
//    @Override
//    public Tuple firstTuple() {
//        if(input.get(0)!=null){
//            return input.get(0);}
//        else{return null;}
//    }
//
//    @Override
//    public Tuple nextTuple() {
//        if(this.iterator().hasNext()){
//            return this.iterator().next();
//        }
//        else{
//            return null;
//        }
//    }
//
//    @Override
//    public boolean isEmptyTuple() {
//        return this.iterator().hasNext();
//    }
//
//hasNext
    // TODO SETUP THE knn using .txt file
    public void setup(){


    }

/*
default using foraward think of knn and maximum rounds of es thinking
* */

public List<Tuple> thinkG(int iteration, List<Tuple> input){
    Iterator<Tuple> oi = input.iterator();
    while(oi.hasNext()){
        addActiveTag((Tag) oi.next());
    }
    Set<Tag> result = forwardThink(100);
    System.out.println(result.toArray()[0].toString());
    Set<Tag> activatedTags = getActiveTags();
    List<Tuple> list  = new ArrayList(activatedTags);

    return list;


}
    public List<Tuple> think(int iteration, List<Tuple> input){
//        Prometheus prometheus = Guice.createInjector(new PrometheusModule()).getInstance(Prometheus.class);
//        ExpertSystem es = prometheus.getExpertSystem();
//        KnowledgeNodeNetwork knn = prometheus.getKnowledgeNodeNetwork();
//        //TODO transform tuples to FACT, perform forward search
//        List<Tuple> copy =input;
//        Iterator inputIterator = input.iterator();
//        Iterator copyIterator = copy.iterator();
//        List<Tuple> output = new ArrayList<>();
//        while(inputIterator.hasNext()){
//            if(inputIterator.next() instanceof Fact){
//                this.addActiveTag((Tag)copyIterator.next());
//            }
//
//        }
//
//        return output;

        boolean leftObs=false;
        boolean frontObs = false;
        boolean rightObs = false;
        boolean wait=false;
        List<Tuple> output = new ArrayList<>();
        for(int i=0;i<input.size();i++){
            if(input.get(i)instanceof Fact){
               Fact temp  = (Fact) input.get(i);
               if(temp.getPredicateName().equals("leftImmovable")){leftObs=true;}
               if(temp.getPredicateName().equals("rightImmovable")){rightObs=true;}
               if(temp.getPredicateName().equals("frontImmovable")){frontObs=true;}
               if(temp.getPredicateName().equals("frontMoveable")){
                  wait = true;
               }

               output.add(temp);
            }
        }
        double ran = (Math.random()*((1-0)+1));
        if(wait){output.add(new Recommendation("@Wait"));}
        else if(leftObs&&frontObs&&rightObs){
            if(ran<0.9){
            output.add(new Recommendation("@rl()"));
            }
            else{
                output.add(new Recommendation("@rr()"));
            }

        }
        else if(frontObs&&leftObs){
            output.add(new Recommendation("@fr()"));
        }
        else if(frontObs&&rightObs){
            output.add(new Recommendation("@fl"));
        }


        else if(!leftObs&&ran<0.5){

            output.add(new Recommendation("@fl()"));
        }
        else if(!rightObs&&ran<0.5){
            output.add(new Recommendation("@fr()"));
        }
        else{
            output.add(new  Recommendation("@f()"));
        }



        return output;
    }
}
