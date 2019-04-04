package gian.compiler.language.simplejava.action;

import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.ast.ref.SuperInitRefNode;
import gian.compiler.language.simplejava.bean.Clazz;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.language.simplejava.utils.JavaDirectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gaojian on 2019/3/31.
 */
public class ExplicitConstructorInvocationAction {

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
            List<Variable> paramList = (List<Variable>) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CALL_PARAM_LIST);
            SuperInitRefNode superInitRefNode = JavaDirectUtils.superInitRefNode(paramList);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, superInitRefNode);

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
            SuperInitRefNode superInitRefNode = JavaDirectUtils.superInitRefNode(new ArrayList<>());
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, superInitRefNode);
            return null;
        }
    }

    public static List<SyntaxDirectedListener> getAllListener(){
        List<SyntaxDirectedListener> allListener = new ArrayList<>();
        allListener.add(new ExplicitConstructorListener());
        allListener.add(new InexplicitConstructorListener());

        return allListener;
    }

}