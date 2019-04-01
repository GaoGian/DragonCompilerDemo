package gian.compiler.language.simplejava.bean;

/**
 * Created by gaojian on 2019/3/27.
 */
public class ClazzField extends Variable {

    protected String permission;

    public ClazzField(String fieldName, VariableType variableType){
        super(fieldName, variableType);
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

}