package gian.compiler.practice.lexical.transform.regex;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import gian.compiler.practice.lexical.parser.LexExpression;
import gian.compiler.practice.lexical.transform.LexConstants;
import gian.compiler.practice.lexical.transform.MyStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 将正则表达式后缀树处理成状态机
 * Created by gaojian on 2019/1/31.
 */
public class LexAutomatonTransformer {

    public static final LexSimplePattern.Metacharacter EPSILON_META = new LexSimplePattern.Metacharacter(LexConstants.EPSILON_STR, false, true);

    /**
     * 根据设定的词法扫描token
     * @param expressions
     * @return
     */
    public static LexCell tranformNFA(List<LexExpression.Expression> expressions){

        List<LexSimplePattern.Metacharacter> postfixMetas;

        return null;
    }

    /**
     * 直接根据followPos生成DFA
     * @param expression
     * @return
     */
    public static LexCell express2DFA(String expression){
        return null;
    }

    /**
     * 先生成NFA再转换成DFA
     * @param expression
     * @return
     */
    public static LexCell tranNFA2DFA(String expression){
        LexCell lexCell = LexAutomatonTransformer.express2NFA(expression);
        return tranNFA2DFA(lexCell);
    }
    public static LexDFACell tranNFA2DFA(LexCell lexCell){

        // 使用子集构造法构造DFA
        AtomicInteger stateNum = new AtomicInteger(0);
        // 起始节点
        LexDFACell tranN2DCell = new LexDFACell();
        LexAggState startN2DState = new LexAggState(String.valueOf(stateNum.getAndIncrement()));
        startN2DState.getAggStateSet().addAll(epsilonClosure(lexCell.getStartState()));

        // 存储生成的DFA状态
        Map<String, LexAggState> allN2DStateMap = new HashMap<>();
        allN2DStateMap.put(startN2DState.getTag(), startN2DState);
        // 存储需要遍历的DFA状态
        List<LexAggState<LexState>> tranStateList = new ArrayList<>();
        tranStateList.add(startN2DState);
        // 存储已遍历的DFA状态
        Set<String> tranStateTag = new HashSet<>();

        // 记录转换符
        Set<LexSimplePattern.Metacharacter> tranMetas = new HashSet<>();
        for(LexEdge edge : lexCell.getEdgeSet()){
            // 排除ε转换符
            if(!edge.getTranPattern().getMeta().equals(LexConstants.EPSILON_STR)) {
                tranMetas.add(edge.getTranPattern());
            }
        }

        // 聚集 ε-closure 集合为DFA节点
        for(int i=0; i<tranStateList.size(); i++){
            LexAggState preTranState = tranStateList.get(i);
            // 判断该状态是否已经处理过了
            if(!tranStateTag.contains(preTranState.getTag())) {
                for (LexSimplePattern.Metacharacter tranMeta : tranMetas) {
                    // 在当前输入符下转变的状态，集合为：ε-closure(move[A, a])
                    Set<LexState> moveStates = new HashSet<>();
                    for (Object state : preTranState.getAggStateSet()) {
                        moveStates.addAll(move((LexState) state, tranMeta));
                    }

                    if(moveStates.size() > 0) {
                        LexAggState subTranState = new LexAggState(String.valueOf(stateNum.getAndIncrement()));
                        subTranState.getAggStateSet().addAll(moveStates);
                        // 保存新生成的DFA状态  FIXME 由于这里是饱和新增，所以不能使用stateName作为标识符，需要使用getTag方法
                        if (allN2DStateMap.get(subTranState.getTag()) == null) {
                            allN2DStateMap.put(subTranState.getTag(), subTranState);
                        } else {
                            subTranState = allN2DStateMap.get(subTranState.getTag());
                        }

                        LexEdge tranEdge = new LexEdge();
                        tranEdge.setStartState(preTranState);
                        tranEdge.setEndState(subTranState);
                        tranEdge.setTranPattern(tranMeta);

                        preTranState.getEdgeMap().put(tranMeta, tranEdge);

                        tranN2DCell.getEdgeSet().add(tranEdge);

                        // 将新生成的 DFA 状态加入到遍历列表
                        tranStateList.add(subTranState);
                    }
                }

                // 添加已处理标记
                tranStateTag.add(preTranState.getTag());
            }
        }

        // 找出接收状态节点(没有指向其他节点的边就是接受节点)
        for(LexAggState tranN2DState : allN2DStateMap.values()){
            boolean isAccState = true;
            if(tranN2DState.getEdgeMap().size() != 0){
                for(LexEdge edge : tranN2DState.getEdgeMap().values()){
                    if(!edge.getEndState().equals(tranN2DState)){
                        isAccState = false;
                    }
                }
            }
            if(isAccState){
                tranN2DCell.getAccStateSet().add(tranN2DState);
            }
        }

        tranN2DCell.setStartState(startN2DState);
        tranN2DCell.getTranMetas().addAll(tranMetas);
        tranN2DCell.getAllStates().addAll(allN2DStateMap.values());

        return tranN2DCell;
    }


