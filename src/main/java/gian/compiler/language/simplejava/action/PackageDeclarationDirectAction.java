package gian.compiler.language.simplejava.action;

import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/3/27.
 */
public class PackageDeclarationDirectAction {

    public static String product_1 = "packageDeclaration → package qualifiedName ;";

    // 包名称声明
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
            String packageName = (String) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.PACKAGE_NAME);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.PACKAGE_NAME, packageName);

            return null;
        }

    }

    // TODO 不需要处理
    public static String product_2 = "packageDeclaration → ε";

    public static List<SyntaxDirectedListener> getAllListener() {
        List<SyntaxDirectedListener> allListener = new ArrayList<>();
        allListener.add(new QualifiedNameListener());

        return allListener;
    }
}