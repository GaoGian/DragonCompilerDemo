import com.alibaba.fastjson.JSON;
import gian.compiler.practice.lexical.parser.LexicalParser;
import gian.compiler.practice.lexical.parser.Token;
import gian.compiler.practice.lexical.transform.LexConstants;
import lex.test.LexUtils;
import gian.compiler.practice.lexical.transform.regex.LexAutomatonTransformer;
import gian.compiler.practice.lexical.transform.regex.copy.LexPattern;
import gian.compiler.practice.lexical.transform.regex.LexSimplePattern;
import lex.test.*;
import org.junit.Test;
import utils.BST;
import utils.TreePrintUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static gian.compiler.practice.lexical.transform.regex.LexAutomatonTransformer.EOF_META;
import static gian.compiler.practice.lexical.transform.regex.LexAutomatonTransformer.buildLexDFANode;

/**
 * Created by Gian on 2019/1/27.
 */
public class NFATest {

    @Test
    public void test(){

        // FIXME 这个表达式会有问题
//        String regular_expression = "a|babb";
        String regular_expression = "(a|b)*abb";
        // 正则表达式生成 NFA
        Cell nfa = NFATransformer.regExp2Nfa(regular_expression);

        System.out.println("---------------------------------------------------------------------------");

        // NFA 生成 DFA
        DtranCell dtranCell = DFATransformer.nfa2Dfa(nfa);
    }

    @Test
    public void testLexNode(){
        String regular_expression = "(a|b)*abb";
        DFATransformer.Dcell dcell = DFATransformer.regExp2Dfa(regular_expression);
    }

    @Test
    public void testLexCompile(){
        String regular_expression = "(a|b)*abb";
        DFATransformer.Dcell dcell = DFATransformer.regExp2Dfa(regular_expression);

        System.out.println("----------------------------DFA compile test-------------------------------");

        String testStmt = "abababaaaaabbabb";
        DFATransformer.Dstate preState = dcell.getStartState();
        for(int index=0; index<testStmt.length(); index++){
            char input = testStmt.charAt(index);
            preState = preState.tranState(input);
            if(preState == null){
                throw new RuntimeException("DFA compile error");
            }
        }

        if(dcell.getEndState() != preState){
            throw new RuntimeException("DFA compile state error");
        }else{
            System.out.println("DFA compile success");
        }

    }

    @Test
    public void patternTest(){
        String str = "112.211";
        String regular_expression = "([A-Z]+)|(\\d+(\\.\\d+)?)";
        LexPattern pattern = LexPattern.compile(regular_expression);
//        LexMatcher matcher = pattern.matcher(str);
//        boolean rs = matcher.matches();
//        System.out.println(rs);

        if(pattern.matchRoot != null) {
            LexPattern.Node preNode = pattern.matchRoot;
            while (true) {
                System.out.println(preNode.getClass());

                preNode = preNode.next;
                if(preNode == null){
                    break;
                }
            }
        }

    }

    @Test
    public void simplePatternTest(){
//        String pattern = "a|dddd";
//        String pattern = "([A-Z]+)|(\\d+(\\.\\d+)?)";
        String pattern = "abc|([A-Z]+)|(\\d+(\\.\\d+)?)";
        List<LexSimplePattern.Metacharacter> metas = LexSimplePattern.compile(pattern);

        System.out.println(JSON.toJSONString(metas));

        List<LexSimplePattern.Metacharacter> postfixMetas = LexSimplePattern.postfix(metas);
        System.out.println("-----------------------------转化后的后缀表达式-----------------------------");
        System.out.println(JSON.toJSONString(postfixMetas));
    }

    @Test
    public void pattern2NfaTest(){
//        String pattern = "adv|bced";
//        String pattern = "(\\d*(\\.\\d+)?)";
//        String pattern = "([A-Z]+)|(\\d*(\\.\\d+)?)";
        String pattern = "abc|([A-Z]+)|(\\d+(\\.\\d+)?)";
        LexAutomatonTransformer.LexCell lexCell = LexAutomatonTransformer.express2NFA(pattern);

        LexUtils.displayLexCell(lexCell);

        System.out.println("-------------------------使用Echarts显示-------------------------------------");

        LexUtils.outputEchart(lexCell);
    }

    @Test
    public void testN2DTest(){
//        String pattern = "(a|b)*abb";
////        String pattern = "adv|bced";
////        String pattern = "(\\d*(\\.\\d+)?)";
////        String pattern = "([A-Z]+)|(\\d*(\\.\\d+)?)";
        String pattern = "abc|([A-Z]+)|(\\d+(\\.\\d+)?)";

        System.out.println("-------------------------originCell  NFA-------------------------------------");
        LexAutomatonTransformer.LexCell originCell = LexAutomatonTransformer.express2NFA(pattern);
        LexUtils.outputEchart(originCell);

        System.out.println("-------------------------NFA 2 DFA-------------------------------------");
        // FIXME 生成的DFA单元需要表示出接受态，方便DFA最小化使用
        LexAutomatonTransformer.LexDFACell lexCell = LexAutomatonTransformer.tranNFA2DFA(originCell);
        LexUtils.outputEchart(lexCell);

        System.out.println("-------------------------最小化 DFA 显示-------------------------------------");
        // FIXME DFA 最小化需要处理多个接受态的情况
        LexAutomatonTransformer.LexCell lexMinCell = LexAutomatonTransformer.minimizeDFA(lexCell);
        LexUtils.outputEchart(lexMinCell);

    }

    @Test
    public void express2DFA(){
        String pattern = "adv|bced";
//        String pattern = "(\\d*(\\.\\d+)?)";
//        String pattern = "([A-Z]+)|(\\d*(\\.\\d+)?)";

        // 转换成后缀表达式
        List<LexSimplePattern.Metacharacter> metas = LexSimplePattern.compile(pattern);
        List<LexSimplePattern.Metacharacter> postfixMetas = LexSimplePattern.postfix(metas);

        // 加上结尾符'\0'
        postfixMetas.add(EOF_META);

        // 先生成语法分析树
        LexAutomatonTransformer.LexDFANode root = buildLexDFANode(postfixMetas, new AtomicInteger(0));

        // 输出语法分析树结果
        LexUtils.print(root);

        // FIXME 正则表达式直接生成DFA还有问题，需要解决
        LexAutomatonTransformer.LexDFACell cell = LexAutomatonTransformer.buildDFA(root);
        LexUtils.outputEchart(cell);
    }

    @Test
    public void testLexParser(){
        List<Token> parseRs = LexicalParser.parser("C:\\Users\\Gian\\Desktop\\Temp\\compilerCode.txt");
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("");
        int line = 0;
        for(Token token : parseRs){
            if(token.getLine() > line){
                line = token.getLine();
                System.out.println("");
            }
            System.out.print(token.toString());
        }
        System.out.println("");
        System.out.println("---------------------------------------------------------------------------");
    }

    @Test
    public void patternTest2(){
        System.out.println("{".matches("\\{"));
    }

    @Test
    public void treeUtilTest(){
        BST<Integer, String> bst = new BST<Integer, String>();
        bst.insert(10, "a");
        bst.insert(12, "b");
        bst.insert(3, "d");
        bst.insert(9, "cdd");
        bst.insert(33, "cff");
        bst.insert(38, "ceee");
        bst.insert(1, "aaaa");
        bst.insert(0, "dddd");
        bst.insert(99, "dddd");
        bst.insert(100, "dddd");
        bst.insert(7, "dddd");
        bst.insert(1, "dddd");
        //从根开始打印
        TreePrintUtil.pirnt(bst.getRoot());

    }

}
