package gian.compiler.practice.lexical.transform;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Gian on 2019/1/27.
 */
public class DtranCell {

    private Set<DtranEdge> edgeSet = new HashSet<DtranEdge>();
    private DtranState startState;
    private DtranState endState;

    public Set<DtranEdge> getEdgeSet() {
        return edgeSet;
    }

    public void setEdgeSet(Set<DtranEdge> edgeSet) {
        this.edgeSet = edgeSet;
    }

    public DtranState getStartState() {
        return startState;
    }

    public void setStartState(DtranState startState) {
        this.startState = startState;
    }

    public DtranState getEndState() {
        return endState;
    }

    public void setEndState(DtranState endState) {
        this.endState = endState;
    }
}
