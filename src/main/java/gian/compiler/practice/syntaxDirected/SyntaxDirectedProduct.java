package gian.compiler.practice.syntaxDirected;

import gian.compiler.practice.syntactic.element.SyntaxProduct;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gaojian on 2019/3/20.
 */
public class SyntaxDirectedProduct extends SyntaxProduct {

    protected List<SyntaxDirectedSymbol> productBody = new ArrayList<>();

    // 该产生式所有语义动作，key：语义动作位置，内嵌语义动作位置小于产生式长度，综合属性处于产生式尾部（可能有多个，按顺序排列）
    protected Map<Integer, List<SyntaxDirectedRecord>> actions = new LinkedHashMap<>();

}