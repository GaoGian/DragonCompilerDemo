package gian.compiler.practice.syntaxDirected;

/**
 * 产生式嵌入的语义动作
 * Created by Gian on 2019/3/21.
 */
public abstract class SyntaxDirectedAction {

    protected String matchSyntaxSymbolTag;

    public void SyntaxDirectedAction(String matchSyntaxSymbolTag){
        this.matchSyntaxSymbolTag = matchSyntaxSymbolTag;
    }

    public boolean isMatch(String matchSyntaxSymbol){
        return this.matchSyntaxSymbolTag.equals(matchSyntaxSymbol);
    }

    public abstract void invokeDirectedAction(SyntaxDirectedContext context);

}
