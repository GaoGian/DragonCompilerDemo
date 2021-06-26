package gian.compiler.utils;

import gian.compiler.front.lexical.parser.Token;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.lexical.transform.regex.LexAutomatonTransformer;
import gian.compiler.front.syntactic.SyntacticLLParser;
import gian.compiler.front.syntactic.element.Item;
import gian.compiler.front.syntactic.element.ItemCollection;
import gian.compiler.front.syntactic.element.SyntaxProduct;
import gian.compiler.front.syntactic.element.SyntaxSymbol;

import java.io.PrintStream;
import java.util.*;

/**
 * Created by gaojian on 2019/4/1.
 */
public class ParseChartUtils {

    static {
        PrintStream print = null;
        try {
            print = new PrintStream("D:\\test.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.setOut(print);
    }

    // 使用ECharts路径图显示
    // https://echarts.baidu.com/examples/editor.html?c=graph-simple
    public static void outputEchart(LexAutomatonTransformer.LexCell lexCell){

        // 广度遍历
        int x = 0, y =0;
        Set<LexAutomatonTransformer.LexState> states = new HashSet<>();
        Set<EcharDemoPoint> pointList = new HashSet<>();
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
        pointList.add(new EcharDemoPoint(startState.toString(), x, y));

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
                    pointList.add(new EcharDemoPoint(targetState.toString(), x, y));
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
            pointList.add(new EcharDemoPoint(endState.toString(), x, y));
            y -= 300;
        }

        List<EchartDemoEdge> echarEdges = new ArrayList<>();
        for(LexAutomatonTransformer.LexEdge edge : lexCell.getEdgeSet()){
            echarEdges.add(new EchartDemoEdge(edge.getStartState().toString(), edge.getEndState().toString(), edge.getTranPattern().getMeta()));
        }

        // 输出
        for(EcharDemoPoint point : pointList){
            System.out.println(point.toString());
        }
        System.out.println("--------------------------------------------------------------------");
        for(EchartDemoEdge echartEdge : echarEdges){
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
        Set<EcharDemoPoint> pointSet = new HashSet<>();
        Set<EchartDemoEdge> echarEdgeSet = new HashSet<>();

        int x = 0;
        int y = 0;
        pointSet.add(new EcharDemoPoint(getCoreItemTag(startItemCollection, showOtherItem), x, y));

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

                EcharDemoPoint newPoiont = new EcharDemoPoint(getCoreItemTag(moveItemCollection, showOtherItem), x, y);
                if (!pointSet.contains(newPoiont)) {
                    pointSet.add(newPoiont);
                }
                EchartDemoEdge newEdge = new EchartDemoEdge(getCoreItemTag(preItemCollection, showOtherItem), getCoreItemTag(moveItemCollection, showOtherItem), moveSymbol.getSymbol());
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

                    EchartDemoEdge subNewEdge = new EchartDemoEdge(getCoreItemTag(moveItemCollection, showOtherItem), getCoreItemTag(subItemCollection, showOtherItem), subMoveSymbol.getSymbol());
                    if (!echarEdgeSet.contains(subNewEdge)) {
                        echarEdgeSet.add(subNewEdge);
                    }
                }

                y -= yIncre;
            }

        }


        // 输出
        System.out.println("----------------------------- pointList size: " + pointSet.size() + " -------------------------------");
        for(EcharDemoPoint point : pointSet){
            System.out.println(point.toString());
        }
        System.out.println("----------------------------- edgeList size: " + echarEdgeSet.size() + " --------------------------------");
        for(EchartDemoEdge echartEdge : echarEdgeSet){
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
        Set<EcharDemoPoint> pointSet = new HashSet<>();
        Set<EchartDemoEdge> echarEdgeSet = new HashSet<>();

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
        for(EcharDemoPoint point : pointSet){
            System.out.println(point.toString());
        }
        System.out.println("----------------------------- edgeList size: " + echarEdgeSet.size() + " --------------------------------");
        for(EchartDemoEdge echartEdge : echarEdgeSet){
            System.out.println(echartEdge.toString());
        }
        System.out.println("---------------------------------------------------------------------");
    }

    public static class UniversalTreeNode<T>{
        private T node;
        private UniversalTreeNodeMatcher matcher;
        private boolean showSubTreeNode;

        private int xAxis;
        private int yAxis;

        public UniversalTreeNode(){}

        public UniversalTreeNode(T node, UniversalTreeNodeMatcher matcher, boolean showSubTreeNode){
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

        public UniversalTreeNodeMatcher<T> getMatcher() {
            return matcher;
        }

        public void setMatcher(UniversalTreeNodeMatcher<T> matcher) {
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
        public static abstract class UniversalTreeNodeMatcher<T>{

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

    public static void outputToken(List<Token> parseRs){
        System.out.println("---------------------------------------------------------------------------");
        int line = 0;
        for(Token token : parseRs){
            if(token.getLine() > line){
                line = token.getLine();
                System.out.println("");
            }
            System.out.print(token.toString());
        }
        System.out.println("");
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