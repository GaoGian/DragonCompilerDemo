package gian.compiler.language.simplejava.env;

import gian.compiler.language.simplejava.bean.Variable;

import java.util.HashMap;
import java.util.Map;

/**
 * 变量作用域
 * Created by gaojian on 2019/3/24.
 */
public class JavaEnvironment {

    protected JavaEnvironment preEnv;
    // 存放作用域内的属性, key：变量名，value：变量
    protected Map<String, Variable> propertyMap = new HashMap<>();

    public JavaEnvironment(){}

    public JavaEnvironment(JavaEnvironment preEnv) {
        this.preEnv = preEnv;
    }

    // 在作用域内查找变量
    // TODO 需要考虑 this.xxx 的情况
    public Variable findVariable(String variableId){
        if(this.propertyMap.get(variableId) != null){
            return this.propertyMap.get(variableId);
        }else{
            if(this.preEnv != null){
                return this.preEnv.getPropertyMap().get(variableId);
            }else{
                return null;
            }
        }
    }

    public JavaEnvironment getPreEnv() {
        return preEnv;
    }

    public void setPreEnv(JavaEnvironment preEnv) {
        this.preEnv = preEnv;
    }

    public Map<String, Variable> getPropertyMap() {
        return propertyMap;
    }

    public void setPropertyMap(Map<String, Variable> propertyMap) {
        this.propertyMap = propertyMap;
    }
}