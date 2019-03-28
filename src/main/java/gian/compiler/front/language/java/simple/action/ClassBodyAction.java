package gian.compiler.front.language.java.simple.action;

import gian.compiler.front.language.java.simple.JavaConstants;
import gian.compiler.front.language.java.simple.bean.ClazzConstructor;
import gian.compiler.front.language.java.simple.bean.ClazzField;
import gian.compiler.front.language.java.simple.bean.ClazzMethod;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;

import java.util.List;

/**
 * Created by gaojian on 2019/3/28.
 */
public class ClassBodyAction {

    public static String product = "classBody → { classBodyDeclaration }";

    public static class ClassBodyDeclarationListener extends SyntaxDirectedListener{

        public ClassBodyDeclarationListener(){
            this.matchProductTag = product;
            this.matchSymbol = "classBodyDeclaration";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            List<ClazzField> fieldList = (List<ClazzField>) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.FIELD_LIST);
            List<ClazzConstructor> constructorList = (List<ClazzConstructor>) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CONSTRUCTOR_LIST);
            List<ClazzMethod> methodList = (List<ClazzMethod>) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.METHOD_LIST);

            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.FIELD_LIST, fieldList);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CONSTRUCTOR_LIST, constructorList);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.METHOD_LIST, methodList);

            return null;
        }
    }

}