package gian.compiler.front.language.java.simple.action;

import gian.compiler.front.language.java.simple.JavaConstants;
import gian.compiler.front.language.java.simple.env.JavaEnvironment;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;

/**
 * Created by gaojian on 2019/3/30.
 */
public class ConstructorBodyAction {

    public static String product = "constructorBody → { explicitConstructorInvocation blockStatement }";

    public static class ConstructorBodyEnterListener extends SyntaxDirectedListener{

        public ConstructorBodyEnterListener(){
            this.matchProductTag = product;
            this.matchSymbol = "{";
            this.matchIndex = 0;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            JavaEnvironment preEnv = (JavaEnvironment) context.getGlobalPropertyMap().get(JavaConstants.CURRENT_ENV);
            JavaEnvironment env = new JavaEnvironment(preEnv);
            context.getGlobalPropertyMap().put(JavaConstants.CURRENT_ENV, env);

            return null;
        }
    }

    public static class ExplicitConstructorInvocationListener extends SyntaxDirectedListener{

        public ExplicitConstructorInvocationListener(){
            this.matchProductTag = product;
            this.matchSymbol = "explicitConstructorInvocation";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            // TODO
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            // TODO
            return null;
        }
    }

    public static class BlockStatementListener extends SyntaxDirectedListener{

        public BlockStatementListener(){
            this.matchProductTag = product;
            this.matchSymbol = "blockStatement";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            // TODO
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            // TODO
            return null;
        }
    }

    public static class ConstructorBodyExitListener extends SyntaxDirectedListener{

        public ConstructorBodyExitListener(){
            this.matchProductTag = product;
            this.matchSymbol = "}";
            this.matchIndex = 3;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            JavaEnvironment env = (JavaEnvironment) context.getGlobalPropertyMap().get(JavaConstants.CURRENT_ENV);
            JavaEnvironment preEnv = env.getPreEnv();
            context.getGlobalPropertyMap().put(JavaConstants.CURRENT_ENV, preEnv);

            return null;
        }
    }


}