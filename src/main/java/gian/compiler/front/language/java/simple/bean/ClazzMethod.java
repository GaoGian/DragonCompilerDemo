package gian.compiler.front.language.java.simple.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/3/27.
 */
public class ClazzMethod {

    private String permission;

    private Object returnType;
    private List<Object> paramList = new ArrayList<>();

    private List<Object> code = new ArrayList<>();

}