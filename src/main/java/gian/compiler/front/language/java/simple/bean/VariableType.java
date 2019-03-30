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