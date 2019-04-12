package gian.compiler.language.simplejava.action;

import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.ast.statement.Stmt;
import gian.compiler.language.simplejava.bean.ClazzConstructor;
import gian.compiler.language.simplejava.bean.Param;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.language.simplejava.ast.AstNode;
import gian.compiler.language.simplejava.env.JavaDirectGlobalProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/3/28.
 */
public class ConstructorDeclarationAction {

    public static String product = "constructorDeclaration → modifierDeclaration Identifier formalParameters constructorBody";

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
            String modifier = (String) context.getBrotherNodeList().get(currentIndex - 3).getSynProperty(JavaConstants.MODIFIER);
            String constructorName = context.getBrotherNodeList().get(currentIndex - 2).getIdToken().getToken();
            List<Param> paramList = (List<Param>) context.getBrotherNodeList().get(currentIndex - 1).getSynProperty(JavaConstants.PARAM_LIST);
            Stmt code = (Stmt) currentTreeNode.getSynProperty(JavaConstants.CODE);

            ClazzConstructor constructor = new ClazzConstructor();
            constructor.setPermission(modifier);
            constructor.setConstructorName(constructorName);
            constructor.setParamList(paramList);
            constructor.setCode(code);

            JavaDirectGlobalProperty.constructorList.add(constructor);

            return null;
        }
    }

    public static List<SyntaxDirectedListener> getAllListener() {
        List<SyntaxDirectedListener> allListener = new ArrayList<>();
        allListener.add(new ConstructorBodyListener());
        return allListener;
    }
}