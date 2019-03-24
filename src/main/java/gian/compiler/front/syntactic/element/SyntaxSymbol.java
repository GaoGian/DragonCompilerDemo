package gian.compiler.front.syntactic.element;

import gian.compiler.front.lexical.transform.LexConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gian on 2019/2/19.
 */
public class SyntaxSymbol {

    protected String symbol;
    protected boolean isTerminal;
    protected List<List<SyntaxSymbol>> body = new ArrayList<>();

    // 是否是正则表达式单元
    protected boolean isRegexTerminal;

    public SyntaxSymbol(){}

    public SyntaxSymbol(String symbol, boolean isTerminal) {
        this.symbol = symbol;
        this.isTerminal = isTerminal;
    }

    public SyntaxSymbol(String symbol, boolean isTerminal, boolean isRegexTerminal) {
        this.symbol = symbol;
        this.isTerminal = isTerminal;
        this.isRegexTerminal = isRegexTerminal;
    }

    public SyntaxSymbol(String symbol, boolean isTerminal, List<List<SyntaxSymbol>> body) {
        this.symbol = symbol;
        this.isTerminal = isTerminal;
        this.body = body;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public boolean isTerminal() {
        return isTerminal;
    }

    public void setTerminal(boolean terminal) {
        isTerminal = terminal;
    }

    public boolean isRegexTerminal() {
        return isRegexTerminal;
    }

    public void setRegexTerminal(boolean regexTerminal) {
        isRegexTerminal = regexTerminal;
    }

    public List<List<SyntaxSymbol>> getBody() {
        return body;
    }

    public void setBody(List<List<SyntaxSymbol>> body) {
        this.body = body;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(symbol);

        str.append(" → ");
        for(int i=0; i<body.size(); i++){
            List<SyntaxSymbol> symbols = body.get(i);

            for(int j=0; j<symbols.size(); j++){
                String bodySymbol = symbols.get(j).getSymbol();
                if(LexConstants.SYNTAX_EMPTY.equals(bodySymbol)){
                    str.append(LexConstants.SYNTAX_EMPTY);
                }else {
                    str.append(bodySymbol);
                }

                if(j<symbols.size()-1) {
                    str.append(" ");
                }
            }

            if(i <= body.size()-2){
                str.append(" | ");
            }

        }
        return str.toString();
    }

    @Override
    public boolean equals(Object other) {
        if(other == null){
            return false;
        }

        if(this == other){
            return true;
        }

        if(this.getSymbol().equals(((SyntaxSymbol) other).getSymbol())){
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
