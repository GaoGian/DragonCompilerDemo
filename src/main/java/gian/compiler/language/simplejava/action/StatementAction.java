package gian.compiler.language.simplejava.action;

import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.env.JavaDirectGlobalProperty;
import gian.compiler.language.simplejava.ast.expression.Expr;
import gian.compiler.language.simplejava.ast.statement.*;
import gian.compiler.language.simplejava.utils.JavaDirectUtils;

/**
 * Created by gaojian on 2019/4/2.
 */
public class StatementAction {

    // TODO
    public static String product_1 = "statement → if ( parExpression ) block elseStatement";
    public static String product_2 = "for ( forControl ) block";

    public static String product_3 = "while ( parExpression ) block";
    public static class WhileListener extends SyntaxDirectedListener{
        public WhileListener(){
            this.matchProductTag = product_3;
            this.matchSymbol = "block";
            this.matchIndex = 4;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            // TODO 是否要放在 parExpression 之前
            While whileNode = new While();
            currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).put(JavaConstants.WHILE_NODE, whileNode);

            // 设置循环闭包
            JavaDirectGlobalProperty.cycleEnclosingStack.push(whileNode);

            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Expr parExpr = (Expr) context.getBrotherNodeList().get(currentIndex - 2).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            Stmt stmt = (Stmt) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

            While whileNode = (While) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).get(JavaConstants.WHILE_NODE);
            whileNode.init(parExpr, stmt);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, whileNode);

            // 消除当前循环闭包
            JavaDirectGlobalProperty.cycleEnclosingStack.pop();

            return null;
        }
    }

    public static String product_4 = "do block while ( parExpression ) ;";
    public static class DoListener extends SyntaxDirectedListener{
        public DoListener(){
            this.matchProductTag = product_4;
            this.matchSymbol = "parExpression";
            this.matchIndex = 4;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            // TODO 是否要放在 parExpression 之前
            Do doNode = new Do();
            currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).put(JavaConstants.DO_NODE, doNode);

            // 设置循环闭包
            JavaDirectGlobalProperty.cycleEnclosingStack.push(doNode);

            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Stmt stmt = (Stmt) context.getBrotherNodeList().get(currentIndex - 2).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            Expr parExpr = (Expr) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

            Do doNode = (Do) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).get(JavaConstants.DO_NODE);
            doNode.init(stmt, parExpr);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, doNode);

            // 消除当前循环闭包
            JavaDirectGlobalProperty.cycleEnclosingStack.pop();

            return null;
        }
    }

    // TODO
    public static String product_5 = "switch ( expression ) switchBlock";
    public static String product_6 = "refVariable = expression ;";
    public static String product_7 = "return expression ;";

    public static String product_8 = "break ;";
    public static class BreakListener extends SyntaxDirectedListener{
        public BreakListener(){
            this.matchProductTag = product_8;
            this.matchSymbol = "break";
            this.matchIndex = 0;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Break breakNode = JavaDirectUtils.breakNode();
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, breakNode);

            return null;
        }
    }

    public static String product_9 = "continue ;";
    public static class ContinueListener extends SyntaxDirectedListener{
        public ContinueListener(){
            this.matchProductTag = product_9;
            this.matchSymbol = "continue";
            this.matchIndex = 0;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Continue continueNode = JavaDirectUtils.continueNode();
            context.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, continueNode);

            return null;
        }
    }

    public static String product_10 = "expression ;";
    public static class ExprListener extends SyntaxDirectedListener{
        public ExprListener(){
            this.matchProductTag = product_10;
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
            Expr code = (Expr) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, code);

            return null;
        }
    }

}