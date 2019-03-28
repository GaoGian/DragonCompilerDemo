package gian.compiler.front.language.java.simple.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/3/27.
 */
public class ClazzMethod {

    private String permission;
    private VariableType returnType;
    private String methodName;
    private List<Object> paramList = new ArrayList<>();
    private List<Object> code = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClazzMethod that = (ClazzMethod) o;

        if (returnType != null ? !returnType.equals(that.returnType) : that.returnType != null) return false;
        if (methodName != null ? !methodName.equals(that.methodName) : that.methodName != null) return false;
        return paramList != null ? paramList.equals(that.paramList) : that.paramList == null;

    }

    @Override
    public int hashCode() {
        return 0;
    }
}