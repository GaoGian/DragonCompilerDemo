package gian.compiler.practice.lexical.transform;

import com.alibaba.fastjson.JSON;
import com.sun.deploy.util.StringUtils;

import java.util.*;

import static gian.compiler.practice.lexical.transform.LexConstants.EOF;

/**
 * Created by gaojian on 2019/1/28.
 */
public class DFATransformer {


    public static DtranCell nfa2Dfa(Cell nfa){
        nfa.edgeSet = Arrays.copyOf(nfa.edgeSet, nfa.edgeCount);

        // 使用子集构造法构造DFA
        int stateNum = 0;
        // 起始节点
        DtranCell dtranCell = new DtranCell();
        DtranState startDtranState = new DtranState(String.valueOf((char)(stateNum + 65)));
        stateNum++;
        startDtranState.addState(epsilonClosure(nfa.startState, nfa.edgeSet));
        // 接受节点
        DtranState endDtranState = null;

        // 存储生成的DFA状态
        Map<String, DtranState> allDtranStateSet = new HashMap<>();
        allDtranStateSet.put(startDtranState.toString(), startDtranState);
        // 存储需要遍历的DFA状态
        List<DtranState> dtranStateList = new ArrayList<>();
        dtranStateList.add(startDtranState);
        // 存储已遍历的DFA状态
        Set<String> dtranStateTag = new HashSet<String>();

        // TODO 确定输入符
        char[] inputs = new char[]{'a', 'b'};
        // TODO 改成使用栈来遍历 DFA 状态--深度遍历，现在是尾递归--广度遍历
        for(int i=0; i<dtranStateList.size(); i++){
            DtranState preDtranState = dtranStateList.get(i);
            // 判断该状态是否已经处理过了
            if(!dtranStateTag.contains(preDtranState.toString())) {
                for (char input : inputs) {
                    // 在当前输入符下转变的状态，集合为：ε-move(move[A, a])
                    DtranState subDtranState = new DtranState(String.valueOf((char)(stateNum + 65)));
                    stateNum++;
                    for (State state : preDtranState.getStateSet()) {
                        subDtranState.addState(move(state, nfa.edgeSet, input));
                    }

                    // 保存新生成的DFA状态
                    if (allDtranStateSet.get(subDtranState.toString()) == null) {
                        allDtranStateSet.put(subDtranState.toString(), subDtranState);
                    } else {
                        subDtranState = allDtranStateSet.get(subDtranState.toString());
                    }

                    DtranEdge dtranEdge = new DtranEdge();
                    dtranEdge.setStartDtranState(preDtranState);
                    dtranEdge.setEndDtranState(subDtranState);
                    dtranEdge.setTransSymbol(input);

                    subDtranState.getDtranEdgeSet().add(dtranEdge);

                    dtranCell.getEdgeSet().add(dtranEdge);

                    // 将新生成的 DFA 状态加入到遍历列表
                    dtranStateList.add(subDtranState);
                }

                // 添加已处理标记
                dtranStateTag.add(preDtranState.toString());
            }
        }

        // 找出接收状态节点
        for(DtranState dtranState : allDtranStateSet.values()){
            for(State state : dtranState.getStateSet()){
                if(state.stateName == nfa.endState.stateName){
                    endDtranState = dtranState;
                    break;
                }
            }

            if(endDtranState != null){
                break;
            }
        }

        dtranCell.setStartState(startDtranState);
        dtranCell.setEndState(endDtranState);

        displayDfa(dtranCell);

        return dtranCell;
    }

    public static Dcell regExp2Dfa(String regular_expression) {
        regular_expression = regular_expression + EOF;
        // 转换成后缀表达式
        String rnpExpression = LexUtils.RNP(regular_expression);

        System.out.println("----------------------------DFA-------------------------------");

        // 先生成语法分析树
        LexNode root = buildLexNode(rnpExpression);

        // 生成 DFA
        Dcell dcell = buildDFA(root);

        System.out.println("----------------------------minimizeDFA-------------------------------");

        // 最小化 DFA
        Dcell minDFA = minimizeDFA(dcell);

        return minDFA;

    }

