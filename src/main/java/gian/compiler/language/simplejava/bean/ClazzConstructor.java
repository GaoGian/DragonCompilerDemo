package gian.compiler.language.simplejava.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/3/27.
 */
public class ClazzConstructor {

    protected String permission;
    protected String constructorName;

    protected List<Variable> paramList = new ArrayList<>();

    protected List<String> code = new ArrayList<>();

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

    public List<Variable> getParamList() {
        return paramList;
    }

    public void setParamList(List<Variable> paramList) {
        this.paramList = paramList;
    }

    public List<String> getCode() {
        return code;
    }

    public void setCode(List<String> code) {
        this.code = code;
    }
}