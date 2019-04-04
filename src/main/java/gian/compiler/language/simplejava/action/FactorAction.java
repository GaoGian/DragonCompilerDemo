package gian.compiler.language.simplejava.action;

import gian.compiler.front.lexical.parser.Token;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.ast.ref.FieldRefNode;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.ast.Constant;
import gian.compiler.language.simplejava.ast.expression.Expr;
import gian.compiler.language.simplejava.utils.JavaDirectUtils;

/**
 * Created by gaojian on 2019/4/1.
 */
public class FactorAction {

    public static String product_1 = "factor → ( expression )";

    public static class ExpressionListener extends SyntaxDirectedListener{

        public ExpressionListener(){
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
            Expr expr = (Expr) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.VARIABLE);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.VARIABLE, expr);

            return null;
        }
    }

    public static String product_2 = "factor → Number";

    public static class NumberListener extends SyntaxDirectedListener{

        public NumberListener(){
            this.matchProductTag = product_2;
            this.matchSymbol = "Number";
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

            // 设置code
            currentTreeNode.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, constant);

            return null;
        }
    }

    public static String product_3 = "factor → Digit";

    public static class DigitListener extends SyntaxDirectedListener{

        public DigitListener(){
            this.matchProductTag = product_3;
            this.matchSymbol = "Digit";
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

            // 设置code
            currentTreeNode.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, constant);

            return null;
        }
    }

    public static String product_4 = "factor → refVariable";

    public static class RefVariableListener extends SyntaxDirectedListener{

        public RefVariableListener(){
            this.matchProductTag = product_4;
            this.matchSymbol = "refVariable";
            this.matchIndex = 0;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            // FIXME 需要考虑引用链及数组元素的情况
            FieldRefNode variableRef = (FieldRefNode) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            currentTreeNode.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, variableRef);

            return null;
        }
    }

}