    /**
     * 最小化DFA
     * @param originDcell
     * @return
     */
    public static Dcell minimizeDFA(Dcell originDcell){
        Set<Dedge> edges = originDcell.getEdgeSet();
        Set<Character> tranChars = originDcell.getTranChar();
        Set<Dstate> states = originDcell.getStates();
        Map<String, Dstate> stateMap = originDcell.getStateMap();

        // 设置初始分组：接收状态组、非接收状态组
        Set<Dstate> unAccStates = new HashSet<>();
        unAccStates.addAll(states);
        unAccStates.remove(originDcell.getEndState());
        Set<Dstate> accStates = new HashSet<>();
        accStates.add(originDcell.getEndState());

        // 记录分组情况
        List<Set<Dstate>> groups = new ArrayList<>();
        groups.add(unAccStates);

        // 判断是否被拆分过
        boolean isSplit = false;
        while(true){
            isSplit = false;

            // 遍历原来的分组，判断是否可以被拆分（只拆分非接受状态组）
            for(Set<Dstate> group : groups){
                // 记录新的分组
                List<Set<Dstate>> newGroups = new ArrayList<>();
                newGroups.addAll(groups);

                for(Character tranChar : tranChars){
                    // 记录同一分组内在不同输入符下的转换集合，根据转换的不同情况进行区分
                    Map<String, Set<Dstate>> newGroupMap = new HashMap<>();
                    for(Dstate state : group){
                        // 判断是否存在对应输入符的转换
                        boolean tranAble = false;

                        Set<Dedge> dtranEdgeSet = state.getDtranEdgeSet();
                        for(Dedge dedge : dtranEdgeSet){
                            // 记录状态在不同输入符下的转换
                            String tranTag = dedge.getEndState().getTag();
                            if(tranChar.equals(dedge.getTransSymbol())){
                                tranAble = true;
                                if(newGroupMap.get(tranTag) == null){
                                    Set<Dstate> newGroup = new HashSet<>();
                                    newGroup.add(state);
                                    newGroupMap.put(tranTag, newGroup);
                                }else{
                                    newGroupMap.get(tranTag).add(state);
                                }
                            }
                        }

                        // 如果没有转换则记录在不能转换分组，TODO 注意key是特殊字符
                        if(!tranAble){
                            if(newGroupMap.get(LexConstants.TRAN_UNABLE) == null){
                                Set<Dstate> newGroup = new HashSet<>();
                                newGroup.add(state);
                                newGroupMap.put(LexConstants.TRAN_UNABLE, newGroup);
                            }else{
                                newGroupMap.get(tranChar).add(state);
                            }
                        }

                    }

                    // 判断分组是否被拆分
                    if(newGroupMap.keySet().size() > 1){
                        // 先清除被拆分的分组
                        newGroups.remove(group);
                        // 加入拆分后的分组，尾递归
                        for(Set<Dstate> newGroup : newGroupMap.values()){
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

        // 加入接收状态组
        groups.add(accStates);

        // 记录新旧节点的映射关系，key：originState.getTag()，value：newState
        Map<String, Dstate> tranMinMap = new HashMap<>();
        // 记录原节点的转换关系, key：originState.getTag()，二级key：tranChar，value：originEndState
        Map<String, Map<Character, Dstate>> originTranMap = new HashMap<>();

        // 根据分组构造新的 DFA 节点
        Set<Dstate> newStates = new HashSet<>();
        Dstate newStartState = null;
        Dstate newEndState = null;
        for(Set<Dstate> group : groups){
            Dstate newState = new Dstate();
            for(Dstate originState : group){
                if(newState.getStateName() == null){
                    newState.setStateName("'" + originState.getStateName() + "'");
                }else{
                    newState.setStateName(newState.getStateName() + "_" + "'" + originState.getStateName() + "'");
                }

                newState.addState(originState.getStateSet());
                newState.getStateNames().addAll(originState.getStateNames());
                newState.getStatePos().addAll(originState.getStatePos());

                // 判断是否是起始节点
                if(originDcell.getStartState().getTag().equals(originState.getTag())){
                    newStartState = newState;
                }
                // 判断是否是接收节点
                if(originDcell.getEndState().getTag().equals(originState.getTag())){
                    newEndState = newState;
                }

                // 记录原节点的转换关系
                for(Dedge dedge : originState.getDtranEdgeSet()){
                    // 记录原节点的转换关系
                    Dstate originEndState = dedge.getEndState();
                    if(originTranMap.get(originState.getTag()) == null){
                        Map<Character, Dstate> tranMap = new HashMap<>();
                        tranMap.put(dedge.getTransSymbol(), originEndState);
                        originTranMap.put(originState.getTag(), tranMap);
                    }else{
                        originTranMap.get(originState.getTag()).put(dedge.getTransSymbol(), originEndState);
                    }
                }

                // 记录新旧节点的映射关系
                tranMinMap.put(originState.getTag(), newState);
            }

            newStates.add(newState);
        }

        // 生成新的转换边
        Set<Dedge> newEdages = new HashSet<>();
        for(String originStateTag : tranMinMap.keySet()){
            Dstate newState = tranMinMap.get(originStateTag);
            Map<Character, Dstate> originTran = originTranMap.get(originStateTag);
            for(Character tranChar : originTran.keySet()){
                Dstate originTranState = originTran.get(tranChar);
                Dstate newTranState = tranMinMap.get(originTranState.getTag());
                Dedge newEdge = new Dedge(newState, newTranState, tranChar);

                // 记录新的转换边
                newEdages.add(newEdge);
                newState.getDtranEdgeSet().add(newEdge);
            }
        }

        // 构造新的Cell
        Dcell minCell = new Dcell();
        minCell.setStartState(newStartState);
        minCell.setEndState(newEndState);
        minCell.getEdgeSet().addAll(newEdages);

        // 输出 DFA
        displayDfa(minCell);

        return minCell;
    }

    /**
     * 通过语法分析树构造DFA
     * @param root
     * @return
     */
    public static Dcell buildDFA(LexNode root){
        int stateNum = 0;

        Map<Integer, LexNode> nodeMap = new HashMap<>();
        getLeafLexNodeMap(root, nodeMap);

        // 记录转换符
        Set<Character> tranChars = new HashSet<>();
        for(LexNode node : nodeMap.values()){
            if(node.getElement() != LexConstants.EOF) {
                tranChars.add(node.getElement());
            }
        }

        // 起始节点
        Dstate startState = new Dstate(String.valueOf((char)(stateNum + 65)));
        stateNum++;
        Set<Integer> rootFirstPos = root.getFirstPos();
        for(Integer pos : rootFirstPos){
            startState.addState(nodeMap.get(pos));
        }

        // 已生成的状态
        Map<String, Dstate> allDstateMap = new HashMap<>();
        allDstateMap.put(startState.getTag(), startState);

        // 待处理的状态
        List<Dstate> dstates = new ArrayList<>();
        dstates.add(startState);

        // 标记处理过的状态
        Set<String> stateTags = new HashSet<>();

        // 记录所有转换边
        Set<Dedge> dedges = new HashSet<>();

        for(int i=0; i<dstates.size(); i++){
            Dstate preState = dstates.get(i);
            if(!stateTags.contains(preState.getTag())){
                for(Character tranChar : tranChars){
                    Dstate subState = new Dstate(String.valueOf((char)(stateNum + 65)));
                    stateNum++;

                    for(LexNode node : preState.getStateSet()){
                        // 只需要添加转换符为tranChar的位置（当前位置符即转换符）的后继节点
                        if(tranChar.equals(node.getElement())) {
                            Set<Integer> followPos = node.getFollowPos();
                            for (Integer pos : followPos) {
                                subState.addState(nodeMap.get(pos));
                            }
                        }
                    }

                    if(allDstateMap.get(subState.getTag()) != null){
                        subState = allDstateMap.get(subState.getTag());
                    }else{
                        allDstateMap.put(subState.getTag(), subState);
                    }

                    Dedge edge = new Dedge(preState, subState, tranChar);
                    preState.getDtranEdgeSet().add(edge);
                    dstates.add(subState);
                    dedges.add(edge);
                }

                // 标记已处理
                stateTags.add(preState.getTag());
            }
        }

        // 找出接收状态节点
        Dstate endState = null;
        for(Dstate dtranState : allDstateMap.values()){
            for(LexNode state : dtranState.getStateSet()){
                if(state.getElement() == LexConstants.EOF){
                    endState = dtranState;
                    break;
                }
            }

            if(endState != null){
                break;
            }
        }

        Dcell dcell = new Dcell();
        dcell.setStartState(startState);
        dcell.setEndState(endState);
        dcell.getEdgeSet().addAll(dedges);

        dcell.getStates().addAll(allDstateMap.values());
        dcell.getTranChar().addAll(tranChars);
        dcell.getStateMap().putAll(allDstateMap);

        // 输出 DFA
        displayDfa(dcell);

        return dcell;
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

    /**
     * 计算NFA的ε-closure集合
     * @param startState
     * @param edgeSet
     */
    public static Set<State> epsilonClosure(State startState, Edge[] edgeSet){
        Set<State> stateSet = new HashSet<>();
        stateSet.add(startState);

        for(int i=0; i<edgeSet.length; i++){
            Edge edge = edgeSet[i];
            if(edge.startState.stateName == startState.stateName && LexConstants.EPSILON == edge.transSymbol){
                stateSet.add(edge.endState);

                stateSet.addAll(epsilonClosure(edge.endState, edgeSet));
            }
        }

        return stateSet;
    }

    public static Set<State> move(State startState, Edge[] edgeSet, char tranChar){
        Set<State> stateSet = new HashSet<>();

        for(int i=0; i<edgeSet.length; i++){
            Edge edge = edgeSet[i];
            if(edge.startState.stateName == startState.stateName && tranChar== edge.transSymbol){
                stateSet.add(edge.endState);
                stateSet.addAll(epsilonClosure(edge.endState, edgeSet));
            }
        }

        return stateSet;
    }

    // 显示
    static void displayDfa(DtranCell dtranCell) {

        System.out.println("DFA 的边数：" + dtranCell.getEdgeSet().size());
        System.out.println("DFA 的起始状态：" + dtranCell.getStartState().getStateName());
        System.out.println("DFA 的结束状态：" + dtranCell.getEndState().getStateName());

        int i=0;
        for(DtranEdge edge : dtranCell.getEdgeSet()){
            System.out.println("第 " + i + 1 + " 条边的起始状态：" + edge.getStartDtranState().getStateName() +
                    "，结束状态：" + edge.getEndDtranState().getStateName() +
                    "，转换符：" + edge.getTransSymbol());
            i++;
        }

        System.out.println("结束");
    }

    // 显示
    static void displayDfa(Dcell dtranCell) {

        System.out.println("DFA 的边数：" + dtranCell.getEdgeSet().size());
        System.out.println("DFA 的起始状态：" + dtranCell.getStartState().toString());
        System.out.println("DFA 的结束状态：" + dtranCell.getEndState().toString());

        int i=0;
        for(Dedge edge : dtranCell.getEdgeSet()){
            System.out.println("第 " + i + 1 + " 条边的起始状态：" + edge.getStartState().toString() +
                    "，结束状态：" + edge.getEndState().toString() +
                    "，转换符：" + edge.getTransSymbol());
            i++;
        }

        System.out.println("结束");
    }

    static class LexNode{

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

    static enum LexNodeType{
        EPSILON, // ε子节点
        LEAF,   // 非空子节点
        OR,     // or 节点
        CAT,    // | 节点
        START   // * 节点
    }

    /**
     * 优化 DFA 状态数据结构
     */
    static class Dstate{

        private String stateName;

        private Set<LexNode> stateSet = new HashSet<>();
        private Set<Dedge> dtranEdgeSet = new HashSet<>();

        private Set<Character> stateNames = new TreeSet<>(new Comparator<Character>() {
            @Override
            public int compare(Character o1, Character o2) {
                return o2.compareTo(o1);//降序排列
            }
        });
        private Set<Integer> statePos = new TreeSet<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);//降序排列
            }
        });

        public Dstate(){

        }

        public Dstate(String stateName){
            this.stateName = stateName;
        }

        public Dstate(Dstate originDstate){
            this.stateName = "'" + originDstate.getStateName() + "'";
            this.stateSet = originDstate.getStateSet();
            this.dtranEdgeSet = originDstate.getDtranEdgeSet();

            this.stateNames = originDstate.getStateNames();
            this.statePos = originDstate.getStatePos();
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

        @Override
        public boolean equals(Object other){
            if(other == null){
                return false;
            }

            Dstate otherState = (Dstate) other;
            if(this.stateSet.size() != otherState.stateSet.size()){
                return false;
            }

            for(LexNode state : otherState.stateSet){
                if(!statePos.contains(state.getPos())){
                    return false;
                }
            }

            return true;

        }

        @Override
        public String toString(){
//            return JSON.toJSONString(stateNames);
            return stateName + ":" + JSON.toJSONString(statePos);
        }

        public String getTag(){
            return JSON.toJSONString(statePos);
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public Set<LexNode> getStateSet() {
            return stateSet;
        }

        public void setStateSet(Set<LexNode> stateSet) {
            this.stateSet = stateSet;
        }

        public Set<Dedge> getDtranEdgeSet() {
            return dtranEdgeSet;
        }

        public void setDtranEdgeSet(Set<Dedge> dtranEdgeSet) {
            this.dtranEdgeSet = dtranEdgeSet;
        }

        public Set<Character> getStateNames() {
            return stateNames;
        }

        public void setStateNames(Set<Character> stateNames) {
            this.stateNames = stateNames;
        }

        public Set<Integer> getStatePos() {
            return statePos;
        }

        public void setStatePos(Set<Integer> statePos) {
            this.statePos = statePos;
        }
    }

    static class Dedge{
        private Dstate startState;
        private Dstate endState;
        private char transSymbol;

        public Dedge(Dstate startState, Dstate endState, char transSymbol) {
            this.startState = startState;
            this.endState = endState;
            this.transSymbol = transSymbol;
        }

        public Dstate getStartState() {
            return startState;
        }

        public void setStartState(Dstate startState) {
            this.startState = startState;
        }

        public Dstate getEndState() {
            return endState;
        }

        public void setEndState(Dstate endState) {
            this.endState = endState;
        }

        public char getTransSymbol() {
            return transSymbol;
        }

        public void setTransSymbol(char transSymbol) {
            this.transSymbol = transSymbol;
        }

        @Override
        public boolean equals(Object other){
            if(other == null){
                return false;
            }

            Dedge otherEdge = (Dedge) other;

            if(this.startState.equals(otherEdge.startState) && this.endState.equals(otherEdge.endState) && this.transSymbol == otherEdge.transSymbol){
                return true;
            }

            return false;

        }
    }

    static class Dcell{
        private Set<Dedge> edgeSet = new HashSet<Dedge>();
        private Dstate startState;
        private Dstate endState;

        private Set<Dstate> states = new HashSet<>();
        private Set<Character> tranChar = new HashSet<>();
        private Map<String, Dstate> stateMap = new HashMap<>();

        public Set<Dedge> getEdgeSet() {
            return edgeSet;
        }

        public void setEdgeSet(Set<Dedge> edgeSet) {
            this.edgeSet = edgeSet;
        }

        public Dstate getStartState() {
            return startState;
        }

        public void setStartState(Dstate startState) {
            this.startState = startState;
        }

        public Dstate getEndState() {
            return endState;
        }

        public void setEndState(Dstate endState) {
            this.endState = endState;
        }

        public Set<Dstate> getStates() {
            return states;
        }

        public void setStates(Set<Dstate> states) {
            this.states = states;
        }

        public Set<Character> getTranChar() {
            return tranChar;
        }

        public void setTranChar(Set<Character> tranChar) {
            this.tranChar = tranChar;
        }

        public Map<String, Dstate> getStateMap() {
            return stateMap;
        }

        public void setStateMap(Map<String, Dstate> stateMap) {
            this.stateMap = stateMap;
        }
    }

}