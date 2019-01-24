package gian.compiler.practice.lexical.token;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Num extends Token {

    public int value;

    public Num(int v){
        super(Tag.NUM);
        value = v;
    }

    @Override
    public String toString(){
        return "" + value;
    }

}
