package gian.compiler.language.simplejava.bean;

import gian.compiler.language.simplejava.ast.AstNode;
import gian.compiler.language.simplejava.ast.statement.Stmt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/3/27.
 */
public class ClazzMethod {

    protected String permission;
    protected VariableType returnType;
    protected String methodName;
    protected List<Param> paramList = new ArrayList<>();
    protected Stmt code;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public VariableType getReturnType() {
        return returnType;
    }

    public void setReturnType(VariableType returnType) {
        this.returnType = returnType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<Param> getParamList() {
        return paramList;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClazzMethod that = (ClazzMethod) o;

        if (returnType != null ? !returnType.equals(that.returnType) : that.returnType != null) return false;
        if (methodName != null ? !methodName.equals(that.methodName) : that.methodName != null) return false;
        return paramList != null ? paramList.equals(that.paramList) : that.paramList == null;

    }

    @Override
    public int hashCode() {
        return 0;
    }
}