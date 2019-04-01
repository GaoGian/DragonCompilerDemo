package gian.compiler.language.simplejava.action;

import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gaojian on 2019/3/28.
 */
public class ExtendsInfoAction {

    public static String product_1 = "extendsInfo → extends classIdentifier";
    public static class ClassIdentifierListener extends SyntaxDirectedListener{

        public ClassIdentifierListener(){
            this.matchProductTag = product_1;
            this.matchSymbol = "classIdentifier";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            String extendClazzName = (String) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CLAZZ_NAME);
            Map<String, String> extendInfo = new HashMap<>();
            extendInfo.put(JavaConstants.CLAZZ_NAME, extendClazzName);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.EXTEND_INFO, extendInfo);

            return null;
        }
    }

    public static String product_2 = "extendsInfo → ε";
    public static class EpsilonListener extends SyntaxDirectedListener{

        public EpsilonListener(){
            this.matchProductTag = product_2;
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
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.EXTEND_INFO, new HashMap<>());

            return null;
        }
    }

}