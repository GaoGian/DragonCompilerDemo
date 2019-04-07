package gian.compiler.language.simplejava.action;

import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/3/28.
 */
public class ModifierDeclarationAction {

    public static String product_1 = "modifierDeclaration → public";
    public static class PublicListener extends SyntaxDirectedListener{

        public PublicListener(){
            this.matchProductTag = product_1;
            this.matchSymbol = "public";
            this.matchIndex = 0;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            String modifier = currentTreeNode.getIdToken().getToken();
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.MODIFIER, modifier);

            return null;
        }
    }

    public static String product_2 = "modifierDeclaration → protected";
    public static class ProtectedListener extends SyntaxDirectedListener{

        public ProtectedListener(){
            this.matchProductTag = product_2;
            this.matchSymbol = "protected";
            this.matchIndex = 0;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            String modifier = currentTreeNode.getIdToken().getToken();
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.MODIFIER, modifier);

            return null;
        }
    }

    public static String product_3 = "modifierDeclaration → private";
    public static class PrivateListener extends SyntaxDirectedListener{

        public PrivateListener(){
            this.matchProductTag = product_3;
            this.matchSymbol = "private";
            this.matchIndex = 0;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            String modifier = currentTreeNode.getIdToken().getToken();
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.MODIFIER, modifier);

            return null;
        }
    }

    public static List<SyntaxDirectedListener> getAllListener() {
        List<SyntaxDirectedListener> allListener = new ArrayList<>();
        allListener.add(new PublicListener());

        return allListener;
    }
}