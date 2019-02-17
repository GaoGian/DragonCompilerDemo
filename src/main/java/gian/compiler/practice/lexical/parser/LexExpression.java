package gian.compiler.practice.lexical.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tingyun on 2018/7/20.
 */
public class LexExpression {

    public static List<Expression> expressions = new ArrayList<>();

    static {

        // 数据类型
        expressions.add(new Expression("int", TokenType.TYPE, false));
        expressions.add(new Expression("float", TokenType.TYPE, false));
        expressions.add(new Expression("String", TokenType.TYPE, false));

        // 关键词
        expressions.add(new Expression("if", TokenType.KEYWORD, false));
        expressions.add(new Expression("while", TokenType.KEYWORD, false));
        expressions.add(new Expression("do", TokenType.KEYWORD, false));
        expressions.add(new Expression("break", TokenType.KEYWORD, false));
        expressions.add(new Expression("true", TokenType.KEYWORD, false));
        expressions.add(new Expression("false", TokenType.KEYWORD, false));

        // 标识符
        expressions.add(new Expression("[A-Za-z]\\w*", TokenType.ID, false));

        // 数字
        expressions.add(new Expression("\\d+(\\.\\d+)?", TokenType.NUMBER, false));

        // 操作符
        expressions.add(new Expression(">", TokenType.OPERATOR, false));
        expressions.add(new Expression("<", TokenType.OPERATOR, false));
        expressions.add(new Expression("=", TokenType.OPERATOR, false));
        expressions.add(new Expression(">=", TokenType.OPERATOR, false));
        expressions.add(new Expression(">=", TokenType.OPERATOR, false));
        expressions.add(new Expression("==", TokenType.OPERATOR, false));
        expressions.add(new Expression("!=", TokenType.OPERATOR, false));
        expressions.add(new Expression("&&", TokenType.OPERATOR, false));
        expressions.add(new Expression("\\|\\|", TokenType.OPERATOR, false));
        expressions.add(new Expression("\\+", TokenType.OPERATOR, false));
        expressions.add(new Expression("-", TokenType.OPERATOR, false));
        expressions.add(new Expression("\\*", TokenType.OPERATOR, false));
        expressions.add(new Expression("/", TokenType.OPERATOR, false));
        expressions.add(new Expression("\\+\\+", TokenType.OPERATOR, false));
        expressions.add(new Expression("--", TokenType.OPERATOR, false));

        // 分隔符
        expressions.add(new Expression("\\{", TokenType.SEPARATOR, false));
        expressions.add(new Expression("\\}", TokenType.SEPARATOR, false));
        expressions.add(new Expression("\\(", TokenType.SEPARATOR, false));
        expressions.add(new Expression("\\)", TokenType.SEPARATOR, false));
        expressions.add(new Expression("\\[", TokenType.SEPARATOR, false));
        expressions.add(new Expression("\\]", TokenType.SEPARATOR, false));

        // 标点符号
        expressions.add(new Expression(",", TokenType.PUNCTUATION, false));
        expressions.add(new Expression(";", TokenType.PUNCTUATION, false));

        // 分隔符
        expressions.add(new Expression("\\s+", TokenType.SECTION, true));

    }

    public static enum TokenType {
        KEYWORD("keyword"), ID("id"), TYPE("type"), NUMBER("number"),
        OPERATOR("operator"), SEPARATOR("separator"),
        PUNCTUATION("punctuation"), SECTION("section");

        private String type;

        private TokenType(String type){
            this.type = type;
        }

        public String getType(){
            return this.type;
        }

    }

    // 词法单元匹配的正则表达式
    public static class Expression {

        private String expression;
        private TokenType type;

        // 准许后面跟着的字符表达式
        private String behindExpression;

        // 这里把识别出token后的action动作用布尔值代替，只需要判断出是否是分隔符就行
        private boolean isEmpty;

        public Expression() {

        }

        public Expression(String expression, TokenType type, boolean isEmpty) {
            this.expression = expression;
            this.type = type;
            this.isEmpty = isEmpty;
        }

        public Expression(String expression, TokenType type, String behindExpression, boolean isEmpty) {
            this.expression = expression;
            this.type = type;
            this.behindExpression = behindExpression;
            this.isEmpty = isEmpty;
        }

        public String getExpression() {
            return expression;
        }

        public void setExpression(String expression) {
            this.expression = expression;
        }

        public TokenType getType() {
            return type;
        }

        public void setType(TokenType type) {
            this.type = type;
        }

        public boolean isEmpty() {
            return isEmpty;
        }

        public void setEmpty(boolean empty) {
            this.isEmpty = empty;
        }
    }

}
