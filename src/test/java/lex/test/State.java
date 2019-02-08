package lex.test;

import java.util.HashSet;
import java.util.Set;

/**
 * 状态节点
 * Created by gaojian on 2019/1/25.
 */
public class State {

    public String stateName;

    // TODO 需要优化算法，由State持有离开边
    public Set<Edge> outEdges = new HashSet<>();
}