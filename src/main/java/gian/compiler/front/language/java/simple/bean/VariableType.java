package gian.compiler.front.language.java.simple.bean;

import gian.compiler.front.language.java.simple.JavaConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据类型
 * 基本数据类型：
 * Class类型
 * 数组类型：
 * Created by gaojian on 2019/3/28.
 */
public class VariableType {

    // SimpleJava数据类型
    public static VariableType INT = new VariableType(JavaConstants.VARIABLE_TYPE_INT, null, false);
    public static VariableType LONG = new VariableType(JavaConstants.VARIABLE_TYPE_LONG, null, false);
    public static VariableType SHORT = new VariableType(JavaConstants.VARIABLE_TYPE_SHORT, null, false);
    public static VariableType FLOAT = new VariableType(JavaConstants.VARIABLE_TYPE_FLOAT, null, false);
    public static VariableType DOUBLE = new VariableType(JavaConstants.VARIABLE_TYPE_DOUBLE, null, false);
    public static VariableType CHAR = new VariableType(JavaConstants.VARIABLE_TYPE_CHAR, null, false);
    public static VariableType BYTE = new VariableType(JavaConstants.VARIABLE_TYPE_BYTE, null, false);
    public static VariableType BOOLEAN = new VariableType(JavaConstants.VARIABLE_TYPE_BOOLEAN, null, false);
    public static VariableType CLAZZ = new VariableType(JavaConstants.VARIABLE_TYPE_CLAZZ, null, false);
    public static VariableType VOID = new VariableType(JavaConstants.VARIABLE_TYPE_VOID, true);

    protected String name;
    protected boolean isVoid;
    protected boolean isArray;
    // 如果是数组的话代表下一维度
    protected VariableType arrayDimension;

    public VariableType(String name, boolean isVoid) {
        this.name = name;
        this.isVoid = isVoid;
    }

    public VariableType(String name, VariableType arrayDimension, boolean isArray) {
        this.name = name;
        this.isArray = isArray;
        this.arrayDimension = arrayDimension;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVoid() {
        return isVoid;
    }

    public void setVoid(boolean aVoid) {
        isVoid = aVoid;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean array) {
        isArray = array;
    }

    public VariableType getArrayDimension() {
        return arrayDimension;
    }

    public void setArrayDimension(VariableType arrayDimension) {
        this.arrayDimension = arrayDimension;
    }

}