package sample.book.main;

import sample.book.lexer.Lexer;
import sample.book.parser.Parser;

import java.io.IOException;

/**
 * Created by tingyun on 2018/7/20.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Lexer lex = new Lexer();
        Parser parser = new Parser(lex);
        parser.program();
        System.out.write('\n');
    }

}
