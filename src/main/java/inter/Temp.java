package inter;

import lexer.Word;
import symbols.Type;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Temp extends Expr {

    public static int count = 0;
    public int number = 0;

    public Temp(Type p){
        super(Word.temp, p);
        number = ++ count;
    }

    public String toString(){
        return "t" + number;
    }

}
