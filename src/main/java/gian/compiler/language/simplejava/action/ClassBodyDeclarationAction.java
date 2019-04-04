package gian.compiler.language.simplejava.action;

import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.bean.ClazzConstructor;
import gian.compiler.language.simplejava.bean.ClazzField;
import gian.compiler.language.simplejava.bean.ClazzMethod;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/3/28.
 */
public class ClassBodyDeclarationAction {

    public static String product_1 = "classBodyDeclaration → fieldDeclaration classBodyDeclaration";
    public static String product_2 = "classBodyDeclaration → constructorDeclaration classBodyDeclaration";
    public static String product_3 = "classBodyDeclaration → methodDeclaration classBodyDeclaration";

    public static class FieldDeclarationListener extends SyntaxDirectedListener{

        public FieldDeclarationListener(){
            this.matchProductTag = product_1;
            this.matchSymbol = "fieldDeclaration";
            this.matchIndex = 0;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            List<ClazzField> fieldList = (List<ClazzField>) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.FIELD_LIST);

            ((List<ClazzField>) context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.FIELD_LIST)).addAll(fieldList);

            return null;
        }
    }

    public static class ConstructorDeclarationListener extends SyntaxDirectedListener{

        public ConstructorDeclarationListener(){
            this.matchProductTag = product_2;
            this.matchSymbol = "constructorDeclaration";
            this.matchIndex = 0;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            List<ClazzConstructor> constructorList = (List<ClazzConstructor>) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CONSTRUCTOR_LIST);
            ((List<ClazzConstructor>) context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CONSTRUCTOR_LIST)).addAll(constructorList);

            return null;
        }
    }

    public static class MethodDeclarationListener extends SyntaxDirectedListener{

        public MethodDeclarationListener(){
            this.matchProductTag = product_3;
            this.matchSymbol = "methodDeclaration";
            this.matchIndex = 0;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            List<ClazzMethod> methodList = (List<ClazzMethod>) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.METHOD_LIST);
            ((List<ClazzMethod>) context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.METHOD_LIST)).addAll(methodList);

            return null;
        }
    }

    public static List<SyntaxDirectedListener> getAllListener() {
        List<SyntaxDirectedListener> allListener = new ArrayList<>();
        allListener.add(new FieldDeclarationListener());
        allListener.add(new ConstructorDeclarationListener());
        allListener.add(new MethodDeclarationListener());

        return null;
    }
}