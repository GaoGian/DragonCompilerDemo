package gian.compiler.language.simplejava.inter.expression;


import gian.compiler.language.simplejava.bean.VariableType;

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
