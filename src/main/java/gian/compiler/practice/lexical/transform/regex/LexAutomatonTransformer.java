package gian.compiler.practice.lexical.transform.regex;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import gian.compiler.practice.lexical.parser.LexExpression;
import gian.compiler.practice.lexical.transform.LexConstants;
import gian.compiler.practice.lexical.transform.LexUtils;
import gian.compiler.practice.lexical.transform.MyStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 将正则表达式后缀树处理成状态机
 * Created by gaojian on 2019/1/31.
 */
public class LexAutomatonTransformer {

    public static LexCell tranformNFA(List<LexExpression.Expression> expressions){

        List<LexSimplePattern.Metacharacter> postfixMetas;

        return null;
    }

    public static LexCell express2NFA(String expression){
        List<LexSimplePattern.Metacharacter> metas = LexSimplePattern.compile(expression);
        List<LexSimplePattern.Metacharacter> postfixMetas = LexSimplePattern.postfix(metas);
        // TODO 需要转换成 DFA
        return express2NFA(postfixMetas, new AtomicInteger(0));
    }

    // 表达式转NFA
    public static LexCell express2NFA(List<LexSimplePattern.Metacharacter> postfixMetas, AtomicInteger stateNum) {

        LexCell cell, left, right;
        MyStack<LexCell> stack = new MyStack<>();

        for(LexSimplePattern.Metacharacter element : postfixMetas){
            if(!element.isLetter()) {
                String meta = element.getMeta();
                switch (meta) {
                    case "|": {
                        right = stack.pop();
                        left = stack.pop();
                        cell = doUnite(left, right, stateNum);
                        stack.push(cell);
                        break;
                    }
                    case "*": {
                        left = stack.pop();
                        cell = doStart(left, stateNum);
                        stack.push(cell);
                        break;
                    }
                    case "+": {
                        left = stack.pop();
                        cell = doOneMore(left, stateNum);
                        stack.push(cell);
                        break;
                    }
                    case "?": {
                        left = stack.pop();
                        cell = doOneLess(left, stateNum);
                        stack.push(cell);
                        break;
                    }
                }
            }else{
                if(!element.isMetaList()){
                    cell = doCell(element, stateNum);
                    stack.push(cell);
                }else{
                    cell = express2NFA(element.getChildMetas(), stateNum);
                    stack.push(cell);
                }
            }

        }

        while(stack.size() > 1){
            right = stack.pop();
            left = stack.pop();
            cell = doJoin(left, right);
            stack.push(cell);
        }

        cell = stack.pop();

        return cell;

    }

    /*
    * 处理 a|b
    *
    *        a_start ----> a_end
    *      ε/                  \ε
    *  start                      end
    *      ε\                  /ε
     *       b_start ----> b_end
     *
    * */
    public static LexCell doUnite(LexCell left, LexCell right, AtomicInteger stateNum) {
        LexCell newCell = new LexCell();

        // 获得新节点
        LexState startState = newLexState(stateNum.getAndIncrement());
        LexState endState = newLexState(stateNum.getAndIncrement());

        // 构建边
        LexSimplePattern.Metacharacter epsilonMeta = new LexSimplePattern.Metacharacter(LexConstants.EPSILON_STR, false, true);
        LexEdge edge1 = new LexEdge(startState, left.getStartState(), epsilonMeta);
        startState.getEdgeMap().put(epsilonMeta, edge1);
        LexEdge edge2 = new LexEdge(startState, right.getStartState(), epsilonMeta);
        startState.getEdgeMap().put(epsilonMeta, edge2);
        LexEdge edge3 = new LexEdge(left.getEndState(), endState, epsilonMeta);
        left.getEndState().getEdgeMap().put(epsilonMeta, edge3);
        LexEdge edge4 = new LexEdge(right.getEndState(), endState, epsilonMeta);
        right.getEndState().getEdgeMap().put(epsilonMeta, edge4);

        List<LexEdge> newCellEdges = new ArrayList<>();
        newCellEdges.add(edge1);
        newCellEdges.add(edge2);
        newCellEdges.add(edge3);
        newCellEdges.add(edge4);

        // 将新构建的四条边加入 edgeMap
        newCell.getEdgeSet().addAll(newCellEdges);

        // 构建单元
        // 先将 left 和 right 的 edgeMap 复制到 newCell
        newCell.getEdgeSet().addAll(left.getEdgeSet());
        newCell.getEdgeSet().addAll(right.getEdgeSet());

        // 构建 newCell 的起始状态和结束状态（只有一个结束状态）
        newCell.setStartState(startState);
        newCell.setEndState(endState);

        return newCell;

    }

