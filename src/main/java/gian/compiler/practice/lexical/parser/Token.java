package gian.compiler.practice.lexical.parser;

/**
 * Created by gaojian on 2019/1/30.
 */
public class Token {

    private String token;
    private LexExpression.TokenType type;

    public Token(String token, LexExpression.TokenType type) {
        this.token = token;
        this.type = type;
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

    @Override
    public String toString(){
        return token + "|" + type.getType();
    }

}