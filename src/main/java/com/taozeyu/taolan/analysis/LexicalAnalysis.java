package com.taozeyu.taolan.analysis;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gaojian on 2019/1/25.
 */
public class LexicalAnalysis {

    // 标识正在读取的标识符的类型
    private static enum State {
        Normal,
        Identifier, Sign, Annotation,
        String, RegEx, Space;
    }


    public LexicalAnalysis(Reader reader) {
        //TODO
    }

    Token read() throws IOException, LexicalAnalysisException {
        //TODO
        return null;
    }


    private State state;
    private final LinkedList<Token> tokenBuffer = new LinkedList<>();
    private StringBuilder readBuffer = null;

    private void refreshBuffer(char c) {
        readBuffer = new StringBuilder();
        readBuffer.append(c);
    }

    private void createToken(Token.Type type) {
        Token token = new Token(type, readBuffer.toString());
        tokenBuffer.addFirst(token);
        readBuffer = null;
    }

    private void createToken(Token.Type type, String value) {
        Token token = new Token(type, value);
        tokenBuffer.addFirst(token);
        readBuffer = null;
    }

    private boolean readChar(char c) throws LexicalAnalysisException {
        //状态机逻辑.

        boolean moveCursor = true;
        Token.Type createType = null;

        // 根据当前读取的标识符类型做相应的处理
        if(state == State.Normal) {

            if(inIdentifierSetButNotRear(c)) {
                state = State.Identifier;
            }
            else if(SignParser.inCharSet(c)) {
                state = State.Sign;
            }
            else if(c == '#') {
                state = State.Annotation;
            }
            else if(c == '\"' | c == '\'') {
                state = State.String;
            }
            else if(c == '`') {
                state = State.RegEx;
            }
            else if(include(Space, c)) {
                state = State.Space;
            }
            else if(c == '\n') {
                createType = Token.Type.NewLine;
            }
            else if(c == '\0') {
                createType = Token.Type.EndSymbol;
            }
            else {
                throw new LexicalAnalysisException(c);
            }
            refreshBuffer(c);

        } else if(state == State.Identifier) {

            if(inIdentifierSetButNotRear(c)) {
                readBuffer.append(c);

            } else if(include(IdentifierRearSign, c)) {
                createType = Token.Type.Identifier;
                readBuffer.append(c);
                state = State.Normal;

            } else {
                createType = Token.Type.Identifier;
                state = State.Normal;
                moveCursor = false;
            }

        } else if(state == State.Sign) {

            if(SignParser.inCharSet(c)) {
                readBuffer.append(c);

            } else {
                List<String> list = SignParser.parse(readBuffer.toString());
                for(String signStr:list) {
                    createToken(Token.Type.Sign, signStr);
                }
                createType = null;
                state = State.Normal;
                moveCursor = false;
            }

        } else if(state == State.Annotation) {

            if(c != '\n' & c != '\0') {
                readBuffer.append(c);
            } else {
                createType = Token.Type.Annotation;
                state = State.Normal;
                moveCursor = false;
            }

        } else if(state == State.String) {

            if(c == '\n') {
                throw new LexicalAnalysisException(c);

            } else if(c == '\0') {
                throw new LexicalAnalysisException(c);

            } else if(transferredMeaningSign) {

                Character tms = StringTMMap.get(c);
                if(tms == null) {
                    throw new LexicalAnalysisException(c);
                }
                readBuffer.append(tms);
                transferredMeaningSign = false;

            } else if(c == '\\') {
                transferredMeaningSign = true;

            } else {
                readBuffer.append(c);
                char firstChar = readBuffer.charAt(0);
                if(firstChar == c) {
                    createType = Token.Type.String;
                    state = State.Normal;
                }
            }

        } else if(state == State.RegEx) {

            if(transferredMeaningSign) {

                if(c != '`') {
                    throw new LexicalAnalysisException(c);
                }
                readBuffer.append(c);
                transferredMeaningSign = false;

            } else if(c =='\\') {
                transferredMeaningSign = true;

            } else if(c == '\0') {
                throw new LexicalAnalysisException(c);

            } else if(c == '`') {
                readBuffer.append(c);
                createType = Token.Type.RegEx;
                state = State.Normal;

            } else {
                readBuffer.append(c);
            }

        } else if(state == State.Space) {

            if(include(Space, c)) {
                readBuffer.append(c);

            } else {
                createType = Token.Type.Space;
                state = State.Normal;
                moveCursor = false;
            }

        }

        if(createType != null) {
            createToken(createType);
        }
        return moveCursor;
    }

    private static final char[] Space = new char[] {' ', '\t'};
    private static final char[] IdentifierRearSign = new char[] {'?', '!'};

    private boolean transferredMeaningSign;

    private static final HashMap<Character, Character> StringTMMap = new HashMap<>();

    static {
        StringTMMap.put('\"', '\"');
        StringTMMap.put('\'', '\'');
        StringTMMap.put('\\', '\\');
        StringTMMap.put('b', '\b');
        StringTMMap.put('f', '\f');
        StringTMMap.put('t', '\t');
        StringTMMap.put('r', '\r');
        StringTMMap.put('n', '\n');
    }

    private boolean inIdentifierSetButNotRear(char c) {
        return (c >= 'a' & c <= 'z' ) | (c >='A' & c <= 'Z') | (c >= '0' & c <= '9')|| (c == '_');
    }

    private boolean include(char[] range, char c) {
        boolean include = false;
        for(int i=0; i<range.length; ++i) {
            if(range[i] == c) {
                include = true;
                break;
            }
        }
        return include;
    }


}