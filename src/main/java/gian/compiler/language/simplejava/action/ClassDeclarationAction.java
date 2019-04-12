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
import gian.compiler.language.simplejava.env.JavaDirectGlobalProperty;

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
            String modifier = (String) context.getBrotherNodeList().get(currentIndex - 4).getSynProperty(JavaConstants.MODIFIER);
            String clazzName = context.getBrotherNodeList().get(currentIndex - 2).getIdToken().getToken();
            String packageName = (String) context.getGlobalPropertyMap().get(JavaConstants.PACKAGE_NAME);
            String clazzAllName = packageName + "." + clazzName;
            String extendClazzName = (String) currentTreeNode.getSynProperty(JavaConstants.EXTEND_INFO);

            List<String> importList = (List<String>) context.getGlobalPropertyMap().get(JavaConstants.IMPORT_LIST);
            Map<String, String> importMap = (Map<String, String>) context.getGlobalPropertyMap().get(JavaConstants.IMPORT_MAP);

            List<ClazzField> fieldList = JavaDirectGlobalProperty.fieldList;
            List<ClazzConstructor> constructorList = JavaDirectGlobalProperty.constructorList;
            List<ClazzMethod> methodList = JavaDirectGlobalProperty.methodList;

            Clazz clazz = new Clazz();
            clazz.setPackageName(packageName);
            clazz.setClazzName(clazzName);
            clazz.setClazzAllName(clazzAllName);
            clazz.setPermission(modifier);
            clazz.setImportList(importList);
            clazz.setImportMap(importMap);
            clazz.setExtendInfo(extendClazzName);
            clazz.setFieldList(fieldList);
            clazz.setConstructorList(constructorList);
            clazz.setMethodList(methodList);

            Map<String, Clazz> clazzMap = (Map<String, Clazz>) context.getGlobalPropertyMap().get(JavaConstants.CLAZZ_MAP);
            clazzMap.put(clazzName, clazz);

            context.getGlobalPropertyMap().put(JavaConstants.CURRENT_CLAZZ_NAME, clazzName);
            context.getGlobalPropertyMap().put(JavaConstants.EXTEND_INFO, extendClazzName);

            return null;
        }
    }

    public static String product_1 = "extendsInfo → extends Identifier";
    public static class ClassExtendListener extends SyntaxDirectedListener{

        public ClassExtendListener(){
            this.matchProductTag = product_1;
            this.matchSymbol = "Identifier";
            this.matchIndex = 1;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            String extendClazzName = (String) currentTreeNode.getSynProperty(JavaConstants.CLAZZ_NAME);
            context.getParentNode().putSynProperty(JavaConstants.EXTEND_INFO, extendClazzName);

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
