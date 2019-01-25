package com.taozeyu.taolan.analysis;

/**
 * Created by gaojian on 2019/1/25.
 */
public class LexicalAnalysisException extends RuntimeException {

    private char c;
    private String msg;

    public LexicalAnalysisException(char c){
        this.c = c;
    }

    public LexicalAnalysisException(String msg){
        this.msg = msg;
    }

}