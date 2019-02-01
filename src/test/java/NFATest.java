import com.alibaba.fastjson.JSON;
import gian.compiler.practice.lexical.transform.*;
import gian.compiler.practice.lexical.transform.regex.LexAutomatonTransformer;
import gian.compiler.practice.lexical.transform.regex.LexMatcher;
import gian.compiler.practice.lexical.transform.regex.LexPattern;
import gian.compiler.practice.lexical.transform.regex.LexSimplePattern;
import org.junit.Test;
import utils.BST;
import utils.TreePrintUtil;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Gian on 2019/1/27.
 */
public class NFATest {

    @Test
    public void test(){

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
//        String pattern = "([A-Z]+)|(\\d+(\\.\\d+)?)";
        String pattern = "(\\d+(\\.\\d+)?)";
        LexAutomatonTransformer.LexCell lexCell = LexAutomatonTransformer.express2NFA(pattern);

        System.out.println("DFA 的边数：" + lexCell.getEdgeSet().size());
        System.out.println("DFA 的起始状态：" + lexCell.getStartState().getStateName());
        System.out.println("DFA 的结束状态：" + lexCell.getEndState().getStateName());

        int i=0;
        for(LexAutomatonTransformer.LexEdge edge : lexCell.getEdgeSet()){
            System.out.println("第 " + i + 1 + " 条边的起始状态：" + edge.getStartState().getStateName() +
                    "，结束状态：" + edge.getEndState().getStateName() +
                    "，转换符：" + edge.getTranPattern().getMeta());
            i++;
        }

        System.out.println("结束");
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
