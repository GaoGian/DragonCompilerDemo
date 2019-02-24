package gian.compiler.practice.syntactic;

import gian.compiler.practice.lexical.transform.LexConstants;
import gian.compiler.practice.syntactic.symbol.SyntaxSymbol;

import java.util.*;

/**
 * Created by gaojian on 2019/1/25.
 */
public class SyntacticParser {

    /**
     * 解析简单文法：
     *      stmt → if expr then stmt else stmt
     *            | if stmt then stmt
     *            | begin stmtList end
     *  stmtList → stmt ; stmtList | stmt | ε
     * @param syntaxs
     * @return
     */
    public static List<SyntaxSymbol> parseSyntaxSymbol(List<String> syntaxs){
        // 解析成终结符/非终结符
        Map<String, List<List<String>>> syntaxMap = new LinkedHashMap<>();
        for(String syntax : syntaxs){
            String head = syntax.split("→")[0];
            String bodys = syntax.split("→")[1];

            String[] products = bodys.split("\\|");
            for(String body : products){
                String[] symbols = body.trim().split(" ");
                List<String> symbolList = new ArrayList<>();
                if(symbols != null && symbols.length >0){
                    symbolList.addAll(Arrays.asList(symbols));
                }else{
                    // 如果是空转换则加入空字符串
                    symbolList.add(LexConstants.SYNTAX_EMPTY);
                }

                if(syntaxMap.get(head.trim()) == null){
                    List<List<String>> bodyList = new ArrayList<>();
                    bodyList.add(Arrays.asList(symbols));
                    syntaxMap.put(head.trim(), bodyList);
                }else{
                    syntaxMap.get(head.trim()).add(Arrays.asList(symbols));
                }
            }

        }

        // 如果在syntaxMap中，则是非终结符号
        Map<String, SyntaxSymbol> exitSymbolMap = new LinkedHashMap<>();
        List<SyntaxSymbol> syntaxSymbolList = new ArrayList<>();
        for(String head : syntaxMap.keySet()){
            SyntaxSymbol headSymbol = getSymbol(head, syntaxMap, exitSymbolMap);
            syntaxSymbolList.add(headSymbol);
        }

        return syntaxSymbolList;
    }

    // 如果产生体中有非终结符并且未解析过，优先解析子非终结符
    public static SyntaxSymbol getSymbol(String head, Map<String, List<List<String>>> syntaxMap, Map<String, SyntaxSymbol> exitSymbolMap){
        List<List<SyntaxSymbol>> productList = new ArrayList<>();
        List<List<String>> productExpressionList = syntaxMap.get(head);
        for(List<String> symbols : productExpressionList){
            List<SyntaxSymbol> productSymbols = new ArrayList<>();
            for(String symbol : symbols) {
                if(exitSymbolMap.get(symbol) == null) {
                    SyntaxSymbol bodySymbol = null;
                    if (syntaxMap.keySet().contains(symbol)) {
                        // 说明是非终结符
                        bodySymbol = new SyntaxSymbol(symbol, false);
                    } else {
                        // 说明是终结符
                        bodySymbol = new SyntaxSymbol(symbol, true);
                    }
                    exitSymbolMap.put(symbol, bodySymbol);
                    productSymbols.add(bodySymbol);
                }else{
                    productSymbols.add(exitSymbolMap.get(symbol));
                }
            }
            productList.add(productSymbols);
        }

        SyntaxSymbol headSymbol = null;
        if(exitSymbolMap.get(head) == null){
            headSymbol = new SyntaxSymbol(head, false);
        }else{
            headSymbol = exitSymbolMap.get(head);
        }
        headSymbol.setBody(productList);

        return headSymbol;
    }

