import gian.compiler.practice.lexical.parser.LexExpression;
import gian.compiler.practice.lexical.parser.LexicalParser;
import gian.compiler.practice.lexical.parser.Token;
import gian.compiler.practice.lexical.transform.LexConstants;
import gian.compiler.practice.syntactic.SyntacticParser;
import gian.compiler.practice.syntactic.symbol.SyntaxProduct;
import gian.compiler.practice.syntactic.symbol.SyntaxSymbol;
import lex.test.LexUtils;
import org.junit.Test;

import java.util.*;

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

//        syntaxs.add("stmt → if expr then stmt else stmt | if expr then stmt");
        syntaxs.add("T → a * F | a * F - F");

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
    public void testMergeCommonFactor2(){

        List<String> syntaxs = new ArrayList<>();

        syntaxs.add("T → T * F | T * F - F ");
        syntaxs.add("F → id ");

        List<SyntaxSymbol> syntaxSymbols = SyntacticParser.parseSyntaxSymbol(syntaxs);

        // 消除前
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }

        System.out.println("-----------------------------左递归-------------------------------------");

        SyntacticParser.eliminateLeftRecursion(syntaxSymbols);

        // 消除后
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }

        System.out.println("-----------------------------公因式-------------------------------------");

        // TODO 不能正确处理提取公因式后的文法
        SyntacticParser.mergeCommonFactor(syntaxSymbols);

        // 提取公因式
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

        for(SyntaxSymbol symbol : syntaxSymbols) {
            System.out.println(symbol.getSymbol() + " -------- " + SyntacticParser.getSyntaxFirst(symbol, syntaxFirstMap));
        }

        System.out.println("-------------------------------FOLLOW----------------------------------");

        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> followMap = SyntacticParser.syntaxFollow(syntaxSymbols, syntaxFirstMap);

        for(SyntaxSymbol syntaxSymbol : syntaxSymbols){
            System.out.println(syntaxSymbol.getSymbol() + " -------- " + SyntacticParser.getSyntaxFollow(syntaxSymbol, followMap));
        }

    }

    @Test
    public void testSyntaxPredict(){

        List<String> syntaxs = new ArrayList<>();

        syntaxs.add("E → E + T | T ");
        syntaxs.add("T → T * F | F ");
        syntaxs.add("F → ( E ) | id ");

        // TODO 不适用二义性文法
//        syntaxs.add("S → i E t S S' | a ");
//        syntaxs.add("S' → e S | ε ");
//        syntaxs.add("E → b ");

        List<SyntaxSymbol> syntaxSymbols = SyntacticParser.parseSyntaxSymbol(syntaxs);

        SyntacticParser.eliminateLeftRecursion(syntaxSymbols);

        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap = SyntacticParser.syntaxFirst(syntaxSymbols);

        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap = SyntacticParser.syntaxFollow(syntaxSymbols, syntaxFirstMap);

        System.out.println("-------------------------------预测分析表----------------------------------");

        Map<SyntaxSymbol, Map<String, Set<SyntaxProduct>>> syntaxPredictMap = SyntacticParser.syntaxPredictMap(syntaxFirstMap, syntaxFollowMap);

        LexUtils.outputLL1SyntaxPredict(syntaxFirstMap, syntaxFollowMap, syntaxPredictMap);
    }

    @Test
    public void testSyntaxPredict2(){

        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("id", LexExpression.TokenType.KEYWORD));
        tokens.add(new Token("+", LexExpression.TokenType.OPERATOR));
        tokens.add(new Token("id", LexExpression.TokenType.KEYWORD));
        tokens.add(new Token("*", LexExpression.TokenType.OPERATOR));
        tokens.add(new Token("id", LexExpression.TokenType.KEYWORD));
        tokens.add(new Token(LexConstants.SYNTAX_END, LexExpression.TokenType.END));

        System.out.println("-------------------------------预测分析表----------------------------------");

        List<String> syntaxs = new ArrayList<>();

        syntaxs.add("E → E + T | T ");
        syntaxs.add("T → T * F | F ");
        syntaxs.add("F → ( E ) | id ");

        System.out.println("----------------------------消除左递归，提取公因式------------------------------");
        List<SyntaxSymbol> syntaxSymbols = SyntacticParser.parseSyntaxSymbol(syntaxs);
        SyntacticParser.eliminateLeftRecursion(syntaxSymbols);
        // 提取后
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }

        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap = SyntacticParser.syntaxFirst(syntaxSymbols);

        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap = SyntacticParser.syntaxFollow(syntaxSymbols, syntaxFirstMap);

        Map<SyntaxSymbol, Map<String, Set<SyntaxProduct>>> syntaxPredictMap = SyntacticParser.syntaxPredictMap(syntaxFirstMap, syntaxFollowMap);

        System.out.println("-------------------------------LL(1)语法分析----------------------------------");

        SyntacticParser.syntaxParse(tokens, syntaxSymbols.get(0), syntaxPredictMap);

    }

    @Test
    public void testSyntaxPredict3(){

        System.out.println("-------------------------------构造文法----------------------------------");

        // TODO 构造文法
        List<String> syntaxs = new ArrayList<>();

        syntaxs.add("stmt → { declared stmts }");
        syntaxs.add("declared → type id ; declared | ε");
        syntaxs.add("type → base component");
        syntaxs.add("base → int | float");
        syntaxs.add("component → [ number ] component | ε");
        syntaxs.add("stmts → whilecycle | docycle | expression ; stmts | ε");
        syntaxs.add("whilecycle → while ( bexpr ) { stmts }");
        syntaxs.add("docycle → do stmts ; while ( bexpr ) ;");
        syntaxs.add("bexpr → factor > factor | factor < factor | factor >= factor | factor <= factor | factor == factor | factor != factor | true | false");
        syntaxs.add("expression → expression + term | expression - term");
        syntaxs.add("term → term * factor | term / factor | factor");
        syntaxs.add("factor → id | factor [ number ]");

        System.out.println("----------------------------文法解析------------------------------");
        List<SyntaxSymbol> syntaxSymbols = SyntacticParser.parseSyntaxSymbol(syntaxs);
        // 消除前
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }

        System.out.println("----------------------------消除左递归------------------------------");
        SyntacticParser.eliminateLeftRecursion(syntaxSymbols);
        // 提取后
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }

        System.out.println("-----------------------------提取公因式-------------------------------------");
        SyntacticParser.mergeCommonFactor(syntaxSymbols);

        // 提取公因式
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols) {
            System.out.println(syntaxSymbol);
        }

        System.out.println("-------------------------------预测分析表----------------------------------");
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap = SyntacticParser.syntaxFirst(syntaxSymbols);

        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap = SyntacticParser.syntaxFollow(syntaxSymbols, syntaxFirstMap);

        Map<SyntaxSymbol, Map<String, Set<SyntaxProduct>>> syntaxPredictMap = SyntacticParser.syntaxPredictMap(syntaxFirstMap, syntaxFollowMap);

        LexUtils.outputLL1SyntaxPredict(syntaxFirstMap, syntaxFollowMap, syntaxPredictMap);

        System.out.println("-------------------------------LL(1)语法分析----------------------------------");

        // TODO 需要将预测分析表的终结符替换成元字符，识别成类似Token的封装类
        List<Token> tokens = LexicalParser.parser("C:\\Users\\Gian\\Desktop\\Temp\\compilerCode.txt");
        SyntacticParser.syntaxParse(tokens, syntaxSymbols.get(0), syntaxPredictMap);

    }


}