    /*
     * 处理 a*
     *                      ε
     *                 ------------
     *          ε    ↓           |    ε
     *   start ---> c_start ---> c_end ---> end
     *     |                                 ↑
     *      ----------------------------------
     *                      ε
     */
    public static LexCell doStart(LexCell cell, AtomicInteger stateNum) {
        LexCell newCell = new LexCell();

        // 获得新节点
        LexState startState = newLexState(stateNum.getAndIncrement());
        LexState endState = newLexState(stateNum.getAndIncrement());

        // 构建边
        LexSimplePattern.Metacharacter epsilonMeta = new LexSimplePattern.Metacharacter(LexConstants.EPSILON_STR, false, true);
        LexEdge edge1 = new LexEdge(startState, endState, epsilonMeta);
        startState.getEdgeMap().put(epsilonMeta, edge1);
        LexEdge edge2 = new LexEdge(startState, cell.getStartState(), epsilonMeta);
        startState.getEdgeMap().put(epsilonMeta, edge2);
        LexEdge edge3 = new LexEdge(cell.getEndState(), cell.getStartState(), epsilonMeta);
        cell.getEndState().getEdgeMap().put(epsilonMeta, edge3);
        LexEdge edge4 = new LexEdge(cell.getEndState(), endState, epsilonMeta);
        cell.getEndState().getEdgeMap().put(epsilonMeta, edge4);

        List<LexEdge> newCellEdges = new ArrayList<>();
        newCellEdges.add(edge1);
        newCellEdges.add(edge2);
        newCellEdges.add(edge3);
        newCellEdges.add(edge4);

        // 将新构建的四条边加入 edgeMap
        newCell.getEdgeSet().addAll(newCellEdges);

        // 先将 cell 的 edgeMap 复制到 newCell
        newCell.getEdgeSet().addAll(cell.getEdgeSet());

        // 构建 newCell 的起始状态和结束状态
        newCell.setStartState(startState);
        newCell.setEndState(endState);

        return newCell;

    }

    /*
     * 处理 a+
     *                      ε
     *                 -----------
     *          ε    ↓          |     ε
     *   start ---> c_start ---> c_end ---> end
     *
     */
    public static LexCell doOneMore(LexCell cell, AtomicInteger stateNum) {
        LexCell newCell = new LexCell();

        // 获得新节点
        LexState startState = newLexState(stateNum.getAndIncrement());
        LexState endState = newLexState(stateNum.getAndIncrement());

        // 构建边
        LexSimplePattern.Metacharacter epsilonMeta = new LexSimplePattern.Metacharacter(LexConstants.EPSILON_STR, false, true);
        LexEdge edge1 = new LexEdge(startState, cell.getStartState(), epsilonMeta);
        startState.getEdgeMap().put(epsilonMeta, edge1);
        LexEdge edge2 = new LexEdge(cell.getEndState(), endState, epsilonMeta);
        cell.getEndState().getEdgeMap().put(epsilonMeta, edge2);
        LexEdge edge3 = new LexEdge(cell.getEndState(), cell.getStartState(), epsilonMeta);
        cell.getEndState().getEdgeMap().put(epsilonMeta, edge3);

        List<LexEdge> newCellEdges = new ArrayList<>();
        newCellEdges.add(edge1);
        newCellEdges.add(edge2);
        newCellEdges.add(edge3);

        // 将新构建的四条边加入 edgeMap
        newCell.getEdgeSet().addAll(newCellEdges);

        // 先将 cell 的 edgeMap 复制到 newCell
        newCell.getEdgeSet().addAll(cell.getEdgeSet());

        // 构建 newCell 的起始状态和结束状态
        newCell.setStartState(startState);
        newCell.setEndState(endState);

        return newCell;

    }

    /*
     * 处理 a?
     *          ε                      ε
     *   start ---> c_start ---> c_end ---> end
     *     |                                 ↑
     *      ----------------------------------
     *                      ε
     */
    public static LexCell doOneLess(LexCell cell, AtomicInteger stateNum) {
        LexCell newCell = new LexCell();

        // 获得新节点
        LexState startState = newLexState(stateNum.getAndIncrement());
        LexState endState = newLexState(stateNum.getAndIncrement());

        // 构建边
        LexSimplePattern.Metacharacter epsilonMeta = new LexSimplePattern.Metacharacter(LexConstants.EPSILON_STR, false, true);
        LexEdge edge1 = new LexEdge(startState, endState, epsilonMeta);
        startState.getEdgeMap().put(epsilonMeta, edge1);
        LexEdge edge2 = new LexEdge(startState, cell.getStartState(), epsilonMeta);
        startState.getEdgeMap().put(epsilonMeta, edge2);
        LexEdge edge3 = new LexEdge(cell.getEndState(), endState, epsilonMeta);
        cell.getEndState().getEdgeMap().put(epsilonMeta, edge3);

        List<LexEdge> newCellEdges = new ArrayList<>();
        newCellEdges.add(edge1);
        newCellEdges.add(edge2);
        newCellEdges.add(edge3);

        // 将新构建的四条边加入 edgeMap
        newCell.getEdgeSet().addAll(newCellEdges);

        // 先将 cell 的 edgeMap 复制到 newCell
        newCell.getEdgeSet().addAll(cell.getEdgeSet());

        // 构建 newCell 的起始状态和结束状态
        newCell.setStartState(startState);
        newCell.setEndState(endState);

        return newCell;

    }

