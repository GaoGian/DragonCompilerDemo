package gian.compiler.practice.lexical.parser;

/**
 * Created by gaojian on 2019/1/30.
 */
public class Token {

    private String token;
    private LexExpression.TokenType type;

    // 文本位置信息
    private int index;
    private int line;

    public Token(String token, LexExpression.TokenType type) {
        this.token = token;
        this.type = type;
    }

    public Token(String token, LexExpression.TokenType type, int index, int line) {
        this.token = token;
        this.type = type;
        this.index = index;
        this.line = line;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LexExpression.TokenType getType() {
        return type;
    }

    public void setType(LexExpression.TokenType type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public String toString(){
        return "<'" + token + "', " + type.getType() + ", " + line + ", " + index + ">;";
//        return "'" + token + "'";
    }

}