package gian.compiler.language.simplejava.action;

import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.bean.VariableArrayType;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;

/**
 * Created by Gian on 2019/3/30.
 */
public class TypeComponentAction {

    public static String product_1 = "typeComponent → ◀[▶ ◀]▶ typeComponent | ε";

    public static class ArrayTypeComponentListener extends SyntaxDirectedListener{

        public ArrayTypeComponentListener(){
            this.matchProductTag = product_1;
            this.matchSymbol = "typeComponent";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            String baseType = (String) context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).get(JavaConstants.VARIABLE_BASE_TYPE);
            currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).put(JavaConstants.VARIABLE_BASE_TYPE, baseType);

            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            // TODO 调整数组处理顺序，方便生成数组节点
            String baseType = (String) context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).get(JavaConstants.VARIABLE_BASE_TYPE);
            VariableType variableType = (VariableType) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.VARIABLE_TYPE);
            VariableArrayType arrayType = new VariableArrayType(0, variableType);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.VARIABLE_TYPE, arrayType);

            return null;
        }
    }

    public static String product_2 = "typeComponent → ε";

    public static class EpsilonListener extends SyntaxDirectedListener{

        public EpsilonListener(){
            this.matchProductTag = product_2;
            this.matchSymbol = "ε";
            this.matchIndex = 0;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            String baseType = (String) context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).get(JavaConstants.VARIABLE_BASE_TYPE);
            VariableType variableType = VariableType.getVariableTypeMap(baseType);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.VARIABLE_TYPE, variableType);

            return null;
        }
    }

}
