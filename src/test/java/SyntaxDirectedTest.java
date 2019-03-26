import gian.compiler.front.lexical.parser.LexExpression;
import gian.compiler.front.lexical.parser.Token;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.SyntacticLRParser;
import gian.compiler.front.syntactic.element.SyntaxSymbol;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.front.syntaxDirected.SyntaxDirectedParser;
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
                        SyntaxTree.SyntaxTreeNode enterDirectActionNode = new SyntaxTree.SyntaxTreeNode(targetNode.getNumber()*100+i, true, new SyntaxSymbol(childDirectAction.enterSyntaxSymbol(null, null, null), true));
                        matchSubTreeNodeList.add(new LexUtils.UniversalTreeNode(enterDirectActionNode, this, true) );
                    }
                    matchSubTreeNodeList.add(new LexUtils.UniversalTreeNode(childNode, this, true));
                    if(childDirectAction != null){
                        // 加入综合记录语义动作节点
                        SyntaxTree.SyntaxTreeNode exitDirectActionNode = new SyntaxTree.SyntaxTreeNode(targetNode.getNumber()*1000+i, true, new SyntaxSymbol(childDirectAction.exitSyntaxSymbol(null, null, null), true));
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
        tokens.add(new Token("+", LexExpression.TokenType.OPERATOR));
        tokens.add(new Token("id", LexExpression.TokenType.ID));
        tokens.add(new Token("*", LexExpression.TokenType.OPERATOR));
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
            public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("expression: L → ◀E▶");

                String code = "";
                return code;
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("action: print(E.value)");

                String code = "print(E.value)";
                return code;
            }
        };
        SyntaxDirectedListener E_to_E_add_T_0_Listener = new SyntaxDirectedListener("E → E + T", 0, "E", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("expression: E → ◀E▶ + T");
                System.out.println("action: E_0.inh = E.syn");

                String code = "E_0.inh = E.syn";
                return code;
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("action: ");

                String code = "";
                return code;
            }
        };
        SyntaxDirectedListener E_to_E_add_T_2_Listener = new SyntaxDirectedListener("E → E + T", 2, "T", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("expression: E → E + ◀T▶");
                // TODO 改造成根据兄弟节点相对位置赋值，或者直接根据节点父节点、兄弟节点相对位置进行赋值
                System.out.println("action: T.inh = E_0.syn");

                String code = "T.inh = E_0.syn";
                return code;
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("action: E.value = E_0.value + T.value");

                String code = "E.value = E_0.value + T.value";
                return code;
            }
        };
        SyntaxDirectedListener E_to_T_Listener = new SyntaxDirectedListener("E → T", 0, "T", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("expression: E → ◀T▶");
                System.out.println("action: T.inh = E.syn");

                String code = "T.inh = E.syn";
                return code;
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("action: E.value = T.value");

                String code = "E.value = T.value";
                return code;
            }
        };
        SyntaxDirectedListener T_to_T_multi_F_0_Listener = new SyntaxDirectedListener("T → T * F", 0, "T", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("expression: T → ◀T▶ * F");
                System.out.println("action: T_0.inh = T.syn");

                String code = "T_0.inh = T.syn";
                return code;
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("action:");

                return "";
            }
        };
        SyntaxDirectedListener T_to_T_multi_F_2_Listener = new SyntaxDirectedListener("T → T * F", 2, "F", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("expression: T → T * ◀F▶");
                System.out.println("action: F.inh = T_0.syn");

                return "F.inh = T_0.syn";
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("action: T.value = T_0.value * F.value");

                return "T.value = T_0.value * F.value";
            }
        };
        SyntaxDirectedListener T_to_F_Listener = new SyntaxDirectedListener("T → F", 0, "F", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("expression: T → ◀F▶");
                System.out.println("action: F.inh = T.syn");

                return "F.inh = T.syn";
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("action: T.value = F.value");

                return "T.value = F.value";
            }
        };
        SyntaxDirectedListener F_to_r_E_r_Listener = new SyntaxDirectedListener("F → ( E )", 1, "E", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("expression: F → ( ◀E▶ )");
                System.out.println("action: E.inh = F.syn");

                return "E.inh = F.syn";
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("action: F.value = E.value");

                return "F.value = E.value";
            }
        };
        SyntaxDirectedListener F_to_id_Listener = new SyntaxDirectedListener("F → id", 0, "id", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("expression: F → ◀id▶");

                return "";
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
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

        System.out.println("------------------------------SyntaxDirectTree Parse----------------------------------");
        SyntaxTree annotionSyntaxTree = SyntaxDirectedParser.syntaxDirectedParser(syntaxTree, syntaxDirectedListenerList);

        System.out.println("------------------------------SyntaxDirectActionTree----------------------------------");
        LexUtils.outputUniversalTreeEchart(new LexUtils.UniversalTreeNode(annotionSyntaxTree.getSyntaxTreeRoot(), getDirectTreeMatcher(), true));

    }

    @Test
    public void testSyntaxDirectType(){
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("int", LexExpression.TokenType.TYPE));
        tokens.add(new Token("[", LexExpression.TokenType.SEPARATOR));
        tokens.add(new Token("2", LexExpression.TokenType.DIGIT));
        tokens.add(new Token("]", LexExpression.TokenType.SEPARATOR));
        tokens.add(new Token("[", LexExpression.TokenType.SEPARATOR));
        tokens.add(new Token("3", LexExpression.TokenType.DIGIT));
        tokens.add(new Token("]", LexExpression.TokenType.SEPARATOR));
        tokens.add(new Token(LexConstants.SYNTAX_END, LexExpression.TokenType.END));

        List<String> syntaxs = new ArrayList<>();
        syntaxs.add("L → T");
        syntaxs.add("T → B C");
        syntaxs.add("B → int");
        syntaxs.add("B → float");
        syntaxs.add("C → [ digit ] C | ε");

        System.out.println("------------------------------SyntaxTree----------------------------------");
        SyntaxTree syntaxTree = SyntacticLRParser.syntaxParseLR(syntaxs, tokens);
        LexUtils.outputUniversalTreeEchart(new LexUtils.UniversalTreeNode(syntaxTree.getSyntaxTreeRoot(), getDirectTreeMatcher(), true), 300, 500);

        System.out.println("------------------------------SyntaxTree DirectAction----------------------------------");
        SyntaxDirectedListener L_to_T_0_Listener = new SyntaxDirectedListener("L → T", 0, "T", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("expression: L → ◀T▶");

                String code = "";
                return code;
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                String type = currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get("type").toString();
                System.out.println("action: T.t = " + type);

                String code = "T.t = " + type;
                return code;
            }
        };
        SyntaxDirectedListener T_to_B_C_0_Listener = new SyntaxDirectedListener("T → B C", 0, "B", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("expression: T → ◀B▶ C");

                String code = "";
                return code;
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("action: ");

                String code = "";
                return code;
            }
        };
        SyntaxDirectedListener T_to_B_C_1_Listener = new SyntaxDirectedListener("T → B C", 1, "C", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("expression: T → B ◀C▶");

                // 获取前一兄弟节点的继承属性：基础类型
                String baseType = context.getBrotherNodeList().get(currentIndex-1).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get("type").toString();
                currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).put("baseType", baseType);

                System.out.println("action: C.baseType = " + baseType);

                String code = "C.baseType = " + baseType;
                return code;
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                // 将最后的综合属性：类型表达式传递给父节点
                String type = currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get("type").toString();
                context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put("type", type);

                System.out.println("action: T.type = C.type");

                String code = "T.type = C.type";
                return code;
            }
        };
        SyntaxDirectedListener B_to_int_Listener = new SyntaxDirectedListener("B → int", 0, "int", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("expression: B → ◀int▶");

                String code = "";
                return code;
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                String type = context.getCurrentNode().getSyntaxSymbol().getSymbol();
                context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put("type", type);

                System.out.println("action: B.type = " + type);

                String code = "B.type = " + type;
                return code;
            }
        };
        SyntaxDirectedListener B_to_float_Listener = new SyntaxDirectedListener("B → float", 0, "float", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("expression: B → ◀float▶");

                String code = "";
                return code;
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                String type = currentTreeNode.getSyntaxSymbol().getSymbol();
                context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put("type", type);

                System.out.println("action: B.type = " + type);

                String code = "B.type = " + type;
                return code;
            }
        };
        SyntaxDirectedListener C_to_b_digit_b_C_3_Listener = new SyntaxDirectedListener("C → [ digit ] C", 3, "C", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("expression: C → [ digit ] ◀C▶");

                String baseType = context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).get("baseType").toString();
                currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).put("baseType", baseType);

                System.out.println("action: C_3.baseType = C.baseType");

                String code = "C_3.baseType = C.baseType";
                return code;
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                String number = context.getBrotherNodeList().get(currentIndex-2).getIdToken().getToken();
                String type = currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get("type").toString();

                currentTreeNode.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put("type", "array(" + number + ", " + type + ")");

                System.out.println("action: C.type = array(" + number + ", " + type + ")");

                String code = "C.type = array(" + number + ", " + type + ")";

                return code;
            }
        };
        SyntaxDirectedListener C_to_epsilon_Listener = new SyntaxDirectedListener("C → ε", 0, "ε", false) {
            @Override
            public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                System.out.println("expression: C → ◀ε▶");

                String code = "";
                return code;
            }
            @Override
            public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
                String type = context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).get("baseType").toString();
                context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put("type", type);

                System.out.println("action: C.type = C.baseType");

                String code = "C.type = C.baseType";
                return code;
            }
        };

        List<SyntaxDirectedListener> syntaxDirectedListenerList = new ArrayList<>();
        syntaxDirectedListenerList.add(L_to_T_0_Listener);
        syntaxDirectedListenerList.add(T_to_B_C_0_Listener);
        syntaxDirectedListenerList.add(T_to_B_C_1_Listener);
        syntaxDirectedListenerList.add(B_to_int_Listener);
        syntaxDirectedListenerList.add(B_to_float_Listener);
        syntaxDirectedListenerList.add(C_to_b_digit_b_C_3_Listener);
        syntaxDirectedListenerList.add(C_to_epsilon_Listener);

        System.out.println("------------------------------SyntaxDirectTree Parse----------------------------------");
        SyntaxTree annotionSyntaxTree = SyntaxDirectedParser.syntaxDirectedParser(syntaxTree, syntaxDirectedListenerList);

        System.out.println("------------------------------SyntaxDirectActionTree----------------------------------");
        LexUtils.outputUniversalTreeEchart(new LexUtils.UniversalTreeNode(annotionSyntaxTree.getSyntaxTreeRoot(), getDirectTreeMatcher(), true));

    }


}