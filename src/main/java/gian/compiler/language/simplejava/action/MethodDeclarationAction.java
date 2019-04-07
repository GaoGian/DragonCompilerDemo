package gian.compiler.language.simplejava.action;

import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.ast.statement.Stmt;
import gian.compiler.language.simplejava.bean.ClazzMethod;
import gian.compiler.language.simplejava.bean.Param;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.language.simplejava.ast.AstNode;

import java.util.List;

/**
 * Created by Gian on 2019/3/30.
 */
public class MethodDeclarationAction {

    public static String product_1 = "methodDeclaration → modifierDeclaration typeDeclaration Identifier formalParameters methodBody";
    public static class MethodBodyLietener extends SyntaxDirectedListener{
        public MethodBodyLietener(){
            this.matchProductTag = product_1;
            this.matchSymbol = "methodBody";
            this.matchIndex = 4;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            VariableType returnVariableType = (VariableType) context.getBrotherNodeList().get(currentIndex - 3).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.VARIABLE_TYPE);

            currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).put(JavaConstants.METHOD_RETURN_TYPE, returnVariableType);

            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {

            String modifier = (String) context.getBrotherNodeList().get(currentIndex - 4).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.MODIFIER);
            VariableType returnVariableType = (VariableType) context.getBrotherNodeList().get(currentIndex - 3).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.VARIABLE_TYPE);
            String methodId = context.getBrotherNodeList().get(currentIndex - 2).getIdToken().getToken();
            List<Param> paramList = (List<Param>) context.getBrotherNodeList().get(currentIndex - 1).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.PARAM_LIST);
            Stmt code = (Stmt) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

            ClazzMethod method = new ClazzMethod();
            method.setPermission(modifier);
            method.setReturnType(returnVariableType);
            method.setMethodName(methodId);
            method.setParamList(paramList);
            method.setCode(code);

            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.METHOD, method);

            return null;
        }
    }

    public static String product_2 = "methodDeclaration → modifierDeclaration void Identifier formalParameters methodBody";
    public static class VoidMethodBodyLietener extends SyntaxDirectedListener{
        public VoidMethodBodyLietener(){
            this.matchProductTag = product_2;
            this.matchSymbol = "methodBody";
            this.matchIndex = 4;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            VariableType voidReturnType = new VariableType(JavaConstants.VARIABLE_TYPE_VOID, VariableType.VOID.getWidth());
            currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).put(JavaConstants.METHOD_RETURN_TYPE, voidReturnType);

            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {

            String modifier = (String) context.getBrotherNodeList().get(currentIndex - 4).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.MODIFIER);
            VariableType voidReturnType = new VariableType(JavaConstants.VARIABLE_TYPE_VOID, VariableType.VOID.getWidth());
            String methodId = context.getBrotherNodeList().get(currentIndex - 2).getIdToken().getToken();
            List<Param> paramList = (List<Param>) context.getBrotherNodeList().get(currentIndex - 1).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.PARAM_LIST);
            Stmt code = (Stmt) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

            ClazzMethod method = new ClazzMethod();
            method.setPermission(modifier);
            method.setReturnType(voidReturnType);
            method.setMethodName(methodId);
            method.setParamList(paramList);
            method.setCode(code);

            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.METHOD, method);

            return null;
        }
    }

    public static String product_3 = "methodBody → block";
    public static class MethodBodyListener extends SyntaxDirectedListener{
        public MethodBodyListener(){
            this.matchProductTag = product_3;
            this.matchSymbol = "block";
            this.matchIndex = 0;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Stmt code = (Stmt) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, code);

            return null;
        }
    }

}
