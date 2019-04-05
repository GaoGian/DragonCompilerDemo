package gian.compiler.language.simplejava.action;

import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.ast.expression.Expr;
import gian.compiler.language.simplejava.ast.statement.Stmt;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.utils.JavaDirectUtils;

import java.util.List;

/**
 * Created by Gian on 2019/4/5.
 */
public class LocalVariableDeclarationStatementAction {

    public static String product = "localVariableDeclarationStatement → typeDeclaration Identifier variableInitializer";
    public static class LocalVariableDeclListener extends SyntaxDirectedListener{
        public LocalVariableDeclListener(){
            this.matchProductTag = product;
            this.matchSymbol = "variableInitializer";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            VariableType variableType = (VariableType) context.getBrotherNodeList().get(currentIndex - 2).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.VARIABLE_TYPE);
            String variableId = currentTreeNode.getIdToken().getToken();
            Expr initCode = (Expr) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

            Variable variable = JavaDirectUtils.variableDeclarate(variableId, variableType);
            if (initCode != null) {
                Stmt assign = JavaDirectUtils.assign(variableId, initCode);
                variable.setCode(assign);
            }

            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.VARIABLE, variable);

            return null;
        }
    }

}
