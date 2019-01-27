package gian.compiler.practice.lexical.transform;

/**
 * Created by Gian on 2019/1/27.
 */
public class DtranEdge {

    private DtranState startDtranState;
    private DtranState endDtranState;
    // 转换输入符
    private char transSymbol;

    public DtranState getStartDtranState() {
        return startDtranState;
    }

    public void setStartDtranState(DtranState startDtranState) {
        this.startDtranState = startDtranState;
    }

    public DtranState getEndDtranState() {
        return endDtranState;
    }

    public void setEndDtranState(DtranState endDtranState) {
        this.endDtranState = endDtranState;
    }

    public char getTransSymbol() {
        return transSymbol;
    }

    public void setTransSymbol(char transSymbol) {
        this.transSymbol = transSymbol;
    }
}
