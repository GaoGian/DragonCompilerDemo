package com.gian.compiler.language.java.simple;
public class SimpleJavaMath {
    public boolean isJava;
    public SimpleJavaMath mather;
    public SimpleJavaMath self = this;
    public String name;
    public int[][] mathElemet = new int[2][5];
    public SimpleJavaMath[][] maths = new SimpleJavaMath[4][10];
    public SimpleJavaMath(boolean isJava, String name){
        this.isJava = isJava;
        this.name = name;
        for(int i=0; i<2; i++){
            for(int j=0; j<5; j++){
                mathElemet[i][j] = i+j;
            }
        }
        for(int i=0; i<4; i++){
            for(int j=0; j<10; j++){
                maths[i][j] = new SimpleJavaMath(isJava, "" + i + "_" + j);
            }
        }
    }
    public int add(int x, int y){
        return x + y;
    }
    public SimpleJavaMath getSelf(){
        return this;
    }
}
