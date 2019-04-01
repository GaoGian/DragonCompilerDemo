package gian.compiler.language.simplejava.action;

import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.language.simplejava.env.JavaDirectGlobalProperty;
import gian.compiler.language.simplejava.env.JavaEnvironment;

/**
 * Created by gaojian on 2019/4/1.
 */
public class BlockAction {

    public static String product = "block → { blockStatement }";

    public static class BlockEnterListener extends SyntaxDirectedListener{

        public BlockEnterListener(){
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
            JavaEnvironment preEnv = JavaDirectGlobalProperty.topEnv;
            JavaDirectGlobalProperty.topEnv = new JavaEnvironment(preEnv);

            return null;
        }

    }

    public static class BlockExitListener extends SyntaxDirectedListener{

        public BlockExitListener(){
            this.matchProductTag = product;
            this.matchSymbol = "}";
            this.matchIndex = 2;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            JavaDirectGlobalProperty.topEnv = JavaDirectGlobalProperty.topEnv.getPreEnv();

            return null;
        }

    }

}