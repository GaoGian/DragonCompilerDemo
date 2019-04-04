package gian.compiler.language.simplejava.action;

import gian.compiler.front.lexical.parser.Token;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.ast.Constant;
import gian.compiler.language.simplejava.ast.expression.Expr;
import gian.compiler.language.simplejava.utils.JavaDirectUtils;

/**
 * Created by gaojian on 2019/4/2.
 */
public class ExpressionAction {

    // TODO
    public static String product_1 = "expression → identifierReference ( expressionList ) methodRefRest";
    public static class MethodCallListener extends SyntaxDirectedListener{
        public MethodCallListener(){
            // TODO
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }
    }

    public static String product_2 = "expression → new Identifier ( expressionList ) methodRefRest";
    public static class NewVariableListener extends SyntaxDirectedListener{
        public NewVariableListener(){
            // TODO
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }
    }

    public static String product_3 = "expression → expression + term";
    public static class AddListener extends SyntaxDirectedListener{
        public AddListener(){
            this.matchProductTag = product_3;
            this.matchSymbol = "term";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Expr expr = (Expr) context.getBrotherNodeList().get(currentIndex - 2).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            Expr term = (Expr) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

            Expr add = JavaDirectUtils.term(expr, term, JavaConstants.JAVA_OPERATOR_ADD);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, add);

            return null;
        }
    }

    public static String product_4 = "expression → expression - term";
    public static class ReduceListener extends SyntaxDirectedListener{
        public ReduceListener(){
            this.matchProductTag = product_4;
            this.matchSymbol = "term";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Expr expr = (Expr) context.getBrotherNodeList().get(currentIndex - 2).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            Expr term = (Expr) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

            Expr reduce = JavaDirectUtils.term(expr, term, JavaConstants.JAVA_OPERATOR_REDUCE);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, reduce);

            return null;
        }
    }

    public static String product_5 = "expression → term";
    public static class TermListener extends SyntaxDirectedListener{

        public TermListener(){
            this.matchProductTag = product_5;
            this.matchSymbol = "term";
            this.matchIndex = 0;
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

    public static String product_6 = "expression → Identifier ++";
    public static class VariableIncListener extends SyntaxDirectedListener{
        public VariableIncListener(){
            this.matchProductTag = product_6;
            this.matchSymbol = "++";
            this.matchIndex = 1;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            String variableName = context.getBrotherNodeList().get(currentIndex - 1).getIdToken().getToken();
            Variable variable = JavaDirectUtils.factor(variableName);

            Expr incExpr = JavaDirectUtils.term(variable, Constant.DIGIT_ONE, JavaConstants.JAVA_OPERATOR_ADD);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, incExpr);

            return null;
        }
    }

    public static String product_7 = "expression → Identifier --";
    public static class VariableDecListener extends SyntaxDirectedListener{
        public VariableDecListener(){
            this.matchProductTag = product_7;
            this.matchSymbol = "--";
            this.matchIndex = 1;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            String variableName = context.getBrotherNodeList().get(currentIndex - 1).getIdToken().getToken();
            Variable variable = JavaDirectUtils.factor(variableName);

            Expr decExpr = JavaDirectUtils.term(variable, Constant.DIGIT_ONE, JavaConstants.JAVA_OPERATOR_REDUCE);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, decExpr);

            return null;
        }
    }

    public static String product_8 = "expression → String";
    public static class StringListener extends SyntaxDirectedListener{

        public StringListener(){
            this.matchProductTag = product_8;
            this.matchSymbol = "String";
            this.matchIndex = 0;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Token token = currentTreeNode.getIdToken();
            Constant constant = JavaDirectUtils.constant(token);

            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, constant);

            return null;
        }
    }

    public static String product_9 = "expression → true";
    public static class FalseListener extends SyntaxDirectedListener{

        public FalseListener(){
            this.matchProductTag = product_9;
            this.matchSymbol = "true";
            this.matchIndex = 0;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            // 设置code
            currentTreeNode.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, Constant.True);

            return null;
        }
    }

    public static String product_10 = "expression → false";
    public static class TrueListener extends SyntaxDirectedListener{

        public TrueListener(){
            this.matchProductTag = product_10;
            this.matchSymbol = "false";
            this.matchIndex = 0;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            // 设置code
            currentTreeNode.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, Constant.False);

            return null;
        }
    }

    // TODO
    public static String product_11 = "expressionList → expression expressionListRest";
    public static String product_12 = "expressionList → ε";
    public static String product_13 = "expressionListRest → , expression expressionListRest";
    public static String product_14 = "expressionListRest → ε";

}