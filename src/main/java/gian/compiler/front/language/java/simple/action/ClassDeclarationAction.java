package gian.compiler.front.language.java.simple.action;

import gian.compiler.front.language.java.simple.JavaConstants;
import gian.compiler.front.language.java.simple.bean.JavaClazz;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;

import java.util.Map;

/**
 * Created by Gian on 2019/3/27.
 */
public class ClassDeclarationAction {

    public static String product = "modifierDeclaration class Identifier extendsInfo classBody";

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

            JavaClazz clazz = new JavaClazz();
            clazz.setClazzName(clazzName);
            clazz.setClazzAllName(clazzAllName);

            Map<String, JavaClazz> clazzMap = (Map<String, JavaClazz>) context.getGlobalPropertyMap().get(JavaConstants.CLAZZ_MAP);
            clazzMap.put(clazzName, clazz);

            return null;
        }
    }

}
