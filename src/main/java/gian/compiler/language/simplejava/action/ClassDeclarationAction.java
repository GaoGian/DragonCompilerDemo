package gian.compiler.language.simplejava.action;

import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.bean.ClazzConstructor;
import gian.compiler.language.simplejava.bean.ClazzField;
import gian.compiler.language.simplejava.bean.ClazzMethod;
import gian.compiler.language.simplejava.bean.Clazz;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gian on 2019/3/27.
 */
public class ClassDeclarationAction {

    public static String product = "classDeclaration → modifierDeclaration class Identifier extendsInfo classBody";

    public static class ClassDeclListener extends SyntaxDirectedListener{

        public ClassDeclListener(){
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
            String modifier = (String) context.getBrotherNodeList().get(currentIndex - 4).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.MODIFIER);
            String clazzName = context.getBrotherNodeList().get(currentIndex - 2).getIdToken().getToken();
            String packageName = (String) context.getGlobalPropertyMap().get(JavaConstants.PACKAGE_NAME);
            String clazzAllName = packageName + "." + clazzName;
            Map<String, String> extendInfo = (Map<String, String>) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.EXTEND_INFO);

            List<String> importList = (List<String>) context.getGlobalPropertyMap().get(JavaConstants.IMPORT_LIST);
            Map<String, String> importMap = (Map<String, String>) context.getGlobalPropertyMap().get(JavaConstants.IMPORT_MAP);

            List<ClazzField> fieldList = (List<ClazzField>) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.FIELD_LIST);
            List<ClazzConstructor> constructorList = (List<ClazzConstructor>) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CONSTRUCTOR_LIST);
            List<ClazzMethod> methodList = (List<ClazzMethod>) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.METHOD_LIST);

            Clazz clazz = new Clazz();
            clazz.setClazzName(clazzName);
            clazz.setClazzAllName(clazzAllName);
            clazz.setPermission(modifier);
            clazz.setImportList(importList);
            clazz.setImportMap(importMap);
            clazz.setExtendInfo(extendInfo);
            clazz.setFieldList(fieldList);
            clazz.setConstructorList(constructorList);
            clazz.setMethodList(methodList);

            Map<String, Clazz> clazzMap = (Map<String, Clazz>) context.getGlobalPropertyMap().get(JavaConstants.CLAZZ_MAP);
            clazzMap.put(clazzName, clazz);

            return null;
        }
    }

    public static String product_1 = "extendsInfo → extends classIdentifier";
    public static class ClassExtendListener extends SyntaxDirectedListener{

        public ClassExtendListener(){
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

    // 不需要处理
    public static String product_2 = "extendsInfo → ε";

    public static List<SyntaxDirectedListener> getAllListener() {
        List<SyntaxDirectedListener> allListener = new ArrayList<>();
        allListener.add(new ClassDeclListener());
        allListener.add(new ClassExtendListener());

        return allListener;
    }

}
