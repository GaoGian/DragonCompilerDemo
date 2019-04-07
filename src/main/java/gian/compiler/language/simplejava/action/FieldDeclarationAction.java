package gian.compiler.language.simplejava.action;

import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.ast.expression.Expr;
import gian.compiler.language.simplejava.ast.statement.Stmt;
import gian.compiler.language.simplejava.bean.ClazzField;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.language.simplejava.ast.AstNode;
import gian.compiler.language.simplejava.utils.JavaDirectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/3/28.
 */
public class FieldDeclarationAction {

    public static String product = "fieldDeclaration → modifierDeclaration typeDeclaration Identifier variableInitializer ;";

    public static class FieldDeclListener extends SyntaxDirectedListener{

        public FieldDeclListener(){
            this.matchProductTag = product;
            this.matchSymbol = "variableInitializer";
            this.matchIndex = 3;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {

            String modifier = (String) context.getBrotherNodeList().get(currentIndex - 3).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.MODIFIER);
            VariableType variableType = (VariableType) context.getBrotherNodeList().get(currentIndex - 2).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.VARIABLE_TYPE);
            String variableId = currentTreeNode.getIdToken().getToken();
            Expr initCode = (Expr) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

            ClazzField clazzField = JavaDirectUtils.variableDeclarate(modifier, variableId, variableType, JavaDirectUtils.assign(variableId, initCode));

            List<ClazzField> fieldList = (List<ClazzField>) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.FIELD_LIST);
            fieldList.add(clazzField);

            return null;
        }
    }

    public static List<SyntaxDirectedListener> getAllListener() {
        List<SyntaxDirectedListener> allListener = new ArrayList<>();
        allListener.add(new FieldDeclListener());
        return allListener;
    }
}