package sample.book.lexer;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Real extends Token {

    public float value;

    public Real(float v){
        super(Tag.REAL);
        value = v;
    }

    @Override
    public String toString(){
        return "" + value;
    }

}