    /**
     * 消除左递归（P134）
     * @param originSyntaxSymbolList
     * @return
     */
    public static void eliminateLeftRecursion(List<SyntaxSymbol> originSyntaxSymbolList){
        // TODO 将原文法按照上下级关系排序

        for(int i=0; i<originSyntaxSymbolList.size(); i++){

            SyntaxSymbol currSyntaxSymbol = originSyntaxSymbolList.get(i);
            List<List<SyntaxSymbol>> currProductBodys = currSyntaxSymbol.getBody();

            // 消除对上级的左递归
            for(int j=0; j<i-1; j++){
                // 判断当前文法是否依赖上级（所有上级）
                SyntaxSymbol preSyntaxSymbol = originSyntaxSymbolList.get(j);
                for(int k=0; k<currProductBodys.size(); k++){
                    List<SyntaxSymbol> currProductBody = currProductBodys.get(k);
                    // 不处理ε产生体
                    // TODO 确认ε产生体是什么样的，是长度为1，并且symbol为""
                    if(currProductBody.size() >= 1 && !currProductBody.get(0).getSymbol().equals(LexConstants.SYNTAX_EMPTY)) {
                        // 判断产生体首位是否和上级相同
                        if (currProductBody.get(0).getSymbol().equals(preSyntaxSymbol.getSymbol())) {
                            // 清除该产生式，后面需要进行替换
                            currProductBodys.remove(k);
                            // 修正遍历位置
                            k--;

                            // 将产生体首位替换成所有上级的所有产生体  TODO 需要处理环和ε产生体
                            // 去掉依赖的上级头部
                            List<SyntaxSymbol> tempProductBody = currProductBody.subList(1, currProductBody.size());
                            List<List<SyntaxSymbol>> preProductBodys = preSyntaxSymbol.getBody();
                            for (int l = 0; l < preProductBodys.size(); l++) {
                                List<SyntaxSymbol> preProductBody = preProductBodys.get(l);
                                // TODO 确认ε产生体是什么样的，是长度为1，并且symbol为""
                                if (preProductBody.size() >= 1 && !preProductBody.get(0).getSymbol().equals(LexConstants.SYNTAX_EMPTY)) {
                                    List<SyntaxSymbol> newCurrProductBody = new ArrayList<>();
                                    // 将产生体首位替换成所有上级的所有产生体
                                    newCurrProductBody.addAll(preProductBody);
                                    newCurrProductBody.addAll(tempProductBody);

                                    // 将替换的产生式加入到产生式列表
                                    currProductBodys.add(newCurrProductBody);
                                }
                            }
                        }
                    }
                }
            }

            // 消除对自己的左递归
            // 判断是否有左递归
            boolean isLeftRecursion = false;
            List<List<SyntaxSymbol>> leftRecursionList = new ArrayList<>();
            List<List<SyntaxSymbol>> unLeftRecursionList = new ArrayList<>();
            for(int m=0; m<currProductBodys.size(); m++) {
                List<SyntaxSymbol> currProductBody = currProductBodys.get(m);
                // 判断产生体是否左递归
                if(currProductBody.size() > 0) {
                    if (currProductBody.get(0).getSymbol().equals(currSyntaxSymbol.getSymbol())) {
                        leftRecursionList.add(currProductBody);
                        // 标记有左递归
                        isLeftRecursion = true;
                    } else {
                        unLeftRecursionList.add(currProductBody);
                    }
                }else{
                    unLeftRecursionList.add(currProductBody);
                }
            }
            // 消除左递归
            if(isLeftRecursion){
                // 生成消除左递归的文法
                SyntaxSymbol eliminateSyntaxSymbol = new SyntaxSymbol(currSyntaxSymbol.getSymbol() + "'", false);
                List<List<SyntaxSymbol>> eliminateProductBodys = new ArrayList<>();
                for(List<SyntaxSymbol> leftRecursionBody : leftRecursionList){
                    // 消除左递归首位
                    leftRecursionBody = leftRecursionBody.subList(1, leftRecursionBody.size());
                    // 加上消除左递归文法
                    leftRecursionBody.add(eliminateSyntaxSymbol);
                    eliminateProductBodys.add(leftRecursionBody);
                }
                // 加上空表达式
                List<SyntaxSymbol> emptyBody = new ArrayList<>();
                emptyBody.add(new SyntaxSymbol(LexConstants.SYNTAX_EMPTY, true));
                eliminateProductBodys.add(emptyBody);
                // 设置消除左递归文法的产生体
                eliminateSyntaxSymbol.setBody(eliminateProductBodys);

                // 将消除的左递归文法符号加入文法列表
                if(i < (originSyntaxSymbolList.size()-1)) {
                    List<SyntaxSymbol> newPreSyntaxSymbolList = new ArrayList<>(originSyntaxSymbolList.subList(0, i + 1));
                    List<SyntaxSymbol> newSubSyntaxSymbolList = new ArrayList<>(originSyntaxSymbolList.subList(i + 1, originSyntaxSymbolList.size()));
                    originSyntaxSymbolList.clear();
                    originSyntaxSymbolList.addAll(newPreSyntaxSymbolList);
                    originSyntaxSymbolList.add(eliminateSyntaxSymbol);
                    originSyntaxSymbolList.addAll(newSubSyntaxSymbolList);
                }else{
                    originSyntaxSymbolList.add(eliminateSyntaxSymbol);
                }

                // 转化原来的左递归文法
                for(List<SyntaxSymbol> unLeftRecursionBody : unLeftRecursionList){
                    // 判断是否是ε产生体，先清空ε产生式
                    if(unLeftRecursionBody.get(0).getSymbol().equals(LexConstants.SYNTAX_EMPTY)){
                        unLeftRecursionBody.clear();
                    }
                    // 在原来没有左递归的产生体后面加上消除左递归的文法符号
                    unLeftRecursionBody.add(eliminateSyntaxSymbol);
                }
                // 替换原来的左递归产生体
                currSyntaxSymbol.setBody(unLeftRecursionList);
            }
        }

    }

