package gian.compiler.front.lexical.parser;

import gian.compiler.front.lexical.transform.LexConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tingyun on 2018/7/20.
 * TODO 需要改造成根据输入自动解析表达式
 *
 * 1、正则表达式词法单元：变量名、数值之类的不定式字符串
 * 2、字面量词法单元：分解符、关键字等
 *
 */
public class LexExpression {

    public static class TokenType {

        private String type;
        // TODO 是否是正则表达式词法单元，Lex语法中通过单独维护对应的正则表达式进行判断，这里集成到一块显示
        // TODO 证明：词法单元类型是在语法分析过程中判断的，如果遇到“正则表达式终结符”时是根据正则表达式匹配的，如果命中则说明该终结符号匹配，
        //                          正则表达式终结符（词法单元类型） --> 正则表达式 --> 词法单元
        // TODO         就是说识别出token后自然就对应该“正则表达式终结符”，由于分离的词法分析和语法分析，因此这里需要携带对应的符号信息
        //                          正则表达式词法单元 --> 词法单元类型 --> 正则表达式终结符
        private boolean isRexgexToken;

        public TokenType(String type){
            this.type = type;
        }

        public TokenType(String type, boolean isRexgexToken){
            this.type = type;
            this.isRexgexToken = isRexgexToken;
        }

        public String getType(){
            return this.type;
        }

        public boolean isRexgexToken() {
            return isRexgexToken;
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
