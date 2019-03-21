package lex.test;

import gian.compiler.practice.lexical.transform.LexConstants;
import gian.compiler.practice.lexical.transform.MyStack;
import gian.compiler.practice.lexical.transform.regex.LexAutomatonTransformer;
import gian.compiler.practice.syntactic.SyntacticLLParser;
import gian.compiler.practice.syntactic.element.Item;
import gian.compiler.practice.syntactic.element.ItemCollection;
import gian.compiler.practice.syntactic.element.SyntaxProduct;
import gian.compiler.practice.syntactic.element.SyntaxSymbol;

import java.util.*;

/**
 * Created by gaojian on 2019/1/28.
 */
public class LexUtils {

    /**
     * 将正则表达式进行逆波兰转化（转后缀）
     * Reverse Polish Notation
     * @param regular_expression
     * @return
     */
    public static String RNP(String regular_expression){

        // 添加“+”符号，方便转换成后缀表达式
        regular_expression = LexUtils.add_join_symbol(regular_expression);
        // 中缀转后缀
        regular_expression = LexUtils.postfix(regular_expression);

        return regular_expression;
    }

    public static String parseRegex(String regular_expression){
        // TODO


        return null;
    }

    // 添加交操作符“+”，便于中缀转后缀表达式，例如 abb->a+b+b
    public static String add_join_symbol(String addStr) {
        int length = addStr.length();
        int return_string_length = 0;
        //转换后的表达式长度最多是原来的两倍
        char[] return_string = new char[2 * length];

        Character first = null;
        Character second = null;
        for (int i = 0; i < length - 1; i++) {
            first = addStr.charAt(i);
            second = addStr.charAt(i + 1);

            // TODO 需要优化
            return_string[return_string_length++] = first;
            // 若第二个是字母、第一个不是 '(' '|' 都要添加
            if (first != '(' && first != '|' && isLetter(second)) {
                return_string[return_string_length++] = '+';
            }
            // 若第二个是'(', 第一个不是'|'、'(' 也要添加
            else if (second == '(' && first != '|' && first != '(') {
                return_string[return_string_length++] = '+';
            }

        }

        // 将最后一个字符写入
        return_string[return_string_length++] = second;
        return_string[return_string_length] = '\0';

        return_string = Arrays.copyOf(return_string, return_string_length);

        String dealStr = new String(return_string);
        System.out.println("加'+'后的表达式：" + dealStr);
        return dealStr;

    }

    // 中缀转后缀
    // TODO 解析出语法分析树
    public static String postfix(String expression) {
        //设定e的最后一个符号式“ε”，而其“ε”一开始先放在栈s的栈底
        expression = expression + LexConstants.EPSILON;

        MyStack<Character> stack = new MyStack<Character>();
        char ch = LexConstants.EPSILON, ch1, op;
        stack.push(ch);
        //读一个字符
        String out_string = "";
        int read_location = 0;
        ch = expression.charAt(read_location++);
        while (!stack.empty()) {
            if (isLetter(ch)) {
                out_string = out_string + ch;
                //cout<<ch;
                ch = expression.charAt(read_location++);
            } else {
                //cout<<"输出操作符："<<ch<<endl;
                ch1 = stack.top();
                if (isp(ch1) < icp(ch)) {
                    stack.push(ch);
                    //cout<<"压栈"<<ch<<"  读取下一个"<<endl;
                    ch = expression.charAt(read_location++);
                } else if (isp(ch1) > icp(ch)) {
                    op = stack.top();
                    stack.pop();
                    //cout<<"退栈"<<op<<" 添加到输出字符串"<<endl;
                    out_string = out_string + op;
                    //cout<<op;
                } else {
                    op = stack.top();
                    stack.pop();
                    //cout<<"退栈"<<op<<"  但不添加到输入字符串"<<endl;

                    if (op == '(') {
                        ch = expression.charAt(read_location++);
                    }
                }
            }
        }
        //cout<<endl;
//        cout<<"后缀表达式："<<out_string<<endl;
        System.out.println("转化后的后缀表达式：" + out_string);
        return out_string;
    }

