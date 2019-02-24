import gian.compiler.practice.syntactic.SyntacticParser;
import gian.compiler.practice.syntactic.symbol.SyntaxSymbol;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }

    }

    @Test
    public void testEliminateLeftRecursion(){

        List<String> syntaxs = new ArrayList<>();

        syntaxs.add("E → E + T | T ");
        syntaxs.add("T → T + F | F ");
        syntaxs.add("F → ( E ) | id ");

        List<SyntaxSymbol> syntaxSymbols = SyntacticParser.parseSyntaxSymbol(syntaxs);

        // 消除前
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }

        System.out.println("----------------------------------------------------------------------");

        SyntacticParser.eliminateLeftRecursion(syntaxSymbols);

        // 消除后
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }
    }

    @Test
    public void testMergeCommonFactor(){

        List<String> syntaxs = new ArrayList<>();

        syntaxs.add("stmt → if expr then stmt else stmt | if expr then stmt");

        List<SyntaxSymbol> syntaxSymbols = SyntacticParser.parseSyntaxSymbol(syntaxs);

        // 提取前
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }

        System.out.println("----------------------------------------------------------------------");

        SyntacticParser.mergeCommonFactor(syntaxSymbols);

        // 提取后
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }
    }

    @Test
    public void testSyntaxFirstAndFolloew(){

        List<String> syntaxs = new ArrayList<>();

        syntaxs.add("E → E + T | T ");
        syntaxs.add("T → T * F | F ");
        syntaxs.add("F → ( E ) | id ");

        List<SyntaxSymbol> syntaxSymbols = SyntacticParser.parseSyntaxSymbol(syntaxs);

        // 消除前
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }

        System.out.println("----------------------------------------------------------------------");

        SyntacticParser.eliminateLeftRecursion(syntaxSymbols);

        // 提取后
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }

        System.out.println("-------------------------------FIRST-----------------------------------");
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap = SyntacticParser.syntaxFirst(syntaxSymbols);

        for(SyntaxSymbol symbol : syntaxFirstMap.keySet()) {
            Map<List<SyntaxSymbol>, Set<String>> productFirstMap = syntaxFirstMap.get(symbol);
            for(List<SyntaxSymbol> product : productFirstMap.keySet()) {
                Set<String> productFirst = productFirstMap.get(product);
                StringBuilder productStr = new StringBuilder();
                productStr.append(symbol.getSymbol());
                productStr.append(" → ");
                for(int i=0; i<product.size(); i++){
                    productStr.append(product.get(i).getSymbol());
                    if(i<product.size()-1){
                        productStr.append(" ");
                    }else{
                        productStr.append(";");
                    }
                }

                System.out.println(productStr.toString() + ", " + productFirst.toString());
            }
        }

//        System.out.println("-------------------------------FOLLOW----------------------------------");
//
//        Map<String, Set<String>> followMap = SyntacticParser.syntaxFollow(syntaxSymbols.get(0));
//
//        for(String syntaxSymbolTag : followMap.keySet()){
//            System.out.println(syntaxSymbolTag + " -------- " + followMap.get(syntaxSymbolTag));
//        }

    }


}
