package gian.compiler.language.simplejava.bean;

/**
 * Created by gaojian on 2019/3/27.
 */
public class ClazzField{

    protected String permission;
    protected Variable variable;

    public ClazzField(String permission, Variable variable){
        this.permission = permission;
        this.variable = variable;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }
}