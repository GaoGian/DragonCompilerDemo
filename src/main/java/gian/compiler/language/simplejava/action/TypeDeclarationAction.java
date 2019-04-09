package gian.compiler.language.simplejava.action;

import gian.compiler.front.lexical.parser.Token;
import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.bean.VariableArrayType;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gian on 2019/3/30.
 */
public class TypeDeclarationAction {

    public static String product_1 = "typeDeclaration → boolean typeComponent";
    public static String product_2 = "typeDeclaration → char typeComponent";
    public static String product_3 = "typeDeclaration → byte typeComponent";
    public static String product_4 = "typeDeclaration → short typeComponent";
    public static String product_5 = "typeDeclaration → int typeComponent";
    public static String product_6 = "typeDeclaration → long typeComponent";
    public static String product_7 = "typeDeclaration → float typeComponent";
    public static String product_8 = "typeDeclaration → double typeComponent";
    public static String product_9 = "typeDeclaration → Identifier typeComponent";

    public static class BooleanTypeListener extends SyntaxDirectedListener{

        public BooleanTypeListener(){
            this.matchProductTag = product_1;
            this.matchSymbol = "typeComponent";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            setBaseType(context, currentTreeNode, currentIndex);
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return getVariableType(context, currentTreeNode, currentIndex);
        }
    }
    public static class CharTypeListener extends SyntaxDirectedListener{

        public CharTypeListener(){
            this.matchProductTag = product_2;
            this.matchSymbol = "typeComponent";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            setBaseType(context, currentTreeNode, currentIndex);
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return getVariableType(context, currentTreeNode, currentIndex);
        }
    }
    public static class ByteTypeListener extends SyntaxDirectedListener{

        public ByteTypeListener(){
            this.matchProductTag = product_3;
            this.matchSymbol = "typeComponent";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            setBaseType(context, currentTreeNode, currentIndex);
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return getVariableType(context, currentTreeNode, currentIndex);
        }
    }
    public static class ShortTypeListener extends SyntaxDirectedListener{

        public ShortTypeListener(){
            this.matchProductTag = product_4;
            this.matchSymbol = "typeComponent";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            setBaseType(context, currentTreeNode, currentIndex);
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return getVariableType(context, currentTreeNode, currentIndex);
        }
    }
    public static class IntTypeListener extends SyntaxDirectedListener{

        public IntTypeListener(){
            this.matchProductTag = product_5;
            this.matchSymbol = "typeComponent";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            setBaseType(context, currentTreeNode, currentIndex);
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return getVariableType(context, currentTreeNode, currentIndex);
        }
    }
    public static class LongTypeListener extends SyntaxDirectedListener{

        public LongTypeListener(){
            this.matchProductTag = product_6;
            this.matchSymbol = "typeComponent";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            setBaseType(context, currentTreeNode, currentIndex);
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return getVariableType(context, currentTreeNode, currentIndex);
        }
    }
    public static class FloatTypeListener extends SyntaxDirectedListener{

        public FloatTypeListener(){
            this.matchProductTag = product_7;
            this.matchSymbol = "typeComponent";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            setBaseType(context, currentTreeNode, currentIndex);
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return getVariableType(context, currentTreeNode, currentIndex);
        }
    }
    public static class DoubleTypeListener extends SyntaxDirectedListener{

        public DoubleTypeListener(){
            this.matchProductTag = product_8;
            this.matchSymbol = "typeComponent";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            setBaseType(context, currentTreeNode, currentIndex);
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return getVariableType(context, currentTreeNode, currentIndex);
        }
    }
    public static class ClassTypeListener extends SyntaxDirectedListener{

        public ClassTypeListener(){
            this.matchProductTag = product_9;
            this.matchSymbol = "typeComponent";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            setBaseType(context, currentTreeNode, currentIndex);
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return getVariableType(context, currentTreeNode, currentIndex);
        }
    }

    public static String product_11 = "typeComponent → [ ] typeComponent";
    public static class ArrayTypeComponentListener extends SyntaxDirectedListener{
        public ArrayTypeComponentListener(){
            this.matchProductTag = product_11;
            this.matchSymbol = "typeComponent";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Token baseType = (Token) context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).get(JavaConstants.VARIABLE_BASE_TYPE);
            currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).put(JavaConstants.VARIABLE_BASE_TYPE, baseType);

            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            // TODO 调整数组处理顺序，方便生成数组节点
            VariableType variableType = (VariableType) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.VARIABLE_TYPE);
            // FIXME 单独处理数组声明
            VariableArrayType arrayType = new VariableArrayType(0, variableType);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.VARIABLE_TYPE, arrayType);

            return null;
        }
    }

    public static String product_12 = "typeComponent → ε";
    public static class TypeComponentEmptyListener extends SyntaxDirectedListener{
        public TypeComponentEmptyListener(){
            this.matchProductTag = product_12;
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
            Token baseType = (Token) context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).get(JavaConstants.VARIABLE_BASE_TYPE);
            VariableType variableType = new VariableType(baseType.getToken(), VariableType.getVariableTypeWidth(baseType.getType().isRexgexToken() ? baseType.getType().getType() : baseType.getToken()));
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.VARIABLE_TYPE, variableType);

            return null;
        }
    }

    private static void setBaseType(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex){
        Token baseType = context.getBrotherNodeList().get(currentIndex - 1).getIdToken();
        currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).put(JavaConstants.VARIABLE_BASE_TYPE, baseType);
    }

    private static String getVariableType(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex){
        VariableType variableType = (VariableType) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.VARIABLE_TYPE);
        context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.VARIABLE_TYPE, variableType);

        return null;
    }

    public static List<SyntaxDirectedListener> getAllListener() {
        List<SyntaxDirectedListener> allListener = new ArrayList<>();
        allListener.add(new BooleanTypeListener());
        allListener.add(new CharTypeListener());
        allListener.add(new ByteTypeListener());
        allListener.add(new ShortTypeListener());
        allListener.add(new IntTypeListener());
        allListener.add(new LongTypeListener());
        allListener.add(new FloatTypeListener());
        allListener.add(new DoubleTypeListener());
        allListener.add(new ClassTypeListener());
        allListener.add(new ArrayTypeComponentListener());
        allListener.add(new TypeComponentEmptyListener());

        return allListener;
    }
}
