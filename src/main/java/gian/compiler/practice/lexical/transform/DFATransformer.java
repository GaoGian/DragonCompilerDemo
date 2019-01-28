package gian.compiler.practice.lexical.transform;

import com.alibaba.fastjson.JSON;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static gian.compiler.practice.lexical.transform.LexConstants.EOF;

/**
 * Created by gaojian on 2019/1/28.
 */
public class DFATransformer {

    public static final Pattern pattern = Pattern.compile("");

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
        Map<String, DtranState> allDtranStateSet = new HashMap<String, DtranState>();
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

    public static DtranCell regExp2Dfa(String regular_expression) {
        regular_expression = regular_expression + EOF;
        // 转换成后缀表达式
        String rnpExpression = LexUtils.RNP(regular_expression);
        // 先生成语法分析树
        LexNode root = buildLexNode(rnpExpression);
        // 计算各位置的nullAble、firstPos、lastPos

        // 计算各位置的followPos

        return null;
    }

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
        String expression = getChildNodeExpression(node);
        Matcher matcher = pattern.matcher(expression);
        return matcher.matches();
    }

    public static String getChildNodeExpression(LexNode node){
        String expression = "";
        if(node.getLeft() != null){
            expression = getChildNodeExpression(node.getLeft());
        }
        expression += node.getLeft().element;
        if(node.getRight() != null){
            expression = getChildNodeExpression(node.getRight());
        }
        return expression;
    }

    // 计算当前节点为根的首字符集合
    public void firstPos(){

    }

    // 计算当前节点为根的最后字符集合
    public void lastPos(){

    }

    // 计算当前节点位置对应的正则表达式
    public void followPos(){

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
                return pos + ": " + JSON.toJSONString(firstPos) + " " + element + " " + JSON.toJSONString(lastPos);
            }else{
                return JSON.toJSONString(firstPos) + " " + element + " " + JSON.toJSONString(lastPos);
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

}