    // 检查输入的字符是否合适 () * | a~z A~Z
    boolean checkCharacter(String checkStr) {
        int length = checkStr.length();
        for (int i = 0; i < length; i++) {
            char check = checkStr.charAt(i);
            if (isLetter(check)) {
//                System.out.println("");
            } else if (check == '(' || check == ')' || check == '*' || check == '|') {
//                System.out.println("");
            } else {
                System.out.println("输入字符不合法");
                throw new RuntimeException("输入字符不合法");
            }
        }
        return true;
    }

    public static boolean isLetter(char check) {
        if (check >= 'a' && check <= 'z' || check >= 'A' && check <= 'Z' || check == LexConstants.EOF) {
            return true;
        }
        return false;
    }

    /*
     优先级表：
          ε	(	*	|	+	)
     isp  0	    1	7	5	3	8
     icp  0	    8	6	4	2	1
    */
    // 优先级 in stack priority
    public static int isp(char c) {

        switch (c) {
            case LexConstants.EOF:
                return -1;
            case LexConstants.EPSILON:
                return 0;
            case '(':
                return 1;
            case '*':
                return 7;
            case '|':
                return 5;
            case '+':
                return 3;
            case ')':
                return 8;
        }
        //若走到这一步，说明出错了
        System.out.println("isp优先级匹配错误");
        throw new RuntimeException("优先级匹配错误");
    }

    // 优先级 in coming priority
    public static int icp(char c) {
        switch (c) {
            case LexConstants.EOF:
                return -1;
            case LexConstants.EPSILON:
                return 0;
            case '(':
                return 8;
            case '*':
                return 6;
            case '|':
                return 4;
            case '+':
                return 2;
            case ')':
                return 1;
        }
        //若走到这一步，说明出错了
        System.out.println("icp优先级匹配错误");
        throw new RuntimeException("icp优先级匹配错误");
    }

//--------------------------------------------------------------------------------------------------//
//-------------------------------- 打印树结构 ------------------------------------------------------//

    public static void print(LexAutomatonTransformer.Node root) {
        // 找到左边的最大偏移量
        int maxLeftOffset = findMaxOffset(root, 0, true);
        int maxRightOffset = findMaxOffset(root, 0, false);
        int offset = Math.max(maxLeftOffset, maxRightOffset);
        // 计算最大偏移量
        Map<Integer, LexPrintLine> lineMap = new HashMap();
        calculateLines(root, offset, lineMap, 0, true);
        Iterator<Integer> lineNumbers = lineMap.keySet().iterator();
        int maxLine = 0;
        while (lineNumbers.hasNext()) {
            int lineNumber = lineNumbers.next();
            if (lineNumber > maxLine) {
                maxLine = lineNumber;
            }
        }
        for (int i = 0; i <= maxLine; i++) {
            LexPrintLine line = lineMap.get(i);
            if (line != null) {
                System.out.println(line.getLineString());
            }
        }

    }

    // TODO 优化行距输出逻辑，解决覆盖问题
    private static void calculateLines(LexAutomatonTransformer.Node parent, int offset,
                                       Map<Integer, LexPrintLine> lineMap, int level, boolean right) {
        if (parent == null) {
            return;
        }
        int nameoffset = parent.toString().length() / 2;
        LexPrintLine line = lineMap.get(level);
        if (line == null) {
            line = new LexPrintLine();
            lineMap.put(level, line);
        }
        line.putString(right ? offset : (offset - nameoffset), parent.toString());
        // 判断有没有下一级
        if (parent.getLeft() == null && parent.getRight() == null) {
            return;
        }
        // 如果有，添加分割线即/\
        LexPrintLine separateLine = lineMap.get(level + 1);
        if (separateLine == null) {
            separateLine = new LexPrintLine();
            lineMap.put(level + 1, separateLine);
        }
        if (parent.getLeft() != null) {
            separateLine.putString(offset - 1, "/");
            calculateLines(parent.getLeft(), offset - nameoffset - 1, lineMap, level + 2, false);
        }
        if (parent.getRight() != null) {
            separateLine.putString(offset + nameoffset + 1, "\\");
            calculateLines(parent.getRight(), offset + nameoffset + 1, lineMap, level + 2, true);
        }

    }