    /*
     *  处理 ab
     *
     *      a_start ---> a_end(b_start) ---> b_end
     *                     ↑                  |
     *                      -------------------
     */
    public static LexCell doJoin(LexCell left, LexCell right) {
        // 将 left 的结束状态和 right 的开始状态合并，将 right 的边复制给 left，将 left 返回
        // 将 right 中所有以 startState 开头的边全部修改
        for (LexEdge rightEdge : right.getEdgeSet()) {
            // 处理出边和循环边
            if (rightEdge.getStartState() == right.getStartState()) {
                rightEdge.setStartState(left.getEndState());
            } else if (rightEdge.getEndState() == right.getStartState()) {
                rightEdge.setEndState(left.getEndState());
            }
        }

        // 复制right边给left
        left.getEdgeSet().addAll(right.getEdgeSet());

        // 将 left 的结束状态更新为 right 的结束状态
        left.setEndState(right.getEndState());

        return left;

    }

    /*
     *  处理 a
     *
     *  a_start ---> a_end
     *
     */
    public static LexCell doCell(LexSimplePattern.Metacharacter meta, AtomicInteger stateNum) {
        LexCell newCell = new LexCell();

        // 获得新节点
        LexState startState = newLexState(stateNum.getAndIncrement());
        LexState endState = newLexState(stateNum.getAndIncrement());

        LexEdge newEdge = new LexEdge(startState, endState, meta);

        startState.getEdgeMap().put(meta, newEdge);

        // 构建单元
        newCell.setStartState(startState);
        newCell.setEndState(endState);
        newCell.getEdgeSet().add(newEdge);

        return newCell;

    }


    /**---------------------------------------------------------相关类--------------------------------------------------------**/

    public static LexState newLexState(Integer stateNum){
        LexState newState = new LexState(String.valueOf((char)(stateNum + 65)));
        return newState;
    }

    public static class LexState{
        protected String stateName;
        // 当前节点的 out 转换边
        protected Multimap<LexSimplePattern.Metacharacter, LexEdge> edgeMap = ArrayListMultimap.create();

        public LexState(){

        }

        public LexState(String stateName) {
            this.stateName = stateName;
        }

        @Override
        public boolean equals(Object other){
            if(other == null){
                return false;
            }

            LexState otherState = (LexState) other;
            if(!this.getTag().equals(otherState.getTag())){
                return false;
            }

            return true;
        }

        /**
         * 获取转换节点
          */
        public List<LexState> tranState(LexSimplePattern.Metacharacter tranMeta){
            List<LexState> tranStates = new ArrayList<>();
            Collection<LexEdge> tranEdges = this.edgeMap.get(tranMeta);
            for(LexEdge tranEdge : tranEdges) {
                tranStates.add(tranEdge.getEndState());
            }
            return tranStates;
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

        public Multimap<LexSimplePattern.Metacharacter, LexEdge> getEdgeMap() {
            return edgeMap;
        }

        public void setEdgeMap(Multimap<LexSimplePattern.Metacharacter, LexEdge> edgeMap) {
            this.edgeMap = edgeMap;
        }
    }

