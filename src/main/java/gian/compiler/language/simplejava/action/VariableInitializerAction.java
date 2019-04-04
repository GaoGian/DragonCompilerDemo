package gian.compiler.language.simplejava.action;

import gian.compiler.front.lexical.parser.Token;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.ast.expression.NewArray;
import gian.compiler.language.simplejava.bean.VariableArrayType;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.ast.expression.Expr;
import gian.compiler.language.simplejava.utils.JavaDirectUtils;

/**
 * Created by gaojian on 2019/4/2.
 */
public class VariableInitializerAction {

    public static String product_1 = "variableInitializer → = expression";
    public static class VariableAssignListener extends SyntaxDirectedListener{
        public VariableAssignListener(){
            this.matchProductTag = product_1;
            this.matchSymbol = "expression";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Expr expr = (Expr) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, expr);

            return null;
        }
    }

    public static String product_2 = "= new arrayBaseType [ Digit ] arraySize";
    public static class ArrayVariableInitListener extends SyntaxDirectedListener{
        public ArrayVariableInitListener(){
            this.matchProductTag = product_1;
            this.matchSymbol = "arraySize";
            this.matchIndex = 5;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Token baseType = context.getBrotherNodeList().get(currentIndex - 4).getIdToken();
            currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).put(JavaConstants.VARIABLE_BASE_TYPE, baseType.getType().isRexgexToken() ? baseType.getType().getType() : baseType.getToken());

            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            String size = context.getBrotherNodeList().get(currentIndex - 2).getIdToken().getToken();
            VariableType variableType = (VariableType) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.VARIABLE_TYPE);
            VariableArrayType arrayType = new VariableArrayType(Integer.valueOf(size), variableType);

            NewArray newArray = JavaDirectUtils.newArray(variableType, arrayType);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, newArray);

            return null;
        }
    }

    public static String product_3 = "arraySize → [ Digit ] arraySize";
    public static class ArraySizeListener extends SyntaxDirectedListener{
        public ArraySizeListener(){
            this.matchProductTag = product_3;
            this.matchSymbol = "arraySize";
            this.matchIndex = 3;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            String baseTypeName = (String) context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).get(JavaConstants.VARIABLE_BASE_TYPE);
            currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).put(JavaConstants.VARIABLE_BASE_TYPE, baseTypeName);

            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            String size = context.getBrotherNodeList().get(currentIndex - 2).getIdToken().getToken();
            VariableType variableType = (VariableType) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.VARIABLE_TYPE);
            VariableArrayType arrayType = new VariableArrayType(Integer.valueOf(size), variableType);

            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.VARIABLE_TYPE, arrayType);

            return null;
        }
    }

    public static String product_4 = "arraySize → ε";
    public static class BaseTypeListener extends SyntaxDirectedListener{
        public BaseTypeListener(){
            this.matchProductTag = product_4;
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
            String baseTypeName = (String) context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).get(JavaConstants.VARIABLE_BASE_TYPE);
            VariableType baseType = VariableType.getVariableTypeWidth(baseTypeName);

            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.VARIABLE_TYPE, baseType);

            return null;
        }
    }

}