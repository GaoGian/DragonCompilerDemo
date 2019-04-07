package gian.compiler.language.simplejava.action;

import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;
import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.ast.Constant;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.env.JavaDirectGlobalProperty;
import gian.compiler.language.simplejava.ast.expression.Expr;
import gian.compiler.language.simplejava.ast.statement.*;
import gian.compiler.language.simplejava.utils.JavaDirectUtils;

/**
 * Created by gaojian on 2019/4/2.
 */
public class StatementAction {

    // TODO 需要处理控制流
    public static String product_1 = "statement → if ( parExpression ) block elseStatement";
    public static class IfListener extends SyntaxDirectedListener{
        public IfListener(){
            this.matchProductTag = product_1;
            this.matchSymbol = "elseStatement";
            this.matchIndex = 5;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Expr parExpr = (Expr) context.getBrotherNodeList().get(currentIndex - 3).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            Stmt ifStmt = (Stmt) context.getBrotherNodeList().get(currentIndex - 1).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            Stmt elseStmt = (Stmt) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            
            Stmt stmt = null;
            // TODO 需要处理控制流
            if(elseStmt == null){
                stmt = JavaDirectUtils.ifNode(parExpr, ifStmt);
            }else{
                stmt = JavaDirectUtils.elseNode(parExpr, ifStmt, elseStmt);
            }
            
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, stmt);
            
