package gian.compiler.practice.lexical.transform;

import java.util.Stack;

/**
 * Created by gaojian on 2019/1/25.
 */
public class MyStack<T> extends Stack<T> {

    public T top(){
        if(super.size() > 0) {
            return (T) super.elementData[super.size() - 1];
        }else{
            return (T) super.elementData[0];
        }
    }

}