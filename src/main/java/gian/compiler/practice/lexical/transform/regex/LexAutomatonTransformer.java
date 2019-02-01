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
        protected String stateName;
        // 当前节点的 out 转换边
        protected Map<LexSimplePattern.Metacharacter, LexEdge> edgeSet = new HashMap<>();

        @Override
        public boolean equals(Object other){
            if(other == null){
                return false;
            }

            LexState otherState = (LexState) other;
            if(!this.stateName.equals(otherState.getStateName())){
                return false;
            }

            return true;
        }

        /**
         * 获取转换节点
          */
        public LexState tranState(LexSimplePattern.Metacharacter tranMeta){
            LexEdge tranEdge = this.edgeSet.get(tranMeta);
            if(tranEdge != null) {
                return tranEdge.getEndState();
            }else{
                return null;
            }
        }

        // 获取标识符
        public String getTag(){
            return this.stateName;
        }

        @Override
        public String toString(){
            return stateName;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public Map<LexSimplePattern.Metacharacter, LexEdge> getEdgeSet() {
            return edgeSet;
        }

        public void setEdgeSet(Map<LexSimplePattern.Metacharacter, LexEdge> edgeSet) {
            this.edgeSet = edgeSet;
        }

    }

    // NFA --> DFA
    public static class LexN2DState extends LexState {
        // DFA节点所有的原聚合节点
        protected Set<String> aggStateSet = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);//降序排列
            }
        });

        @Override
        public String getTag(){
            return JSON.toJSONString(aggStateSet);
        }

        @Override
        public boolean equals(Object other) {
            if(other == null){
                return false;
            }

            LexN2DState otherState = (LexN2DState) other;
            if(this.aggStateSet.size() != otherState.aggStateSet.size()){
                return false;
            }

            for(String nfaStateName : otherState.aggStateSet){
                if(!this.aggStateSet.contains(nfaStateName)){
                    return false;
                }
            }

            return true;
        }

        @Override
        public String toString(){
            return this.getStateName() + ":" + JSON.toJSONString(aggStateSet);
        }

        public Set<String> getAggStateSet() {
            return aggStateSet;
        }

        public void setAggStateSet(Set<String> aggStateSet) {
            this.aggStateSet = aggStateSet;
        }

    }

    // Regex --> DFA
    public static class LexR2DState extends LexState {
        protected Set<Integer> statePos = new TreeSet<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);//降序排列
            }
        });

        @Override
        public String getTag(){
            return JSON.toJSONString(statePos);
        }

        @Override
        public boolean equals(Object other){
            if(other == null){
                return false;
            }

            LexR2DState otherState = (LexR2DState) other;
            if(this.statePos.size() != otherState.statePos.size()){
                return false;
            }

            for(Integer pos : otherState.statePos){
                if(!this.statePos.contains(pos)){
                    return false;
                }
            }

            return true;
        }

        @Override
        public String toString(){
            return this.getStateName() + ":" + JSON.toJSONString(statePos);
        }

        public Set<Integer> getStatePos() {
            return statePos;
        }

        public void setStatePos(Set<Integer> statePos) {
            this.statePos = statePos;
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
            if(otherState.getStartState().equals(this.startState)
                    && otherState.getEndState().equals(this.endState)
                    && otherState.getTranPattern().equals(this.tranPattern)){

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
        private Set<LexEdge> edgeSet = new HashSet<>();

    }



}