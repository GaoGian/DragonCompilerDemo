package gian.compiler.practice.lexical.transform;

/**
 * Created by Gian on 2019/1/27.
 */
public class DtranCell {

    public DtranState[] edgeSet = new DtranState[100];  // 100的大小足够了
    public int edgeCount;
    public DtranState startState;
    public DtranState endState;

}
