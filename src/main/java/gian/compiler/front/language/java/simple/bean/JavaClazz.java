package gian.compiler.front.language.java.simple.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gaojian on 2019/3/27.
 */
public class JavaClazz {

    protected String packageName;
    protected List<String> importList = new ArrayList<>();
    protected Map<String, String> importMap = new HashMap<>();

    protected String permission;

    protected String clazzName;
    protected String clazzAllName;

    protected Map<String, String> extendInfo = new HashMap<>();

    protected List<ClazzField> fieldList = new ArrayList<>();
    protected List<ClazzConstructor> constructorList = new ArrayList<>();
    protected List<ClazzMethod> methodList = new ArrayList<>();

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<String> getImportList() {
        return importList;
    }

    public void setImportList(List<String> importList) {
        this.importList = importList;
    }

    public Map<String, String> getImportMap() {
        return importMap;
    }

    public void setImportMap(Map<String, String> importMap) {
        this.importMap = importMap;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public String getClazzAllName() {
        return clazzAllName;
    }

    public void setClazzAllName(String clazzAllName) {
        this.clazzAllName = clazzAllName;
    }

    public Map<String, String> getExtendInfo() {
        return extendInfo;
    }

    public void setExtendInfo(Map<String, String> extendInfo) {
        this.extendInfo = extendInfo;
    }

    public List<ClazzField> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<ClazzField> fieldList) {
        this.fieldList = fieldList;
    }

    public List<ClazzConstructor> getConstructorList() {
        return constructorList;
    }

    public void setConstructorList(List<ClazzConstructor> constructorList) {
        this.constructorList = constructorList;
    }

    public List<ClazzMethod> getMethodList() {
        return methodList;
    }

    public void setMethodList(List<ClazzMethod> methodList) {
        this.methodList = methodList;
    }
}