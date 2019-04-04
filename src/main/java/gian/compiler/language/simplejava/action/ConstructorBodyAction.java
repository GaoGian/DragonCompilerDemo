package gian.compiler.language.simplejava.action;

import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.ast.AstNode;
import gian.compiler.language.simplejava.ast.ref.SuperInitRefNode;
import gian.compiler.language.simplejava.ast.statement.Stmt;
import gian.compiler.language.simplejava.env.JavaDirectGlobalProperty;
import gian.compiler.language.simplejava.env.JavaEnvironment;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;

import java.util.ArrayList;
import java.util.List;

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
            JavaEnvironment preEnv = JavaDirectGlobalProperty.topEnv;
            JavaDirectGlobalProperty.topEnv = new JavaEnvironment(preEnv);

            List<String> code = new ArrayList<>();
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, code);

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
            SuperInitRefNode superInitRefNode = (SuperInitRefNode) context.getBrotherNodeList().get(currentIndex - 1).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            Stmt stmt = (Stmt) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

            AstNode code = new AstNode();
            code.getChileNode().add(superInitRefNode);
            code.getChileNode().add(stmt);

            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, code);

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
            JavaDirectGlobalProperty.topEnv = JavaDirectGlobalProperty.topEnv.getPreEnv();

            return null;
        }
    }

    public static List<SyntaxDirectedListener> getAllListener(){
        List<SyntaxDirectedListener> allListener = new ArrayList<>();
        allListener.add(new ConstructorBodyEnterListener());
        allListener.add(new BlockStatementListener());
        allListener.add(new ConstructorBodyExitListener());

        return allListener;
    }

}