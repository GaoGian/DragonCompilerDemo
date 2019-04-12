package gian.compiler.language.simplejava.bean;

import gian.compiler.language.simplejava.ast.AstNode;
import gian.compiler.language.simplejava.ast.statement.Stmt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/3/27.
 */
public class ClazzConstructor {

    protected String permission;
    protected String constructorName;

    protected List<Param> paramList = new ArrayList<>();

    protected Stmt code;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getConstructorName() {
        return constructorName;
    }

    public void setConstructorName(String constructorName) {
        this.constructorName = constructorName;
    }

    public List<Param> getParamList() {
        if(paramList != null) {
            return paramList;
        }else{
            return new ArrayList<>();
        }
    }

    public void setParamList(List<Param> paramList) {
        this.paramList = paramList;
    }

    public Stmt getCode() {
        return code;
    }

    public void setCode(Stmt code) {
        this.code = code;
    }
}