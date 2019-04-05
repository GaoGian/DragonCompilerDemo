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
            this.matchProductTag = product_2;
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

    public static String product_12 = "arrayBaseType → boolean | char | byte | short | int | long | float | double | Identifier";
    public static class BooleanArrayListener extends SyntaxDirectedListener{
        public BooleanArrayListener(){
            this.matchProductTag = product_12;
            this.matchSymbol = "boolean";
            this.matchIndex = 0;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.VARIABLE_BASE_TYPE, currentTreeNode.getIdToken().getToken());
            return null;
        }
    }
    public static String product_13 = "arrayBaseType → char";
    public static class CharArrayListener extends SyntaxDirectedListener{
        public CharArrayListener(){
            this.matchProductTag = product_13;
            this.matchSymbol = "char";
            this.matchIndex = 0;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.VARIABLE_BASE_TYPE, currentTreeNode.getIdToken().getToken());
            return null;
        }
    }
    public static String product_14 = "arrayBaseType → byte";
    public static class ByteArrayListener extends SyntaxDirectedListener{
        public ByteArrayListener(){
            this.matchProductTag = product_14;
            this.matchSymbol = "byte";
            this.matchIndex = 0;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.VARIABLE_BASE_TYPE, currentTreeNode.getIdToken().getToken());
            return null;
        }
    }
    public static String product_15 = "arrayBaseType → short";
    public static class ShortArrayListener extends SyntaxDirectedListener{
        public ShortArrayListener(){
            this.matchProductTag = product_15;
            this.matchSymbol = "short";
            this.matchIndex = 0;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.VARIABLE_BASE_TYPE, currentTreeNode.getIdToken().getToken());
            return null;
        }
    }
    public static String product_16 = "arrayBaseType → int";
    public static class IntArrayListener extends SyntaxDirectedListener{
        public IntArrayListener(){
            this.matchProductTag = product_16;
            this.matchSymbol = "int";
            this.matchIndex = 0;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.VARIABLE_BASE_TYPE, currentTreeNode.getIdToken().getToken());
            return null;
        }
    }
    public static String product_17 = "arrayBaseType → long";
    public static class LongArrayListener extends SyntaxDirectedListener{
        public LongArrayListener(){
            this.matchProductTag = product_17;
            this.matchSymbol = "int";
            this.matchIndex = 0;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.VARIABLE_BASE_TYPE, currentTreeNode.getIdToken().getToken());
            return null;
        }
    }
    public static String product_18 = "arrayBaseType → float";
    public static class FloatArrayListener extends SyntaxDirectedListener{
        public FloatArrayListener(){
            this.matchProductTag = product_18;
            this.matchSymbol = "float";
            this.matchIndex = 0;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.VARIABLE_BASE_TYPE, currentTreeNode.getIdToken().getToken());
            return null;
        }
    }
    public static String product_19 = "arrayBaseType → double";
    public static class DoubleArrayListener extends SyntaxDirectedListener{
        public DoubleArrayListener(){
            this.matchProductTag = product_19;
            this.matchSymbol = "double";
            this.matchIndex = 0;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.VARIABLE_BASE_TYPE, currentTreeNode.getIdToken().getToken());
            return null;
        }
    }
    public static String product_20 = "arrayBaseType → Identifier";
    public static class ClazzArrayListener extends SyntaxDirectedListener{
        public ClazzArrayListener(){
            this.matchProductTag = product_20;
            this.matchSymbol = "Identifier";
            this.matchIndex = 0;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.VARIABLE_BASE_TYPE, currentTreeNode.getIdToken().getToken());
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
            VariableType baseType = new VariableType(baseTypeName, VariableType.getVariableTypeWidth(baseTypeName));

            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.VARIABLE_TYPE, baseType);

            return null;
        }
    }

}