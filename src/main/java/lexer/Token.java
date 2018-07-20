package lexer;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Token {

    public int tag;

    public Token(){};

    public Token(int t){
        this.tag = t;
    }

    @Override
    public String toString(){
        return "" + (char)tag;
    }

}
