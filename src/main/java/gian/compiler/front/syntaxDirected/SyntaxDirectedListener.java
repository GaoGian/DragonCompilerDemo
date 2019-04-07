package gian.compiler.front.syntaxDirected;

import gian.compiler.front.syntactic.element.SyntaxTree;

/**
 * 产生式嵌入的语义动作
 * Created by Gian on 2019/3/21.
 */
public abstract class SyntaxDirectedListener {

    // 匹配的产生式标识（父节点产生式字符串，和输入格式保持一致）
    protected String matchProductTag;
    // 当前节点在兄弟节点的位置
    protected Integer matchIndex;
    // 当前节点文法符号
    protected String matchSymbol;
    // 是否是匹配叶子节点（终结符）
    protected Boolean isLeaf;

    public static Integer lexline;
    public static Integer lexindex;

    public SyntaxDirectedListener(){}

    public SyntaxDirectedListener(String matchProductTag, Integer matchIndex, String matchSymbol, Boolean isLeaf){
        this.matchProductTag = matchProductTag;
        this.matchIndex = matchIndex;
        this.matchSymbol = matchSymbol;
        this.isLeaf = isLeaf;
    }

    public boolean isMatch(String matchProductTag, Integer matchIndex, String matchSymbol){
        if(!this.matchProductTag.equals(matchProductTag) || !this.matchIndex.equals(matchIndex) || !this.matchSymbol.equals(matchSymbol)){
            return false;
        }else{
            return true;
        }
    }

    // 遍历节点前执行，返回code
    public abstract String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex);

    // 离开节点时执行，返回code
    public abstract String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex);

    public void setProperties(String matchProductTag, Integer matchIndex, String matchSymbol, Boolean isLeaf){
        this.matchProductTag = matchProductTag;
        this.matchIndex = matchIndex;
        this.matchSymbol = matchSymbol;
        this.isLeaf = isLeaf;
    }

    public String getMatchProductTag() {
        return matchProductTag;
    }

    public void setMatchProductTag(String matchProductTag) {
        this.matchProductTag = matchProductTag;
    }

    public Integer getMatchIndex() {
        return matchIndex;
    }

    public void setMatchIndex(Integer matchIndex) {
        this.matchIndex = matchIndex;
    }

    public String getMatchSymbol() {
        return matchSymbol;
    }

    public void setMatchSymbol(String matchSymbol) {
        this.matchSymbol = matchSymbol;
    }

    public Boolean getLeaf() {
        return isLeaf;
    }

    public void setLeaf(Boolean leaf) {
        isLeaf = leaf;
    }
}
