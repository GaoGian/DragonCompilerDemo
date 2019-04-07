package gian.compiler.language.simplejava.action;

import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.ast.expression.Expr;
import gian.compiler.language.simplejava.utils.JavaDirectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/4/2.
 */
public class TermAction {

    public static String product_1 = "term → term * factor";
    public static class MultiFactorListener extends SyntaxDirectedListener{

        public MultiFactorListener(){
            this.matchProductTag = product_1;
            this.matchSymbol = "factor";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Variable lvariable = (Variable) context.getBrotherNodeList().get(currentIndex - 2).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            String operator = context.getBrotherNodeList().get(currentIndex - 1).getIdToken().getToken();
            Variable rvariable = (Variable) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

            Expr term = JavaDirectUtils.term(lvariable, rvariable, operator);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, term);

            return null;
        }
    }

    public static String product_2 = "term → term / factor";
    public static class DivFactorListener extends SyntaxDirectedListener{

        public DivFactorListener(){
            this.matchProductTag = product_2;
            this.matchSymbol = "factor";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Variable lvariable = (Variable) context.getBrotherNodeList().get(currentIndex - 2).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            String operator = context.getBrotherNodeList().get(currentIndex - 1).getIdToken().getToken();
            Variable rvariable = (Variable) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

            Expr term = JavaDirectUtils.term(lvariable, rvariable, operator);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, term);

            return null;
        }
    }

    public static String product_3 = "term → factor";
    public static class FactorAction extends SyntaxDirectedListener{

        public FactorAction(){
            this.matchProductTag = product_3;
            this.matchSymbol = "factor";
            this.matchIndex = 0;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Expr factor = (Expr) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, factor);

            return null;
        }

    }

    public static List<SyntaxDirectedListener> getAllListener() {
        List<SyntaxDirectedListener> allListener = new ArrayList<>();
        allListener.add(new MultiFactorListener());
        allListener.add(new DivFactorListener());
        allListener.add(new FactorAction());

        return allListener;
    }
}