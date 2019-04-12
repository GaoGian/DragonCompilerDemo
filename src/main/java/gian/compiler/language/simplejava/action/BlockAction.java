package gian.compiler.language.simplejava.action;

import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.ast.statement.Seq;
import gian.compiler.language.simplejava.ast.statement.Stmt;
import gian.compiler.language.simplejava.env.JavaDirectGlobalProperty;
import gian.compiler.language.simplejava.env.JavaEnvironment;
import gian.compiler.language.simplejava.utils.JavaDirectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/4/1.
 */
public class BlockAction{

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
            JavaDirectUtils.nestEnv();

            return null;
        }

    }

    public static class BlockStatementListener extends SyntaxDirectedListener{
        public BlockStatementListener(){
            this.matchProductTag = product;
            this.matchSymbol = "blockStatement";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Stmt code = (Stmt) currentTreeNode.getSynProperty(JavaConstants.CODE);
            context.getParentNode().putSynProperty(JavaConstants.CODE, code);
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
            JavaDirectUtils.exitEnv();

            return null;
        }

    }

    public static String product_1 = "blockStatement → localVariableDeclarationStatement ; blockStatement";
    public static class LocalVariableDeclListener extends SyntaxDirectedListener{
        public LocalVariableDeclListener(){
            this.matchProductTag = product_1;
            this.matchSymbol = "blockStatement";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Stmt localVariableDeclStmt = (Stmt) context.getBrotherNodeList().get(currentIndex - 2).getSynProperty(JavaConstants.CODE);
            Stmt blockCode = (Stmt) currentTreeNode.getSynProperty(JavaConstants.CODE);
            Stmt stmt = null;
            if(blockCode != null){
                stmt = new Seq(localVariableDeclStmt, blockCode);
            }else{
                stmt = localVariableDeclStmt;
            }

            context.getParentNode().putSynProperty(JavaConstants.CODE, stmt);

            return null;
        }
    }

    public static String product_2 = "blockStatement → statement blockStatement";
    public static class StatementListener extends SyntaxDirectedListener{
        public StatementListener(){
            this.matchProductTag = product_2;
            this.matchSymbol = "blockStatement";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Stmt statement = (Stmt) context.getBrotherNodeList().get(currentIndex - 1).getSynProperty(JavaConstants.CODE);
            Stmt blockCode = (Stmt) currentTreeNode.getSynProperty(JavaConstants.CODE);
            Stmt stmt = null;
            if(blockCode != null){
                stmt = new Seq(statement, blockCode);
            }else{
                stmt = statement;
            }

            context.getParentNode().putSynProperty(JavaConstants.CODE, stmt);
            return null;
        }
    }

    public static List<SyntaxDirectedListener> getAllListener() {
        List<SyntaxDirectedListener> allListener = new ArrayList<>();
        allListener.add(new BlockEnterListener());
        allListener.add(new BlockStatementListener());
        allListener.add(new BlockExitListener());
        allListener.add(new LocalVariableDeclListener());
        allListener.add(new StatementListener());

        return allListener;
    }

}