import gian.compiler.practice.lexical.parser.LexExpression;
import gian.compiler.practice.lexical.parser.LexicalParser;
import gian.compiler.practice.lexical.parser.Token;
import gian.compiler.practice.lexical.transform.LexConstants;
import gian.compiler.practice.syntactic.SyntacticParser;
import gian.compiler.practice.syntactic.symbol.SyntaxProduct;
import gian.compiler.practice.syntactic.symbol.SyntaxSymbol;
import gian.compiler.utils.ParseUtils;
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

        LexUtils.outputLL1SyntaxPredict(syntaxFirstMap, syntaxFollowMap, syntaxPredictMap);

        System.out.println("-------------------------------LL(1)语法分析----------------------------------");

        SyntacticParser.syntaxParse(tokens, syntaxSymbols.get(0), syntaxPredictMap);

    }

    @Test
    public void testSyntaxPredict3(){

        System.out.println("-------------------------------读取文法----------------------------------");
        // TODO 构造文法
        List<String> syntaxs = ParseUtils.getFile("syntaxContentFile.txt", true);
        // 消除前
        for(String line : syntaxs) {
            System.out.println(line);
        }

        System.out.println("----------------------------文法解析------------------------------");
        // TODO 修改终结符的识别方式，需要和词法规则联系起来
        // TODO 终结符有几类：关键字、符号、变量、值
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

        System.out.println("------------------------------解析测试代码----------------------------------");
        System.out.println("------------------------------原样显示代码----------------------------------");
        // TODO 文法中的终结符需要和 LexExpression 元字符进行匹配，然后在判断输入符是否是对应的LexExpression的符号（验证正则表达式是否匹配，已经TokenType是否一致）
        // TODO 需要区分关键字和ID类别的终结符
        /** 一般情况下词法分析是在文法分析过程中进行的，根据语义返回识别的符号，这里由于分开处理，所以需要进行额外的关联映射 **/
        List<Token> tokens = LexicalParser.parser(ParseUtils.getFile("compilerCode.txt", true), LexExpression.expressions);
        int line = 0;
        for(Token token : tokens){
            if(token.getLine() > line){
                line = token.getLine();
                System.out.println("");
            }
            System.out.print(token.toString());
        }
        System.out.println("");
        System.out.println("------------------------------显示详细信息----------------------------------");
        line = 0;
        for(Token token : tokens){
            if(token.getLine() > line){
                line = token.getLine();
                System.out.println("");
            }
            System.out.print(token.getToken() + " ");
        }
        System.out.println("");

        System.out.println("-------------------------------LL(1)语法分析----------------------------------");
        SyntacticParser.syntaxParse(tokens, syntaxSymbols.get(0), syntaxPredictMap);

    }


}
