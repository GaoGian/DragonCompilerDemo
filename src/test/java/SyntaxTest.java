import gian.compiler.practice.syntactic.SyntacticParser;
import gian.compiler.practice.syntactic.symbol.SyntaxSymbol;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gian on 2019/2/19.
 */
public class SyntaxTest {

    @Test
    public void test(){
        List<String> syntaxs = new ArrayList<>();

        syntaxs.add("stmt → if expr then stmt else stmt | if stmt then stmt | begin stmtList end");
        syntaxs.add("stmtList → stmt ; stmtList | stmt | ");

        List<SyntaxSymbol> syntaxSymbols = SyntacticParser.parseSyntaxSymbol(syntaxs);

        System.out.println(syntaxSymbols);

    }

}
