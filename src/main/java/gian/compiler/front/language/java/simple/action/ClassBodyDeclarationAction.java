package gian.compiler.front.language.java.simple.action;

import gian.compiler.front.language.java.simple.JavaConstants;
import gian.compiler.front.language.java.simple.bean.ClazzConstructor;
import gian.compiler.front.language.java.simple.bean.ClazzField;
import gian.compiler.front.language.java.simple.bean.ClazzMethod;
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

    public static String product_1 = "classBodyDeclaration → fieldDeclaration constructorDeclaration methodDeclaration";
    public static class FieldDeclarationListener extends SyntaxDirectedListener{

        public FieldDeclarationListener(){
            this.matchProductTag = product_1;
            this.matchSymbol = "fieldDeclaration";
            this.matchIndex = 0;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            List<ClazzField> fieldList = new ArrayList<>();
            currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.FIELD_LIST, fieldList);

            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            List<ClazzField> fieldList = (List<ClazzField>) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.FIELD_LIST);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.FIELD_LIST, fieldList);

            return null;
        }
    }

    public static class ConstructorDeclarationListener extends SyntaxDirectedListener{

        public ConstructorDeclarationListener(){
            this.matchProductTag = product_1;
            this.matchSymbol = "constructorDeclaration";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            List<ClazzConstructor> constructorList = new ArrayList<>();
            currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CONSTRUCTOR_LIST, constructorList);

            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            List<ClazzConstructor> constructorList = (List<ClazzConstructor>) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CONSTRUCTOR_LIST);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CONSTRUCTOR_LIST, constructorList);

            return null;
        }
    }

    public static class MethodDeclarationListener extends SyntaxDirectedListener{

        public MethodDeclarationListener(){
            this.matchProductTag = product_1;
            this.matchSymbol = "methodDeclaration";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            List<ClazzMethod> methodList = new ArrayList<>();
            currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.METHOD_LIST, methodList);

            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            List<ClazzMethod> methodList = (List<ClazzMethod>) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.METHOD_LIST);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.METHOD_LIST, methodList);

            return null;
        }
    }

    public static String product_2 = "classBodyDeclaration → ε";
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
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.FIELD_LIST, new ArrayList<>());
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CONSTRUCTOR_LIST, new ArrayList<>());
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.METHOD_LIST, new ArrayList<>());

            return null;
        }
    }

}