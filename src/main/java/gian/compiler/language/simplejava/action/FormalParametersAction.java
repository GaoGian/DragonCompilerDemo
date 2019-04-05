package gian.compiler.language.simplejava.action;

import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.bean.Param;
import gian.compiler.language.simplejava.bean.VariableType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Gian on 2019/4/5.
 */
public class FormalParametersAction {

    public static String product_1 = "formalParameters → ( formalParameterDecls )";
    public static class ParameterListener extends SyntaxDirectedListener{
        public ParameterListener(){
            this.matchProductTag = product_1;
            this.matchSymbol = "formalParameterDecls";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            List<Param> paramList = (List<Param>) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.PARAM_LIST);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.PARAM_LIST, paramList);

            return null;
        }
    }

    public static String product_2 = "formalParameterDecls → typeDeclaration Identifier formalParameterDeclsRest";
    public static class ParameterDeclsListener extends SyntaxDirectedListener{
        public ParameterDeclsListener(){
            this.matchProductTag = product_2;
            this.matchSymbol = "formalParameterDeclsRest";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            List<Param> paramList = (List<Param>) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.PARAM_LIST);
            if(paramList == null){
                paramList = new ArrayList<>();
            }
            VariableType variableType = (VariableType) context.getBrotherNodeList().get(currentIndex - 2).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.VARIABLE_TYPE);
            String paramName = context.getBrotherNodeList().get(currentIndex - 1).getIdToken().getToken();
            Param param = new Param(paramName, variableType);
            paramList.add(param);
            Collections.reverse(paramList);

            context.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.PARAM_LIST, paramList);

            return null;
        }
    }

    public static String product_3 = "formalParameterDeclsRest → , typeDeclaration ◀Identifier▶ formalParameterDeclsRest";
    public static class ParameterDeclsRestListener extends SyntaxDirectedListener{
        public ParameterDeclsRestListener(){
            this.matchProductTag = product_2;
            this.matchSymbol = "formalParameterDeclsRest";
            this.matchIndex = 3;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            List<Param> paramList = (List<Param>) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.PARAM_LIST);
            if(paramList == null){
                paramList = new ArrayList<>();
            }
            VariableType variableType = (VariableType) context.getBrotherNodeList().get(currentIndex - 2).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.VARIABLE_TYPE);
            String paramName = context.getBrotherNodeList().get(currentIndex - 1).getIdToken().getToken();
            Param param = new Param(paramName, variableType);
            paramList.add(param);
            Collections.reverse(paramList);

            context.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.PARAM_LIST, paramList);

            return null;
        }
    }

}