    /**
     * 计算 NFA 的ε-closure集合
     * @param targetState
     */
    public static Set<LexState> epsilonClosure(LexState targetState){
        Set<LexState> stateSet = new HashSet<>();
        stateSet.add(targetState);

        for(LexEdge edge : targetState.getEdgeMap().get(EPSILON_META)){
            stateSet.add(edge.getEndState());
            stateSet.addAll(epsilonClosure(edge.getEndState()));
        }

        return stateSet;
    }

    /**
     * 计算 NFA 的转换集合
     * @param targetState
     * @return
     */
    public static Set<LexState> move(LexState targetState, LexSimplePattern.Metacharacter tranMeta){
        Set<LexState> stateSet = new HashSet<>();

        for(LexEdge edge : targetState.getEdgeMap().get(tranMeta)){
            stateSet.add(edge.getEndState());
            stateSet.addAll(epsilonClosure(edge.getEndState()));
        }

        return stateSet;
    }

    /**
     * FIXME 解决BUG
     * 最小化DFA
     * @param originCell
     * @return
     */
    public static LexDFACell minimizeDFA(LexDFACell originCell){
        Set<LexSimplePattern.Metacharacter> tranMetas = originCell.getTranMetas();
        Set<LexAggState> states = new HashSet(originCell.getAllStates());

        // 记录分组情况
        // 设置初始分组：接收状态组、非接收状态组
        List<Set<LexAggState>> groups = new ArrayList<>();
        // 非接收状态组
        Set<LexAggState> unAccStates = new HashSet<>();
        unAccStates.addAll(states);
        unAccStates.removeAll(originCell.getAccStateSet());
        groups.add(unAccStates);
        // 接收状态组
        for(LexAggState originAccState : originCell.getAccStateSet()) {
            // 每个接受态对应一个分组
            Set<LexAggState> accStates = new HashSet<>();
            accStates.add(originAccState);
            groups.add(accStates);
        }

        // 判断是否被拆分过
        boolean isSplit = false;
        while(true){
            isSplit = false;

            // 遍历原来的分组，判断是否可以被拆分（只拆分非接受状态组）
            for(Set<LexAggState> group : groups){
                // 记录新的分组
                List<Set<LexAggState>> newGroups = new ArrayList<>();
                newGroups.addAll(groups);

                for(LexSimplePattern.Metacharacter tranMeta : tranMetas){
                    // 记录同一分组内在不同输入符下的转换集合，根据转换的不同情况进行区分
                    Map<String, Set<LexAggState>> newGroupMap = new HashMap<>();
                    for(LexAggState state : group){
                        // 判断是否存在对应输入符的转换
                        boolean tranAble = false;

                        Set<LexEdge> tranEdgeSet = new HashSet<>(state.getEdgeMap().values());
                        for(LexEdge dedge : tranEdgeSet){
                            // 记录状态在不同输入符下的转换
                            String tranTag = dedge.getEndState().getTag();
                            if(tranMeta.equals(dedge.getTranPattern())){
                                tranAble = true;
                                if(newGroupMap.get(tranTag) == null){
                                    Set<LexAggState> newGroup = new HashSet<>();
                                    newGroup.add(state);
                                    newGroupMap.put(tranTag, newGroup);
                                }else{
                                    newGroupMap.get(tranTag).add(state);
                                }
                            }
                        }

                        // 如果没有转换则记录在不能转换分组
                        if(!tranAble){
                            if(newGroupMap.get(LexConstants.TRAN_UNABLE) == null){
                                Set<LexAggState> newGroup = new HashSet<>();
                                newGroup.add(state);
                                newGroupMap.put(LexConstants.TRAN_UNABLE, newGroup);
                            }else{
                                newGroupMap.get(LexConstants.TRAN_UNABLE).add(state);
                            }
                        }

                    }

                    // 判断分组是否被拆分
                    if(newGroupMap.keySet().size() > 1){
                        // 先清除被拆分的分组
                        newGroups.remove(group);
                        // 加入拆分后的分组，尾递归
                        for(Set<LexAggState> newGroup : newGroupMap.values()){
                            newGroups.add(newGroup);
                        }

                        // 更新分组列表
                        groups = newGroups;

                        isSplit = true;
                    }

                    if(isSplit){
                        break;
                    }
                }

                if(isSplit){
                    break;
                }
            }

            if(!isSplit){
                break;
            }

        }

        // 记录新旧节点的映射关系，key：originState.getTag()，value：newState
        Map<String, LexAggState> tranMinMap = new HashMap<>();
        // 记录原节点的转换关系, key：originState.getTag()，二级key：tranChar，value：originEndState
        Map<String, Map<LexSimplePattern.Metacharacter, LexAggState>> originTranMap = new HashMap<>();

        // 根据分组构造新的 DFA 节点
        Set<LexAggState> newStateSet = new HashSet<>();
        LexState newStartState = null;
        Set<LexAggState> newEndStateSet = new HashSet<>();
        for(Set<LexAggState> group : groups){
            LexAggState newState = new LexAggState();
            for(LexAggState originState : group){
                if(newState.getStateName() == null){
                    newState.setStateName(originState.getStateName());
                }else{
                    newState.setStateName(newState.getStateName() + "_" + originState.getStateName());
                }

                // 最小化后需要加入所有原来节点的内部节点
                newState.getAggStateSet().addAll(originState.getAggStateSet());

                // 判断是否是起始节点
                if(originCell.getStartState().equals(originState)){
                    newStartState = newState;
                }
                // 判断是否是接收节点
                if(originCell.getAccStateSet().contains(originState)){
                    newEndStateSet.add(newState);
                }

                // 记录原节点的转换关系
                for(LexEdge dedge : originState.getEdgeMap().values()){
                    // 记录原节点的转换关系
                    LexAggState originEndState = (LexAggState) dedge.getEndState();
                    if(originTranMap.get(originState.getTag()) == null){
                        Map<LexSimplePattern.Metacharacter, LexAggState> tranMap = new HashMap<>();
                        tranMap.put(dedge.getTranPattern(), originEndState);
                        originTranMap.put(originState.getTag(), tranMap);
                    }else{
                        originTranMap.get(originState.getTag()).put(dedge.getTranPattern(), originEndState);
                    }
                }

                // 记录新旧节点的映射关系
                tranMinMap.put(originState.getTag(), newState);
            }

            newStateSet.add(newState);
        }

        // 生成新的转换边
        Set<LexEdge> newEdages = new HashSet<>();
        for(String originStateTag : tranMinMap.keySet()){
            LexAggState newState = tranMinMap.get(originStateTag);
            Map<LexSimplePattern.Metacharacter, LexAggState> originTran = originTranMap.get(originStateTag);
            for(LexSimplePattern.Metacharacter tranMeta : originTran.keySet()){
                LexAggState originTranState = originTran.get(tranMeta);
                LexAggState newTranState = tranMinMap.get(originTranState.getTag());
                LexEdge newEdge = new LexEdge(newState, newTranState, tranMeta);

                // 记录新的转换边
                newEdages.add(newEdge);
                newState.getEdgeMap().put(tranMeta, newEdge);
            }
        }

        // 构造新的Cell
        LexDFACell minCell = new LexDFACell();
        minCell.setStartState(newStartState);
        minCell.getAccStateSet().addAll(newEndStateSet);
        minCell.getEdgeSet().addAll(newEdages);

        return minCell;
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
                    case LexConstants.UNITE_STR: {
                        right = stack.pop();
                        left = stack.pop();
                        cell = doUnite(left, right, stateNum);
                        stack.push(cell);
                        break;
                    }
                    case LexConstants.START_STR: {
                        left = stack.pop();
                        cell = doStart(left, stateNum);
                        stack.push(cell);
                        break;
                    }
                    case LexConstants.ONE_MORE_STR: {
                        left = stack.pop();
                        cell = doOneMore(left, stateNum);
                        stack.push(cell);
                        break;
                    }
                    case LexConstants.ONE_LESS_STR: {
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

        // 如果长度大于1，则需要把所有单元首尾相接
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
        LexEdge edge1 = new LexEdge(startState, left.getStartState(), EPSILON_META);
        startState.getEdgeMap().put(EPSILON_META, edge1);
        LexEdge edge2 = new LexEdge(startState, right.getStartState(), EPSILON_META);
        startState.getEdgeMap().put(EPSILON_META, edge2);
        LexEdge edge3 = new LexEdge(left.getEndState(), endState, EPSILON_META);
        left.getEndState().getEdgeMap().put(EPSILON_META, edge3);
        LexEdge edge4 = new LexEdge(right.getEndState(), endState, EPSILON_META);
        right.getEndState().getEdgeMap().put(EPSILON_META, edge4);

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
        LexEdge edge1 = new LexEdge(startState, endState, EPSILON_META);
        startState.getEdgeMap().put(EPSILON_META, edge1);
        LexEdge edge2 = new LexEdge(startState, cell.getStartState(), EPSILON_META);
        startState.getEdgeMap().put(EPSILON_META, edge2);
        LexEdge edge3 = new LexEdge(cell.getEndState(), cell.getStartState(), EPSILON_META);
        cell.getEndState().getEdgeMap().put(EPSILON_META, edge3);
        LexEdge edge4 = new LexEdge(cell.getEndState(), endState, EPSILON_META);
        cell.getEndState().getEdgeMap().put(EPSILON_META, edge4);

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
        LexEdge edge1 = new LexEdge(startState, cell.getStartState(), EPSILON_META);
        startState.getEdgeMap().put(EPSILON_META, edge1);
        LexEdge edge2 = new LexEdge(cell.getEndState(), endState, EPSILON_META);
        cell.getEndState().getEdgeMap().put(EPSILON_META, edge2);
        LexEdge edge3 = new LexEdge(cell.getEndState(), cell.getStartState(), EPSILON_META);
        cell.getEndState().getEdgeMap().put(EPSILON_META, edge3);

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
        LexEdge edge1 = new LexEdge(startState, endState, EPSILON_META);
        startState.getEdgeMap().put(EPSILON_META, edge1);
        LexEdge edge2 = new LexEdge(startState, cell.getStartState(), EPSILON_META);
        startState.getEdgeMap().put(EPSILON_META, edge2);
        LexEdge edge3 = new LexEdge(cell.getEndState(), endState, EPSILON_META);
        cell.getEndState().getEdgeMap().put(EPSILON_META, edge3);

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
        // 将 right 中所有包含 startState 的边全部修改
        for (LexEdge rightEdge : right.getEdgeSet()) {
            if (rightEdge.getStartState().equals(right.getStartState())) {
                rightEdge.setStartState(left.getEndState());
                // 给left的结束节点增加新的离开边
                left.getEndState().getEdgeMap().put(rightEdge.getTranPattern(), rightEdge);
            } else if (rightEdge.getEndState().equals(right.getStartState())) {
                rightEdge.setEndState(left.getEndState());
            }
        }

        // 将right的起始节点设置为left的结束节点
        right.setStartState(left.getEndState());

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
        String stateName = String.valueOf((char)(stateNum + 65));
        LexState newState = new LexState(stateName);
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

        @Override
        public int hashCode(){
            return this.getTag().hashCode();
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

    // NFA --> DFA      LexState
    // Regex --> DFA    Integer
    public static class LexAggState<T> extends LexState {
        // DFA节点所有的原聚合节点
        protected Set<T> aggStateSet = new TreeSet<T>(new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });

        public LexAggState(){}

        public LexAggState(String stateName){
            super(stateName);
        }

        @Override
        public String getTag(){
            return JSON.toJSONString(aggStateSet);
        }

        @Override
        public boolean equals(Object other) {
            if(other == null){
                return false;
            }

            LexAggState otherState = (LexAggState) other;
            if(this.aggStateSet.size() != otherState.getAggStateSet().size()){
                return false;
            }

            for(Object state : otherState.getAggStateSet()){
                if(!this.aggStateSet.contains(state)){
                    return false;
                }
            }

            return true;
        }

        @Override
        public int hashCode(){
            return this.getTag().hashCode();
        }

        @Override
        public String toString(){
            StringBuilder str = new StringBuilder();
            str.append("{");
            str.append(this.getStateName());
            if(this.aggStateSet.size() > 0){
                str.append("#{");
                List temp = new ArrayList(this.aggStateSet);
                for(int i=0; i<temp.size(); i++){
                    str.append(temp.get(i).toString());
                    if(i < (temp.size()-1)){
                        str.append(",");
                    }
                }
                str.append("}");
            }
            str.append("}");

            return str.toString();

        }

        public Set<T> getAggStateSet() {
            return aggStateSet;
        }

        public void setAggStateSet(Set<T> aggStateSet) {
            this.aggStateSet = aggStateSet;
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

        @Override
        public int hashCode(){
            return this.toString().hashCode();
        }

        @Override
        public String toString(){
            return this.startState.toString() + ":" + this.endState.toString() + ":" + this.tranPattern.toString();
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

        private Set<LexSimplePattern.Metacharacter> tranMetas = new HashSet<>();
        private Set<LexState> allStates = new HashSet<>();

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

        public Set<LexSimplePattern.Metacharacter> getTranMetas() {
            return tranMetas;
        }

        public void setTranMetas(Set<LexSimplePattern.Metacharacter> tranMetas) {
            this.tranMetas = tranMetas;
        }

        public Set<LexState> getAllStates() {
            return allStates;
        }

        public void setAllStates(Set<LexState> allStates) {
            this.allStates = allStates;
        }
    }

    public static class LexDFACell extends LexCell{
        private Set<LexAggState> accStateSet = new HashSet<>();

        public Set<LexAggState> getAccStateSet() {
            return accStateSet;
        }

        public void setAccStateSet(Set<LexAggState> accStateSet) {
            this.accStateSet = accStateSet;
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

        @Override
        public int hashCode(){
            return this.toString().hashCode();
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
                    LexNode cell = doLexNode(element, LexNodeType.OR, null, left, right);
                    stack.push(cell);
                    break;
                }
                case '*': {
                    LexNode left = stack.top();
                    stack.pop();
                    LexNode cell = doLexNode(element, LexNodeType.START, null, left, null);
                    stack.push(cell);
                    break;
                }
                case '+': {
                    LexNode right = stack.top();
                    stack.pop();
                    LexNode left = stack.top();
                    stack.pop();
                    LexNode cell = doLexNode(element, LexNodeType.CAT, null, left, right);
                    stack.push(cell);
                    break;
                }
                default: {
                    LexNode node = doLexNode(element, LexNodeType.LEAF, lexNodeNum++, null, null);
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

        return root;
    }

    public static LexNode doLexNode(char element, LexNodeType type, Integer pos, LexNode left, LexNode right){
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