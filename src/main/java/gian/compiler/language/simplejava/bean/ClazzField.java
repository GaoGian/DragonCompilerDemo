package gian.compiler.language.simplejava.bean;

import gian.compiler.language.simplejava.ast.statement.Stmt;

/**
 * Created by gaojian on 2019/3/27.
 */
public class ClazzField extends Variable{

    protected String permission;

    public ClazzField(String permission, String name, VariableType type, Stmt code){
        super(name, type, code);
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String toString(){
        return "classField: type_" + declType.getName() + "-name_" + this.name;
    }

}