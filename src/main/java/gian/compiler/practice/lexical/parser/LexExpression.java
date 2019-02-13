package gian.compiler.practice.lexical.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tingyun on 2018/7/20.
 */
public class LexExpression {

    public static List<Expression> expressions = new ArrayList<>();

    static {
        // 关键词
        expressions.add(new Expression("if", ExpressionType.KEYWORD, false));
        expressions.add(new Expression("while", ExpressionType.KEYWORD, false));
        expressions.add(new Expression("do", ExpressionType.KEYWORD, false));
        expressions.add(new Expression("break", ExpressionType.KEYWORD, false));
        expressions.add(new Expression("true", ExpressionType.KEYWORD, false));
        expressions.add(new Expression("false", ExpressionType.KEYWORD, false));

        // 标识符
        expressions.add(new Expression("[A-Za-z]\\w*", ExpressionType.ID, false));

        // 数字
        expressions.add(new Expression("\\d+(\\.\\d+)?", ExpressionType.NUMBER, false));

        // 操作符
        expressions.add(new Expression(">", ExpressionType.OPERATOR, false));
        expressions.add(new Expression("<", ExpressionType.OPERATOR, false));
        expressions.add(new Expression("=", ExpressionType.OPERATOR, false));
        expressions.add(new Expression(">=", ExpressionType.OPERATOR, false));
        expressions.add(new Expression(">=", ExpressionType.OPERATOR, false));
        expressions.add(new Expression("==", ExpressionType.OPERATOR, false));
        expressions.add(new Expression("!=", ExpressionType.OPERATOR, false));
        expressions.add(new Expression("&&", ExpressionType.OPERATOR, false));
        expressions.add(new Expression("||", ExpressionType.OPERATOR, false));

        // 分隔符
        expressions.add(new Expression("{", ExpressionType.SEPARATOR, false));
        expressions.add(new Expression("}", ExpressionType.SEPARATOR, false));
        expressions.add(new Expression("(", ExpressionType.SEPARATOR, false));
        expressions.add(new Expression(")", ExpressionType.SEPARATOR, false));
        expressions.add(new Expression("[", ExpressionType.SEPARATOR, false));
        expressions.add(new Expression("]", ExpressionType.SEPARATOR, false));

        // 标点符号
        expressions.add(new Expression(",", ExpressionType.PUNCTUATION, false));
        expressions.add(new Expression(";", ExpressionType.PUNCTUATION, false));

        // 分隔符
        expressions.add(new Expression("\\s+", ExpressionType.SECTION, true));

    }

    public static enum ExpressionType{
        ID, KEYWORD, NUMBER, OPERATOR, SEPARATOR, PUNCTUATION, SECTION
    }

    // 词法单元匹配的正则表达式
    public static class Expression {

        private String expression;
        private ExpressionType type;
        //
        private boolean isEmpty;

        public Expression() {

        }

        public Expression(String expression, ExpressionType type, boolean isEmpty) {
            this.expression = expression;
            this.type = type;
            this.isEmpty = isEmpty;
        }

        public String getExpression() {
            return expression;
        }

        public void setExpression(String expression) {
            this.expression = expression;
        }

        public ExpressionType getType() {
            return type;
        }

        public void setType(ExpressionType type) {
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
