package gian.compiler.front.syntaxDirected;

import gian.compiler.front.exception.ParseException;
import gian.compiler.front.syntactic.element.SyntaxSymbol;
import gian.compiler.front.syntactic.element.SyntaxTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 遍历语法分析树，执行匹配的语义动作
 * Created by gaojian on 2019/3/23.
 */
public class SyntaxDirectedParser {

    /**
     * 执行语法分析树
     *
     */
    public static SyntaxTree syntaxDirectedParser(SyntaxTree syntaxTree, List<SyntaxDirectedListener> syntaxDirectedListenerList){
        // 建立语法树节点和语义动作映射
        Map<Integer, SyntaxDirectedListener> syntaxDirectActionMap = matchSyntaxTreeNodeDirectAction(syntaxTree.getSyntaxTreeRoot(), 0, syntaxDirectedListenerList, new HashMap<>());

        // 创建语义分析上下文环境      TODO 设置上下文环境：父节点、兄弟节点，位置信息
        SyntaxDirectedContext context = new SyntaxDirectedContext(syntaxTree);

        // 后续遍历语法树，执行相关语义动作
        SyntaxTree.SyntaxTreeNode annotionSyntaxTreeRoot = executeSyntaxTreeDirectAction(syntaxTree.getSyntaxTreeRoot(), 0, context, syntaxDirectActionMap);

        // 返回注释语法分析树
        SyntaxTree annotionSyntaxTree = new SyntaxTree(annotionSyntaxTreeRoot);

        return annotionSyntaxTree;
    }

    // 深度遍历语法树，执行相关语义动作
    // TODO 上下文环境是否需要按照作用域进行分层？？或者单独设置环境变量
    private static SyntaxTree.SyntaxTreeNode executeSyntaxTreeDirectAction(SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex,
                                                     SyntaxDirectedContext context, Map<Integer, SyntaxDirectedListener> syntaxDirectActionMap){

        // 生成注释语法分析树节点
        SyntaxTree.SyntaxTreeNode annotionNode = new SyntaxTree.SyntaxTreeNode(currentTreeNode);
        List<SyntaxTree.SyntaxTreeNode> subAnnotionNodeList = new ArrayList<>();

        // 设置上下文信息：父节点、兄弟节点，位置信息
        // TODO 根节点暂时不考虑
        if(currentTreeNode.getParentNode() != null) {
            setDirectedContextInfo(context, currentIndex, currentTreeNode);
        }

        // 获取匹配语义动作
        SyntaxDirectedListener syntaxDirectedListener = syntaxDirectActionMap.get(currentTreeNode.getNumber());

        if(syntaxDirectedListener != null){
            if(currentTreeNode.isLeafNode() || currentTreeNode.isIdNode()) {
                syntaxDirectedListener.lexline = currentTreeNode.getIdToken().getLine();
                syntaxDirectedListener.lexindex = currentTreeNode.getIdToken().getIndex();
            }
        }

        // 执行预处理语义动作
        if(syntaxDirectedListener != null) {
            String code = syntaxDirectedListener.enterSyntaxSymbol(context, currentTreeNode, currentIndex);

            // 加入预处理语义动作节点
            SyntaxTree.SyntaxTreeNode enterDirectActionNode = new SyntaxTree.SyntaxTreeNode(currentTreeNode.getNumber()*100+currentIndex, true, new SyntaxSymbol(code, true));
            subAnnotionNodeList.add(enterDirectActionNode);
        }

        // 深度遍历下级语法节点，执行子节点语义动作
        // TODO 可以根据需要创建每个节点的属性集合，交由具体的action处理
        for(int i=0; i<currentTreeNode.getSubProductNodeList().size(); i++){
            SyntaxTree.SyntaxTreeNode childNode = currentTreeNode.getSubProductNodeList().get(i);
            SyntaxTree.SyntaxTreeNode childAnnotionNode = executeSyntaxTreeDirectAction(childNode, i, context, syntaxDirectActionMap);

            // 加入下级注释节点
            subAnnotionNodeList.add(childAnnotionNode);
        }

        // 遍历完下级节点后重新设置上下文信息：父节点、兄弟节点，位置信息
        if(currentTreeNode.getParentNode() != null) {
            setDirectedContextInfo(context, currentIndex, currentTreeNode);
        }

        // 执行综合语义处理动作
        // TODO 根节点暂时不考虑
        if(syntaxDirectedListener != null) {
            String code = syntaxDirectedListener.exitSyntaxSymbol(context, currentTreeNode, currentIndex);

            // 加入综合记录语义动作节点
            SyntaxTree.SyntaxTreeNode exitDirectActionNode = new SyntaxTree.SyntaxTreeNode(currentTreeNode.getNumber()*1000+currentIndex, true, new SyntaxSymbol(code, true));
            subAnnotionNodeList.add(exitDirectActionNode);
        }

        annotionNode.setSubProductNodeList(subAnnotionNodeList);

        return annotionNode;
    }

    // 匹配语法分析树节点语义动作
    private static Map<Integer, SyntaxDirectedListener> matchSyntaxTreeNodeDirectAction(SyntaxTree.SyntaxTreeNode syntaxTreeNode, Integer syntaxTreeNodeIndex,
                                                       List<SyntaxDirectedListener> syntaxDirectedListenerList, Map<Integer, SyntaxDirectedListener> syntaxDirectActionMap){

        // TODO 这里暂时不处理增广文法根节点，将增广文法节点的语义动作下移到文法其实节点
        if(syntaxTreeNode.getParentNode() != null) {
            // 匹配当前节点
            for (SyntaxDirectedListener syntaxDirectedListener : syntaxDirectedListenerList) {
                // FIXME 这里用 product 的 toString 方法不太妥
                String productTag = syntaxTreeNode.getParentNode().getProduct().toString();
                if (syntaxDirectedListener.isMatch(productTag, syntaxTreeNodeIndex, syntaxTreeNode.getSyntaxSymbol().getSymbol())) {
                    if (syntaxDirectActionMap.get(syntaxTreeNode.getNumber()) != null) {
                        throw new ParseException("语义动作匹配异常，当前节点：" + syntaxTreeNode.getNumber());
                    } else {
                        syntaxDirectActionMap.put(syntaxTreeNode.getNumber(), syntaxDirectedListener);
                    }
                }
            }
        }

        // 匹配下级节点
        for(int i=0; i<syntaxTreeNode.getSubProductNodeList().size(); i++){
            SyntaxTree.SyntaxTreeNode childNode = syntaxTreeNode.getSubProductNodeList().get(i);
            matchSyntaxTreeNodeDirectAction(childNode, i, syntaxDirectedListenerList, syntaxDirectActionMap);
        }

        return syntaxDirectActionMap;
    }

    private static void setDirectedContextInfo(SyntaxDirectedContext context, Integer currentIndex, SyntaxTree.SyntaxTreeNode currentNode){
        context.setCurrentNodeIndex(currentIndex);
        context.setCurrentNode(currentNode);
        context.setParentNode(currentNode.getParentNode());
        context.setBrotherNodeList(currentNode.getParentNode().getSubProductNodeList());

        if(currentNode.isLeafNode() || currentNode.isIdNode()){
            context.setLine(currentNode.getIdToken().getLine());
            context.setLine(currentNode.getIdToken().getIndex());
        }

    }

}