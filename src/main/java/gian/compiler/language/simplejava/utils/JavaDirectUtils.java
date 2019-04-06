package gian.compiler.language.simplejava.utils;

import gian.compiler.front.lexical.parser.Token;
import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.ast.AstNode;
import gian.compiler.language.simplejava.ast.Constant;
import gian.compiler.language.simplejava.ast.ref.SuperInitRefNode;
import gian.compiler.language.simplejava.bean.ClazzField;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableArrayType;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.env.JavaDirectGlobalProperty;
import gian.compiler.language.simplejava.env.JavaEnvironment;
import gian.compiler.language.simplejava.exception.JavaDirectException;
import gian.compiler.language.simplejava.ast.expression.*;
import gian.compiler.language.simplejava.ast.statement.*;

import java.util.List;

/**
 * 生成AST节点
 * Created by gaojian on 2019/3/31.
 */
public class JavaDirectUtils {

    public static Seq stmts(Stmt stmt, Stmt stmts){
        return new Seq(stmt, stmts);    // 这里通过递归，逐个解析后续语句
    }

    public static Switch switchNode(Expr expr, Stmt stmt){
        return new Switch(expr, stmt);
    }

    public static Case caseNode(Expr expr, Stmt stmt){
        return new Case(expr, stmt);
    }

    public static For forNode(Stmt init, Expr control, Stmt update, Stmt blockStmt){
        return new For(init, control, update, blockStmt);
    }

    public static If ifNode(Expr expr, Stmt stmt){
        return new If(expr, stmt);
    }

    public static Else elseNode(Expr expr, Stmt trueStmt, Stmt falseStmt){
        return new Else(expr, trueStmt, falseStmt);
    }

    public static While whileNode(Expr expr, Stmt stmt){
        While whileNode = new While();
        whileNode.init(expr, stmt);
        return whileNode;
    }

    public static Do doNode(Stmt stmt, Expr expr){
        Do doNode = new Do();
        doNode.init(stmt, expr);
        return doNode;
    }

    public static Break breakNode(){
        return new Break();
    }

    public static Continue continueNode(){
        return new Continue();
    }

    public static Return returnNode(Expr expr){
        return new Return(expr);
    }

    public static Stmt arrayAssign(String variableName, Access arrayInfo, Expr assign){
        Variable id = JavaDirectUtils.findVariable(variableName);
        if(id == null){
            error(variableName + " undeclared");
        }

        Stmt stmt = new SetElem(arrayInfo, assign);
        return stmt;
    }

    public static Stmt assign(String variableName, Expr assign){
        Variable id = JavaDirectUtils.findVariable(variableName);
        if(id == null){
            error(variableName + " undeclared");
        }

        Stmt stmt = new Set(id, assign);
        return stmt;
    }

    public static Access array(Variable variable, Expr factor, Access array){
        VariableType variableType = variable.getType();

        if(!(variableType instanceof VariableArrayType)){
            Expr width = new Temp(variableType);
            Expr index = new Arith(JavaConstants.JAVA_OPERATOR_MULIT, factor, width);

            return new Access(variable, index, variableType);
        }else{
            Expr width = new Temp(variableType);
            Expr index_1 = new Arith(JavaConstants.JAVA_OPERATOR_MULIT, factor, width);
            Expr index_2 = new Arith(JavaConstants.JAVA_OPERATOR_ADD, array.index, index_1);

            return new Access(variable, index_2, ((VariableArrayType) variableType).getBaseVariableType());
        }

    }

    public static Expr or(Expr expr1, Expr expr2){     // 判断布尔值表达式
        Expr expr = new Or(JavaConstants.JAVA_OPERATOR_OR, expr1, expr2);
        return expr;
    }

    public static Expr and(Expr expr1, Expr expr2){
        Expr expr = new And(JavaConstants.JAVA_OPERATOR_JOIN, expr1, expr2);
        return expr;
    }

    public static Expr rel(Expr lexpr, Expr rexpr, String rel){
        return new Rel(rel, lexpr, rexpr);
    }

    public static Expr term(Expr lvariable, Expr rvariable, String op){
        if(op != null){
            Expr expr = new Arith(op, lvariable, rvariable);
            return expr;
        }else{
            return lvariable;
        }
    }

    public static Variable factor(String variableName){
        // TODO 需要考虑变量引用链的情况
        return JavaDirectUtils.findVariable(variableName);
    }

    public static NewArray newArray(VariableType baseType, VariableArrayType variableArrayType){
        return new NewArray(baseType, variableArrayType);
    }

    public static Variable variableDeclarate(String variableName, VariableType variableType){
        Variable variable = new Variable(variableName, variableType, null);
        JavaDirectGlobalProperty.topEnv.getPropertyMap().put(variableName, variable);
        return variable;
    }

    public static ClazzField variableDeclarate(String permission, String variableName, VariableType variableType, AstNode code){
        ClazzField clazzField = new ClazzField(permission, variableName, variableType, code);
        JavaDirectGlobalProperty.topEnv.getPropertyMap().put(variableName, clazzField);
        return clazzField;
    }

    public static Variable findVariable(String variableName){
        return JavaDirectGlobalProperty.topEnv.findVariable(variableName);
    }

    public static Constant constant(Token token){
        return new Constant(token.getToken(), new VariableType(token.getToken(), VariableType.getVariableTypeWidth(token.getType().getType())));
    }

    public static SuperInitRefNode superInitRefNode(List<Variable> paramList){
        return new SuperInitRefNode(paramList);
    }

    public static void nestEnv(){
        JavaEnvironment preEnv = JavaDirectGlobalProperty.topEnv;
        JavaDirectGlobalProperty.topEnv = new JavaEnvironment(preEnv);
    }

    public static void exitEnv(){
        JavaDirectGlobalProperty.topEnv = JavaDirectGlobalProperty.topEnv.getPreEnv();
    }

    public static void error(String s){
        throw new JavaDirectException("near line " + JavaDirectGlobalProperty.lexline + ": " + s);
    }

}