            return null;
        }
    }
    
    public static String product_1_1 = "elseStatement → else block";
    public static class ElseListener extends SyntaxDirectedListener{
        public ElseListener(){
            this.matchProductTag = product_1_1;
            this.matchSymbol = "block";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Stmt stmt = (Stmt) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, stmt);

            return null;
        }
    }

    public static String product_1_2 = "elseStatement → else if ( parExpression ) block elseStatement";
    public static class ElseIfListener extends SyntaxDirectedListener{
        public ElseIfListener(){
            this.matchProductTag = product_1_2;
            this.matchSymbol = "elseStatement";
            this.matchIndex = 6;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Expr parExpr = (Expr) context.getBrotherNodeList().get(currentIndex - 3).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            Stmt ifStmt = (Stmt) context.getBrotherNodeList().get(currentIndex - 1).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            Stmt elseStmt = (Stmt) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

            Stmt stmt = null;
            // TODO 需要处理控制流
            if(elseStmt == null){
                stmt = JavaDirectUtils.ifNode(parExpr, ifStmt);
            }else{
                stmt = JavaDirectUtils.elseNode(parExpr, ifStmt, elseStmt);
            }

            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, stmt);

            return null;
        }
    }
    
    public static String product_2 = "statement → for ( forControl ) block";
    public static class ForEnterListener extends SyntaxDirectedListener{
        public ForEnterListener(){
            this.matchProductTag = product_2;
            this.matchSymbol = "for";
            this.matchIndex = 0;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            JavaDirectUtils.nestEnv();

            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }
    }
    public static class ForListener extends SyntaxDirectedListener{
        public ForListener(){
            this.matchProductTag = product_2;
            this.matchSymbol = "block";
            this.matchIndex = 4;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            For forNode = new For();
            currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).put(JavaConstants.FOR_NODE, forNode);
            // 设置最近循环
            JavaDirectGlobalProperty.cycleEnclosingStack.push(forNode);
            
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            SyntaxTree.SyntaxTreeNode controlNode = context.getBrotherNodeList().get(currentIndex - 2);
            Stmt initStmt = (Stmt) controlNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.FOR_INIT_CODE);
            Expr controlExpr = (Expr) controlNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.FOR_CONTROL_CODE);
            Stmt updateStmt = (Stmt) controlNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.FOR_UPDATE_CODE);
            Stmt blockStmt = (Stmt) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

            For forCode = (For) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).get(JavaConstants.FOR_NODE);
            forCode.init(initStmt, controlExpr, updateStmt, blockStmt);

            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, forCode);

            // 跳出当前循环
            JavaDirectGlobalProperty.cycleEnclosingStack.pop();
            return null;
        }
    }
    public static class ForExitListener extends SyntaxDirectedListener{
        public ForExitListener(){
            this.matchProductTag = product_2;
            this.matchSymbol = "block";
            this.matchIndex = 4;
            this.isLeaf = false;
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

    public static String product_2_1 = "forControl → forInit ; parExpression ; forUpdate";
    public static class ForControlListener extends SyntaxDirectedListener{
        public ForControlListener(){
            this.matchProductTag = product_2_1;
            this.matchSymbol = "forUpdate";
            this.matchIndex = 4;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Stmt initStmt = (Stmt) context.getBrotherNodeList().get(currentIndex - 4).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            Expr controlExpr = (Expr) context.getBrotherNodeList().get(currentIndex - 2).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            Stmt updateStmt = (Stmt) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

            context.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.FOR_INIT_CODE, initStmt);
            context.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.FOR_CONTROL_CODE, controlExpr);
            context.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.FOR_UPDATE_CODE, updateStmt);

            return null;
        }
    }

    public static String product_2_2 = "forInit → localVariableDeclarationStatement";
    public static class ForInitListener extends SyntaxDirectedListener{
        public ForInitListener(){
            this.matchProductTag = product_2_2;
            this.matchSymbol = "localVariableDeclarationStatement";
            this.matchIndex = 0;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Stmt stmt = (Stmt) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, stmt);

            return null;
        }
    }

    public static String product_2_3 = "forUpdate → expression";
    public static class ForUpdateListener extends SyntaxDirectedListener{
        public ForUpdateListener(){
            this.matchProductTag = product_2_3;
            this.matchSymbol = "expression";
            this.matchIndex = 0;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Expr expr = (Expr) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, expr);

            return null;
        }
    }

    public static String product_3 = "statement → while ( parExpression ) block";
    public static class WhileListener extends SyntaxDirectedListener{
        public WhileListener(){
            this.matchProductTag = product_3;
            this.matchSymbol = "block";
            this.matchIndex = 4;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            While whileNode = new While();
            currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).put(JavaConstants.WHILE_NODE, whileNode);
            // 设置最近循环
            JavaDirectGlobalProperty.cycleEnclosingStack.push(whileNode);

            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Expr parExpr = (Expr) context.getBrotherNodeList().get(currentIndex - 2).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            Stmt stmt = (Stmt) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

            While whileNode = (While) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).get(JavaConstants.WHILE_NODE);
            whileNode.init(parExpr, stmt);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, whileNode);

            // 跳出当前循环
            JavaDirectGlobalProperty.cycleEnclosingStack.pop();

            return null;
        }
    }

    public static String product_4 = "statement → do block while ( parExpression ) ;";
    public static class DoListener extends SyntaxDirectedListener{
        public DoListener(){
            this.matchProductTag = product_4;
            this.matchSymbol = "parExpression";
            this.matchIndex = 4;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            // TODO 是否要放在 parExpression 之前
            Do doNode = new Do();
            currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).put(JavaConstants.DO_NODE, doNode);
            // 设置最近循环
            JavaDirectGlobalProperty.cycleEnclosingStack.push(doNode);

            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Stmt stmt = (Stmt) context.getBrotherNodeList().get(currentIndex - 2).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            Expr parExpr = (Expr) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

            Do doNode = (Do) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).get(JavaConstants.DO_NODE);
            doNode.init(stmt, parExpr);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, doNode);

            // 跳出当前循环
            JavaDirectGlobalProperty.cycleEnclosingStack.pop();

            return null;
        }
    }

    // TODO
    public static String product_5 = "statement → switch ( expression ) switchBlock";
    public static class SwitchListener extends SyntaxDirectedListener{
        public SwitchListener(){
            this.matchProductTag = product_5;
            this.matchSymbol = "switchBlock";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Expr expr = (Expr) context.getBrotherNodeList().get(currentIndex - 2).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            // TODO 需要获取表达式返回的临时变量
            Variable result = expr.variable;
            currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).put(JavaConstants.EXPR_RESULT, result);

            Switch switchNode = new Switch();
            currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).put(JavaConstants.SWITCH_NODE, switchNode);
            
            // 设置最近循环
            JavaDirectGlobalProperty.cycleEnclosingStack.push(switchNode);

            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Expr expr = (Expr) context.getBrotherNodeList().get(currentIndex - 2).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            Stmt caseStmt = (Stmt) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

            Switch switchNode = (Switch) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).get(JavaConstants.SWITCH_NODE);
            switchNode.init(expr, caseStmt);
            
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, switchNode);

            // 跳出当前循环
            JavaDirectGlobalProperty.cycleEnclosingStack.pop();
            return null;
        }
    }
    
    public static String product_5_1 = "switchBlock → { switchBlockStatementGroup defaultSwitchLabel }";
    public static class SwitchBlockEnterListener extends SyntaxDirectedListener{
        public SwitchBlockEnterListener(){
            this.matchProductTag = product_5_1;
            this.matchSymbol = "switchBlockStatementGroup";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            JavaDirectUtils.nestEnv();

            Variable result = (Variable) context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).get(JavaConstants.EXPR_RESULT);
            currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).put(JavaConstants.EXPR_RESULT, result);
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }
    }
    public static class SwitchBlockListener extends SyntaxDirectedListener{
        public SwitchBlockListener(){
            this.matchProductTag = product_5_1;
            this.matchSymbol = "defaultSwitchLabel";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Stmt caseStmt = (Stmt) context.getBrotherNodeList().get(currentIndex - 1).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            Stmt defaultStmt = (Stmt) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            Stmt code = null;
            if(caseStmt != null && defaultStmt != null) {
                code = JavaDirectUtils.stmts(caseStmt, defaultStmt);
            }else if(caseStmt != null && defaultStmt == null){
                code = caseStmt;
            }else if(caseStmt == null && defaultStmt != null){
                code = defaultStmt;
            }

            if(code != null) {
                context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, code);
            }

            return null;
        }
    }
    public static class SwitchBlockExitListener extends SyntaxDirectedListener{
        public SwitchBlockExitListener(){
            this.matchProductTag = product_5_1;
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

    public static String product_5_2 = "switchBlockStatementGroup → switchLabel blockStatement switchBlockStatementGroup";
    public static class SwitchLableListener extends SyntaxDirectedListener{
        public SwitchLableListener(){
            this.matchProductTag = product_5_2;
            this.matchSymbol = "switchLabel";
            this.matchIndex = 0;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Variable result = (Variable) context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).get(JavaConstants.EXPR_RESULT);
            currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).put(JavaConstants.EXPR_RESULT, result);
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }
    }
    public static class SwitchCaseListener extends SyntaxDirectedListener{
        public SwitchCaseListener(){
            this.matchProductTag = product_5_2;
            this.matchSymbol = "switchBlockStatementGroup";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Variable result = (Variable) context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).get(JavaConstants.EXPR_RESULT);
            currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).put(JavaConstants.EXPR_RESULT, result);
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Expr caseExpr = (Expr) context.getBrotherNodeList().get(currentIndex - 2).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            Stmt stmt = (Stmt) context.getBrotherNodeList().get(currentIndex - 1).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            Case caseStmt = JavaDirectUtils.caseNode(caseExpr, stmt);
            Stmt caseStmts = (Stmt) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

            Stmt code = null;
            if(caseStmts != null){
                code = JavaDirectUtils.stmts(caseStmt, caseStmts);
            }else{
                code = caseStmt;
            }

            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, code);
            return null;
        }
    }

    public static String product_5_3_1 = "switchLabel → case Digit";
    public static class CaseDigitListener extends SyntaxDirectedListener{
        public CaseDigitListener(){
            this.matchProductTag = product_5_3_1;
            this.matchSymbol = "Digit";
            this.matchIndex = 1;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Variable result = (Variable) context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).get(JavaConstants.EXPR_RESULT);
            Constant caseConstant = JavaDirectUtils.constant(currentTreeNode.getIdToken());

            Expr caseExpr = JavaDirectUtils.rel(result, caseConstant, JavaConstants.JAVA_OPERATOR_EQ);

            context.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, caseExpr);

            return null;
        }
    }

    public static String product_5_3_2 = "switchLabel → case Number :";
    public static class CaseNumberListener extends SyntaxDirectedListener{
        public CaseNumberListener(){
            this.matchProductTag = product_5_3_2;
            this.matchSymbol = "Number";
            this.matchIndex = 1;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Variable result = (Variable) context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).get(JavaConstants.EXPR_RESULT);
            Constant caseConstant = JavaDirectUtils.constant(currentTreeNode.getIdToken());

            Expr caseExpr = JavaDirectUtils.rel(result, caseConstant, JavaConstants.JAVA_OPERATOR_EQ);

            context.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, caseExpr);

            return null;
        }
    }

    public static String product_5_3_3 = "switchLabel → case String :";
    public static class CaseStringListener extends SyntaxDirectedListener{
        public CaseStringListener(){
            this.matchProductTag = product_5_3_3;
            this.matchSymbol = "String";
            this.matchIndex = 1;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Variable result = (Variable) context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_INH).get(JavaConstants.EXPR_RESULT);
            Constant caseConstant = JavaDirectUtils.constant(currentTreeNode.getIdToken());

            Expr caseExpr = JavaDirectUtils.rel(result, caseConstant, JavaConstants.JAVA_OPERATOR_EQ);

            context.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, caseExpr);

            return null;
        }
    }

    public static String product_5_4 = "defaultSwitchLabel → default : blockStatement";
    public static class DefaultCaseListener extends SyntaxDirectedListener{
        public DefaultCaseListener(){
            this.matchProductTag = product_5_4;
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
            Stmt defaultStmt = (Stmt) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            context.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, defaultStmt);

            return null;
        }
    }

    public static String product_6 = "statement → refVariable = expression ;";
    public static class VariableAssignListener extends SyntaxDirectedListener{
        public VariableAssignListener(){
            this.matchProductTag = product_6;
            this.matchSymbol = "expression";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            // TODO 需要根据 refVariable 的监听器修改
            Variable variable = (Variable) context.getBrotherNodeList().get(currentIndex - 2).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.VARIABLE);
            Expr expr = (Expr) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

            Stmt assign = JavaDirectUtils.assign(variable.getName(), expr);

            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, assign);

            return null;
        }
    }

    public static String product_7 = "statement → return expression ;";
    public static class ReturnListener extends SyntaxDirectedListener{
        public ReturnListener(){
            this.matchProductTag = product_7;
            this.matchSymbol = "expression";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Expr expr = (Expr) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);

            Return returnNode = JavaDirectUtils.returnNode(expr);

            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, returnNode);

            return null;
        }
    }

    public static String product_8 = "statement → break ;";
    public static class BreakListener extends SyntaxDirectedListener{
        public BreakListener(){
            this.matchProductTag = product_8;
            this.matchSymbol = "break";
            this.matchIndex = 0;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Break breakNode = JavaDirectUtils.breakNode();
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, breakNode);

            return null;
        }
    }

    public static String product_9 = "statement → continue ;";
    public static class ContinueListener extends SyntaxDirectedListener{
        public ContinueListener(){
            this.matchProductTag = product_9;
            this.matchSymbol = "continue";
            this.matchIndex = 0;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Continue continueNode = JavaDirectUtils.continueNode();
            context.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, continueNode);

            return null;
        }
    }

    public static String product_10 = "statement → expression ;";
    public static class ExprListener extends SyntaxDirectedListener{
        public ExprListener(){
            this.matchProductTag = product_10;
            this.matchSymbol = "expression";
            this.matchIndex = 0;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Expr code = (Expr) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CODE);
            context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.CODE, code);

            return null;
        }
    }

}