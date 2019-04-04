package gian.compiler.language.simplejava.bean;

import gian.compiler.language.simplejava.ast.AstNode;

/**
 * Created by gaojian on 2019/3/27.
 */
public class ClazzField extends Variable{

    protected String permission;

    public ClazzField(String permission, String name, VariableType type, AstNode code){
        super(name, type, code);
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

}