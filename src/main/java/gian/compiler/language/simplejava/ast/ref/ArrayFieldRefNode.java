package gian.compiler.language.simplejava.ast.ref;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/4/4.
 */
public class ArrayFieldRefNode extends FieldRefNode {

    // 数组引用维度
    public List<Integer> dimension = new ArrayList<>();

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(this.caller + "." + this.callName );
        for(Integer index : dimension){
            str.append("[" + index + "]");
        }
        return str.toString();
    }

}