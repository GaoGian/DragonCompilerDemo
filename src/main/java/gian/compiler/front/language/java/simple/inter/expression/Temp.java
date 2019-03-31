package gian.compiler.front.language.java.simple.inter.expression;


import gian.compiler.front.language.java.simple.bean.VariableType;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Temp extends Expr {

    // TODO 需要调整临时变量编号生成方式
    public static int count = 0;
    public int number = 0;

    public Temp(Integer lexline, VariableType p){
        super(lexline, p);
        number = ++count;
    }

    public String toString(){
        return "t" + number;
    }

}
