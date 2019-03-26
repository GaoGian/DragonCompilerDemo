package gian.compiler.front.syntactic.element;

import gian.compiler.front.lexical.parser.Token;
import gian.compiler.front.lexical.transform.LexConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        protected boolean isIdNode;
        protected Token idToken;
        protected SyntaxTreeNode parentNode;
        protected SyntaxProduct product;
        protected SyntaxSymbol syntaxSymbol;
        protected List<SyntaxTreeNode> subProductNodeList = new ArrayList<>();

        // 存储语义分析继承属性、综合属性，一级key：属性类型 inh、syn，二级key：属性名称，value：属性值
        protected Map<String, Map<String, Object>> propertyMap = new HashMap<>();

        public SyntaxTreeNode(){}

        // 生成注释语法树节点
        public SyntaxTreeNode(SyntaxTreeNode copyNode){
            this.number = copyNode.getNumber();
            this.isLeafNode = copyNode.isLeafNode();
            this.isIdNode = copyNode.isIdNode();
            this.idToken = copyNode.getIdToken();
            this.parentNode = copyNode.getParentNode();
            this.product = copyNode.getProduct();
            this.syntaxSymbol = copyNode.getSyntaxSymbol();
        }

        public SyntaxTreeNode(Integer number, boolean isLeafNode, SyntaxSymbol syntaxSymbol) {
            this.number = number;
            this.isLeafNode = isLeafNode;
            this.syntaxSymbol = syntaxSymbol;

            initPropertyMap();
        }

        public SyntaxTreeNode(Integer number, boolean isLeafNode, boolean isIdNode, Token idToken, SyntaxSymbol syntaxSymbol) {
            this.number = number;
            this.isLeafNode = isLeafNode;
            this.isIdNode = isIdNode;
            this.idToken = idToken;
            this.syntaxSymbol = syntaxSymbol;

            initPropertyMap();
        }

        public SyntaxTreeNode(Integer number, boolean isLeafNode, SyntaxProduct product, List<SyntaxTreeNode> subProductNodeList) {
            this.number = number;
            this.isLeafNode = isLeafNode;
            this.product = product;
            this.syntaxSymbol = product.getHead();
            this.subProductNodeList = subProductNodeList;

            initPropertyMap();

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

        public boolean isIdNode() {
            return isIdNode;
        }

        public void setIdNode(boolean idNode) {
            isIdNode = idNode;
        }

        public Token getIdToken() {
            return idToken;
        }

        public void setIdToken(Token idToken) {
            this.idToken = idToken;
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

        public SyntaxSymbol getSyntaxSymbol() {
            return syntaxSymbol;
        }

        public void setSyntaxSymbol(SyntaxSymbol syntaxSymbol) {
            this.syntaxSymbol = syntaxSymbol;
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

        public void initPropertyMap(){
            this.propertyMap.put(LexConstants.SYNTAX_DIRECT_PROPERTY_INH, new HashMap<>());
            this.propertyMap.put(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN, new HashMap<>());
        }

        public Map<String, Map<String, Object>> getPropertyMap() {
            return propertyMap;
        }

        public void setPropertyMap(Map<String, Map<String, Object>> propertyMap) {
            this.propertyMap = propertyMap;
        }

        @Override
        public String toString(){
            if(!this.isIdNode || !this.idToken.getType().isRexgexToken()) {
                return this.number + " : " + this.syntaxSymbol.getSymbol();
            }else{
                return this.number + " : " + this.syntaxSymbol.getSymbol() + "=" + this.idToken.getToken();
            }
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
