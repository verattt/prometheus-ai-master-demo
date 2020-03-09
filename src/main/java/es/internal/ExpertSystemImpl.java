package es.internal;

import javax.inject.Inject;
import java.util.*;

import Interfaces.LayerInput;
import Interfaces.LayerOutput;
import Interfaces.Thinking;
import com.google.inject.Guice;
import com.google.inject.assistedinject.Assisted;
import es.api.ExpertSystem;
import prometheus.api.Prometheus;
import prometheus.guice.PrometheusModule;
import tags.*;
import tags.Tuple;
/**
 * Implementation of the ES.
 */
class ExpertSystemImpl implements ExpertSystem, LayerInput,Thinking{
    private final Thinker thinker;
    private final Teacher teacher;
    private final Rester rester;
    private final Set<Rule> readyRules;
    private final Set<Rule> activeRules;
    private final Set<Fact> facts;
    private final Set<Recommendation> recommendations;
    private Set<Tag> input;
    private List<Tuple> output;

    @Inject
    ExpertSystemImpl(
            @Assisted("readyRules") final Set<Rule> readyRules,
            @Assisted("activeRules") final Set<Rule> activeRules,
            @Assisted("facts") final Set<Fact> facts,
            @Assisted("recommendations")
            final Set<Recommendation> recommendations,
            final ThinkerFactory thinkerFactory,
            final TeacherFactory teacherFactory,
            final ResterFactory resterFactory) {
        this.readyRules = readyRules;
        this.activeRules = activeRules;
        this.facts = facts;
        this.recommendations = recommendations;
        this.thinker = thinkerFactory
                .create(readyRules, activeRules, facts, recommendations);
        this.teacher = teacherFactory.create(readyRules);
        this.rester = resterFactory.create(readyRules);
    }

    @Override
    public Set<Recommendation> think() {
        return thinker.think(false, Integer.MAX_VALUE);
    }

    @Override
    public Set<Recommendation> think(final boolean generateRule) {
        return thinker.think(generateRule, Integer.MAX_VALUE);
    }

    @Override
    public Set<Recommendation> think(final boolean generateRule,
                                     final int numberOfCycles) {
        return thinker.think(generateRule, numberOfCycles);
    }


    @Override
    public void teach(final String sentence) {
        teacher.teach(sentence);
    }

    @Override
    public void rest(final int numberOfCycles) {
        rester.rest(numberOfCycles);
    }

    @Override
    public void reset() {
        activeRules.clear();
        readyRules.clear();
        facts.clear();
        recommendations.clear();
    }

    @Override
    public void deactivateRules() {
        readyRules.addAll(activeRules);
        activeRules.clear();
    }

    @Override
    public void addTags(final Set<Tag> tags) {
        for (final Tag t : tags) {
            addTag(t);
        }
    }

    @Override
    public boolean addTag(final Tag tag) {
        if (tag instanceof Rule) {
            return addReadyRule((Rule) tag);
        } else if (tag instanceof Fact) {
            return addFact((Fact) tag);
        } else if (tag instanceof Recommendation) {
            return addRecommendation((Recommendation) tag);
        }
        return false;
    }

    @Override
    public boolean addReadyRule(final Rule rule) {
        return readyRules.add(rule);
    }

    @Override
    public boolean removeReadyRule(final Rule rule) {
        return readyRules.remove(rule);
    }

    @Override
    public boolean addFact(final Fact fact) {
        return facts.add(fact);
    }

    @Override
    public boolean removeFact(final Fact fact) {
        return facts.remove(fact);
    }

    @Override
    public boolean addRecommendation(final Recommendation rec) {
        return recommendations.add(rec);
    }

    @Override
    public Set<Rule> getReadyRules() {
        return Collections.unmodifiableSet(readyRules);
    }

    @Override
    public Set<Rule> getActiveRules() {
        return Collections.unmodifiableSet(activeRules);
    }

    @Override
    public Set<Fact> getFacts() {
        return Collections.unmodifiableSet(facts);
    }

    @Override
    public Set<Recommendation> getRecommendations() {
        return Collections.unmodifiableSet(recommendations);
    }

    @Override
    public void recieveDataStream(List<Tuple> x) {
        this.input = new HashSet<>();
        for(Tuple a: x){
            if(a instanceof Tag){
                input.add((Tag)a);
            }
        }

    }




    public Iterator<Tag> iterator()
    {
        return input.iterator();
    }
    public List<Tuple> thinkG(int iteration, List<Tuple> tuples){
        Set<Tag> output =new HashSet<>();
        for(Tuple a: tuples){
            if(a instanceof Tag){
                output.add((Tag)a);
            }
        }
        addTags(output);
        Set<Recommendation> x =think();
        if(x!=null){
        List<Tuple> rec = new ArrayList<>(x);
            return rec;
        }
        return null;
    }

    @Override
    public List<Tuple> think(int iterate, List<Tuple> tuples) {
        reset();
        recieveDataStream(tuples);
       addTags(input);
        List<Tuple> rec =new ArrayList<>(getRecommendations());
        return rec;
    }
//    @Override
//    public Tuple firstTuple() {
//        if(input.get(0)!=null){
//            return input.get(0);}
//            else{return null;}
//    }
//
//    @Override
//    public Tuple nextTuple() {
//
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

}
