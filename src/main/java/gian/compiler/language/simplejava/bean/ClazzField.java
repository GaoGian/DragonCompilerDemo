package gian.compiler.language.simplejava.bean;

import gian.compiler.language.simplejava.inter.AstNode;

/**
 * Created by gaojian on 2019/3/27.
 */
public class ClazzField extends Variable{

    protected String permission;
    public AstNode code;

    public ClazzField(String permission, String name, VariableType type, AstNode code){
        super(name, type);
        this.permission = permission;
        this.code = code;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public AstNode getCode() {
        return code;
    }

    public void setCode(AstNode code) {
        this.code = code;
    }

}