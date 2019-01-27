package gian.compiler.practice.lexical.transform;

import com.alibaba.fastjson.JSON;

import java.util.*;

/**
 * DFA节点
 * Created by Gian on 2019/1/27.
 */
public class DtranState {


    private String stateName;

    private Set<State> stateSet = new HashSet<>();
    private Set<DtranEdge> dtranEdgeSet = new HashSet<>();

    private Set<String> stateNames = new TreeSet<>(new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o2.compareTo(o1);//降序排列
        }
    });

    public DtranState(String stateName){
        this.stateName = stateName;
    }

    public void addState(State state){
        this.stateSet.add(state);

        this.stateNames.add(state.stateName);
    }

    public void addState(Set<State> stateSet){
        this.stateSet.addAll(stateSet);

        for(State state : stateSet){
            this.stateNames.add(state.stateName);
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

    @Override
    public String toString(){
        return JSON.toJSONString(stateNames);
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Set<State> getStateSet() {
        return stateSet;
    }

    public void setStateSet(Set<State> stateSet) {
        this.stateSet = stateSet;
    }

    public Set<DtranEdge> getDtranEdgeSet() {
        return dtranEdgeSet;
    }

    public void setDtranEdgeSet(Set<DtranEdge> dtranEdgeSet) {
        this.dtranEdgeSet = dtranEdgeSet;
    }

    public Set<String> getStateNames() {
        return stateNames;
    }

    public void setStateNames(Set<String> stateNames) {
        this.stateNames = stateNames;
    }
}
