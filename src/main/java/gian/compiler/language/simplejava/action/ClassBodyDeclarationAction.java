package gian.compiler.language.simplejava.action;

import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.bean.ClazzConstructor;
import gian.compiler.language.simplejava.bean.ClazzField;
import gian.compiler.language.simplejava.bean.ClazzMethod;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.language.simplejava.env.JavaDirectGlobalProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/3/28.
 */
public class ClassBodyDeclarationAction {

    public static String product_1 = "classBodyDeclaration → fieldDeclaration classBodyDeclaration";
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

            return null;
        }
    }

    public static String product_2 = "classBodyDeclaration → constructorDeclaration classBodyDeclaration";
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

            return null;
        }
    }

    public static String product_3 = "classBodyDeclaration → methodDeclaration classBodyDeclaration";
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

            return null;
        }
    }

    public static List<SyntaxDirectedListener> getAllListener() {
        List<SyntaxDirectedListener> allListener = new ArrayList<>();
        allListener.add(new FieldDeclarationListener());
        allListener.add(new ConstructorDeclarationListener());
        allListener.add(new MethodDeclarationListener());

        return allListener;
    }
}