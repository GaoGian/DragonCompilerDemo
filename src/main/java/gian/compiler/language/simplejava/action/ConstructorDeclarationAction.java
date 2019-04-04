package gian.compiler.language.simplejava.action;

import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.bean.ClazzConstructor;
import gian.compiler.language.simplejava.bean.Param;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.language.simplejava.inter.AstNode;

import java.util.List;

/**
 * Created by gaojian on 2019/3/28.
 */
public class ConstructorDeclarationAction {

    public static String product = "constructorDeclaration → modifierDeclaration Identifier formalParameters constructorBody";

    public static class ModifierDeclarationListener extends SyntaxDirectedListener{
        public ModifierDeclarationListener(){
            this.matchProductTag = product;
            this.matchSymbol = "modifierDeclaration";
            this.matchIndex = 0;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }
    }

    public static class IdentifierListener extends SyntaxDirectedListener{

        public IdentifierListener(){
            this.matchProductTag = product;
            this.matchSymbol = "Identifier";
            this.matchIndex = 1;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {

            return null;
        }
    }

    public static class FormalParametersListener extends SyntaxDirectedListener{

        public FormalParametersListener(){
            this.matchProductTag = product;
            this.matchSymbol = "formalParameters";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }
    }

    public static class ConstructorBodyListener extends SyntaxDirectedListener{

        public ConstructorBodyListener(){
            this.matchProductTag = product;
            this.matchSymbol = "constructorBody";
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
            String constructorName = context.getBrotherNodeList().get(currentIndex - 2).getIdToken().getToken();
            List<Param> paramList = (List<Param>) context.getBrotherNodeList().get(currentIndex - 1).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.PARAM_LIST);
            AstNode code = (AstNode) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

            ClazzConstructor constructor = new ClazzConstructor();
            constructor.setPermission(modifier);
            constructor.setConstructorName(constructorName);
            constructor.setParamList(paramList);
            constructor.setCode(code);

            List<ClazzConstructor> constructorList = (List<ClazzConstructor>) context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CONSTRUCTOR_LIST);
            constructorList.add(constructor);

            return null;
        }
    }

}