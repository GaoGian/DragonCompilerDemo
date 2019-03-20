package gian.compiler.practice.syntactic.element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/3/20.
 */
public class SyntaxTree {

    public SyntaxTreeNode syntaxTreeRoot;

    public SyntaxTree(){}

    public SyntaxTree(SyntaxTreeNode syntaxTreeRoot) {
        this.syntaxTreeRoot = syntaxTreeRoot;
    }

    public SyntaxTreeNode getSyntaxTreeRoot() {
        return syntaxTreeRoot;
    }

    public void setSyntaxTreeRoot(SyntaxTreeNode syntaxTreeRoot) {
        this.syntaxTreeRoot = syntaxTreeRoot;
    }


    public static class SyntaxTreeNode{

        protected boolean isLeafNode;
        protected SyntaxProduct productNode;
        protected List<SyntaxTreeNode> subProductNodeList = new ArrayList<>();

        public SyntaxTreeNode(){}

        public SyntaxTreeNode(SyntaxProduct productNode) {
            this.productNode = productNode;
        }

        public SyntaxTreeNode(boolean isLeafNode, SyntaxProduct productNode) {
            this.isLeafNode = isLeafNode;
            this.productNode = productNode;
        }

        public SyntaxTreeNode(SyntaxProduct productNode, List<SyntaxTreeNode> subProductNodeList) {
            this.productNode = productNode;
            this.subProductNodeList.addAll(subProductNodeList);
        }

        public SyntaxTreeNode(boolean isLeafNode, SyntaxProduct productNode, List<SyntaxTreeNode> subProductNodeList) {
            this.isLeafNode = isLeafNode;
            this.productNode = productNode;
            this.subProductNodeList.addAll(subProductNodeList);
        }

        public boolean isLeafNode() {
            return isLeafNode;
        }

        public void setLeafNode(boolean leafNode) {
            isLeafNode = leafNode;
        }

        public SyntaxProduct getProductNode() {
            return productNode;
        }

        public void setProductNode(SyntaxProduct productNode) {
            this.productNode = productNode;
        }

        public List<SyntaxTreeNode> getSubProductNodeList() {
            return subProductNodeList;
        }

        public void setSubProductNodeList(List<SyntaxTreeNode> subProductNodeList) {
            this.subProductNodeList.addAll(subProductNodeList);
        }
    }

}
