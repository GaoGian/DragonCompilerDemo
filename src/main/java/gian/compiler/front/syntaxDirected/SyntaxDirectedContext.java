package gian.compiler.front.syntaxDirected;

import gian.compiler.front.syntactic.element.SyntaxTree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 语义执行上下文，包括语法分析树、父节点、兄弟节点、当前执行的语法分析树位置等等
 * Created by Gian on 2019/3/21.
 */
public class SyntaxDirectedContext {

    protected SyntaxTree syntaxTree;
    protected SyntaxTree.SyntaxTreeNode parentNode;
    protected List<SyntaxTree.SyntaxTreeNode> brotherNodeList;

    Map<String, Object> propertyMap = new HashMap<>();

    public SyntaxDirectedContext(SyntaxTree syntaxTree) {
        this.syntaxTree = syntaxTree;
    }

    public SyntaxTree getSyntaxTree() {
        return syntaxTree;
    }

    public void setSyntaxTree(SyntaxTree syntaxTree) {
        this.syntaxTree = syntaxTree;
    }

    public SyntaxTree.SyntaxTreeNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(SyntaxTree.SyntaxTreeNode parentNode) {
        this.parentNode = parentNode;
    }

    public List<SyntaxTree.SyntaxTreeNode> getBrotherNodeList() {
        return brotherNodeList;
    }

    public void setBrotherNodeList(List<SyntaxTree.SyntaxTreeNode> brotherNodeList) {
        this.brotherNodeList = brotherNodeList;
    }

    public Map<String, Object> getPropertyMap() {
        return propertyMap;
    }

    public void setPropertyMap(Map<String, Object> propertyMap) {
        this.propertyMap = propertyMap;
    }
}
