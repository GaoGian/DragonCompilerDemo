package gian.compiler.practice.syntaxDirected;

import gian.compiler.practice.syntactic.element.SyntaxSymbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gaojian on 2019/3/20.
 */
public class SyntaxDirectedSymbol extends SyntaxSymbol {

    protected List<SyntaxDirectedProduct> products = new ArrayList<>();

    protected Map<String, Object> propertyMap = new HashMap<>();

}