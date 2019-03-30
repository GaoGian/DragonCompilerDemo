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
    protected boolean isBaseType;
    protected boolean isArray;
    protected List<Integer> arrayDimension = new ArrayList<>();

    public VariableType(String name, boolean isBaseType, boolean isArray) {
        this.name = name;
        this.isBaseType = isBaseType;
        this.isArray = isArray;
    }

    public VariableType(String name, boolean isBaseType, boolean isArray, List<Integer> arrayDimension) {
        this.name = name;
        this.isBaseType = isBaseType;
        this.isArray = isArray;
        this.arrayDimension = arrayDimension;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBaseType() {
        return isBaseType;
    }

    public void setBaseType(boolean baseType) {
        isBaseType = baseType;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean array) {
        isArray = array;
    }

    public List<Integer> getArrayDimension() {
        return arrayDimension;
    }

    public void setArrayDimension(List<Integer> arrayDimension) {
        this.arrayDimension = arrayDimension;
    }
}