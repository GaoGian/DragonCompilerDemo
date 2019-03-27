package gian.compiler.front.language.java.simple.action;

import gian.compiler.front.language.java.simple.bean.JavaClazz;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gaojian on 2019/3/27.
 */
public class JavaLanguageDirectAction {

    public static String product = "javaLanguage → packageDeclaration importDeclaration classDeclaration";

    // 包声明
    public static class PackageDeclarationListener extends SyntaxDirectedListener{

        public PackageDeclarationListener(){
            this.matchProductTag = product;
            this.matchSymbol = "packageDeclaration";
            this.matchIndex = 0;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            // key: 类全名, value: clazz
            Map<String, JavaClazz> clazzMap = new HashMap<>();
            context.getGlobalPropertyMap().put("clazzMap", clazzMap);

            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            String packageName = (String) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get("packageName");
            context.getGlobalPropertyMap().put("packageName", packageName);

            return null;
        }

    }

    // 引入包列表声明
    public static class ImportDeclarationListener extends SyntaxDirectedListener{

        public ImportDeclarationListener(){
            this.matchProductTag = product;
            this.matchSymbol = "importDeclaration";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            List<String> importList = new ArrayList<>();
            Map<String, String> importMap = new HashMap<>();
            context.getGlobalPropertyMap().put("importList", importList);
            context.getGlobalPropertyMap().put("importMap", importMap);

            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            List<String> importList = (List<String>) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get("importList");
            JavaClazz clazz = (JavaClazz) context.getPropertyMap().get(context.getParentNode()).get("JavaClazz");
            clazz.setImportList(importList);

            return null;
        }

    }

    // 类声明
    public static class ClassDeclarationListener extends SyntaxDirectedListener{

        public ClassDeclarationListener(){
            this.matchProductTag = product;
            this.matchSymbol = "classDeclaration";
            this.matchIndex = 2;
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


}