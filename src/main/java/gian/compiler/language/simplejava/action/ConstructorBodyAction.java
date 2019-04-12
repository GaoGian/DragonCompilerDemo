package gian.compiler.language.simplejava.action;

import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.ast.AstNode;
import gian.compiler.language.simplejava.ast.expression.Expr;
import gian.compiler.language.simplejava.ast.ref.SuperInitRefNode;
import gian.compiler.language.simplejava.ast.statement.Stmt;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.env.JavaDirectGlobalProperty;
import gian.compiler.language.simplejava.env.JavaEnvironment;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.language.simplejava.utils.JavaDirectUtils;

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
            JavaDirectUtils.nestEnv();
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
            SuperInitRefNode superInitRefNode = (SuperInitRefNode) context.getBrotherNodeList().get(currentIndex - 1).getSynProperty(JavaConstants.CODE);
            Stmt stmt = (Stmt) currentTreeNode.getSynProperty(JavaConstants.CODE);

            Stmt code = JavaDirectUtils.stmts(superInitRefNode, stmt);

            context.getParentNode().putSynProperty(JavaConstants.CODE, code);

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
            JavaDirectUtils.exitEnv();

            return null;
        }
    }

    public static String product_1 = "explicitConstructorInvocation → super ( expressionList ) ;";
    public static class ExplicitConstructorListener extends SyntaxDirectedListener{
        public ExplicitConstructorListener(){
            this.matchProductTag = product_1;
            this.matchSymbol = "expressionList";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            List<Expr> paramList = (List<Expr>) currentTreeNode.getSynProperty(JavaConstants.CALL_PARAM_LIST);
            SuperInitRefNode superInitRefNode = JavaDirectUtils.superInitRefNode(context, paramList);
            context.getParentNode().putSynProperty(JavaConstants.CODE, superInitRefNode);

            return null;
        }
    }

    public static String product_2 = "explicitConstructorInvocation → ε";
    public static class InexplicitConstructorListener extends SyntaxDirectedListener{
        public InexplicitConstructorListener(){
            this.matchProductTag = product_2;
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
            SuperInitRefNode superInitRefNode = JavaDirectUtils.superInitRefNode(context, new ArrayList<>());
            context.getParentNode().putSynProperty(JavaConstants.CODE, superInitRefNode);
            return null;
        }
    }

    public static List<SyntaxDirectedListener> getAllListener(){
        List<SyntaxDirectedListener> allListener = new ArrayList<>();
        allListener.add(new ConstructorBodyEnterListener());
        allListener.add(new BlockStatementListener());
        allListener.add(new ConstructorBodyExitListener());
        allListener.add(new ExplicitConstructorListener());
        allListener.add(new InexplicitConstructorListener());

        return allListener;
    }

}