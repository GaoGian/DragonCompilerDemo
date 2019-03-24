import gian.compiler.practice.lexical.parser.LexExpression;
import gian.compiler.practice.lexical.parser.Token;
import gian.compiler.practice.lexical.transform.LexConstants;
import gian.compiler.practice.syntactic.SyntacticLLParser;
import gian.compiler.practice.syntactic.SyntacticLRParser;
import gian.compiler.practice.syntactic.element.ItemCollection;
import gian.compiler.practice.syntactic.element.SyntaxSymbol;
import gian.compiler.practice.syntactic.element.SyntaxTree;
import gian.compiler.practice.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.practice.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.practice.syntaxDirected.SyntaxDirectedParser;
import lex.test.LexUtils;
import org.junit.Test;

import java.util.*;

/**
 * Created by gaojian on 2019/3/20.
 */
public class SyntaxDirectedTest {

    // 匹配语法分析树
    public static LexUtils.UniversalTreeNode.UniversalTreeNodeMatcher getDirectTreeMatcher(){
        return new LexUtils.UniversalTreeNode.UniversalTreeNodeMatcher<SyntaxTree.SyntaxTreeNode>(){
            @Override
            public List<LexUtils.UniversalTreeNode> getChildTreeNode(SyntaxTree.SyntaxTreeNode targetNode) {
                List<LexUtils.UniversalTreeNode> matchSubTreeNodeList = new ArrayList<>();
                for(SyntaxTree.SyntaxTreeNode childNode : targetNode.getSubProductNodeList()){
                    matchSubTreeNodeList.add(new LexUtils.UniversalTreeNode(childNode, this, true));
                }
                return matchSubTreeNodeList;
            }
        };
    }

    // 匹配注释语法分析树
    public static LexUtils.UniversalTreeNode.UniversalTreeNodeMatcher getDirectActionTreeMatcher(Map<Integer, SyntaxDirectedListener> syntaxDirectActionMap){
        return new LexUtils.UniversalTreeNode.UniversalTreeNodeMatcher<SyntaxTree.SyntaxTreeNode>(){
            @Override
            public List<LexUtils.UniversalTreeNode> getChildTreeNode(SyntaxTree.SyntaxTreeNode targetNode) {
                List<LexUtils.UniversalTreeNode> matchSubTreeNodeList = new ArrayList<>();
                for(int i=0; i<targetNode.getSubProductNodeList().size(); i++){
                    SyntaxTree.SyntaxTreeNode childNode = targetNode.getSubProductNodeList().get(i);
                    SyntaxDirectedListener childDirectAction = syntaxDirectActionMap.get(childNode.getNumber());
                    if(childDirectAction != null){
                        // 加入预处理语义动作节点
                        SyntaxTree.SyntaxTreeNode enterDirectActionNode = new SyntaxTree.SyntaxTreeNode(targetNode.getNumber()*10+i, true, new SyntaxSymbol(childDirectAction.enterSyntaxSymbol(null), true));
                        matchSubTreeNodeList.add(new LexUtils.UniversalTreeNode(enterDirectActionNode, this, true) );
                    }
                    matchSubTreeNodeList.add(new LexUtils.UniversalTreeNode(childNode, this, true));
                    if(childDirectAction != null){
                        // 加入综合记录语义动作几点
                        SyntaxTree.SyntaxTreeNode exitDirectActionNode = new SyntaxTree.SyntaxTreeNode(targetNode.getNumber()*100+i, true, new SyntaxSymbol(childDirectAction.exitSyntaxSymbol(null), true));
                        matchSubTreeNodeList.add(new LexUtils.UniversalTreeNode(exitDirectActionNode, this, true) );
                    }
                }
                return matchSubTreeNodeList;
            }
        };
    }

    @Test
    public void testSyntaxTree(){
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("id", LexExpression.TokenType.ID));
        tokens.add(new Token("*", LexExpression.TokenType.OPERATOR));
        tokens.add(new Token("id", LexExpression.TokenType.ID));
        tokens.add(new Token("+", LexExpression.TokenType.OPERATOR));
        tokens.add(new Token("id", LexExpression.TokenType.ID));
        tokens.add(new Token(LexConstants.SYNTAX_END, LexExpression.TokenType.END));

        List<String> syntaxs = new ArrayList<>();
        syntaxs.add("E → E + T | T ");
        syntaxs.add("T → T * F | F ");
        syntaxs.add("F → ( E ) | id ");