    /**
     * 提取左公因式       TODO 需要优化
     * @param originSyntaxSymbolList
     */
    public static void mergeCommonFactor(List<SyntaxSymbol> originSyntaxSymbolList){

        for(int i=0; i<originSyntaxSymbolList.size(); i++){

            SyntaxSymbol syntaxSymbol = originSyntaxSymbolList.get(i);
            List<List<SyntaxSymbol>> originProductBodys = syntaxSymbol.getBody();

            // 记录相同左公因式的产生式，key：公因式，value：产生式
            Map<List<SyntaxSymbol>, List<List<SyntaxSymbol>>> group = new LinkedHashMap<>();
            // 初始分组
            boolean hasCommonFactor = false;
            for (int j = 0; j < originProductBodys.size(); j++) {
                List<SyntaxSymbol> productBody = originProductBodys.get(j);
                List<SyntaxSymbol> groupKey = new ArrayList<>();
                groupKey.add(productBody.get(0));
                if(group.get(groupKey) == null){
                    List<List<SyntaxSymbol>> groupElement = new ArrayList<>();
                    groupElement.add(productBody);
                    group.put(groupKey, groupElement);
                }else{
                    group.get(groupKey).add(productBody);
                    hasCommonFactor = true;
                }
            }

            // 如果本来就没有公因式则直接返回
            if(!hasCommonFactor){
                break;
            }

            // TODO 需要考虑多个产生式递增公因式的情况，例如：abc | abdd | abde      暂时不处理
            // 继续分组
            Map<List<SyntaxSymbol>, List<List<SyntaxSymbol>>> result = new LinkedHashMap<>();
            while (group.size() > 0) {
                Map<List<SyntaxSymbol>, List<List<SyntaxSymbol>>> tranGroup = new LinkedHashMap<>();
                Iterator<List<SyntaxSymbol>> iterator = group.keySet().iterator();
                while(iterator.hasNext()){
                    List<SyntaxSymbol> groupKey = iterator.next();
                    List<List<SyntaxSymbol>> groupElement = group.get(groupKey);
                    // 判断之前同一组的元素是否有后继公因式
                    Map<SyntaxSymbol, List<List<SyntaxSymbol>>> tempGroup = new LinkedHashMap<>();
                    // FIXME 暂时这样处理公因式长度超过产生式长度
                    SyntaxSymbol nullSymbol = new SyntaxSymbol("null", true);
                    for(List<SyntaxSymbol> product : groupElement){

                        if(product.size() > groupKey.size()){
                            SyntaxSymbol tempGroupKey = product.get(groupKey.size());
                            if(tempGroup.get(tempGroupKey) == null){
                                List<List<SyntaxSymbol>> tempGroupElement = new ArrayList<>();
                                tempGroupElement.add(product);
                                tempGroup.put(tempGroupKey, tempGroupElement);
                            }else{
                                tempGroup.get(tempGroupKey).add(product);
                            }

                        }else{
                            // FIXME 暂时这样处理公因式长度超过产生式长度
                            List<List<SyntaxSymbol>> tempGroupElement = new ArrayList<>();
                            tempGroupElement.add(product);
                            tempGroup.put(nullSymbol, tempGroupElement);
                        }
                    }

                    // 判断是否还有相同的公因式
                    if(tempGroup.size() == 1){
                        // 还有相同的公因式，更新公因式
                        groupKey.addAll(tempGroup.keySet());
                        tranGroup.put(groupKey, groupElement);
                    }else{
                        // TODO 需要考虑多个产生式递增公因式的情况，例如：abc | abdd | abde      暂时不处理
                        // 没有后续的公因子，返回最长公因式
                        result.put(groupKey, groupElement);
                        iterator.remove();
                    }
                }

                group = tranGroup;
            }

            // 根据左公因式转换文法
            originProductBodys.clear();
            for(List<SyntaxSymbol> groupKey : result.keySet()){

                // 处理非公共部分
                List<List<SyntaxSymbol>> groupElement = result.get(groupKey);
                if(groupElement.size() > 1){
                    SyntaxSymbol commonFactorSymbol = new SyntaxSymbol(syntaxSymbol.getSymbol() + "~", false);
                    for(List<SyntaxSymbol> originSyntaxSymbol : groupElement){
                        originSyntaxSymbol.removeAll(groupKey);
                        if(originSyntaxSymbol.size() > 0){
                            originSyntaxSymbol.add(syntaxSymbol);
                            commonFactorSymbol.getBody().add(originSyntaxSymbol);
                        }
                    }
                    // 加上空表达式
                    List<SyntaxSymbol> emptyBody = new ArrayList<>();
                    emptyBody.add(new SyntaxSymbol(LexConstants.SYNTAX_EMPTY, true));
                    commonFactorSymbol.getBody().add(emptyBody);

                    // 先加上公因式表达式
                    List<SyntaxSymbol> newCommonProduct = new ArrayList<>(groupKey);
                    newCommonProduct.add(commonFactorSymbol);
                    originProductBodys.add(newCommonProduct);

                    // 将提取的左公因式文法符号加入文法列表
                    if(i < (originSyntaxSymbolList.size()-1)) {
                        List<SyntaxSymbol> newPreSyntaxSymbolList = new ArrayList<>(originSyntaxSymbolList.subList(0, i + 1));
                        List<SyntaxSymbol> newSubSyntaxSymbolList = new ArrayList<>(originSyntaxSymbolList.subList(i + 1, originSyntaxSymbolList.size()));
                        originSyntaxSymbolList.clear();
                        originSyntaxSymbolList.addAll(newPreSyntaxSymbolList);
                        originSyntaxSymbolList.add(commonFactorSymbol);
                        originSyntaxSymbolList.addAll(newSubSyntaxSymbolList);
                    }else{
                        originSyntaxSymbolList.add(commonFactorSymbol);
                    }

                }else{
                    originProductBodys.add(groupKey);
                }

            }


        }

    }