    // NFA --> DFA
    public static class LexN2DState extends LexState {
        // DFA节点所有的原聚合节点
        protected Set<LexState> aggStateSet = new TreeSet<>(new Comparator<LexState>() {
            @Override
            public int compare(LexState o1, LexState o2) {
                return o2.getTag().compareTo(o1.getTag());//降序排列
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

            for(LexState nfaState : otherState.aggStateSet){
                if(!this.aggStateSet.contains(nfaState)){
                    return false;
                }
            }

            return true;
        }

        @Override
        public String toString(){
            return this.getStateName() + ":" + JSON.toJSONString(aggStateSet);
        }

        public Set<LexState> getAggStateSet() {
            return aggStateSet;
        }

        public void setAggStateSet(Set<LexState> aggStateSet) {
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

        public LexEdge(){

        }

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
        private LexState endState;
        private Set<LexEdge> edgeSet = new HashSet<>();

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

        public Set<LexEdge> getEdgeSet() {
            return edgeSet;
        }

        public void setEdgeSet(Set<LexEdge> edgeSet) {
            this.edgeSet = edgeSet;
        }
    }


    public static class LexNode{

        private char element;
        private Integer pos;
        private LexNodeType type;

        private LexNode left;
        private LexNode right;

        private Set<Integer> firstPos = new HashSet<>();
        private Set<Integer> lastPos = new HashSet<>();
        private Set<Integer> followPos = new HashSet<>();

        public char getElement() {
            return element;
        }

        public void setElement(char element) {
            this.element = element;
        }

        public Integer getPos() {
            return pos;
        }

        public void setPos(Integer pos) {
            this.pos = pos;
        }

        public LexNode getLeft() {
            return left;
        }

        public void setLeft(LexNode left) {
            this.left = left;
        }

        public LexNode getRight() {
            return right;
        }

        public void setRight(LexNode right) {
            this.right = right;
        }

        public LexNodeType getType() {
            return type;
        }

        public void setType(LexNodeType type) {
            this.type = type;
        }

        public Set<Integer> getFirstPos() {
            return firstPos;
        }

        public void setFirstPos(Set<Integer> firstPos) {
            this.firstPos = firstPos;
        }

        public Set<Integer> getLastPos() {
            return lastPos;
        }

        public void setLastPos(Set<Integer> lastPos) {
            this.lastPos = lastPos;
        }

        public Set<Integer> getFollowPos() {
            return followPos;
        }

        public void setFollowPos(Set<Integer> followPos) {
            this.followPos = followPos;
        }

        @Override
        public String toString(){
            if(pos != null) {
                return pos + ":" + JSON.toJSONString(firstPos) + "'" + element+ "'" + JSON.toJSONString(lastPos) + ":" + JSON.toJSONString(followPos);
            }else{
                return JSON.toJSONString(firstPos) + "'" + element+ "'" + JSON.toJSONString(lastPos) + ":" + JSON.toJSONString(followPos);
            }
        }

    }

    public static enum LexNodeType{
        EPSILON, // ε子节点
        LEAF,   // 非空子节点
        OR,     // or 节点
        CAT,    // | 节点
        START   // * 节点
    }


    public static void computeFollowPos(LexNode root){
        Map<Integer, LexNode> nodeMap = new HashMap<>();
        getLeafLexNodeMap(root, nodeMap);
        followPos(root, nodeMap);
    }

    public static void getLeafLexNodeMap(LexNode node, Map<Integer, LexNode> nodeMap){
        if(node.getType().equals(LexNodeType.LEAF)){
            nodeMap.put(node.getPos(), node);
        }else{
            if(node.getLeft() != null){
                getLeafLexNodeMap(node.getLeft(), nodeMap);
            }
            if(node.getRight() != null){
                getLeafLexNodeMap(node.getRight(), nodeMap);
            }
        }
    }


    public static void computeFirstAndLastPos(LexNode node){
        // firstPos
        node.getFirstPos().addAll(firstPos(node));
        // lastPos
        node.getLastPos().addAll(lastPos(node));

        // childNode
        if(node.getLeft() != null){
            computeFirstAndLastPos(node.getLeft());
        }
        if(node.getRight() != null){
            computeFirstAndLastPos(node.getRight());
        }
    }

    /**
     * 构造词法分析树
     * @param express
     * @return
     */
    public static LexNode buildLexNode(String express){
        int lexNodeNum = 1;
        MyStack<LexNode> stack = new MyStack<>();
        for (int i = 0; i < express.length(); i++) {
            char element = express.charAt(i);
            switch (element) {
                case '|': {
                    LexNode right = stack.top();
                    stack.pop();
                    LexNode left = stack.top();
                    stack.pop();
                    LexNode cell = do_LexNode(element, LexNodeType.OR, null, left, right);
                    stack.push(cell);
                    break;
                }
                case '*': {
                    LexNode left = stack.top();
                    stack.pop();
                    LexNode cell = do_LexNode(element, LexNodeType.START, null, left, null);
                    stack.push(cell);
                    break;
                }
                case '+': {
                    LexNode right = stack.top();
                    stack.pop();
                    LexNode left = stack.top();
                    stack.pop();
                    LexNode cell = do_LexNode(element, LexNodeType.CAT, null, left, right);
                    stack.push(cell);
                    break;
                }
                default: {
                    LexNode node = do_LexNode(element, LexNodeType.LEAF, lexNodeNum++, null, null);
                    stack.push(node);
                }
            }
        }

        System.out.println("语法分析树构造完毕");

        LexNode root = stack.pop();

        // 计算各位置的nullAble、firstPos、lastPos
        computeFirstAndLastPos(root);
        // 计算各位置的followPos
        computeFollowPos(root);

        // 输出语法分析树结果
        LexUtils.print(root);

        return root;
    }

    public static LexNode do_LexNode(char element, LexNodeType type, Integer pos, LexNode left, LexNode right){
        LexNode node = new LexNode();
        node.setElement(element);
        node.setPos(pos);
        node.setType(type);

        node.setLeft(left);
        node.setRight(right);

        return node;
    }

    // 语法分析书节点是否可以推导出ε
    public static boolean nullAble(LexNode node){
        boolean result = false;
        if(node.type.equals(LexNodeType.EPSILON)){
            result = true;
        }else if(node.type.equals(LexNodeType.OR)){
            result = nullAble(node.getLeft()) || nullAble(node.getRight());
        }else if(node.type.equals(LexNodeType.CAT)){
            result = nullAble(node.getLeft()) && nullAble(node.getRight());
        }else if(node.type.equals(LexNodeType.START)){
            result = true;
        }else if(node.type.equals(LexNodeType.LEAF)){
            result = false;
        }
        return result;
    }

    // 计算当前节点为根的首字符集合
    public static Set<Integer> firstPos(LexNode node){
        Set<Integer> pos = new HashSet<>();
        if(node.type.equals(LexNodeType.EPSILON)){

        }else if(node.type.equals(LexNodeType.OR)){
            pos.addAll(firstPos(node.getLeft()));
            pos.addAll(firstPos(node.getRight()));
        }else if(node.type.equals(LexNodeType.CAT)){
            if(nullAble(node.getLeft())){
                pos.addAll(firstPos(node.getLeft()));
                pos.addAll(firstPos(node.getRight()));
            }else{
                pos.addAll(firstPos(node.getLeft()));
            }
        }else if(node.type.equals(LexNodeType.START)){
            pos.addAll(firstPos(node.getLeft()));
        }else if(node.type.equals(LexNodeType.LEAF)){
            pos.add(node.getPos());
        }

        return pos;
    }

    // 计算当前节点为根的最后字符集合
    public static Set<Integer> lastPos(LexNode node){
        Set<Integer> pos = new HashSet<>();
        if(node.type.equals(LexNodeType.EPSILON)){

        }else if(node.type.equals(LexNodeType.OR)){
            pos.addAll(lastPos(node.getLeft()));
            pos.addAll(lastPos(node.getRight()));
        }else if(node.type.equals(LexNodeType.CAT)){
            if(nullAble(node.getRight())){
                pos.addAll(lastPos(node.getLeft()));
                pos.addAll(lastPos(node.getRight()));
            }else{
                pos.addAll(lastPos(node.getRight()));
            }
        }else if(node.type.equals(LexNodeType.START)){
            pos.addAll(lastPos(node.getLeft()));
        }else if(node.type.equals(LexNodeType.LEAF)){
            pos.add(node.getPos());
        }

        return pos;
    }

    // 计算当前节点位置对应的正则表达式
    public static void followPos(LexNode node, Map<Integer, LexNode> lexNodeMap){
        if(node.getType().equals(LexNodeType.CAT)){
            if(node.getRight() != null){
                LexNode rightChild = node.getRight();
                Set<Integer> rightFirstPos = rightChild.getFirstPos();

                if(node.getLeft() != null){
                    LexNode leftChild = node.getLeft();
                    Set<Integer> leftLastPos = leftChild.getLastPos();

                    for(Integer lastPos : leftLastPos){
                        LexNode lastPosNode = lexNodeMap.get(lastPos);
                        lastPosNode.getFollowPos().addAll(rightFirstPos);
                    }
                }
            }
        }else if(node.getType().equals(LexNodeType.START)){
            Set<Integer> firstPos = node.getFirstPos();
            Set<Integer> lastPos = node.getLastPos();
            for(Integer pos : lastPos){
                LexNode lastPosNode = lexNodeMap.get(pos);
                lastPosNode.getFollowPos().addAll(firstPos);
            }
        }

        // child
        if(!node.getType().equals(LexNodeType.LEAF)){
            if(node.getLeft() != null){
                followPos(node.getLeft(), lexNodeMap);
            }
            if(node.getRight() != null){
                followPos(node.getRight(), lexNodeMap);
            }
        }
    }

}