package gian.compiler.practice.lexical.transform;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * DFA节点
 * Created by Gian on 2019/1/27.
 */
public class DtranState {


    public String stateName;

    Set<State> stateSet = new HashSet<>();
    Set<String> stateNames = new HashSet<>();

    public DtranState(State startState, Edge[] edgeSet){
        // TODO 需要优化算法，由State持有离开边
        this.epsilonClosure(startState, edgeSet);
    }

    public void epsilonClosure(State startState, Edge[] edgeSet){
        for(int i=0; i<edgeSet.length; i++){
            Edge edge = edgeSet[i];
            if(edge.startState.stateName == startState.stateName
                    && LexConstants.EPSILON == edge.transSymbol){
                stateSet.add(edge.endState);

                this.epsilonClosure(edge.endState, edgeSet);
            }
        }
    }

    @Override
    public boolean equals(Object other){
        if(other == null){
            return false;
        }

        DtranState otherState = (DtranState) other;
        if(this.stateSet.size() != otherState.stateSet.size()){
            return false;
        }

        for(State state : otherState.stateSet){
            if(!stateNames.contains(state.stateName)){
                return false;
            }
        }

        return true;

    }

}
