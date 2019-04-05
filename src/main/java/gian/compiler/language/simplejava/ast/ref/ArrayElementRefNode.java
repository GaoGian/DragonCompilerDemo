package gian.compiler.language.simplejava.ast.ref;

import java.util.List;

/**
 * Created by Gian on 2019/4/5.
 */
public class ArrayElementRefNode extends RefNode {

    public List<Integer> arrayIndex;

    public List<Integer> getArrayIndex() {
        return arrayIndex;
    }

    public void setArrayIndex(List<Integer> arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    @Override
    public String toString(){
        StringBuffer str = new StringBuffer();
        for(Integer index : arrayIndex){
            str.append("[" + index + "]");
        }
        str.append(next.toString());

        return str.toString();
    }

}