    /**
     * 需要打印的某一行
     *
     * @author zhuguohui
     *
     */
    private static class PrintLine {
        /**
         * 记录了offset和String的map
         */
        Map<Integer, String> printItemsMap = new HashMap<>();
        int maxOffset = 0;

        public void putString(int offset, String info) {
            printItemsMap.put(offset, info);
            if (offset > maxOffset) {
                maxOffset = offset;
            }
        }

        public String getLineString() {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i <= maxOffset; i++) {
                String info = printItemsMap.get(i);
                if (info == null) {
                    buffer.append(" ");
                } else {
                    buffer.append(info);
                    i += info.length();
                }
            }
            return buffer.toString();
        }

    }

    private static int findMaxOffset(LexAutomatonTransformer.Node parent, int offset, boolean findLeft) {
        if (parent != null) {
            offset += parent.toString().length();
        }
        if (findLeft && parent.getLeft() != null) {
            offset += 1;
            return findMaxOffset(parent.getLeft(), offset, findLeft);
        }
        if (!findLeft && parent.getRight() != null) {
            return findMaxOffset(parent.getRight(), offset, findLeft);
        }
        return offset;
    }

    /**
     * 需要打印的某一行
     *
     * @author zhuguohui
     *
     */
    static class LexPrintLine {
        /**
         * 记录了offset和String的map
         */
        Map<Integer, String> printItemsMap = new HashMap<>();
        int maxOffset = 0;

        public void putString(int offset, String info) {
            printItemsMap.put(offset, info);
            if (offset > maxOffset) {
                maxOffset = offset;
            }
        }

        public String getLineString() {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i <= maxOffset; i++) {
                String info = printItemsMap.get(i);
                if (info == null) {
                    buffer.append(" ");
                } else {
                    buffer.append(info);
                    i += info.length();
                }
            }
            return buffer.toString();
        }

    }

    public static class PatternNode{
        private String pattern;
        private PatternNode leftPattern;
        private PatternNode rightPattern;

        public PatternNode(){

        }

        public PatternNode(String pattern, PatternNode leftPattern, PatternNode rightPattern) {
            this.pattern = pattern;
            this.leftPattern = leftPattern;
            this.rightPattern = rightPattern;
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public PatternNode getLeftPattern() {
            return leftPattern;
        }

        public void setLeftPattern(PatternNode leftPattern) {
            this.leftPattern = leftPattern;
        }

        public PatternNode getRightPattern() {
            return rightPattern;
        }

        public void setRightPattern(PatternNode rightPattern) {
            this.rightPattern = rightPattern;
        }
    }


    public static void displayLexCell(LexAutomatonTransformer.LexCell lexCell){

        System.out.println("DFA 的边数：" + lexCell.getEdgeSet().size());
        System.out.println("DFA 的起始状态：" + lexCell.getStartState().getStateName());
        System.out.println("DFA 的结束状态：" + lexCell.getEndState().getStateName());

        int i=0;
        for(LexAutomatonTransformer.LexEdge edge : lexCell.getEdgeSet()){
            System.out.println("第 " + i + 1 + " 条边的起始状态：" + edge.getStartState().getStateName() +
                    "，结束状态：" + edge.getEndState().getStateName() + "，转换符：" + edge.getTranPattern().getMeta());

            i++;
        }

        System.out.println("结束");

    }

    // 使用ECharts路径图显示
    // https://echarts.baidu.com/examples/editor.html?c=graph-simple
    public static void outputEchart(LexAutomatonTransformer.LexCell lexCell){

        // 广度遍历
        int x = 0, y =0;
        Set<LexAutomatonTransformer.LexState> states = new HashSet<>();
        Set<LexUtils.EcharDemoPoint> pointList = new HashSet<>();
        LexAutomatonTransformer.LexState startState = lexCell.getStartState();

        Set<LexAutomatonTransformer.LexState> endStateSet = new HashSet<>();
        if(lexCell.getEndState() != null) {
            // NFA
            endStateSet.add(lexCell.getEndState());
        }else{
            // DFA
            endStateSet.addAll(((LexAutomatonTransformer.LexDFACell) lexCell).getAccStateSet());
        }

        states.add(startState);
        pointList.add(new LexUtils.EcharDemoPoint(startState.toString(), x, y));

        Set<LexAutomatonTransformer.LexEdge> recordEdges = new HashSet<>();
        Collection<LexAutomatonTransformer.LexEdge> edges = startState.getEdgeMap().values();
        while(edges.size()>0) {
            y = 0;
            x += 400;
            Set<LexAutomatonTransformer.LexEdge> newEdges = new HashSet<>();
            for (LexAutomatonTransformer.LexEdge edge : edges) {
                LexAutomatonTransformer.LexState targetState = edge.getEndState();
                if (!endStateSet.contains(targetState) && !states.contains(targetState)) {
                    states.add(targetState);
                    pointList.add(new LexUtils.EcharDemoPoint(targetState.toString(), x, y));
                    y -= 300;
                }

                if(!recordEdges.contains(edge)){
                    recordEdges.add(edge);
                    newEdges.addAll(targetState.getEdgeMap().values());
                }
            }
            edges = newEdges;
        }
        states.addAll(endStateSet);
        x += 400;
        for(LexAutomatonTransformer.LexState endState : endStateSet) {
            pointList.add(new LexUtils.EcharDemoPoint(endState.toString(), x, y));
            y -= 300;
        }

        List<LexUtils.EchartDemoEdge> echarEdges = new ArrayList<>();
        for(LexAutomatonTransformer.LexEdge edge : lexCell.getEdgeSet()){
            echarEdges.add(new LexUtils.EchartDemoEdge(edge.getStartState().toString(), edge.getEndState().toString(), edge.getTranPattern().getMeta()));
        }

        // 输出
        for(LexUtils.EcharDemoPoint point : pointList){
            System.out.println(point.toString());
        }
        System.out.println("--------------------------------------------------------------------");
        for(LexUtils.EchartDemoEdge echartEdge : echarEdges){
            System.out.println(echartEdge.toString());
        }

    }

    // 使用ECharts路径图显示
    // https://echarts.baidu.com/examples/editor.html?c=graph-simple
    public static void outputSyntaxEchart(ItemCollection startItemCollection){
        outputSyntaxEchart(startItemCollection, 300, 300);
    }

    public static void outputSyntaxEchart(ItemCollection startItemCollection, int xIncre, int yIncre){
        outputSyntaxEchart(startItemCollection, xIncre, yIncre, false);
    }

    public static void outputSyntaxEchart(ItemCollection startItemCollection, int xIncre, int yIncre, boolean showOtherItem){

        // 广度遍历
        Set<LexUtils.EcharDemoPoint> pointSet = new HashSet<>();
        Set<LexUtils.EchartDemoEdge> echarEdgeSet = new HashSet<>();

        int x = 0;
        int y = 0;
        pointSet.add(new LexUtils.EcharDemoPoint(getCoreItemTag(startItemCollection, showOtherItem), x, y));

        List<Map<String, Object>> moveItemCollectionList = new ArrayList<>();
        for(SyntaxSymbol moveSymbol : startItemCollection.getMoveItemCollectionMap().keySet()) {
            ItemCollection moveItemCollection = startItemCollection.getMoveItemCollectionMap().get(moveSymbol);
            Map<String, Object> map = new HashMap<>();
            map.put("preIC", startItemCollection);
            map.put("symbol", moveSymbol);
            map.put("subIC", moveItemCollection);
            moveItemCollectionList.add(map);
        }
        Set<ItemCollection> allItemCollection = new HashSet<>();
        allItemCollection.add(startItemCollection);
        allItemCollection.addAll(startItemCollection.getMoveItemCollectionMap().values());
        int index = 0;
        boolean hasNew = true;
        while(hasNew) {
            hasNew = false;
            x += xIncre;
            y = 0;
            int end = moveItemCollectionList.size();
            for (; index<end; index++) {
                Map<String, Object> map = moveItemCollectionList.get(index);
                ItemCollection preItemCollection = (ItemCollection) map.get("preIC");
                SyntaxSymbol moveSymbol = (SyntaxSymbol) map.get("symbol");
                ItemCollection moveItemCollection = (ItemCollection) map.get("subIC");

                LexUtils.EcharDemoPoint newPoiont = new LexUtils.EcharDemoPoint(getCoreItemTag(moveItemCollection, showOtherItem), x, y);
                if (!pointSet.contains(newPoiont)) {
                    pointSet.add(newPoiont);
                }
                LexUtils.EchartDemoEdge newEdge = new LexUtils.EchartDemoEdge(getCoreItemTag(preItemCollection, showOtherItem), getCoreItemTag(moveItemCollection, showOtherItem), moveSymbol.getSymbol());
                if (!echarEdgeSet.contains(newEdge)) {
                    echarEdgeSet.add(newEdge);
                }

                for(SyntaxSymbol subMoveSymbol : moveItemCollection.getMoveItemCollectionMap().keySet()){
                    ItemCollection subItemCollection = moveItemCollection.getMoveItemCollectionMap().get(subMoveSymbol);
                    if(!allItemCollection.contains(subItemCollection)) {
                        Map<String, Object> subMap = new HashMap<>();
                        subMap.put("preIC", moveItemCollection);
                        subMap.put("symbol", subMoveSymbol);
                        subMap.put("subIC", subItemCollection);
                        moveItemCollectionList.add(subMap);

                        allItemCollection.add(subItemCollection);
                        hasNew = true;
                    }

                    LexUtils.EchartDemoEdge subNewEdge = new LexUtils.EchartDemoEdge(getCoreItemTag(moveItemCollection, showOtherItem), getCoreItemTag(subItemCollection, showOtherItem), subMoveSymbol.getSymbol());
                    if (!echarEdgeSet.contains(subNewEdge)) {
                        echarEdgeSet.add(subNewEdge);
                    }
                }

                y -= yIncre;
            }

        }


        // 输出
        System.out.println("----------------------------- pointList size: " + pointSet.size() + " -------------------------------");
        for(LexUtils.EcharDemoPoint point : pointSet){
            System.out.println(point.toString());
        }
        System.out.println("----------------------------- edgeList size: " + echarEdgeSet.size() + " --------------------------------");
        for(LexUtils.EchartDemoEdge echartEdge : echarEdgeSet){
            System.out.println(echartEdge.toString());
        }
        System.out.println("---------------------------------------------------------------------");

    }

    public static String getCoreItemTag(ItemCollection startItemCollection, boolean showOtherItem){
        List<Item> tempItemList = new ArrayList<>();
        if(!showOtherItem) {
            for (Item tempItem : startItemCollection.getItemList()) {
                if (tempItem.getIndex() > 0) {
                    tempItemList.add(tempItem);
                }
            }
        }else{
            tempItemList.addAll(startItemCollection.getItemList());
        }

        if(!(startItemCollection instanceof ItemCollection.AcceptItemCollection)) {
            if (tempItemList.size() == 0) {
                // 说明是初始项集
                tempItemList.add(startItemCollection.getItemList().get(0));
            }
        }

        if(!(startItemCollection instanceof ItemCollection.AcceptItemCollection)) {
            return startItemCollection.getNumber() + ":" + tempItemList.toString();
        }else{
            return startItemCollection.toString();
        }
    }

    // 输出树结构
    public static void outputUniversalTreeEchart(UniversalTreeNode rootNode){
        outputUniversalTreeEchart(rootNode, 300, 300);
    }

    public static void outputUniversalTreeEchart(UniversalTreeNode rootNode, int xInc, int yInc){

        // 广度遍历
        Set<LexUtils.EcharDemoPoint> pointSet = new HashSet<>();
        Set<LexUtils.EchartDemoEdge> echarEdgeSet = new HashSet<>();

        int x = 0;
        int y = 0;

        List<UniversalTreeNode> currentTreeNodeList = new ArrayList<>();
        currentTreeNodeList.add(rootNode);

        Map<UniversalTreeNode, UniversalTreeNode> preAndSubMap = new HashMap<>();

        Integer preStartIndex = x;
        while(currentTreeNodeList.size() > 0){
            x = preStartIndex;
            preStartIndex = null;
            List<UniversalTreeNode> tempTreeNodeList = new ArrayList<>();
            for(UniversalTreeNode treeNode : currentTreeNodeList){
                x = preAndSubMap.get(treeNode) == null ? x : preAndSubMap.get(treeNode).getxAxis() > x ? preAndSubMap.get(treeNode).getxAxis() : x;
                pointSet.add(new EcharDemoPoint(treeNode.toString(), x, y));
                treeNode.setxAxis(x);
                treeNode.setyAxis(y);

                if(treeNode.showSubTreeNode){
                    List<UniversalTreeNode> subTreeNodeList = treeNode.getSubTreeNode();
                    for (UniversalTreeNode subTreeNode : subTreeNodeList) {
                        echarEdgeSet.add(new EchartDemoEdge(treeNode.toString(), subTreeNode.toString(), ""));

                        tempTreeNodeList.add(subTreeNode);
                        preAndSubMap.put(subTreeNode, treeNode);

                        if(preStartIndex == null){
                            preStartIndex = x;
                        }
                    }
                }

                x += xInc;
            }

            currentTreeNodeList = tempTreeNodeList;

            y += yInc;
        }


        // 输出
        System.out.println("----------------------------- pointList size: " + pointSet.size() + " -------------------------------");
        for(LexUtils.EcharDemoPoint point : pointSet){
            System.out.println(point.toString());
        }
        System.out.println("----------------------------- edgeList size: " + echarEdgeSet.size() + " --------------------------------");
        for(LexUtils.EchartDemoEdge echartEdge : echarEdgeSet){
            System.out.println(echartEdge.toString());
        }
        System.out.println("---------------------------------------------------------------------");
    }

    public static class UniversalTreeNode<T>{
        private T node;
        private UniversalTreeNodeMatch matcher;
        private boolean showSubTreeNode;

        private int xAxis;
        private int yAxis;

        public UniversalTreeNode(){}

        public UniversalTreeNode(T node, UniversalTreeNodeMatch matcher, boolean showSubTreeNode){
            this.node = node;
            this.matcher = matcher;
            this.showSubTreeNode = showSubTreeNode;
        }

        public List<UniversalTreeNode> getSubTreeNode(){
            return this.matcher.getChildTreeNode(this.node);
        }

        public T getNode() {
            return node;
        }

        public void setNode(T node) {
            this.node = node;
        }

        public UniversalTreeNodeMatch<T> getMatcher() {
            return matcher;
        }

        public void setMatcher(UniversalTreeNodeMatch<T> matcher) {
            this.matcher = matcher;
        }

        public int getxAxis() {
            return xAxis;
        }

        public void setxAxis(int xAxis) {
            this.xAxis = xAxis;
        }

        public int getyAxis() {
            return yAxis;
        }

        public void setyAxis(int yAxis) {
            this.yAxis = yAxis;
        }

        @Override
        public String toString(){
            return this.node.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UniversalTreeNode<?> that = (UniversalTreeNode<?>) o;

            return node != null ? node.toString().equals(that.node.toString()) : that.node == null;

        }

        @Override
        public int hashCode() {
            return 0;
        }

        // 由于匹配不同的数据结构
        public static abstract class UniversalTreeNodeMatch<T>{

            public abstract List<UniversalTreeNode> getChildTreeNode(T targetNode);

        }

    }


    public static class EcharDemoPoint{
        private String name;
        private int x;
        private int y;

        public EcharDemoPoint(String name, int x, int y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        @Override
        public String toString(){
            return "{name:'" + this.name + "',x:" + this.x + ",y:" + this.y + "},";
        }

        @Override
        public int hashCode(){
            return this.name.hashCode();
        }

        @Override
        public boolean equals(Object other){
            if(other == null){
                return false;
            }

            if(!this.name.equals(((EcharDemoPoint) other).getName())){
                return false;
            }

            return true;
        }

    }

    public static class EchartDemoEdge{
        private String source;
        private String target;
        private String label;

        public EchartDemoEdge(String source, String target, String label) {
            this.source = source;
            this.target = target;
            this.label = label;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getLabel() {
            return this.label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        @Override
        public String toString(){
            return "{source:'" + this.source + "',target:'" + this.target + "',label:{normal:{show:true,formatter: '" + this.label.replace("\\", "\\\\") + "'}},lineStyle:{normal:{curveness:0.3}}},";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            EchartDemoEdge that = (EchartDemoEdge) o;

            if (!source.equals(that.source)) return false;
            if (!target.equals(that.target)) return false;
            return label.equals(that.label);

        }

        @Override
        public int hashCode() {
            int result = source.hashCode();
            result = 31 * result + target.hashCode();
            result = 31 * result + label.hashCode();
            return result;
        }
    }

    /**
     * 输出LL(1)预测分析表
     */
    public static void outputLL1SyntaxPredict(Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap,
                                              Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap,
                                              Map<SyntaxSymbol, Map<String, Set<SyntaxProduct>>> syntaxPredictMap){

        Set<String> allTerminalSymbol = SyntacticLLParser.getAllTerminalSymbol(syntaxFirstMap, syntaxFollowMap);
        Set<SyntaxSymbol> allNonTerminalSymbol = SyntacticLLParser.getAllNonTerminalSymbol(syntaxFirstMap);

        // 使用bootstrap表格显示, http://www.runoob.com/try/try.php?filename=bootstrap3-table-basic
        StringBuilder str = new StringBuilder();
        str.append("<table class=\"table\">\n");
        str.append("<thead>\n");
        str.append("    <th>非终结符号</th>\n");
        for(String terminalSymbol : allTerminalSymbol){
            if(!terminalSymbol.equals(LexConstants.SYNTAX_EMPTY)){
                str.append("<th>" + terminalSymbol + "</th>\n");
            }
        }
        str.append("</thead>\n");
        str.append("<tbody>\n");
        for(SyntaxSymbol nonTerminalSymbol : allNonTerminalSymbol){
            str.append("<tr>\n");
            str.append("    <th>" + nonTerminalSymbol.getSymbol() + "</th>\n");
            for(String terminalSymbol : allTerminalSymbol){
                if(!terminalSymbol.equals(LexConstants.SYNTAX_EMPTY)) {
                    if(syntaxPredictMap.get(nonTerminalSymbol).get(terminalSymbol) != null) {
                        String productStr = syntaxPredictMap.get(nonTerminalSymbol).get(terminalSymbol).toString();
                        str.append("    <th>" + productStr.toString().substring(1, productStr.length()-1) + "</th>\n");
                    }else{
                        str.append("    <th></th>\n");
                    }
                }
            }
            str.append("</tr>\n");
        }
        str.append("</tbody>\n");
        str.append("</table>\n");

        System.out.println(str.toString());
    }

    /**
     * 输出LR分析表
     * // SLR分析表，一级key：项集，二级key：ACTION|GOTO，三级key：输入符，四级key：动作类型、迁移状态
     */
    public static void outputLRPredictMap(Map<ItemCollection, Map<String, Map<SyntaxSymbol, List<Map<String, Object>>>>> predictSLRMap){

        List<ItemCollection> itemCollectionList = new ArrayList<>(predictSLRMap.keySet());
        itemCollectionList.sort(new Comparator<ItemCollection>() {
            @Override
            public int compare(ItemCollection o1, ItemCollection o2) {
                return o1.getNumber().compareTo(o2.getNumber());
            }
        });

        Set<SyntaxSymbol> actionSymbolSet = getLRPredictSymbol(predictSLRMap, LexConstants.SYNTAX_LR_ACTION);
        Set<SyntaxSymbol> gotoSymbolSet = getLRPredictSymbol(predictSLRMap, LexConstants.SYNTAX_LR_GOTO);
        // 使用bootstrap表格显示, http://www.runoob.com/try/try.php?filename=bootstrap3-table-basic
        StringBuilder str = new StringBuilder();
        str.append("<table class=\"table\">\n");
        str.append("<thead>\n");
        str.append("    <th>状态</th>\n");
        for(SyntaxSymbol actionSymbol : actionSymbolSet){
            if(!actionSymbol.getSymbol().equals(LexConstants.SYNTAX_EMPTY)){
                str.append("<th>" + actionSymbol.getSymbol() + "</th>\n");
            }else{
                str.append("<th><span style='color:red'>" + actionSymbol.getSymbol() + "</span></th>\n");
            }
        }
        for(SyntaxSymbol gotoSymbol : gotoSymbolSet){
            if(!gotoSymbol.getSymbol().equals(LexConstants.SYNTAX_EMPTY)){
                str.append("<th>" + gotoSymbol.getSymbol() + "</th>\n");
            }
        }
        str.append("</thead>\n");

        str.append("<tbody>\n");
        for(ItemCollection itemCollection : itemCollectionList){
            str.append("<tr>\n");
//            str.append("    <td>" + itemCollection.toString() + "</td>\n");
            str.append("    <td>" + itemCollection.getNumber() + "</td>\n");
            for(SyntaxSymbol actionSymbol : actionSymbolSet){
                    if(predictSLRMap.get(itemCollection).get(LexConstants.SYNTAX_LR_ACTION) != null) {
                        if (predictSLRMap.get(itemCollection).get(LexConstants.SYNTAX_LR_ACTION).get(actionSymbol) != null) {
                            Map<String, Object> actionInfo = predictSLRMap.get(itemCollection).get(LexConstants.SYNTAX_LR_ACTION).get(actionSymbol).get(0);
                            str.append("    <td>");
                            if(actionSymbol.getSymbol().equals(LexConstants.SYNTAX_EMPTY)) {
                                str.append("<span style='color:red'>");
                            }

                            if(actionInfo.get(LexConstants.SYNTAX_LR_ACTION_NEXT_ITEMCOLLECTION) instanceof ItemCollection) {
                                str.append(actionInfo.get(LexConstants.SYNTAX_LR_ACTION_TYPE).toString().replace("ACTION_", "").substring(0, 1).toLowerCase() + "_" + ((ItemCollection)actionInfo.get(LexConstants.SYNTAX_LR_ACTION_NEXT_ITEMCOLLECTION)).getNumber());
                            }else{
                                str.append(actionInfo.get(LexConstants.SYNTAX_LR_ACTION_TYPE).toString().replace("ACTION_", "").substring(0, 1).toLowerCase() + "_" + ((Item)actionInfo.get(LexConstants.SYNTAX_LR_ACTION_NEXT_ITEMCOLLECTION)).getSyntaxProduct().getNumber());
                            }

                            if(actionSymbol.getSymbol().equals(LexConstants.SYNTAX_EMPTY)) {
                                str.append("</span>");
                            }
                            str.append("    </td>\n");
                        } else {
                            str.append("    <td></td>\n");
                        }
                    }else{
                        str.append("    <td></td>\n");
                    }
            }

            for(SyntaxSymbol gotoSymbol : gotoSymbolSet){
                if(!gotoSymbol.getSymbol().equals(LexConstants.SYNTAX_EMPTY)) {
                    if(predictSLRMap.get(itemCollection).get(LexConstants.SYNTAX_LR_GOTO) != null) {
                        if (predictSLRMap.get(itemCollection).get(LexConstants.SYNTAX_LR_GOTO).get(gotoSymbol) != null) {
                            Map<String, Object> actionInfo = predictSLRMap.get(itemCollection).get(LexConstants.SYNTAX_LR_GOTO).get(gotoSymbol).get(0);
                            str.append("    <td>" + ((ItemCollection)actionInfo.get(LexConstants.SYNTAX_LR_ACTION_NEXT_ITEMCOLLECTION)).getNumber() + "</td>\n");
                        } else {
                            str.append("    <td></td>\n");
                        }
                    }else{
                        str.append("    <td></td>\n");
                    }
                }
            }

            str.append("</tr>\n");
        }
        str.append("</tbody>\n");
        str.append("</table>\n");

        System.out.println(str.toString());
    }

    public static Set<SyntaxSymbol> getLRPredictSymbol(Map<ItemCollection, Map<String, Map<SyntaxSymbol, List<Map<String, Object>>>>> predictSLRMap, String type){
        Set<SyntaxSymbol> predictSymbol = new LinkedHashSet<>();

        for(ItemCollection itemCollection : predictSLRMap.keySet()){
            if(predictSLRMap.get(itemCollection).get(type) != null) {
                for (SyntaxSymbol syntaxSymbol : predictSLRMap.get(itemCollection).get(type).keySet()) {
                    predictSymbol.add(syntaxSymbol);
                }
            }
        }

        return predictSymbol;
    }

}