    /**
     * 计算文法符号 FIRST 集合
     *
     * TODO FIRST 集合是根据文法符号计算，跟产生式和位置无关
     * TODO FIRST 集合是产生式体的 FIRST 集合，如果一个文法符号有多个产生式体，则每个产生式体都有对应的FIRST集合
     *
     * TODO 需要保证文法已经处理过“左递归”、“抽取公因式”
     *
     * 一直执行下列规则，知道没有新的中介符号或ε加入到FIRST集合
     * 1、如果是非终结符，则加入产生体首个文法符号的 FIRST 集合
     * 2、如果该文法符号能够推导出ε，则加入下一个文法符号的 FIRST 集合，以此类推知道末尾
     * 3、如果文法符号本身能够推导出ε，则加入ε
     */
    public static Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirst(List<SyntaxSymbol> syntaxSymbols){

        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap = new LinkedHashMap<>();
        boolean hasNewFirstElement = true;
        while(hasNewFirstElement) {
            // 循环出口
            boolean newTag = false;
            for (int i=(syntaxSymbols.size()-1); i>=0; i--) {
                SyntaxSymbol syntaxSymbol = syntaxSymbols.get(i);
                Map<List<SyntaxSymbol>, Set<String>> syntaxProductFirstMap = getSyntaxProductFirstMap(syntaxSymbol, syntaxFirstMap);
                for (List<SyntaxSymbol> product : syntaxSymbol.getBody()) {
                    if(product.get(0).getSymbol().equals(LexConstants.SYNTAX_EMPTY)){
                        // 3、说明是ε产生式，直接加入
                        if(addSyntaxProductFirst(syntaxSymbol, product, LexConstants.SYNTAX_EMPTY, syntaxFirstMap)){
                            newTag = true;
                        }
                    }else{
                        if(product.get(0).isTerminal()) {
                            // 1、如果是终结符，则直接返回对应的字符串
                            if(addSyntaxProductFirst(syntaxSymbol, product, product.get(0).getSymbol(), syntaxFirstMap)){
                                newTag = true;
                            }
                        }else{
                            // 2、加入产生式体首个文法符号的FIRST，如果产生式前面的文法符号能够推导出ε，则加入后面文法符号的 FIRST 集合
                            for(int j=0; j<product.size(); j++) {
                                SyntaxSymbol productSymbol = product.get(j);
                                Set<String> targetSyntaxFirst = getSyntaxFirst(productSymbol, syntaxFirstMap);
                                // 2、如果该文法符号能够推导出ε，则加入下一个文法符号的 FIRST 集合
                                boolean hasEmpty = targetSyntaxFirst.contains(LexConstants.SYNTAX_EMPTY);
                                if(hasEmpty) {
                                    // 如果产生式最后一个符号还能够推导出ε，则加入ε
                                    if (j < product.size() - 1) {
                                        targetSyntaxFirst.remove(LexConstants.SYNTAX_EMPTY);
                                    }
                                    if (addSourceSyntaxFirst(syntaxSymbol, product, targetSyntaxFirst, syntaxFirstMap)) {
                                        newTag = true;
                                    }
                                }else{
                                    if (addSourceSyntaxFirst(syntaxSymbol, product, targetSyntaxFirst, syntaxFirstMap)) {
                                        newTag = true;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            hasNewFirstElement = newTag;
        }

        return syntaxFirstMap;
    }

    private static Map<List<SyntaxSymbol>, Set<String>> getSyntaxProductFirstMap(SyntaxSymbol syntaxSymbol, Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap){
        if(syntaxFirstMap.get(syntaxSymbol) == null){
            Map<List<SyntaxSymbol>, Set<String>> syntaxProductFirstMap = new LinkedHashMap<>();
            syntaxFirstMap.put(syntaxSymbol, syntaxProductFirstMap);
        }

        return syntaxFirstMap.get(syntaxSymbol);
    }

    private static Set<String> getSyntaxFirst(SyntaxSymbol targetSymbol, Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap){
        Set<String> targetSyntaxFirst = new HashSet<>();
        if(syntaxFirstMap.get(targetSymbol) == null){
            Map<List<SyntaxSymbol>, Set<String>> productFirstMap = new LinkedHashMap<>();
            syntaxFirstMap.put(targetSymbol, productFirstMap);
        }

        for(List<SyntaxSymbol> sourceProduct : syntaxFirstMap.get(targetSymbol).keySet()) {
            targetSyntaxFirst.addAll(syntaxFirstMap.get(targetSymbol).get(sourceProduct));
        }

        return targetSyntaxFirst;
    }

    private static boolean addSourceSyntaxFirst(SyntaxSymbol targetSymbol, List<SyntaxSymbol> targetProduct, Set<String> sourceSymbolFirst,
                                 Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap){

        boolean hasNewFirstElement = false;

        if(syntaxFirstMap.get(targetSymbol) == null){
            Map<List<SyntaxSymbol>, Set<String>> productFirstMap = new LinkedHashMap<>();
            syntaxFirstMap.put(targetSymbol, productFirstMap);
        }else{
            if(syntaxFirstMap.get(targetSymbol).get(targetProduct) == null){
                Set<String> first = new HashSet<>();
                syntaxFirstMap.get(targetSymbol).put(targetProduct, first);
            }
        }

        // 递归出口
        if(!syntaxFirstMap.get(targetSymbol).get(targetProduct).containsAll(sourceSymbolFirst)){
            syntaxFirstMap.get(targetSymbol).get(targetProduct).addAll(sourceSymbolFirst);
            hasNewFirstElement = true;
        }

        return hasNewFirstElement;
    }

    private static boolean addSyntaxProductFirst(SyntaxSymbol syntaxSymbol, List<SyntaxSymbol> product, String firstSymbol,
                                       Map<SyntaxSymbol,Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap){

        boolean hasNewFirstElement = false;

        if(syntaxFirstMap.get(syntaxSymbol) == null){
            Map<List<SyntaxSymbol>, Set<String>> productFirstMap = new LinkedHashMap<>();
            syntaxFirstMap.put(syntaxSymbol, productFirstMap);
        }else{
            if(syntaxFirstMap.get(syntaxSymbol).get(product) == null){
                Set<String> first = new HashSet<>();
                syntaxFirstMap.get(syntaxSymbol).put(product, first);
            }
        }

        // 递归出口
        if(!syntaxFirstMap.get(syntaxSymbol).get(product).contains(firstSymbol)){
            syntaxFirstMap.get(syntaxSymbol).get(product).add(firstSymbol);
            hasNewFirstElement = true;
        }

        return hasNewFirstElement;
    }

    /**
     * 计算文法符号 FOLLOW 集合
     *
     * TODO 需要保证文法已经处理过“左递归”、“抽取公因式”
     * TODO FOLLOW 集合应该是根据某个文法在某个产生式的某个位置计算，但是由于以下原因不需要考虑
     *      A、处理过左递归和提取过公因式，同一文法符号的不同产生式的 FIRST 集合不相交
     *      B、
     *
     * FIXME 感觉龙书上的 FOLLOW 集合没有对产生式不同位置的同一文法符号的FOLLOW作区分（是所有集合额总集），
     * FIXME 应该像LALR那样，由上到下做传播，这样就能区分同一文法符号在不同位置不同状态具有不同的FOLLOW集合
     *
     * 1、起始文法符号 S 的 $ ∈ FOLLOW(S)
     * 2、产生式 A→αBβ，(FIRST(β)-ε) ∈ FOLLOW(B)
     * 3、产生式 A→αB (B在产生式尾部) 或 A→αBβ（其中ε∈ FIRST(β)），那么 FOLLOW(A) ∈ FOLLOW(B)
     */
//    public static Map<String, Set<String>> syntaxFollow(SyntaxSymbol startSyntaxSymbol){
//        // 记录所有符号在不同产生式的不同位置的 FOLLOW 集合
//        // key: head + symbol + product(body) + index(body), value: FOLLOW
//        Map<String, Set<String>> followCollectionMap = new HashMap<>();
//
//        // 1、起始文法符号 S 的 FOLLOW 加入 $
//        Set<String> startSyntaxSymbolFollow = new HashSet<>();
//        startSyntaxSymbolFollow.add(LexConstants.SYNTAX_EMPTY);
//        recordSymbolFollowMap(followCollectionMap, startSyntaxSymbolFollow, getHeadTag(startSyntaxSymbol));
//
//        // 生成所有文法符号在不同产生式的不同位置的 FOLLOW 集
//        setProductFollow(startSyntaxSymbol, startSyntaxSymbol.getBody(), followCollectionMap);
//
//        return followCollectionMap;
//    }
//
//    private static void setProductFollow(SyntaxSymbol headSyntaxSymbol, List<List<SyntaxSymbol>> productList,
//                                                Map<String, Set<String>> followCollectionMap){
//
//        String headSymbolTag = getHeadTag(headSyntaxSymbol);
//        for(List<SyntaxSymbol> product : productList) {
//            if (!product.get(0).getSymbol().equals(LexConstants.SYNTAX_EMPTY)) {
//
//                // 先计算一级产生式的FOLLOW
//                for (int i = 0; i < product.size(); i++) {
//                    SyntaxSymbol symbol = product.get(i);
//                    String symbolTag = getProductSymbolTag(product, headSyntaxSymbol, symbol, i);
//
//                    if (i < product.size() - 1) {
//                        // 2、产生式 A→αBβ，(FIRST(β)-ε) ∈ FOLLOW(B)
//                        Set<String> nextSymbolFirst = syntaxFirst(product.get(i + 1));
//                        recordSymbolFollowMap(followCollectionMap, nextSymbolFirst, symbolTag);
//                    } else {
//                        // 3、产生式 A→αB (B在产生式尾部) 或 A→αBβ（其中ε∈ FIRST(β)），那么 FOLLOW(A) ∈ FOLLOW(B)
//                        Set<String> headFollow = followCollectionMap.get(headSymbolTag);
//                        recordSymbolFollowMap(followCollectionMap, headFollow, symbolTag);
//                        // 3、A→αBβ（其中ε∈ FIRST(β)），那么 FOLLOW(A) ∈ FOLLOW(B)，（从后往前递推）
//                        int j = i;
//                        SyntaxSymbol subSymbol = symbol;
//                        while(j>0) {
//                            j--;
//                            if (syntaxFirst(subSymbol).contains(LexConstants.SYNTAX_EMPTY)) {
//                                SyntaxSymbol preSymbol = product.get(j);
//                                String preSymbolTag = getProductSymbolTag(product, headSyntaxSymbol, preSymbol, j);
//                                recordSymbolFollowMap(followCollectionMap, followCollectionMap.get(headSymbolTag), preSymbolTag);
//
//                                subSymbol = preSymbol;
//                            }else{
//                                break;
//                            }
//                        }
//                    }
//                }
//
//                // FIXME 计算二级产生式的FOLLOW
//                for (int i = 0; i < product.size(); i++) {
//                    SyntaxSymbol symbol = product.get(i);
//                    if(!symbol.isTerminal()){
//                        if(followCollectionMap.get(getHeadTag(symbol)) == null) {
//                            setProductFollow(symbol, symbol.getBody(), followCollectionMap);
//                        }
//                    }
//                }
//
//            }
//        }
//    }
//
//    // 更新FOLLOW集合
//    private static boolean recordSymbolFollowMap(Map<String, Set<String>> followCollectionMap, Set<String> symbolFollow, String symbolTag){
//        boolean hasNewFollow = false;
//        if(symbolFollow != null && symbolFollow.size()>0) {
//            if (followCollectionMap.get(symbolTag) == null) {
//                Set<String> follow = new HashSet<>();
//                follow.addAll(symbolFollow);
//                followCollectionMap.put(symbolTag, follow);
//                hasNewFollow = true;
//            } else {
//                if (followCollectionMap.get(symbolTag).containsAll(symbolFollow)) {
//                    hasNewFollow = false;
//                } else {
//                    followCollectionMap.get(symbolTag).addAll(symbolFollow);
//                    hasNewFollow = true;
//                }
//            }
//        }
//        return hasNewFollow;
//    }

    private static String getHeadTag(SyntaxSymbol headSymbol){
        return "head|" + headSymbol.getSymbol();
    }

    private static String getProductSymbolTag(List<SyntaxSymbol> product, SyntaxSymbol headSymbol, SyntaxSymbol tailSymbol, int index){
        StringBuilder str = new StringBuilder();
        str.append("body|" + headSymbol.getSymbol() + "→");

        for(int i=0; i<product.size(); i++){
            str.append(product.get(i).getSymbol());
            if(i<product.size()-1){
                str.append(":");
            }
        }

        str.append("|" + tailSymbol.getSymbol() + "|" + index);

        return str.toString();
    }

}