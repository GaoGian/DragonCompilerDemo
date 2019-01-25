package gian.compiler.practice.lexical.transform;

import java.util.Stack;

/**
 * Created by gaojian on 2019/1/25.
 */
public class MyStack<T> extends Stack {

    public T top(){
        return (T) super.elementData[0];
    }

}