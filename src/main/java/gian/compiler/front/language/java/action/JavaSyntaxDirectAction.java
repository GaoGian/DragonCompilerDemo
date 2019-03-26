package gian.compiler.front.language.java.action;

import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;

/**
 * Created by gaojian on 2019/3/24.
 */
public class JavaSyntaxDirectAction extends SyntaxDirectedListener {
    @Override
    public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
        return "";
    }

    @Override
    public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
        return "";
    }
}