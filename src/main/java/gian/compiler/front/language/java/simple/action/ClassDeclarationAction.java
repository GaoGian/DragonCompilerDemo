package gian.compiler.front.language.java.simple.action;

import gian.compiler.front.language.java.simple.JavaConstants;
import gian.compiler.front.language.java.simple.bean.ClazzConstructor;
import gian.compiler.front.language.java.simple.bean.ClazzField;
import gian.compiler.front.language.java.simple.bean.ClazzMethod;
import gian.compiler.front.language.java.simple.bean.JavaClazz;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gian on 2019/3/27.
 */
public class ClassDeclarationAction {

    public static String product = "classDeclaration → modifierDeclaration class Identifier extendsInfo classBody";

    public static class ModifierDeclarationListener extends SyntaxDirectedListener{

        public ModifierDeclarationListener(){
            this.matchProductTag = product;
            this.matchSymbol = "modifierDeclaration";
            this.matchIndex = 0;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            // 该节点是终结符节点，有终结符进行赋值
            return null;
        }

    }

    public static class IdentifierListener extends SyntaxDirectedListener{

        public IdentifierListener(){
            this.matchProductTag = product;
            this.matchSymbol = "Identifier";
            this.matchIndex = 2;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            String clazzName = currentTreeNode.getIdToken().getToken();
            String packageName = (String) context.getGlobalPropertyMap().get(JavaConstants.PACKAGE_NAME);
            String clazzAllName = packageName + "." + clazzName;

            String modifier = (String) context.getBrotherNodeList().get(currentIndex - 2).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.MODIFIER);
            List<String> importList = (List<String>) context.getGlobalPropertyMap().get(JavaConstants.IMPORT_LIST);
            Map<String, String> importMap = (Map<String, String>) context.getGlobalPropertyMap().get(JavaConstants.IMPORT_MAP);

            JavaClazz clazz = new JavaClazz();
            clazz.setClazzName(clazzName);
            clazz.setClazzAllName(clazzAllName);
            clazz.setPermission(modifier);
            clazz.setImportList(importList);
            clazz.setImportMap(importMap);

            Map<String, JavaClazz> clazzMap = (Map<String, JavaClazz>) context.getGlobalPropertyMap().get(JavaConstants.CLAZZ_MAP);
            clazzMap.put(clazzName, clazz);

            return null;
        }
    }

    public static class ExtendsInfoListener extends SyntaxDirectedListener{

        public ExtendsInfoListener(){
            this.matchProductTag = product;
            this.matchSymbol = "extendsInfo";
            this.matchIndex = 3;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Map<String, String> extendInfo = (Map<String, String>) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.EXTEND_INFO);
            String clazzName = context.getBrotherNodeList().get(currentIndex - 1).getIdToken().getToken();
            Map<String, JavaClazz> clazzMap = (Map<String, JavaClazz>) context.getGlobalPropertyMap().get(JavaConstants.CLAZZ_MAP);
            JavaClazz clazz = clazzMap.get(clazzName);
            clazz.setExtendInfo(extendInfo);

            return null;
        }
    }

    public static class ClassBodyListener extends SyntaxDirectedListener{

        public ClassBodyListener(){
            this.matchProductTag = product;
            this.matchSymbol = "classBody";
            this.matchIndex = 4;
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

            String clazzName = context.getBrotherNodeList().get(currentIndex - 1).getIdToken().getToken();
            Map<String, JavaClazz> clazzMap = (Map<String, JavaClazz>) context.getGlobalPropertyMap().get(JavaConstants.CLAZZ_MAP);
            JavaClazz clazz = clazzMap.get(clazzName);

            clazz.setFieldList(fieldList);
            clazz.setConstructorList(constructorList);
            clazz.setMethodList(methodList);

            return null;
        }
    }


}
