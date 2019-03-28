package gian.compiler.front.language.java.simple.action;

import gian.compiler.front.language.java.simple.JavaConstants;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;

/**
 * Created by gaojian on 2019/3/28.
 */
public class ClassIdentifierAction {

    public static String product = "classIdentifier → Identifier";

    public static class IdentifierListener extends SyntaxDirectedListener{

        public IdentifierListener(){
            this.matchProductTag = product;
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
            String clazzName = currentTreeNode.getIdToken().getToken();
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CLAZZ_NAME, clazzName);

            return null;
        }
    }

}