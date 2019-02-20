package gian.compiler.practice.syntactic.symbol;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gian on 2019/2/19.
 */
public class SyntaxSymbol {

    protected String symbol;
    protected boolean isTerminal;
    protected List<List<SyntaxSymbol>> body = new ArrayList<>();

    public SyntaxSymbol(){}

    public SyntaxSymbol(String symbol, boolean isTerminal) {
        this.symbol = symbol;
        this.isTerminal = isTerminal;
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
                str.append(symbols.get(j).getSymbol());
                str.append(" ");
            }

            if(i <= body.size()-2){
                str.append("| ");
            }

        }
        return str.toString();
    }
}
