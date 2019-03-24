package gian.compiler.practice.syntaxDirected;

import gian.compiler.practice.exception.ParseException;
import gian.compiler.practice.syntactic.element.SyntaxTree;

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
    public static void syntaxDirectedParser(SyntaxTree syntaxTree, List<SyntaxDirectedListener> syntaxDirectedListenerList){
        // 建立语法树节点和语义动作映射
        Map<Integer, SyntaxDirectedListener> syntaxDirectActionMap = matchSyntaxTreeNodeDirectAction(syntaxTree.getSyntaxTreeRoot(), 0, syntaxDirectedListenerList, new HashMap<>());

        // 创建语义分析上下文环境      TODO 设置上下文环境：父节点、兄弟节点，位置信息
        SyntaxDirectedContext context = new SyntaxDirectedContext(syntaxTree);

        // 后续遍历语法树，执行相关语义动作
        executeSyntaxTreeDirectAction(syntaxTree.getSyntaxTreeRoot(), context, syntaxDirectActionMap);

    }

    // 深度遍历语法树，执行相关语义动作
    // TODO 上下文环境是否需要按照作用域进行分层？？或者单独设置环境变量
    public static void executeSyntaxTreeDirectAction(SyntaxTree.SyntaxTreeNode syntaxTreeNode, SyntaxDirectedContext context,
                                                     Map<Integer, SyntaxDirectedListener> syntaxDirectActionMap){

        // 获取匹配语义动作
        SyntaxDirectedListener syntaxDirectedListener = syntaxDirectActionMap.get(syntaxTreeNode.getNumber());

        if(syntaxDirectedListener != null) {
            // 执行预处理语义动作
            syntaxDirectedListener.enterSyntaxSymbol(context);
        }

        // 深度遍历下级语法节点，执行子节点语义动作
        // TODO 需要创建每个分析树节点对应的属性集合，交由具体的action处理
        for(int i=0; i<syntaxTreeNode.getSubProductNodeList().size(); i++){
            SyntaxTree.SyntaxTreeNode childNode = syntaxTreeNode.getSubProductNodeList().get(i);
            // TODO 设置上下文信息：父节点、兄弟节点，位置信息
            executeSyntaxTreeDirectAction(childNode, context, syntaxDirectActionMap);
        }

        if(syntaxDirectedListener != null) {
            // 执行综合语义处理动作
            syntaxDirectedListener.exitSyntaxSymbol(context);
        }

    }


    // 匹配语法分析树节点语义动作
    public static Map<Integer, SyntaxDirectedListener> matchSyntaxTreeNodeDirectAction(SyntaxTree.SyntaxTreeNode syntaxTreeNode, Integer syntaxTreeNodeIndex,
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

}