package gian.compiler.language.simplejava.action;

import gian.compiler.language.simplejava.JavaConstants;
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
    public static class ImportListener extends SyntaxDirectedListener{

        public ImportListener(){
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
            List<String> importList = (List<String>) context.getGlobalPropertyMap().get(JavaConstants.IMPORT_LIST);
            Map<String, String> importMap = (Map<String, String>) context.getGlobalPropertyMap().get(JavaConstants.IMPORT_MAP);

            String importClazzAllName = (String) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.IMPORT_CLAZZ_ALL_NAME);
            importList.add(importClazzAllName);

            String importClazzName = importClazzAllName.substring(importClazzAllName.lastIndexOf("\\.") + 1);
            importMap.put(importClazzName, importClazzAllName);

            return null;
        }

    }

    // 不需要处理
    public static String product_1_2 = "importDeclaration → ε";

    public static String product_2_1 = "qualifiedName → Identifier IdentifierRepeat";
    public static class QualifiedNameListener extends SyntaxDirectedListener{
        public QualifiedNameListener(){
            this.matchProductTag = product_2_1;
            this.matchSymbol = "IdentifierRepeat";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            String qualifiedName = context.getBrotherNodeList().get(currentIndex - 1).getIdToken().getToken();
            currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).put(JavaConstants.IMPORT_CLAZZ_QUALIFIE_DNAME, qualifiedName);
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            String importClazzAllName = (String) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.IMPORT_CLAZZ_ALL_NAME);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.IMPORT_CLAZZ_ALL_NAME, importClazzAllName);
            return null;
        }
    }

    public static String product_2_2 = "IdentifierRepeat → . Identifier IdentifierRepeat";
    public static class QualifiedNameRepeatListener extends SyntaxDirectedListener{
        public QualifiedNameRepeatListener(){
            this.matchProductTag = product_2_2;
            this.matchSymbol = "IdentifierRepeat";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            String preQuailifiedName = (String) context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).get(JavaConstants.IMPORT_CLAZZ_QUALIFIE_DNAME);
            String qualifiedName = context.getBrotherNodeList().get(currentIndex - 1).getIdToken().getToken();
            currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).put(JavaConstants.IMPORT_CLAZZ_QUALIFIE_DNAME, preQuailifiedName + "." +qualifiedName);

            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            String importClazzAllName = (String) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.IMPORT_CLAZZ_ALL_NAME);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.IMPORT_CLAZZ_ALL_NAME, importClazzAllName);
            return null;
        }
    }

    public static String product_2_3 = "IdentifierRepeat → ε";
    public static class QualifiedNameRepeatEndListener extends SyntaxDirectedListener{
        public QualifiedNameRepeatEndListener(){
            this.matchProductTag = product_2_3;
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
            String importClazzAllName = (String) context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).get(JavaConstants.IMPORT_CLAZZ_QUALIFIE_DNAME);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.IMPORT_CLAZZ_ALL_NAME, importClazzAllName);

            return null;
        }
    }

    public static List<SyntaxDirectedListener> getAllListener() {
        List<SyntaxDirectedListener> allListener = new ArrayList<>();
        allListener.add(new ImportListener());
        allListener.add(new QualifiedNameListener());
        allListener.add(new QualifiedNameRepeatListener());
        allListener.add(new QualifiedNameRepeatEndListener());

        return allListener;
    }
}