        System.out.println("------------------------------SyntaxTree----------------------------------");
        SyntaxTree syntaxTree = SyntacticLRParser.syntaxParseLR(syntaxs, tokens);
        LexUtils.outputUniversalTreeEchart(new LexUtils.UniversalTreeNode(syntaxTree.getSyntaxTreeRoot(), getDirectTreeMatcher(), true));

    }

    @Test
    public void testSyntaxTree1(){
        System.out.println("------------------------------SyntaxTree----------------------------------");
        SyntaxTree syntaxTree = SyntacticLRParser.syntaxParseLR("syntaxContentFile.txt", "compilerCode.txt", LexExpression.expressions, true);
        LexUtils.outputUniversalTreeEchart(new LexUtils.UniversalTreeNode(syntaxTree.getSyntaxTreeRoot(), getDirectTreeMatcher(), true), 300, 500);
    }

    @Test
    public void testSyntaxDirectActionTree(){
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("id", LexExpression.TokenType.ID));
        tokens.add(new Token("*", LexExpression.TokenType.OPERATOR));
        tokens.add(new Token("id", LexExpression.TokenType.ID));
        tokens.add(new Token("+", LexExpression.TokenType.OPERATOR));
        tokens.add(new Token("id", LexExpression.TokenType.ID));
        tokens.add(new Token(LexConstants.SYNTAX_END, LexExpression.TokenType.END));

        List<String> syntaxs = new ArrayList<>();
        syntaxs.add("L → E");
        syntaxs.add("E → E + T | T ");
        syntaxs.add("T → T * F | F ");
        syntaxs.add("F → ( E ) | id ");

        System.out.println("------------------------------SyntaxTree----------------------------------");
        SyntaxTree syntaxTree = SyntacticLRParser.syntaxParseLR(syntaxs, tokens);

        System.out.println("------------------------------SyntaxTree DirectAction----------------------------------");
        SyntaxDirectedListener Er_to_E_0_Listener = new SyntaxDirectedListener("L → E", 0, "E", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context) {
                System.out.println("expression: L → ◀E▶");

                String code = "";
                return code;
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context) {
                System.out.println("action: print(E.value)");

                String code = "print(E.value)";
                return code;
            }
        };
        SyntaxDirectedListener E_to_E_add_T_0_Listener = new SyntaxDirectedListener("E → E + T", 0, "E", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context) {
                System.out.println("expression: E → ◀E▶ + T");
                System.out.println("action: E_0.inh = E.syn");

                String code = "E_0.inh = E.syn";
                return code;
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context) {
                System.out.println("action: ");

                String code = "";
                return code;
            }
        };
        SyntaxDirectedListener E_to_E_add_T_2_Listener = new SyntaxDirectedListener("E → E + T", 2, "T", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context) {
                System.out.println("expression: E → E + ◀T▶");
                // TODO 改造成根据兄弟节点相对位置赋值，或者直接根据节点父节点、兄弟节点相对位置进行赋值
                System.out.println("action: T.inh = E_0.syn");

                String code = "T.inh = E_0.syn";
                return code;
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context) {
                System.out.println("action: E.value = E_0.value + T.value");

                String code = "E.value = E_0.value + T.value";
                return code;
            }
        };
        SyntaxDirectedListener E_to_T_Listener = new SyntaxDirectedListener("E → T", 0, "T", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context) {
                System.out.println("expression: E → ◀T▶");
                System.out.println("action: T.inh = E.syn");

                String code = "T.inh = E.syn";
                return code;
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context) {
                System.out.println("action: E.value = T.value");

                String code = "E.value = T.value";
                return code;
            }
        };
        SyntaxDirectedListener T_to_T_multi_F_0_Listener = new SyntaxDirectedListener("T → T * F", 0, "T", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context) {
                System.out.println("expression: T → ◀T▶ * F");
                System.out.println("action: T_0.inh = T.syn");

                String code = "T_0.inh = T.syn";
                return code;
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context) {
                System.out.println("action:");

                return "";
            }
        };
        SyntaxDirectedListener T_to_T_multi_F_2_Listener = new SyntaxDirectedListener("T → T * F", 2, "F", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context) {
                System.out.println("expression: T → T * ◀F▶");
                System.out.println("action: F.inh = T_0.syn");

                return "F.inh = T_0.syn";
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context) {
                System.out.println("action: T.value = T_0.value * F.value");

                return "T.value = T_0.value * F.value";
            }
        };
        SyntaxDirectedListener T_to_F_Listener = new SyntaxDirectedListener("T → F", 0, "F", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context) {
                System.out.println("expression: T → ◀F▶");
                System.out.println("action: F.inh = T.syn");

                return "F.inh = T.syn";
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context) {
                System.out.println("action: T.value = F.value");

                return "T.value = F.value";
            }
        };
        SyntaxDirectedListener F_to_r_E_r_Listener = new SyntaxDirectedListener("F → ( E )", 1, "E", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context) {
                System.out.println("expression: F → ( ◀E▶ )");
                System.out.println("action: E.inh = F.syn");

                return "E.inh = F.syn";
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context) {
                System.out.println("action: F.value = E.value");

                return "F.value = E.value";
            }
        };
        SyntaxDirectedListener F_to_id_Listener = new SyntaxDirectedListener("F → id", 0, "id", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context) {
                System.out.println("expression: F → ◀id▶");

                return "";
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context) {
                System.out.println("action: F.value = id.lexval");

                return "F.value = id.lexval";
            }
        };

        List<SyntaxDirectedListener> syntaxDirectedListenerList = new ArrayList<>();
        syntaxDirectedListenerList.add(Er_to_E_0_Listener);
        syntaxDirectedListenerList.add(E_to_E_add_T_0_Listener);
        syntaxDirectedListenerList.add(E_to_E_add_T_2_Listener);
        syntaxDirectedListenerList.add(E_to_T_Listener);
        syntaxDirectedListenerList.add(T_to_T_multi_F_0_Listener);
        syntaxDirectedListenerList.add(T_to_T_multi_F_2_Listener);
        syntaxDirectedListenerList.add(T_to_F_Listener);
        syntaxDirectedListenerList.add(F_to_r_E_r_Listener);
        syntaxDirectedListenerList.add(F_to_id_Listener);

        System.out.println("------------------------------SyntaxDirectActionTree----------------------------------");
        Map<Integer, SyntaxDirectedListener> syntaxDirectActionMap = SyntaxDirectedParser.matchSyntaxTreeNodeDirectAction(syntaxTree.getSyntaxTreeRoot(), 0, syntaxDirectedListenerList, new HashMap<>());
        LexUtils.outputUniversalTreeEchart(new LexUtils.UniversalTreeNode(syntaxTree.getSyntaxTreeRoot(), getDirectActionTreeMatcher(syntaxDirectActionMap), true));

        System.out.println("------------------------------SyntaxDirectTree Parse----------------------------------");
        SyntaxDirectedParser.syntaxDirectedParser(syntaxTree, syntaxDirectedListenerList);

    }

    @Test
    public void testSyntaxDirect(){



    }


}