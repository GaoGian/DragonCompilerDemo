package gian.compiler.language.simplejava.utils;

import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.bean.VariableArrayType;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.env.JavaDirectGlobalProperty;
import gian.compiler.language.simplejava.exception.JavaDirectException;
import gian.compiler.language.simplejava.ast.Constant;
import gian.compiler.language.simplejava.ast.expression.*;
import gian.compiler.language.simplejava.ast.statement.*;

/**
 * 生成AST节点
 * Created by gaojian on 2019/3/31.
 */
public class JavaDirectUtils {

    public Stmt stmts(Stmt stmt, Stmt stmts){
        return new Seq(stmt, stmts);    // 这里通过递归，逐个解析后续语句
    }

    public static Switch switchNode(Expr expr, Stmt stmt){
        return new Switch(expr, stmt);
    }

    public static For forNode(Stmt init, Expr control, Stmt update){
        return new For(init, control, update);
    }

    public static If ifNode(Expr expr, Stmt stmt){
        return new If(expr, stmt);
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

    public static Stmt arrayAssign(String variableName, Access arrayInfo, Expr assign){
        Variable id = JavaDirectGlobalProperty.topEnv.getPropertyMap().get(variableName);
        if(id == null){
            error(variableName + " undeclared");
        }

        Stmt stmt = new SetElem(arrayInfo, assign);
        return stmt;
    }

    public static Stmt assign(String variableName, Expr assign){
        Variable id = JavaDirectGlobalProperty.topEnv.getPropertyMap().get(variableName);
        if(id == null){
            error(variableName + " undeclared");
        }

        Stmt stmt = new Set(id, assign);
        return stmt;
    }

    public static Access array(Variable variable, Expr factor, Access array){
        VariableType variableType = variable.getType();

        if(!(variableType instanceof VariableArrayType)){
            Expr width = new Constant(String.valueOf(variableType.getWidth()), VariableType.INT);
            Expr index = new Arith(JavaConstants.JAVA_OPERATOR_MULIT, factor, width);

            return new Access(variable, index, variableType);
        }else{
            Expr width = new Constant(String.valueOf(((VariableArrayType) variableType).getBaseVariableType().getWidth()), VariableType.INT);
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
        Variable variable = JavaDirectGlobalProperty.topEnv.getPropertyMap().get(variableName);
        return variable;
    }

    public static Variable variableDeclarate(String variableName, VariableType variableType){
        Variable variable = new Variable(variableName, variableType, null);
        JavaDirectGlobalProperty.topEnv.getPropertyMap().put(variableName, variable);
        return variable;
    }

    public static void error(String s){
        throw new JavaDirectException("near line " + JavaDirectGlobalProperty.lexline + ": " + s);
    }

}