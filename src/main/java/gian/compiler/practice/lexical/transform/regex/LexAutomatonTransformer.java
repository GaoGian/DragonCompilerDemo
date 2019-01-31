package gian.compiler.practice.lexical.transform.regex;

import com.alibaba.fastjson.JSON;
import gian.compiler.practice.lexical.parser.LexExpression;

import java.util.*;

/**
 * 将正则表达式后缀树处理成状态机
 * Created by gaojian on 2019/1/31.
 */
public class LexAutomatonTransformer {

    public static LexCell tranformNFA(List<LexExpression.Expression> expressions){

            List<LexSimplePattern.Metacharacter> postfixMetas;

    }

    public static class LexState{
        private String stateName;
        private Map<LexState, LexEdge> edgeSet = new HashMap<>();
        private Set<LexState> stateSet = new HashSet<>();
        // TODO 需要设置 NFA、DFA 通用的标识符集合

        // DFA 时使用
        public String getTag(){
            return JSON.toJSONString(stateSet);
        }

        @Override
        public boolean equals(Object other){
            if(other == null){
                return false;
            }

            LexState otherState = (LexState) other;
            if(this.stateSet.size() != otherState.stateSet.size()){
                return false;
            }

            // TODO 需要设置 NFA、DFA 通用的标识符集合
            for(LexState state : otherState.stateSet){
                if(!this.stateSet.contains(state)){
                    return false;
                }
            }

            return true;
        }

        @Override
        public String toString(){
            return stateName + ":" + JSON.toJSONString(stateSet);
        }

        public void addState(LexNode state){
            this.stateSet.add(state);

            this.stateNames.add(state.getElement());
            this.statePos.add(state.getPos());
        }

        public void addState(Set<LexNode> stateSet){
            this.stateSet.addAll(stateSet);

            for(LexNode state : stateSet){
                this.stateNames.add(state.getElement());
                this.statePos.add(state.getPos());
            }
        }

        public Dstate tranState(char input){
            for(Dedge dedge : this.getDtranEdgeSet()){
                if(dedge.getTransSymbol() == input){
                    return dedge.getEndState();
                }
            }
            return null;
        }


    }

    public static class LexEdge{

        private LexState startState;
        private LexState endState;
        private LexSimplePattern.Metacharacter tranPattern;

        public LexEdge(LexState startState, LexState endState, LexSimplePattern.Metacharacter tranPattern) {
            this.startState = startState;
            this.endState = endState;
            this.tranPattern = tranPattern;
        }

        @Override
        public boolean equals(Object other){
            if(other == null){
                return false;
            }

            LexEdge otherState = (LexEdge) other;
            if(otherState.getStartState().equals(this.startState) && otherState.getEndState().equals(this.endState) && otherState.getTranPattern() == this.tranPattern){
                return true;
            }

            return false;
        }

        public LexState getStartState() {
            return startState;
        }

        public void setStartState(LexState startState) {
            this.startState = startState;
        }

        public LexState getEndState() {
            return endState;
        }

        public void setEndState(LexState endState) {
            this.endState = endState;
        }

        public LexSimplePattern.Metacharacter getTranPattern() {
            return tranPattern;
        }

        public void setTranPattern(LexSimplePattern.Metacharacter tranPattern) {
            this.tranPattern = tranPattern;
        }
    }

    public static class LexCell{

        private LexState startState;
        private Set<LexState> endStatesSet = new HashSet<>();
        private Map<LexState, LexEdge> edgeSet = new HashMap<>();

        private Set<LexState> states = new HashSet<>();
        private Set<LexSimplePattern.Metacharacter> tranChar = new HashSet<>();
        private Map<String, LexState> stateMap = new HashMap<>();

        public LexState getStartState() {
            return startState;
        }

        public void setStartState(LexState startState) {
            this.startState = startState;
        }

        public Set<LexState> getEndStatesSet() {
            return endStatesSet;
        }

        public void setEndStatesSet(Set<LexState> endStatesSet) {
            this.endStatesSet = endStatesSet;
        }

        public Map<LexState, LexEdge> getEdgeSet() {
            return edgeSet;
        }

        public void setEdgeSet(Map<LexState, LexEdge> edgeSet) {
            this.edgeSet = edgeSet;
        }

        public Set<LexState> getStates() {
            return states;
        }

        public void setStates(Set<LexState> states) {
            this.states = states;
        }

        public Set<LexSimplePattern.Metacharacter> getTranChar() {
            return tranChar;
        }

        public void setTranChar(Set<LexSimplePattern.Metacharacter> tranChar) {
            this.tranChar = tranChar;
        }

        public Map<String, LexState> getStateMap() {
            return stateMap;
        }

        public void setStateMap(Map<String, LexState> stateMap) {
            this.stateMap = stateMap;
        }
    }

    // 用于 DFA、NFA 通用的标识集合
    public static class LexTagPos {

    }

}