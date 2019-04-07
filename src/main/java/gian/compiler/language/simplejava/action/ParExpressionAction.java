package gian.compiler.language.simplejava.action;

import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.ast.expression.Expr;
import gian.compiler.language.simplejava.utils.JavaDirectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/4/2.
 */
public class ParExpressionAction {

    public static String product_1 = "parExpression → expressionTermRest || expressionTermRest";
    public static class OrExprListener extends SyntaxDirectedListener{
        public OrExprListener(){
            this.matchProductTag = product_1;
            this.matchSymbol = "expressionTermRest";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Expr expr1 = (Expr) context.getBrotherNodeList().get(currentIndex - 1).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            String operate = context.getBrotherNodeList().get(currentIndex - 1).getIdToken().getToken();
            Expr expr2 = (Expr) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

            Expr orExpr = JavaDirectUtils.or(expr1, expr2);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, orExpr);

            return null;
        }
    }

    public static String product_2 = "parExpression → expressionTermRest";
    public static class OrParExprListener extends SyntaxDirectedListener{
        public OrParExprListener(){
            this.matchProductTag = product_2;
            this.matchSymbol = "expressionTermRest";
            this.matchIndex = 0;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Expr parExpr = (Expr) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, parExpr);

            return null;
        }
    }

    public static String product_3 = "expressionTermRest → expressionTerm && expressionTerm";
    public static class AndExprListener extends SyntaxDirectedListener{
        public AndExprListener(){
            this.matchProductTag = product_3;
            this.matchSymbol = "expressionTerm";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Expr expr1 = (Expr) context.getBrotherNodeList().get(currentIndex - 1).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            String operate = context.getBrotherNodeList().get(currentIndex - 1).getIdToken().getToken();
            Expr expr2 = (Expr) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

            Expr orExpr = JavaDirectUtils.and(expr1, expr2);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, orExpr);

            return null;
        }
    }

    public static String product_4 = "expressionTermRest → expressionTerm";
    public static class AndExprRestListener extends SyntaxDirectedListener{

        public AndExprRestListener(){
            this.matchProductTag = product_4;
            this.matchSymbol = "expressionTerm";
            this.matchIndex = 0;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Expr parExpr = (Expr) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, parExpr);

            return null;
        }
    }

    public static String product_5 = "expressionTerm → expressionFactor < expressionFactor";
    public static class LtExprListener extends SyntaxDirectedListener{
        public LtExprListener(){
            this.matchProductTag = product_5;
            this.matchSymbol = "expressionFactor";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            setRelExpr(context, currentTreeNode, currentIndex);
            return null;
        }
    }

    public static String product_6 = "expressionTerm → expressionFactor <= expressionFactor";
    public static class LeExprListener extends SyntaxDirectedListener{
        public LeExprListener(){
            this.matchProductTag = product_6;
            this.matchSymbol = "expressionFactor";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            setRelExpr(context, currentTreeNode, currentIndex);
            return null;
        }
    }

    public static String product_7 = "expressionTerm → expressionFactor > expressionFactor";
    public static class GtExprListener extends SyntaxDirectedListener{
        public GtExprListener(){
            this.matchProductTag = product_7;
            this.matchSymbol = "expressionFactor";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            setRelExpr(context, currentTreeNode, currentIndex);
            return null;
        }
    }

    public static String product_8 = "expressionTerm → expressionFactor >= expressionFactor";
    public static class GeExprListener extends SyntaxDirectedListener{
        public GeExprListener(){
            this.matchProductTag = product_8;
            this.matchSymbol = "expressionFactor";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            setRelExpr(context, currentTreeNode, currentIndex);
            return null;
        }
    }

    public static String product_9 = "expressionTerm → expressionFactor";
    public static class RelExprListener extends SyntaxDirectedListener{
        public RelExprListener(){
            this.matchProductTag = product_9;
            this.matchSymbol = "expressionFactor";
            this.matchIndex = 0;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Expr parExpr = (Expr) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, parExpr);

            return null;
        }
    }

    public static String product_10_1 = "expressionFactor → expression == expression";
    public static class EqListener extends SyntaxDirectedListener{
        public EqListener(){
            this.matchProductTag = product_10_1;
            this.matchSymbol = "expression";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            setRelExpr(context, currentTreeNode, currentIndex);
            return null;
        }
    }

    public static String product_10_2 = "expressionFactor → expression != expression";
    public static class NeListener extends SyntaxDirectedListener{
        public NeListener(){
            this.matchProductTag = product_10_2;
            this.matchSymbol = "expression";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            setRelExpr(context, currentTreeNode, currentIndex);
            return null;
        }
    }

    public static String product_10_3 = "expressionFactor → expression";
    public static class ExprFactorListener extends SyntaxDirectedListener{
        public ExprFactorListener(){
            this.matchProductTag = product_10_3;
            this.matchSymbol = "expression";
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

    public static void setRelExpr(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex){
        Expr expr1 = (Expr) context.getBrotherNodeList().get(currentIndex - 1).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
        String operate = context.getBrotherNodeList().get(currentIndex - 1).getIdToken().getToken();
        Expr expr2 = (Expr) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

        Expr orExpr = JavaDirectUtils.rel(expr1, expr2, operate);
        context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, orExpr);
    }


    public static List<SyntaxDirectedListener> getAllListener() {
        List<SyntaxDirectedListener> allListener = new ArrayList<>();
        allListener.add(new OrExprListener());
        allListener.add(new OrParExprListener());
        allListener.add(new AndExprListener());
        allListener.add(new AndExprRestListener());
        allListener.add(new LtExprListener());
        allListener.add(new LeExprListener());
        allListener.add(new GtExprListener());
        allListener.add(new GeExprListener());
        allListener.add(new RelExprListener());
        allListener.add(new EqListener());
        allListener.add(new NeListener());
        allListener.add(new ExprFactorListener());

        return allListener;
    }

}