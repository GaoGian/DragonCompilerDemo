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

        protected Integer number;
        protected boolean isLeafNode;
        protected SyntaxTreeNode parentNode;
        protected SyntaxProduct product;
        protected SyntaxSymbol syntaxSymbol;
        protected List<SyntaxTreeNode> subProductNodeList = new ArrayList<>();

        public SyntaxTreeNode(){}

        public SyntaxTreeNode(Integer number, boolean isLeafNode, SyntaxSymbol syntaxSymbol) {
            this.number = number;
            this.isLeafNode = isLeafNode;
            this.syntaxSymbol = syntaxSymbol;
        }

        public SyntaxTreeNode(Integer number, boolean isLeafNode, SyntaxProduct product) {
            this.number = number;
            this.isLeafNode = isLeafNode;
            this.product = product;
            this.syntaxSymbol = product.getHead();
        }

        public SyntaxTreeNode(Integer number, boolean isLeafNode, SyntaxProduct product, List<SyntaxTreeNode> subProductNodeList) {
            this.number = number;
            this.isLeafNode = isLeafNode;
            this.product = product;
            this.syntaxSymbol = product.getHead();
            this.subProductNodeList = subProductNodeList;

            // 反向关联父节点
            for(SyntaxTreeNode childNode : subProductNodeList){
                childNode.setParentNode(this);
            }
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public boolean isLeafNode() {
            return isLeafNode;
        }

        public void setLeafNode(boolean leafNode) {
            isLeafNode = leafNode;
        }

        public SyntaxTreeNode getParentNode() {
            return parentNode;
        }

        public void setParentNode(SyntaxTreeNode parentNode) {
            this.parentNode = parentNode;
        }

        public SyntaxProduct getProduct() {
            return product;
        }

        public void setProduct(SyntaxProduct product) {
            this.product = product;
        }

        public List<SyntaxTreeNode> getSubProductNodeList() {
            return subProductNodeList;
        }

        public void setSubProductNodeList(List<SyntaxTreeNode> subProductNodeList) {
            this.subProductNodeList = subProductNodeList;

            // 反向关联父节点
            for(SyntaxTreeNode childNode : subProductNodeList){
                childNode.setParentNode(this);
            }

        }

        @Override
        public String toString(){
            return this.number + " : " + this.syntaxSymbol.getSymbol();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SyntaxTreeNode that = (SyntaxTreeNode) o;

            return number != null ? number.equals(that.number) : that.number == null;

        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

}
