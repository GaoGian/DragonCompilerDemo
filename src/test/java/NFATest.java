import gian.compiler.practice.lexical.transform.Cell;
import gian.compiler.practice.lexical.transform.DtranEdge;
import gian.compiler.practice.lexical.transform.DtranState;
import gian.compiler.practice.lexical.transform.NFATransformer;
import gian.compiler.utils.BST;
import gian.compiler.utils.TreePrintUtil;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Gian on 2019/1/27.
 */
public class NFATest {

    @Test
    public void test(){
        String regular_expression = "(a|b)*abb";
        // 正则表达式生成
        Cell nfa = NFATransformer.transform(regular_expression);

        // 使用子集构造法构造DFA
        Set<DtranState> dtranStateSet = new HashSet<>();
        Set<DtranEdge> dtranEdgeSet = new HashSet<>();
        DtranState startDtranState = new DtranState(nfa.startState, nfa.edgeSet);
        dtranStateSet.add(startDtranState);

        // TODO 确定输入符
        char[] inputs = new char[]{'a', 'b'};
        for(char input : inputs){

        }

    }

    @Test
    public void treeUtilTest(){
        BST<Integer, String> bst = new BST<>();
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
