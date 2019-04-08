package gian.compiler.language.simplejava.utils;

import gian.compiler.front.lexical.parser.Token;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.ast.expression.Constant;
import gian.compiler.language.simplejava.ast.ref.*;
import gian.compiler.language.simplejava.bean.*;
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

    public static Stmt stmts(Stmt stmt, Stmt stmts){
        if(stmt != null && stmts != null) {
            return new Seq(stmt, stmts);    // 这里通过递归，逐个解析后续语句
        }else if(stmt != null && stmts == null){
            return stmt;
        }else if(stmt == null && stmts != null){
            return stmts;
        }else{
            throw new JavaDirectException("没有语义动作");
        }
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

    public static Stmt assign(String variableName, Expr assign){
        Variable id = JavaDirectUtils.findVariable(variableName);
        if(id == null){
            error(variableName + " undeclared");
        }

        Stmt stmt = new Set(id, assign);
        return stmt;
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

    public static Variable variableDeclarate(String variableName, VariableType variableType){
        Variable variable = new Variable(variableName, variableType, null);
        JavaDirectGlobalProperty.topEnv.getPropertyMap().put(variableName, variable);
        return variable;
    }

    public static ClazzField variableDeclarate(String permission, String variableName, VariableType variableType, Stmt code){
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

    public static NewArray newArray(VariableType baseType, VariableArrayType variableArrayType){
        return new NewArray(baseType, variableArrayType);
    }

    public static Temp temp(VariableType type){
        return new Temp(type);
    }

    public static void nestEnv(){
        JavaEnvironment preEnv = JavaDirectGlobalProperty.topEnv;
        JavaDirectGlobalProperty.topEnv = new JavaEnvironment(preEnv);
    }

    public static void exitEnv(){
        JavaDirectGlobalProperty.topEnv = JavaDirectGlobalProperty.topEnv.getPreEnv();
    }

    public static MethodRefNode methodRefNode(String callName, List<Expr> paramList){
        return new MethodRefNode(callName, paramList);
    }

    public static ConstructorRefNode constructorRefNode(String newClassName, List<Expr> paramList){
        return new ConstructorRefNode(newClassName, paramList);
    }

    public static FieldRefNode fieldRefNode(String fieldName){
        return new FieldRefNode(fieldName);
    }

    public static ThisRefNode thisRefNode(SyntaxDirectedContext context){
        String clazzName = (String) context.getGlobalPropertyMap().get(JavaConstants.CURRENT_CLAZZ_NAME);
        return new ThisRefNode(new VariableType(clazzName, VariableType.getVariableTypeWidth(JavaConstants.VARIABLE_TYPE_CLAZZ)));
    }

    public static SuperInitRefNode superInitRefNode(SyntaxDirectedContext context, List<Expr> paramList){
        String extendClazzName = (String) context.getGlobalPropertyMap().get(JavaConstants.EXTEND_INFO);
        return new SuperInitRefNode(new VariableType(extendClazzName, VariableType.getVariableTypeWidth(JavaConstants.VARIABLE_TYPE_CLAZZ)), paramList);
    }

    public static ArrayElementRefNode arrayElementRefNode(String callName, List<Expr> index){
        return new ArrayElementRefNode(callName, index);
    }

    public static StringJoin stringJoin(Expr expr1, Expr expr2){
        return new StringJoin(expr1, expr2);
    }

    public static void updateLastRef(RefNode preRefCall, RefNode updateRefNode){
        getLastRef(preRefCall).getPreRef().setNextRef(updateRefNode);
    }

    public static void appendRef(RefNode preRefCall, RefNode nextRef){
        getLastRef(preRefCall).setNextRef(nextRef);
    }

    // 获取引用链最后一个引用
    public static RefNode getLastRef(RefNode refCall){
        if(refCall.getNextRef() != null){
            return getLastRef(refCall.getNextRef());
        }else{
            return refCall;
        }
    }

    public static Param getParam(String paramName, VariableType variableType){
        return new Param(paramName, variableType);
    }

    public static void setMethodReturnType(VariableType returnVariableType){
        JavaDirectGlobalProperty.methodVariableType = returnVariableType;
    }

    public static void error(String s){
        throw new JavaDirectException("near line " + JavaDirectGlobalProperty.lexline + ": " + s);
    }

}