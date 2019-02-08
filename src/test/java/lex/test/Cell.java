package lex.test;

/**
 * 单元
 * Created by gaojian on 2019/1/25.
 */
public class Cell {

    public Edge[] edgeSet = new Edge[100];  // 100的大小足够了
    public int edgeCount;
    public State startState;
    public State endState;

}