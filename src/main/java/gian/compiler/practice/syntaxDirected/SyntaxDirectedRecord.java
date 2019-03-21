package gian.compiler.practice.syntaxDirected;


import gian.compiler.practice.syntactic.element.SyntaxProduct;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 每个产生式对应的语义动作，通过序号来区分具体执行位置
 * Created by gaojian on 2019/3/20.
 */
public class SyntaxDirectedRecord {

    private SyntaxProduct targetSyntaxProduct;
    private Map<Integer, List<SyntaxDirectedAction>> directedActionMap = new LinkedHashMap<>();


}