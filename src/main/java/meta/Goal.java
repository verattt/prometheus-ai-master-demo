package meta;

import java.util.Objects;

public class Goal {
    private Node goal;
    private int priority = -1;

    public Goal(Node goal, int priority) {
        this.goal = goal;
        this.priority = priority;
    }

    public Node getGoal() {
        return goal;
    }

    public void setGoal(Node goal) {
        this.goal = goal;
    }

    public int getPriority(){
        return this.priority;
    }

    public void setPriority(int priority){
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Goal)) return false;
        Goal goal1 = (Goal) o;
        return getPriority() == goal1.getPriority() &&
                Objects.equals(getGoal(), goal1.getGoal());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getGoal(), getPriority());
    }
}
