import gian.compiler.practice.lexical.transform.*;
import org.junit.Test;
import utils.BST;
import utils.TreePrintUtil;

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
        DFATransformer.regExp2Dfa(regular_expression);
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
