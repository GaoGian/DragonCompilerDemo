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

    // 当前遍历节点父节点下的位置
    protected Integer currentNodeIndex;
    protected SyntaxTree.SyntaxTreeNode currentNode;

    protected SyntaxTree.SyntaxTreeNode parentNode;
    protected List<SyntaxTree.SyntaxTreeNode> brotherNodeList;

    // 存放属性的地方，以父节点作为key存储分层域
    Map<SyntaxTree.SyntaxTreeNode, Map<String, Object>> propertyMap = new HashMap<>();

    // 存放全局属性
    Map<String, Object> globalPropertyMap = new HashMap<>();

    public SyntaxDirectedContext(SyntaxTree syntaxTree) {
        this.syntaxTree = syntaxTree;
    }

    public SyntaxTree getSyntaxTree() {
        return syntaxTree;
    }

    public void setSyntaxTree(SyntaxTree syntaxTree) {
        this.syntaxTree = syntaxTree;
    }

    public Integer getCurrentNodeIndex() {
        return currentNodeIndex;
    }

    public void setCurrentNodeIndex(Integer currentNodeIndex) {
        this.currentNodeIndex = currentNodeIndex;
    }

    public SyntaxTree.SyntaxTreeNode getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(SyntaxTree.SyntaxTreeNode currentNode) {
        this.currentNode = currentNode;
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

    public Map<SyntaxTree.SyntaxTreeNode, Map<String, Object>> getPropertyMap() {
        return propertyMap;
    }

    public void setPropertyMap(Map<SyntaxTree.SyntaxTreeNode, Map<String, Object>> propertyMap) {
        this.propertyMap = propertyMap;
    }

    public Map<String, Object> getGlobalPropertyMap() {
        return globalPropertyMap;
    }

    public void setGlobalPropertyMap(Map<String, Object> globalPropertyMap) {
        this.globalPropertyMap = globalPropertyMap;
    }
}
