package gian.compiler.front.language.java.simple.action;

import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gaojian on 2019/3/27.
 */
public class ImportDeclarationAction {

    public static String product_1 = "importDeclaration → import qualifiedName ; importDeclaration";

    // 引入包声明
    public static class QualifiedNameListener extends SyntaxDirectedListener{

        public QualifiedNameListener(){
            this.matchProductTag = product_1;
            this.matchSymbol = "qualifiedName";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            List<String> importList = (List<String>) context.getGlobalPropertyMap().get("importList");
            Map<String, String> importMap = (Map<String, String>) context.getGlobalPropertyMap().get("importMap");

            String importClazzAllName = (String) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get("importClazzAllName");
            importList.add(importClazzAllName);

            String importClazzName = importClazzAllName.substring(importClazzAllName.lastIndexOf("\\.") + 1);
            importMap.put(importClazzName, importClazzAllName);

            return null;
        }

    }

    // 后续包声明
    public static class ImportDeclarationListener extends SyntaxDirectedListener{

        public ImportDeclarationListener(){
            this.matchProductTag = product_1;
            this.matchSymbol = "importDeclaration";
            this.matchIndex = 3;
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

    public static String product_2 = "importDeclaration → ε";

    // 无引入声明
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
            return null;
        }

    }

}