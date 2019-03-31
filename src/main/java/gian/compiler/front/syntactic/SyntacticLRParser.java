package gian.compiler.front.syntactic;

import com.google.common.collect.Sets;
import gian.compiler.front.exception.ParseException;
import gian.compiler.front.lexical.parser.LexExpression;
import gian.compiler.front.lexical.parser.LexicalParser;
import gian.compiler.front.lexical.parser.Token;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.lexical.transform.MyStack;
import gian.compiler.front.syntactic.element.*;
import gian.compiler.utils.ParseUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * LR 分析器
 * Created by gaojian on 2019/3/1.
 */
public class SyntacticLRParser {

/**----------------------------------------------------SLR-----------------------------------------------------------------------**/

    /**
     * 现将原来的文法转变为增广文法，再生成初始项集簇
     * @param syntaxSymbols
     * @return
     */
    public static ItemCollection getStartItemCollection(List<SyntaxSymbol> syntaxSymbols, int itemCollectionNo){
        // 处理成增广文法，起始开始符号
        List<SyntaxSymbol> augmentStartSymbolProduct = new ArrayList<>();
        augmentStartSymbolProduct.add(syntaxSymbols.get(0));
        SyntaxSymbol augmentStartSymbol = new SyntaxSymbol(LexConstants.AUGMENT_SYNTAX_TAG + syntaxSymbols.get(0).getSymbol(), false);
        augmentStartSymbol.getBody().add(augmentStartSymbolProduct);

        List<SyntaxSymbol> augmentedSyntax = new ArrayList<>();
        augmentedSyntax.add(augmentStartSymbol);
        augmentedSyntax.addAll(syntaxSymbols);

        // 分离成多个产生式
        List<SyntaxProduct> syntaxProducts = getSyntaxProducts(augmentedSyntax, true);

        // 根据产生式生成初始项集簇
        List<Item> startItems = new ArrayList<>();
        for(SyntaxProduct syntaxProduct : syntaxProducts){
            Item item = new Item(syntaxProduct, 0);
            startItems.add(item);
        }

        ItemCollection startItemCollection = new ItemCollection(itemCollectionNo, startItems);
        return startItemCollection;
    }

    public static List<SyntaxProduct> getSyntaxProducts(List<SyntaxSymbol> syntaxSymbols){
        return getSyntaxProducts(syntaxSymbols, false);
    }

    public static List<SyntaxProduct> getSyntaxProducts(List<SyntaxSymbol> syntaxSymbols, boolean isAugmented){
        List<SyntaxProduct> syntaxProducts = new ArrayList<>();
        int number = 0;
        if(!isAugmented){
            number++;
        }
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols){
            for(List<SyntaxSymbol> product : syntaxSymbol.getBody()){
                SyntaxProduct syntaxProduct = new SyntaxProduct(number, syntaxSymbol, product);
                syntaxProducts.add(syntaxProduct);

                number++;
            }
        }
        return syntaxProducts;
    }

    /**
     * 计算项集闭包（用于项集状态迁移后计算新的项集）
     *
     * 1、將 项集I 中的各项加入到 CLOSURE (I)中；
     * 2、如果 A→α· Bβ在 CLOUSE(I) 中，B→γ是一个产生式，B→·γ不在 CLOSURE(I)中，就将B→·γ加入到CLOSURE(I)中；
     * 3、不算运用规则1、2，直到没有新项
     *
     */
    public static ItemCollection closure(List<Item> items, int itemCollectionNo, Map<SyntaxSymbol, Set<SyntaxProduct>> symbolProductMap){
        List<Item> closureItem = new ArrayList<>();
        // 1、將 项集I 中的各项加入到 CLOSURE (I)中；
        closureItem.addAll(items);
        // 3、不算运用规则1、2，直到没有新项
        boolean hasNew = true;
        while(hasNew) {
            hasNew = false;
            for (int i=0; i<closureItem.size(); i++) {
                Item item = closureItem.get(i);
                if(item.getIndex() < item.getSyntaxProduct().getProduct().size()) {
                    SyntaxSymbol syntaxSymbol = item.getSyntaxProduct().getProduct().get(item.getIndex());
                    if (!syntaxSymbol.isTerminal()) {
                        // 2、如果 A→α· Bβ在 CLOUSE(I) 中，B→γ是一个产生式，B→·γ不在 CLOSURE(I)中，就将B→·γ加入到CLOSURE(I)中；
                        for(SyntaxProduct syntaxProduct : symbolProductMap.get(syntaxSymbol)) {
                            // TODO 是否需要排除ε产生式
                            Item newItem = new Item(syntaxProduct, 0);
                            if (!closureItem.contains(newItem)) {
                                closureItem.add(newItem);
                                hasNew = true;
                            }
                        }
                    }
                }else{
                    continue;
                }
            }
        }

        ItemCollection closureItemCollection = new ItemCollection(itemCollectionNo, closureItem);

        return closureItemCollection;
    }

    public static ItemCollection closure(ItemCollection itemollection, int itemCollectionNo, Map<SyntaxSymbol, Set<SyntaxProduct>> symbolProductMap){
        List<Item> items = itemollection.getItemList();
        return closure(items, itemCollectionNo, symbolProductMap);
    }

    /**
     * 当前项集经由X转换符边离开后的项集状态，返回新的项集（CLOSURE）
     *
     * 1、GOTO(A,X)定义为将项集I中所有形如[A→α·Xβ]的项所对应的项[A→αX·β]集合的CLOSURE闭包
     *
     */
    public static ItemCollection moveItem(List<Item> items, SyntaxSymbol moveSymbol, int itemCollectionNo,
                                          Map<SyntaxSymbol, Set<SyntaxProduct>> symbolProductMap){

        List<Item> moveItemList = new ArrayList<>();
        for(Item item : items){
            // 如果位置符大于产生式长度说明已经是规约项，goto函数只对非规约项进行处理
            // TODO 是否需要对ε、$进行判断
            if(item.getIndex() < item.getSyntaxProduct().getProduct().size()){
                // 判断是否可以通过 X 边离开
                if(item.getSyntaxProduct().getProduct().get(item.getIndex()).equals(moveSymbol)){
                    Item moveItem = new Item(item.getSyntaxProduct(), item.getIndex() + 1);
                    if(!moveItemList.contains(moveItem)) {
                        moveItemList.add(moveItem);
                    }
                }
            }
        }

        if(moveItemList.size() > 0) {
            // 计算新项集的 CLOSURE 闭包
            ItemCollection moveItemCollection = closure(moveItemList, itemCollectionNo, symbolProductMap);

            return moveItemCollection;
        }else{
            return null;
        }
    }

    public static ItemCollection moveItem(ItemCollection itemCollection, SyntaxSymbol moveSymbol, int itemCollectionNo, Map<SyntaxSymbol, Set<SyntaxProduct>> symbolProductMap){
        List<Item> items = itemCollection.getItemList();
        return moveItem(items, moveSymbol, itemCollectionNo, symbolProductMap);
    }

    public static Map<SyntaxSymbol, Set<SyntaxProduct>> getSymbolProductMap(List<SyntaxProduct> syntaxProducts){
        Map<SyntaxSymbol, Set<SyntaxProduct>> symbolProductMap = new LinkedHashMap<>();
        for(SyntaxProduct syntaxProduct : syntaxProducts){
            SyntaxSymbol syntaxSymbol = syntaxProduct.getHead();
            if(symbolProductMap.get(syntaxSymbol) == null){
                Set<SyntaxProduct> syntaxProductSet = new LinkedHashSet<>();
                symbolProductMap.put(syntaxSymbol, syntaxProductSet);
            }
            symbolProductMap.get(syntaxSymbol).add(syntaxProduct);
        }
        return symbolProductMap;
    }

    /**
     * 生成 LR0 自动机
     * @return
     */
    public static void getLR0ItemCollectionNodes(SyntaxProduct startSyntaxProduct, ItemCollection itemCollection, Set<SyntaxSymbol> allGotoSymtaxSymbol,
                                                        Map<SyntaxSymbol, Set<SyntaxProduct>> symbolProductMap, AtomicInteger number,
                                                        Map<ItemCollection, ItemCollection> allItemCollectionMap){

        allItemCollectionMap.put(itemCollection, itemCollection);

        for(SyntaxSymbol syntaxSymbol : allGotoSymtaxSymbol){
            // TODO 需要特别处理 $ 终结符，判断是否可以转换成接收状态
            if(syntaxSymbol.getSymbol().equals(LexConstants.SYNTAX_END)){
                for(Item item : itemCollection.getItemList()){
                    if(item.getSyntaxProduct().equals(startSyntaxProduct) && item.getIndex() == 1){
                        // 说明是起始文法归约状态，加入接收状态
                        ItemCollection.AcceptItemCollection acceptItemCollection = new ItemCollection.AcceptItemCollection();
                        itemCollection.getMoveItemCollectionMap().put(syntaxSymbol, acceptItemCollection);
                    }
                }
            }

            ItemCollection moveItemCollection = moveItem(itemCollection, syntaxSymbol, number.getAndIncrement(), symbolProductMap);
            if(moveItemCollection != null){
                if(allItemCollectionMap.get(moveItemCollection) != null) {
                    itemCollection.getMoveItemCollectionMap().put(syntaxSymbol, allItemCollectionMap.get(moveItemCollection));
                }else{
                    itemCollection.getMoveItemCollectionMap().put(syntaxSymbol, moveItemCollection);
                    allItemCollectionMap.put(moveItemCollection, moveItemCollection);

                    getLR0ItemCollectionNodes(startSyntaxProduct, moveItemCollection, allGotoSymtaxSymbol, symbolProductMap, number, allItemCollectionMap);
                }
            }
        }

    }

    /**
     * LR(0) 语法分析
     * TODO 这里的处理是优先进行移入操作，如果不能移入再在做规约处理，规约后再做移入（规约包括最后一个符号可以推导为空的情况，参考LL分析）
     * TODO（因此不会出现过度规约的情况）
     * TODO 可以解决类似 if else then 这种空桥的情况
     * FIXME 由于规约向包括最后一个符号推导为空的情况，可能出现的问题是 /factor component/ stmt 如果component可以推导为空, 是否会出现读入的字符应该属于stmt而被推到为component的情况？这种情况是否可以通过修改文法改变？
     *
     * FIXME 该处理方式有问题，只会优先处理移入操作，导致都是按照最右归约处理，如果是需要先归约再移入的话会出现问题，
     * FIXME 这里需要参考预测分析表归约时候的判断，根据后一个输入符是否是FOLLOW集特定子集（需要由推导链向下传播）判断
     * FIXME 关于“可行前缀”，如果是处于项集推导过程中的某一位置，那么就可以保证栈中的前缀就是可行前缀，只要不是过度归约（可以根据上一点来判断是否是归约还是移入）
     *
     */
    public static boolean syntaxParseLR0(ItemCollection startItemCollection, List<Token> tokenList,
                                         Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap,
                                         Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap){

        // 是否成功接收文法
        boolean acceptSuccess = true;

        // 记录当前推导的位置（推导链路）
        MyStack<ItemCollection> itemCollectionStack = new MyStack<>();
        itemCollectionStack.push(startItemCollection);
        // 记录待归约的文法符号
        MyStack<SyntaxSymbol> syntaxSymbolStack = new MyStack<>();
        syntaxSymbolStack.push(new SyntaxSymbol(LexConstants.SYNTAX_END, true));

        for(int i=0; i<tokenList.size(); i++){
            Token token = tokenList.get(i);
            ItemCollection currentItemCollection = itemCollectionStack.top();

            SyntaxSymbol moveSymbol = null;
            if(token.getType().isRexgexToken()){
                // 如果是正则表达式词法单元，则对应的文法符号是其类型
                moveSymbol = new SyntaxSymbol(token.getType().getType(), true);
            }else{
                // 如果是直接词法单元，则对应的文法符号是其本身字符串
                moveSymbol = new SyntaxSymbol(token.getToken(), true);
            }

            // 根据输入符获取离开后的项集
            ItemCollection nextItemCollection = currentItemCollection.getMoveItemCollectionMap().get(moveSymbol);

            // 根据项集判断是移入还是归约操作
            if(nextItemCollection != null){
                // 将后续项集和转换符号压入栈中
                syntaxShiftLR(nextItemCollection, moveSymbol, itemCollectionStack, syntaxSymbolStack);

                // 判断是否是接收状态
                if(nextItemCollection instanceof ItemCollection.AcceptItemCollection){
                    if(i == (tokenList.size() - 1)){
                        // 如果下一状态是接收状态，并且已经是字符流末尾，则说明LR(0)解析成功
                        System.out.println("LR(0) parse success");
                        return acceptSuccess;
                    }else{
                        throw new ParseException("LR(0)解析错误，还未到输入流末尾");
                    }
                }

                // 判断是移入还是归约操作
                if(nextItemCollection.getItemList().size() == 1 && nextItemCollection.getMoveItemCollectionMap().size() == 0) {
                    // 有后继状态，并且项集只有一个项，推导位置处于末尾，说明是归约操作     // TODO 归约状态判定条件是否正确
                    syntaxReduceLR(nextItemCollection, null, itemCollectionStack, syntaxSymbolStack, syntaxFirstMap, syntaxFollowMap);
                }else{
                    // 如果移入后不能规约，需要根据后续输入符号进一步处理

                }
            }else{
                // TODO 如果没有后继状态，但是未到输入流末尾，则需要进行规约操作，需要将输入流位置回退一位，保证停留在当前输入符号
                // TODO 可能会有 规约/规约 冲突
                // FIXME 这里可以参考LL(1)分析，如果项集处于倒数第二推导位置项的FIRST集包含ε（该项类似LL的当前展开项），并且当前符号在产生体的FOLLOW中，则也可以进行规约
                // FIXME 现在是无法移入就尝试规约，应该根据“可行前缀”和“向前看输入符”进行判断，参考LR分析表
                syntaxReduceLR(currentItemCollection, moveSymbol, itemCollectionStack, syntaxSymbolStack, syntaxFirstMap, syntaxFollowMap);
                // 需要回退一位输入，保证停留在当前输入符号
                i--;
            }

        }

        return acceptSuccess;
    }

    public static boolean syntaxParseLR0(List<String> syntaxs, List<Token> tokenList,
                                         Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap,
                                         Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap){

        List<SyntaxSymbol> syntaxSymbols = SyntacticLLParser.parseSyntaxSymbol(syntaxs);

        // 获取初始项集节点
        AtomicInteger itemCollectionNo = new AtomicInteger(0);
        ItemCollection startItemCollection = SyntacticLRParser.getStartItemCollection(syntaxSymbols, itemCollectionNo.getAndIncrement());

        // 获取LR(O)后续项集节点
        List<SyntaxProduct> syntaxProducts = SyntacticLRParser.getSyntaxProducts(syntaxSymbols);
        Set<SyntaxSymbol> allGotoSymtaxSymbol = SyntacticLRParser.getAllGotoSymtaxSymbol(syntaxProducts);
        Map<SyntaxSymbol, Set<SyntaxProduct>> symbolProductMap = SyntacticLRParser.getSymbolProductMap(syntaxProducts);
        Map<ItemCollection, ItemCollection> allItemCollectionMap = new LinkedHashMap<>();
        SyntacticLRParser.getLR0ItemCollectionNodes(startItemCollection.getItemList().get(0).getSyntaxProduct(), startItemCollection, allGotoSymtaxSymbol, symbolProductMap, itemCollectionNo, allItemCollectionMap);

        // LR0 parse
        return SyntacticLRParser.syntaxParseLR0(startItemCollection, tokenList, syntaxFirstMap, syntaxFollowMap);

    }

    public static boolean syntaxParseLR0(String syntaxFile, String targetProgarmFile, List<LexExpression.Expression> expressions, boolean isClassPath,
                                         Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap,
                                         Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap){
        // 读取文法文件
        List<String> syntaxs = ParseUtils.getFile(syntaxFile, isClassPath);

        // 解析目标语言文件生成词法单元数据
        List<Token> tokens = LexicalParser.parser(ParseUtils.getFile(targetProgarmFile, isClassPath), expressions);

        return syntaxParseLR0(syntaxs, tokens, syntaxFirstMap, syntaxFollowMap);
    }

    /**执行规约动作**/
    public static void syntaxReduceLR(ItemCollection reduceItemCollection, SyntaxSymbol input,
                                      MyStack<ItemCollection> itemCollectionStack, MyStack<SyntaxSymbol> syntaxSymbolStack,
                                      Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap,
                                      Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap){

        // 获取所有规约项      TODO 可能存在 规约/规约 冲突
        Set<Item> reduceItemSet = new HashSet<>();
        for(Item item : reduceItemCollection.getItemList()){
            if(item.getIndex() == item.getSyntaxProduct().getProduct().size()){
                reduceItemSet.add(item);
            }
        }

        boolean isEmptyReduce = false;
        if(reduceItemSet.size() == 0) {
            // 没有规约项，直接返回
            // FIXME 无法对存在的能推导出空产生式的
            // FIXME 这里可以参考LL(1)分析，如果项集处于倒数第二推导位置项的FIRST集包含ε（该项类似LL的当前展开项），并且当前符号在产生体的FOLLOW中，则也可以进行规约
            // FIXME FOLLOW是超级，需要根据项集的不同推导位置提取，参考"LALR传播与自发生"
            // 循环所有项，判断是否有上述情况的项，以该项作为规约项
            for(Item item : reduceItemCollection.getItemList()){
                if(item.getIndex() == item.getSyntaxProduct().getProduct().size()-1){
                    //判断该产生式的FIRST集合是否有ε，输入符号是否在FOLLOW集中
                    Set<String> firstSet = SyntacticLLParser.getSyntaxFirst(item.getSyntaxProduct().getProduct().get(item.getIndex()), syntaxFirstMap);
                    Set<String> followSet = SyntacticLLParser.getSyntaxFollow(item.getSyntaxProduct().getHead(), syntaxFollowMap);
                    if(firstSet.contains(LexConstants.SYNTAX_EMPTY) && followSet.contains(input.getSymbol())){
                        // 说明类似LL中推到为ε的产生式
                        reduceItemSet.add(item);
                        isEmptyReduce = true;
                    }
                }
            }

            if(reduceItemSet.size() == 0) {
                throw new ParseException("当前项集没有规约项，项集：" + reduceItemCollection.getNumber());
            }
        }

        if(reduceItemSet.size() > 1){
            throw new ParseException("存在 规约/规约 冲突，项集：" + reduceItemCollection.getNumber());
        }

        // 正常情况时只有一个规约项
        for (Item reduceItem : reduceItemSet) {
            // 规约产生式
            SyntaxProduct reduceProduct = reduceItem.getSyntaxProduct();
            List<SyntaxSymbol> reduceProductBody = reduceProduct.getProduct();
            // 将产生式体对应的项集探针（按照产生式的长度）
            if(!isEmptyReduce) {
                for (int i = 0; i < reduceProductBody.size(); i++) {
                    itemCollectionStack.pop();
                    syntaxSymbolStack.pop();
                }
            }else{
                // 如果是按照空产生式规约，则回退长度减一，因为实际之推进了reduceProductBody.size()-1个项集
                for (int i = 0; i < reduceProductBody.size()-1; i++) {
                    itemCollectionStack.pop();
                    syntaxSymbolStack.pop();
                }
            }

            // 规约后回退到的项集
            ItemCollection currentItemCollection = itemCollectionStack.top();
            // 规约后的文法符号
            SyntaxSymbol reduceSymbol = reduceItem.getSyntaxProduct().getHead();

            // 输出规约信息
            System.out.println("按照 " + reduceProduct.toString() + " 规约，当前状态：" + currentItemCollection.getNumber() + ", 规约符号：" + reduceSymbol.getSymbol());

            // 归约后根据归约的符号进行状态迁移
            syntaxGotoLR(currentItemCollection, reduceSymbol, itemCollectionStack, syntaxSymbolStack, syntaxFirstMap, syntaxFollowMap);

        }

    }

    /**执行移入动作**/
    public static void syntaxShiftLR(ItemCollection shiftItemCollection, SyntaxSymbol shiftSyntaxSymbol,
                                     MyStack<ItemCollection> itemCollectionStack, MyStack<SyntaxSymbol> syntaxSymbolStack){

        itemCollectionStack.push(shiftItemCollection);
        syntaxSymbolStack.push(shiftSyntaxSymbol);

        System.out.println("移入：" + shiftSyntaxSymbol.getSymbol() + ", 当前状态：" + itemCollectionStack.top().getNumber());

    }

    /**归约后需要根据归约符号进行移入操作，以后后继的归约操作**/
    public static void syntaxGotoLR(ItemCollection currentItemCollection, SyntaxSymbol reduceSyntaxSymbol,
                                    MyStack<ItemCollection> itemCollectionStack, MyStack<SyntaxSymbol> syntaxSymbolStack,
                                    Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap,
                                    Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap){

        // 根据输入符获取离开后的项集
        ItemCollection nextItemCollection = currentItemCollection.getMoveItemCollectionMap().get(reduceSyntaxSymbol);

        // 根据项集判断是移入还是归约操作
        if(nextItemCollection != null){
            // 将后续项集和转换符号压入栈中
            syntaxShiftLR(nextItemCollection, reduceSyntaxSymbol, itemCollectionStack, syntaxSymbolStack);

            // 判断是移入还是归约操作
            if(nextItemCollection.getItemList().size() == 1 && nextItemCollection.getMoveItemCollectionMap().size() == 0) {
                // 有后继状态，并且项集只有一个项，推导位置处于末尾，说明是归约操作
                // TODO 归约状态判定条件是否正确
                syntaxReduceLR(nextItemCollection, null, itemCollectionStack, syntaxSymbolStack, syntaxFirstMap, syntaxFollowMap);
            }else{
                // goto后不能规约，需要根据后续输入符号进一步处理，交由上层程序处理

            }
        }else{
            throw new ParseException("规约后发生错误, 规约项集：" + currentItemCollection.getNumber() + ", 规约符号：" + reduceSyntaxSymbol.getSymbol());
        }
    }

    public static void setPredictAction(Map<ItemCollection, Map<String, Map<SyntaxSymbol, List<Map<String, Object>>>>> predictSLRMap,
                                        ItemCollection itemCollection, String actionType, SyntaxSymbol syntaxSymbol, Map<String, Object> actionInfo){

        if (predictSLRMap.get(itemCollection) == null) {
            Map<String, Map<SyntaxSymbol, List<Map<String, Object>>>> itemCollectionPredictMap = new LinkedHashMap<>();
            predictSLRMap.put(itemCollection, itemCollectionPredictMap);
        }

        if (predictSLRMap.get(itemCollection).get(actionType) == null) {
            Map<SyntaxSymbol, List<Map<String, Object>>> terminalActionMap = new LinkedHashMap<>();
            predictSLRMap.get(itemCollection).put(actionType, terminalActionMap);
        }

        if (predictSLRMap.get(itemCollection).get(actionType).get(syntaxSymbol) == null) {
            List<Map<String, Object>> actions = new ArrayList<>();
            predictSLRMap.get(itemCollection).get(actionType).put(syntaxSymbol, actions);
        }

        // 只加入新动作
        if(predictSLRMap.get(itemCollection).get(actionType).get(syntaxSymbol).size() == 0){
            predictSLRMap.get(itemCollection).get(actionType).get(syntaxSymbol).add(actionInfo);
        }else{
            Map<String, Object> otherAction = predictSLRMap.get(itemCollection).get(actionType).get(syntaxSymbol).get(0);
            if(!otherAction.get(LexConstants.SYNTAX_LR_ACTION_TYPE).equals(actionInfo.get(LexConstants.SYNTAX_LR_ACTION_TYPE))
                    || !otherAction.get(LexConstants.SYNTAX_LR_ACTION_NEXT_ITEMCOLLECTION).equals(actionInfo.get(LexConstants.SYNTAX_LR_ACTION_NEXT_ITEMCOLLECTION))){
                predictSLRMap.get(itemCollection).get(actionType).get(syntaxSymbol).add(actionInfo);
            }
        }

        if (predictSLRMap.get(itemCollection).get(actionType).get(syntaxSymbol).size() > 1) {
            String confictActions = "";
            for (Map<String, Object> action : predictSLRMap.get(itemCollection).get(actionType).get(syntaxSymbol)) {
                confictActions += action.get(LexConstants.SYNTAX_LR_ACTION_TYPE).toString() + action.get(LexConstants.SYNTAX_LR_ACTION_NEXT_ITEMCOLLECTION).toString();
                confictActions += "|";
            }

            throw new ParseException("LR分析表存在动作冲突，项集：" + itemCollection.getNumber() + ", 终结符：" + syntaxSymbol.getSymbol() + ", 冲突集合：" + confictActions);
        }

    }

    // 获取所有项集
    public static Map<Integer, ItemCollection> getAllItemCollectionMap(ItemCollection startItemCollection){
        Map<Integer, ItemCollection> allItemCollectionMap = new LinkedHashMap<>();
        allItemCollectionMap.put(startItemCollection.getNumber(), startItemCollection);

        // 用于广度遍历
        Set<ItemCollection> tempItemCollectionSet = new LinkedHashSet<>();
        tempItemCollectionSet.addAll(allItemCollectionMap.values());

        boolean hasNew = true;
        while(hasNew){
            hasNew = false;
            Set<ItemCollection> tempSubItemCollectionSet = new LinkedHashSet<>();
            for(ItemCollection itemCollection : tempItemCollectionSet){
                if(!(itemCollection instanceof ItemCollection.AcceptItemCollection)){
                    if(allItemCollectionMap.get(itemCollection.getNumber()) == null){
                        allItemCollectionMap.put(itemCollection.getNumber(), itemCollection);
                    }
                }
                for(ItemCollection subItemCollection : itemCollection.getMoveItemCollectionMap().values()){
                    // 排除接收状态项集
                    if(!(subItemCollection instanceof ItemCollection.AcceptItemCollection)){
                        if(allItemCollectionMap.get(subItemCollection.getNumber()) == null){
                            allItemCollectionMap.put(subItemCollection.getNumber(), subItemCollection);
                            hasNew = true;

                            tempSubItemCollectionSet.addAll(subItemCollection.getMoveItemCollectionMap().values());
                        }
                    }
                }
            }

            tempItemCollectionSet = tempSubItemCollectionSet;
        }

        return allItemCollectionMap;
    }

    // 获取所有action使用的终结符
    public static Set<SyntaxSymbol> getAllTerminalSymbol(List<SyntaxProduct> syntaxProducts){
        Set<SyntaxSymbol> terminalSymbolSet = new LinkedHashSet<>();
        for(SyntaxProduct syntaxProduct : syntaxProducts){
            if(syntaxProduct.getHead().isTerminal()){
                terminalSymbolSet.add(syntaxProduct.getHead());
            }

            for(SyntaxSymbol syntaxSymbol : syntaxProduct.getProduct()){
                if(syntaxSymbol.isTerminal() && !syntaxSymbol.getSymbol().equals(LexConstants.SYNTAX_EMPTY)){
                    terminalSymbolSet.add(syntaxSymbol);
                }
            }
        }

        terminalSymbolSet.add(new SyntaxSymbol(LexConstants.SYNTAX_END, true));

        return terminalSymbolSet;
    }

    // 获取所有goto使用的非终结符
    public static Set<SyntaxSymbol> getAllNonTerminalSymbol(List<SyntaxProduct> syntaxProducts){
        Set<SyntaxSymbol> nonTerminalSymbolSet = new LinkedHashSet<>();
        for(SyntaxProduct syntaxProduct : syntaxProducts){
            if(!syntaxProduct.getHead().isTerminal()){
                nonTerminalSymbolSet.add(syntaxProduct.getHead());
            }

            for(SyntaxSymbol syntaxSymbol : syntaxProduct.getProduct()){
                if(!syntaxSymbol.isTerminal()){
                    nonTerminalSymbolSet.add(syntaxProduct.getHead());
                }
            }
        }

        return nonTerminalSymbolSet;
    }

    /**
     * SLR分析：生成的是SLR分析表
     * */
    public static void syntaxParseSLR(String syntaxFile, String targetProgarmFile, List<LexExpression.Expression> expressions, boolean isClassPath){
        // 解析目标语言文件生成词法单元数据
        List<Token> tokens = LexicalParser.parser(ParseUtils.getFile(targetProgarmFile, isClassPath), expressions);

        // 读取文法文件
        List<String> syntaxs = ParseUtils.getFile(syntaxFile, isClassPath);
        // 解析文法文件
        List<SyntaxSymbol> syntaxSymbols = SyntacticLLParser.parseSyntaxSymbol(syntaxs);

        // 生成所有项集
        AtomicInteger itemCollectionNo = new AtomicInteger(0);
        ItemCollection startItemCollection = SyntacticLRParser.getStartItemCollection(syntaxSymbols, itemCollectionNo.getAndIncrement());
        List<SyntaxProduct> syntaxProducts = SyntacticLRParser.getSyntaxProducts(syntaxSymbols);
        Set<SyntaxSymbol> allGotoSymtaxSymbol = SyntacticLRParser.getAllGotoSymtaxSymbol(syntaxProducts);
        Map<SyntaxSymbol, Set<SyntaxProduct>> symbolProductMap = SyntacticLRParser.getSymbolProductMap(syntaxProducts);
        Map<ItemCollection, ItemCollection> allItemCollectionMap = new LinkedHashMap<>();
        SyntacticLRParser.getLR0ItemCollectionNodes(startItemCollection.getItemList().get(0).getSyntaxProduct(), startItemCollection, allGotoSymtaxSymbol, symbolProductMap, itemCollectionNo, allItemCollectionMap);

        // 生成SLR分析表
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap = SyntacticLLParser.syntaxFirst(syntaxSymbols);
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap = SyntacticLLParser.syntaxFollow(syntaxSymbols, syntaxFirstMap);
        Map<ItemCollection, Map<String, Map<SyntaxSymbol, List<Map<String, Object>>>>> predictSLRMap = SyntacticLRParser.predictLRMap(startItemCollection, syntaxSymbols, syntaxFirstMap, syntaxFollowMap);

        // 根据SLR分析表解析文本
        SyntacticLRParser.syntaxParseLR(startItemCollection, tokens, predictSLRMap);

    }

/**----------------------------------------------------LR 1-----------------------------------------------------------------------**/

    /**
     * LR1分析
     * TODO LALR分析解析
     *      TODO LALR是否会出现“移入/归约”冲突：如果有冲突，说明移入项的后一字符A和归约项的向前看符号FIRST(B)重叠，说明同一文法出现在两个不同层次产生式的不同位置（首位、尾部），修改文法不要让同一文法出现在不同层次、出现的位置不同，
     *      TODO 出现“归约/归约”冲突：归约和上面的类似
     *      TODO 文法注意事项：不要让高层次跨级依赖下级文法，下级文法不要回调上级文法，不要让文法出现在不同产生式的不同位置
     */
    public static Map<Integer, ItemCollection> getLRItemCollectionMap(List<SyntaxSymbol> syntaxSymbols){
        // 处理成增广文法，起始开始符号
        List<SyntaxSymbol> augmentStartSymbolProduct = new ArrayList<>();
        augmentStartSymbolProduct.add(syntaxSymbols.get(0));
        SyntaxSymbol augmentStartSymbol = new SyntaxSymbol(LexConstants.AUGMENT_SYNTAX_TAG + syntaxSymbols.get(0).getSymbol(), false);
        augmentStartSymbol.getBody().add(augmentStartSymbolProduct);

        List<SyntaxSymbol> augmentedSyntax = new ArrayList<>();
        augmentedSyntax.add(augmentStartSymbol);
        augmentedSyntax.addAll(syntaxSymbols);

        // 分离成多个产生式
        List<SyntaxProduct> syntaxProducts = getSyntaxProducts(augmentedSyntax, true);
        Map<SyntaxSymbol, Set<SyntaxProduct>> symbolProductMap = SyntacticLRParser.getSymbolProductMap(syntaxProducts);

        // TODO 考虑到LR(1)文法可以按照空产生式进行推导，因此不需要排序空产生式，将空产生式作为一个单独的项集考虑
        // FIXME 需要考虑是否会出现一个项集两个空产生式的情况，会导致 归约/归约 冲突
        List<SyntaxSymbol> allGotoSymtaxSymbol = new ArrayList<>(SyntacticLRParser.getAllGotoSymtaxSymbol(syntaxProducts));
        allGotoSymtaxSymbol.add(new SyntaxSymbol(LexConstants.SYNTAX_EMPTY, true));

        // 生成所有的FIRST、FOLLOW集合
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap = SyntacticLLParser.syntaxFirst(syntaxSymbols);
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap = SyntacticLLParser.syntaxFollow(syntaxSymbols, syntaxFirstMap);

        // 生产起始增广项集
        AtomicInteger itemCollectionNo = new AtomicInteger(0);
        Item augmentedItem = new Item(syntaxProducts.get(0), 0, LexConstants.SYNTAX_END);
        ItemCollection  startItemCollection = syntaxLRClosure(new ItemCollection(itemCollectionNo.getAndIncrement(), augmentedItem), symbolProductMap, syntaxFirstMap);

        // 记录生成的项集
        Map<ItemCollection, ItemCollection> allItemCollections = new LinkedHashMap<>();
        allItemCollections.put(startItemCollection, startItemCollection);

        // TODO 使用CLOSURE、GOTO函数生成所有项集
        Set<ItemCollection> preItemCollections = Sets.newHashSet(allItemCollections.keySet());
        while(preItemCollections.size() > 0){
            // 记录新生成的项集
            Set<ItemCollection> tempPreItemCollections = new HashSet<>();

            for(SyntaxSymbol gotoSymbol : allGotoSymtaxSymbol){
                // 遍历新加入项集
                for(ItemCollection preItemCollection : preItemCollections){
                    // TODO 考虑到LR(1)文法可以按照空产生式进行推导，因此不需要排序空产生式，将空产生式作为一个单独的项集考虑
                    // FIXME 需要考虑是否会出现一个项集两个空产生式的情况，会导致 归约/归约 冲突
                    // 计算goto集合
                    ItemCollection gotoItemCollection = syntaxLRGoto(preItemCollection, gotoSymbol, startItemCollection.getItemList().get(0).getSyntaxProduct(), symbolProductMap, syntaxFirstMap);

                    if(gotoItemCollection != null) {
                        // 如果原来的集合找不到goto后的项集，说明是新项集
                        if (allItemCollections.get(gotoItemCollection) == null) {
                            // 记录生成的新项集
                            allItemCollections.put(gotoItemCollection, gotoItemCollection);
                            tempPreItemCollections.add(gotoItemCollection);
                            // 设置新项集编码
                            gotoItemCollection.setNumber(itemCollectionNo.getAndIncrement());
                        }

                        // 设置转换关系
                        preItemCollection.getMoveItemCollectionMap().put(gotoSymbol, allItemCollections.get(gotoItemCollection));

                    }
                }
            }

            // 设置下次遍历的新项集
            preItemCollections = tempPreItemCollections;

        }

        // 将集合转变成编号映射
        Map<Integer, ItemCollection> allItemCollectionMap = new LinkedHashMap<>();
        for(ItemCollection itemCollection : allItemCollections.keySet()){
            allItemCollectionMap.put(itemCollection.getNumber(), itemCollection);
        }

        return allItemCollectionMap;
    }

    // 提取文法中所有的文法符号，包括终结符、非终结符，排除ε、$
    public static Set<SyntaxSymbol> getAllGotoSymtaxSymbol(List<SyntaxProduct> syntaxProducts){
        Set<SyntaxSymbol> gotoSyntaxSymbol = new LinkedHashSet<>();

        for(SyntaxProduct syntaxProduct : syntaxProducts){
            if(!gotoSyntaxSymbol.contains(syntaxProduct.getHead())){
                gotoSyntaxSymbol.add(syntaxProduct.getHead());
            }

            for(SyntaxSymbol syntaxSymbol : syntaxProduct.getProduct()){
                if(!syntaxSymbol.getSymbol().equals(LexConstants.SYNTAX_EMPTY)){
                    if (!gotoSyntaxSymbol.contains(syntaxSymbol)) {
                        gotoSyntaxSymbol.add(syntaxSymbol);
                    }
                }
            }
        }

        // 加入终结符，用来转换成接收状态
        gotoSyntaxSymbol.add(new SyntaxSymbol(LexConstants.SYNTAX_END, true));

        return gotoSyntaxSymbol;
    }

    /*CLOSURE函数*/
    public static ItemCollection syntaxLRClosure(ItemCollection itemCollection,
                                                 Map<SyntaxSymbol, Set<SyntaxProduct>> symbolProductMap,
                                                 Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap){

        // 根据SyntaxProduct及index记录项，用于合并项的lookforward集合
        Map<SyntaxProduct, Map<Integer, Item>> reduceItemMap = new HashMap<>();

        for(int i=0; i<itemCollection.getItemList().size(); i++){
            Item item = itemCollection.getItemList().get(i);
            if(item.getIndex() < item.getSyntaxProduct().getProduct().size()) {
                // 计算当前位置推导的向前看符号
                // TODO 验证是否需要处理ε产生式
                // TODO 需要循环将上级lookforward集加入到当前lookforward
                Set<String> lookSymbolSet = getLAItemLookSymbolSet(item, syntaxFirstMap);

                // 加入所有[B→·γ, FIRST(βa)]项
                SyntaxSymbol syntaxSymbol = item.getSyntaxProduct().getProduct().get(item.getIndex());
                if(!syntaxSymbol.isTerminal()) {
                    Set<SyntaxProduct> syntaxProducts = symbolProductMap.get(syntaxSymbol);
                    for (SyntaxProduct product : syntaxProducts) {

                        if(reduceItemMap.get(product) == null){
                            Map<Integer, Item> newItemMap = new HashMap<>();
                            reduceItemMap.put(product, newItemMap);
                        }

                        if(reduceItemMap.get(product).get(0) == null){
                            Item newItem = new Item(product, 0, lookSymbolSet);
                            reduceItemMap.get(product).put(0, newItem);

                            // 加入新项
                            itemCollection.getItemList().add(newItem);
                        }else{
                            // 合并lookforward集合
                            reduceItemMap.get(product).get(0).getLookForwardSymbolSet().addAll(lookSymbolSet);
                        }

                    }
                }

            }
        }

        return itemCollection;

    }

    /*GOTO函数*/
    public static ItemCollection syntaxLRGoto(ItemCollection itemCollection, SyntaxSymbol gotoSymbol, SyntaxProduct startProduct,
                                              Map<SyntaxSymbol, Set<SyntaxProduct>> symbolProductMap,
                                              Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap){

        // 加入可直接移入的项
        List<Item> itemList = new ArrayList<>();
        for(Item item : itemCollection.getItemList()){
            if(item.getIndex() == item.getSyntaxProduct().getProduct().size()
                    && item.getSyntaxProduct().equals(startProduct)
                    && gotoSymbol.getSymbol().equals(LexConstants.SYNTAX_END)){

                // 说明是接收状态
                return new ItemCollection.AcceptItemCollection();

            }else {
                if (item.getIndex() < item.getSyntaxProduct().getProduct().size()) {
                    SyntaxSymbol syntaxSymbol = item.getSyntaxProduct().getProduct().get(item.getIndex());
                    if (syntaxSymbol.equals(gotoSymbol)) {
                        Item newItem = new Item(item.getSyntaxProduct(), item.getIndex() + 1, item.getLookForwardSymbolSet());
                        itemList.add(newItem);
                    }
                }
            }
        }

        // 计算移入后的CLOSURE闭包项集
        if(itemList.size() > 0) {
            ItemCollection gotoItemCollection = new ItemCollection();
            gotoItemCollection.setItemList(itemList);

            // 计算CLOSURE集合
            gotoItemCollection = syntaxLRClosure(gotoItemCollection, symbolProductMap, syntaxFirstMap);

            return gotoItemCollection;
        }else{
            return null;
        }
    }

    /**
     * 获取向前看符号
     *
     *
     * */
    public static Set<String> getLAItemLookSymbolSet(Item item, Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap){
        Set<String> lookSymbolSet = new HashSet<>();
        if(item.getIndex() == item.getSyntaxProduct().getProduct().size()){
            // A → α·，$     这种情况不需要推导，CLOSURE不会推导这种项
//            lookSymbolSet.addAll(item.getLookForwardSymbolSet());
        }else if(item.getIndex() == item.getSyntaxProduct().getProduct().size()-1){
            // A → α·B，c/d      lookSymbol = [c/d]
            lookSymbolSet.addAll(item.getLookForwardSymbolSet());
        }else{
            // A → α·BC，c/d TODO lookSymbol = [FIRST(C) + c/d] 需要验证如果B→ε，C→ε，是否使用FIRST(C)+c/d
            for(int i=(item.getIndex()+1); i<item.getSyntaxProduct().getProduct().size(); i++){
                SyntaxSymbol syntaxSymbol = item.getSyntaxProduct().getProduct().get(i);
                if(syntaxSymbol.isTerminal()){
                    lookSymbolSet.add(syntaxSymbol.getSymbol());
                    break;
                }else{
                    lookSymbolSet.addAll(SyntacticLLParser.getSyntaxFirst(syntaxSymbol, syntaxFirstMap));

                    boolean hasEmpty = false;
                    if (lookSymbolSet.contains(LexConstants.SYNTAX_EMPTY)) {
                        hasEmpty = true;
                        lookSymbolSet.remove(LexConstants.SYNTAX_EMPTY);
                    } else {
                        break;
                    }

                    if(hasEmpty) {
                        if (i == item.getSyntaxProduct().getProduct().size() - 1) {
                            lookSymbolSet.addAll(item.getLookForwardSymbolSet());
                        }
                    }
                }
            }
        }
        return lookSymbolSet;
    }

    /**
     * 构造LR分析表
     * 1、如果[A→α·aβ]在I[i]中，并且GOTO(I[i],a)=I[j]，那么将ACTION[i,a]设置为“移入j”。a是终结符号
     * 2、如果[A→α·]在I[i]中，那么对于FOLLOW(A)中的所有a，那么将ACTION[i,a]设置为“归约A→α”。A不是^S
     * 3、如果[^S→S·]在I[i]中，那么将ACTION[i,$]设置为“接受”
     * 4、其他情况是error状态
     * 5、状态i对于各个非终结符A的GOTO转换使用下列规则构造：如果GOTO(I[i],A)=I[j]，那么GOTO[i,A]=j
     *
     * TODO 这里改造成每个动作都应改变后的状态，相当于不需要再根据 syntaxParseLR0 方法进行实时推导，只要根据每个动作对应的下个项集状态就好了
     *
     */
    public static Map<ItemCollection, Map<String, Map<SyntaxSymbol, List<Map<String, Object>>>>> predictLRMap(ItemCollection startItemCollection, List<SyntaxSymbol> syntaxSymbols,
                                                                                                              Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap,
                                                                                                              Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap){

        List<SyntaxProduct> syntaxProducts = SyntacticLRParser.getSyntaxProducts(syntaxSymbols);

        // TODO 考虑到LR(1)文法可以按照空产生式进行推导，因此不需要排序空产生式，将空产生式作为一个单独的项集考虑
        // FIXME 需要考虑是否会出现一个项集两个空产生式的情况，会导致 归约/归约 冲突
        List<SyntaxSymbol> terminalSymbolSet = new ArrayList<>(getAllTerminalSymbol(syntaxProducts));
        terminalSymbolSet.add(new SyntaxSymbol(LexConstants.SYNTAX_EMPTY, true));

        Set<SyntaxSymbol> nonTerminalSymbolSet = getAllNonTerminalSymbol(syntaxProducts);

        Map<Integer, ItemCollection> allItemCollectionMap = getAllItemCollectionMap(startItemCollection);

        List<Integer> allItemCollectionNumberList = new ArrayList<>(allItemCollectionMap.keySet());
        allItemCollectionNumberList.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });

        // SLR分析表，一级key：项集，二级key：ACTION|GOTO，三级key：输入符，四级key：动作类型、迁移状态
        Map<ItemCollection, Map<String, Map<SyntaxSymbol, List<Map<String, Object>>>>> predictLRMap = new LinkedHashMap<>();

        for(Integer itemCollectionNum : allItemCollectionNumberList){
            ItemCollection itemCollection = allItemCollectionMap.get(itemCollectionNum);
            Map<SyntaxSymbol, ItemCollection> moveItemCollectionMap = itemCollection.getMoveItemCollectionMap();

            // 记录移入
            // TODO 记录可以移入的终结符
            List<String> shiftSymbol = new ArrayList<>();
            for(SyntaxSymbol terminalSymbol : terminalSymbolSet){
                if(terminalSymbol.isTerminal()) {
                    if (moveItemCollectionMap.get(terminalSymbol) != null) {
                        // 可直接进行移入操作
                        ItemCollection moveItemCollection = moveItemCollectionMap.get(terminalSymbol);
                        Map<String, Object> actionInfo = new LinkedHashMap<>();
                        if(moveItemCollection == null){
                            continue;
                        }else if(moveItemCollection instanceof ItemCollection.AcceptItemCollection) {
                            // 3、如果[^S→S·]在I[i]中，那么将ACTION[i,$]设置为“接受”
                            // 接收操作
                            actionInfo.put(LexConstants.SYNTAX_LR_ACTION_TYPE, LexConstants.SYNTAX_LR_ACTION_ACCEPT);
                            actionInfo.put(LexConstants.SYNTAX_LR_ACTION_NEXT_ITEMCOLLECTION, moveItemCollection);

                            setPredictAction(predictLRMap, itemCollection, LexConstants.SYNTAX_LR_ACTION, terminalSymbol, actionInfo);
                        } else {
                            // 1、如果[A→α·aβ]在I[i]中，并且GOTO(I[i],a)=I[j]，那么将ACTION[i,a]设置为“移入j”。a是终结符号
                            // 移入操作
                            // TODO 判断是否是空产生式移入
                            if(!terminalSymbol.getSymbol().equals(LexConstants.SYNTAX_EMPTY)) {
                                actionInfo.put(LexConstants.SYNTAX_LR_ACTION_TYPE, LexConstants.SYNTAX_LR_ACTION_SHIFT);
                                actionInfo.put(LexConstants.SYNTAX_LR_ACTION_NEXT_ITEMCOLLECTION, moveItemCollection);

                                setPredictAction(predictLRMap, itemCollection, LexConstants.SYNTAX_LR_ACTION, terminalSymbol, actionInfo);

                                // TODO 记录可以移入的终结符
                                shiftSymbol.add(terminalSymbol.getSymbol());
                            }else{
                                // TODO 空产生式移入需要特别表示
                                actionInfo.put(LexConstants.SYNTAX_LR_ACTION_TYPE, LexConstants.SYNTAX_LR_ACTION_SHIFT_EPSILON);
                                actionInfo.put(LexConstants.SYNTAX_LR_ACTION_NEXT_ITEMCOLLECTION, moveItemCollection);

                                // TODO 如果是空产生式需要将转换符替换成 lookforwar集合，每个字符都有单独的转换边到达该项集
                                // TODO 验证是否会出现 移入/移入 冲突，即 lookforwar集合中的元素和其他项的元素重叠
                                Set<String> emptyLookforwardSet = new HashSet<>();
                                for(Item emptyItem : moveItemCollection.getItemList()) {
                                    for(String lookforward : emptyItem.getLookForwardSymbolSet()){
                                        if(!emptyLookforwardSet.contains(lookforward)){
                                            emptyLookforwardSet.add(lookforward);
                                        }else{
                                            throw new ParseException("多个空产生式向前看符号有重叠，不能唯一确认, 项集：" + moveItemCollection.getNumber());
                                        }
                                    }
                                }
                                for(String lookforward : emptyLookforwardSet){
                                    // TODO 只有不在shiftSymbol可移入符号的向前看符号，才可以进行空产生式移入，避免出现移入/空产生式移入冲突
                                    if(!shiftSymbol.contains(lookforward)) {
                                        SyntaxSymbol lookforwardSymbol = new SyntaxSymbol(lookforward, true);
                                        setPredictAction(predictLRMap, itemCollection, LexConstants.SYNTAX_LR_ACTION, lookforwardSymbol, actionInfo);
                                    }
                                }
                            }
                        }

                    }
                }
            }

            // 设置规约项
            if(!itemCollection.equals(startItemCollection)) {
                // 2、如果[A→α·]在I[i]中，那么对于FOLLOW(A)中的所有a，那么将ACTION[i,a]设置为“归约A→α”。A不是^S
                // 判断是否有归约项
                Set<Item> reduceItemList = new HashSet<>();
                for (Item item : itemCollection.getItemList()) {
                    if (item.getIndex() == item.getSyntaxProduct().getProduct().size()) {
                        if(!item.getSyntaxProduct().equals(startItemCollection.getItemList().get(0).getSyntaxProduct())) {
                            // 已到达推导末尾，可以进行归约（排除接收项）
                            reduceItemList.add(item);
                        }
                    }
                }

                if (reduceItemList.size() == 0) {
                    // TODO 似乎不用做太多操作
                } else {
                    // FIXME 验证调整成LR后同一项集是否会存在多个归约项
                    Set<String> reduceLookforwardSet = new HashSet<>();
                    for(Item reduceItem : reduceItemList){
                        for(String lookforward : reduceItem.getLookForwardSymbolSet()){
                            if(!reduceLookforwardSet.contains(lookforward)){
                                reduceLookforwardSet.add(lookforward);
                            }else{
                                throw new ParseException("多个归约项向前看符号存在重叠，不能唯一确认，可能存在 规约/规约 冲突，项集：" + itemCollection.getNumber());
                            }
                        }
                    }

                    for (Item reduceItem : reduceItemList) {
                        Map<String, Object> actionInfo = new LinkedHashMap<>();
                        // 2、如果[A→α·]在I[i]中，那么对于FOLLOW(A)中的所有a，那么将ACTION[i,a]设置为“归约A→α”。A不是^S
                        // 归约操作
                        actionInfo.put(LexConstants.SYNTAX_LR_ACTION_TYPE, LexConstants.SYNTAX_LR_ACTION_REDUCE);
                        actionInfo.put(LexConstants.SYNTAX_LR_ACTION_NEXT_ITEMCOLLECTION, reduceItem);

                        // LR项集中只有遇到lookSymbolSet才能进行归约，而不是follow集
                        Set<String> lookSymbolSet = reduceItem.getLookForwardSymbolSet();
                        for (String lookSymbol : lookSymbolSet) {
                            if (!lookSymbol.equals(LexConstants.SYNTAX_EMPTY)) {
                                SyntaxSymbol terminalSymbol = new SyntaxSymbol(lookSymbol, true);

                                setPredictAction(predictLRMap, itemCollection, LexConstants.SYNTAX_LR_ACTION, terminalSymbol, actionInfo);

                            }
                        }
                    }
                }

            }

            // 5、状态i对于各个非终结符A的GOTO转换使用下列规则构造：如果GOTO(I[i],A)=I[j]，那么GOTO[i,A]=j
            // 记录GOTO
            for(SyntaxSymbol nonTerminalSymbol : nonTerminalSymbolSet){
                if(moveItemCollectionMap.get(nonTerminalSymbol) != null){
                    ItemCollection moveItemCollection = moveItemCollectionMap.get(nonTerminalSymbol);
                    Map<String, Object> actionInfo = new LinkedHashMap<>();
                    actionInfo.put(LexConstants.SYNTAX_LR_ACTION_TYPE, LexConstants.SYNTAX_LR_ACTION_GOTO);
                    actionInfo.put(LexConstants.SYNTAX_LR_ACTION_NEXT_ITEMCOLLECTION, moveItemCollection);

                    setPredictAction(predictLRMap, itemCollection, LexConstants.SYNTAX_LR_GOTO, nonTerminalSymbol, actionInfo);

                }
            }

        }

        return predictLRMap;

    }

    /**
     * 根据LR分析表解析生成语法分析树（只包含非终结符号节点，终结符号节点需要对比产生式获取）
     * LR分析表，一级key：项集，二级key：ACTION|GOTO，三级key：输入符，四级key：动作类型、迁移状态
     */
    public static SyntaxTree syntaxParseLR(ItemCollection startItemCollection, List<Token> tokens,
                                           Map<ItemCollection, Map<String, Map<SyntaxSymbol, List<Map<String, Object>>>>> predictLRMap){

        // 语法分析树根节点
        SyntaxTree syntaxTree = new SyntaxTree();
        // 语法分析树节点编号
        AtomicInteger syntaxTreeNodeNum = new AtomicInteger(0);
        // 用于记录生成的语法分析树节点
        MyStack<SyntaxTree.SyntaxTreeNode> currentSubProductNodeStack = new MyStack<>();

        // 记录当前推导的位置（推导链路）
        MyStack<ItemCollection> itemCollectionStack = new MyStack<>();
        itemCollectionStack.push(startItemCollection);
        System.out.println("移入 " + startItemCollection.getNumber());

        ItemCollection currentItemCollection = startItemCollection;
        for(int i=0; i<tokens.size(); i++){
            Token token = tokens.get(i);
            SyntaxSymbol tokenSyntaxSymbol = null;
            if(token.getType().isRexgexToken()){
                tokenSyntaxSymbol = new SyntaxSymbol(token.getType().getType(), true);
            }else{
                tokenSyntaxSymbol = new SyntaxSymbol(token.getToken(), true);
            }

            Map<String, Map<SyntaxSymbol, List<Map<String, Object>>>> itemCollectionPredictMap = predictLRMap.get(currentItemCollection);
            Map<SyntaxSymbol, List<Map<String, Object>>> actionPredictMap = itemCollectionPredictMap.get(LexConstants.SYNTAX_LR_ACTION);

            List<Map<String, Object>> actionOperats = actionPredictMap.get(tokenSyntaxSymbol);

            try {
                if (actionOperats == null || actionOperats.size() == 0) {
                    // 说明对应的操作为报错
                    throw new ParseException("LR分析表ACTION异常，项集" + currentItemCollection.getNumber() + ", 输入符：" + tokenSyntaxSymbol.getSymbol());
                } else if (actionOperats.size() > 1) {
                    String confictActions = "";
                    for (Map<String, Object> actionInfo : actionOperats) {
                        confictActions += actionInfo.get(LexConstants.SYNTAX_LR_ACTION_TYPE).toString() + ((ItemCollection) actionInfo.get(LexConstants.SYNTAX_LR_ACTION_NEXT_ITEMCOLLECTION)).getNumber();
                        confictActions += "|";
                    }

                    throw new ParseException("LR分析表ACTION存在冲突，项集：" + currentItemCollection.getNumber() + ", 终结符：" + tokenSyntaxSymbol.getSymbol() + ", 冲突集合：" + confictActions);
                } else {
                    Map<String, Object> actionInfo = actionOperats.get(0);
                    if (actionInfo.get(LexConstants.SYNTAX_LR_ACTION_TYPE).equals(LexConstants.SYNTAX_LR_ACTION_SHIFT)) {
                        // 说明是移入操作，压入下一项集状态
                        currentItemCollection = syntaxLRShiftByPredictMap(actionInfo, itemCollectionStack, token, syntaxTreeNodeNum, currentSubProductNodeStack);

                    } else if (actionInfo.get(LexConstants.SYNTAX_LR_ACTION_TYPE).equals(LexConstants.SYNTAX_LR_ACTION_SHIFT_EPSILON)) {
                        // 说明是空产生式移入操作，压入下一项集状态
                        // TODO 这里出于技术考虑，将token替换成ε
                        Token epsilonToken = new Token(LexConstants.EPSILON_STR, new LexExpression.TokenType(LexConstants.SYNTAX_EMPTY));
                        currentItemCollection = syntaxLRShiftByPredictMap(actionInfo, itemCollectionStack, epsilonToken, syntaxTreeNodeNum, currentSubProductNodeStack);

                        // 空产生式移入操作需要保持输入符不变
                        i--;
                    } else if (actionInfo.get(LexConstants.SYNTAX_LR_ACTION_TYPE).equals(LexConstants.SYNTAX_LR_ACTION_REDUCE)) {
                        // 说明是规约操作，根据规约产生式先弹出对应数量的项集状态，再压入GOTO后的项集状态
                        currentItemCollection = syntaxLRReduceByPredictMap(actionInfo, tokenSyntaxSymbol, itemCollectionStack, predictLRMap, syntaxTreeNodeNum, currentSubProductNodeStack);

                        // 归约后输入符需要保持不变
                        i--;

                    } else if (actionInfo.get(LexConstants.SYNTAX_LR_ACTION_TYPE).equals(LexConstants.SYNTAX_LR_ACTION_ACCEPT)) {
                        // 说明是接收状态
                        if (i == tokens.size() - 1) {
                            System.out.println("接收");

                            // 接收状态设置根节点，根节点为上一次归约使用的产生式（当前接收的产生式是增广文法的产生式）
                            syntaxTree.setSyntaxTreeRoot(currentSubProductNodeStack.top());

                            break;
                        } else {
                            throw new ParseException("SLR分析表接收状态异常，味道输入流末尾");
                        }
                    }
                }
            } catch (ParseException e){
                throw new ParseException("line: " + token.getLine() + ", index: " + token.getIndex() + ", error: " + e.getMessage());
            }

        }

        return syntaxTree;
    }

    public static SyntaxTree syntaxParseLR(List<String> syntaxs, List<Token> tokens){
        List<SyntaxSymbol> syntaxSymbols = SyntacticLLParser.parseSyntaxSymbol(syntaxs);

        // 生成所有 LR ItemCollection 集合
        Map<Integer, ItemCollection> allLRItemCollectionMap = SyntacticLRParser.getLRItemCollectionMap(syntaxSymbols);

        // 生成 LR PredictMap 与分析表
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap = SyntacticLLParser.syntaxFirst(syntaxSymbols);
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap = SyntacticLLParser.syntaxFollow(syntaxSymbols, syntaxFirstMap);
        Map<ItemCollection, Map<String, Map<SyntaxSymbol, List<Map<String, Object>>>>> predictLRMap = SyntacticLRParser.predictLRMap(allLRItemCollectionMap.get(0), syntaxSymbols, syntaxFirstMap, syntaxFollowMap);

        // 根据LR PredictMap 预测分析表解析目标代码
        return syntaxParseLR(allLRItemCollectionMap.get(0), tokens, predictLRMap);
    }

    public static SyntaxTree syntaxParseLR(String lexicalFile, String syntaxFile, String targetProgarmFile, boolean isClassPath){
        // 读取词法文件
        List<String> lexicalContent = ParseUtils.getFile(lexicalFile, isClassPath);
        List<LexExpression.Expression> expressions = LexicalParser.readExpressionFile(lexicalContent);

        return syntaxParseLR(syntaxFile, targetProgarmFile, expressions, isClassPath);
    }

    public static SyntaxTree syntaxParseLR(String syntaxFile, String targetProgarmFile, List<LexExpression.Expression> expressions, boolean isClassPath){
        // 读取文法文件
        List<String> syntaxs = ParseUtils.getFile(syntaxFile, isClassPath);

        // 解析目标语言文件生成词法单元数据
        List<Token> tokens = LexicalParser.parser(ParseUtils.getFile(targetProgarmFile, isClassPath), expressions);
        List<SyntaxSymbol> syntaxSymbols = SyntacticLLParser.parseSyntaxSymbol(syntaxs);

        // 生成所有 LR ItemCollection 集合
        Map<Integer, ItemCollection> allLRItemCollectionMap = SyntacticLRParser.getLRItemCollectionMap(syntaxSymbols);

        // 生成 LR PredictMap 与分析表
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Set<String>>> syntaxFirstMap = SyntacticLLParser.syntaxFirst(syntaxSymbols);
        Map<SyntaxSymbol, Map<List<SyntaxSymbol>, Map<Integer, Set<String>>>> syntaxFollowMap = SyntacticLLParser.syntaxFollow(syntaxSymbols, syntaxFirstMap);
        Map<ItemCollection, Map<String, Map<SyntaxSymbol, List<Map<String, Object>>>>> predictLRMap = SyntacticLRParser.predictLRMap(allLRItemCollectionMap.get(0), syntaxSymbols, syntaxFirstMap, syntaxFollowMap);

        // 根据LR PredictMap 预测分析表解析目标代码
        return syntaxParseLR(allLRItemCollectionMap.get(0), tokens, predictLRMap);
    }

    /* 构建语法分析树 */
    public static void constructSyntaxTree(Map<String, Object> actionInfo, boolean isShift, Token input,
                                           AtomicInteger syntaxTreeNodeNum, MyStack<SyntaxTree.SyntaxTreeNode> currentSubProductNodeStack){

        SyntaxTree.SyntaxTreeNode productNode = null;
        if(isShift){
            // 如果是移入操作，说明是终结符号，加入叶子节点
            productNode = new SyntaxTree.SyntaxTreeNode(syntaxTreeNodeNum.getAndIncrement(), true, true, input, new SyntaxSymbol(input.getType().isRexgexToken() ? input.getType().getType() : input.getToken(), true));
            currentSubProductNodeStack.push(productNode);
        }else{
            // 获取归约项对应的产生式
            Item reduceItem = (Item) actionInfo.get(LexConstants.SYNTAX_LR_ACTION_NEXT_ITEMCOLLECTION);
            SyntaxProduct reduceProduct = reduceItem.getSyntaxProduct();

            List<SyntaxTree.SyntaxTreeNode> subTreeNodeList = new ArrayList<>();
            for (SyntaxSymbol symbol : reduceProduct.getProduct()) {
                subTreeNodeList.add(currentSubProductNodeStack.pop());
            }
            // 需要反转一下
            Collections.reverse(subTreeNodeList);

            productNode = new SyntaxTree.SyntaxTreeNode(syntaxTreeNodeNum.getAndIncrement(), false, reduceProduct, subTreeNodeList);
            currentSubProductNodeStack.push(productNode);
        }

    }

    /*shift操作*/
    public static ItemCollection syntaxLRShiftByPredictMap(Map<String, Object> actionInfo, MyStack<ItemCollection> itemCollectionStack, Token input,
                                                           AtomicInteger syntaxTreeNodeNum, MyStack<SyntaxTree.SyntaxTreeNode> currentSubProductNodeStack){

        // 说明是移入操作，压入下一项集状态
        ItemCollection shiftItemCollection = (ItemCollection) actionInfo.get(LexConstants.SYNTAX_LR_ACTION_NEXT_ITEMCOLLECTION);
        itemCollectionStack.push(shiftItemCollection);

        System.out.println("移入 " + shiftItemCollection.getNumber());

        constructSyntaxTree(actionInfo, true, input, syntaxTreeNodeNum, currentSubProductNodeStack);

        return shiftItemCollection;
    }

    /*reduce操作*/
    public static ItemCollection syntaxLRReduceByPredictMap(Map<String, Object> actionInfo, SyntaxSymbol tokenSyntaxSymbol, MyStack<ItemCollection> itemCollectionStack,
                                                            Map<ItemCollection, Map<String, Map<SyntaxSymbol, List<Map<String, Object>>>>> predictLRMap,
                                                            AtomicInteger syntaxTreeNodeNum, MyStack<SyntaxTree.SyntaxTreeNode> currentSubProductNodeStack){

        // 说明是规约操作，根据规约产生式先弹出对应数量的项集状态，再压入GOTO后的项集状态
        Item reduceItem = (Item) actionInfo.get(LexConstants.SYNTAX_LR_ACTION_NEXT_ITEMCOLLECTION);
        for(int j=0; j<=reduceItem.getIndex()-1; j++){
            itemCollectionStack.pop();
        }

        System.out.println("规约 " + reduceItem.toString());

        // 构建语法分析树
        constructSyntaxTree(actionInfo, false, null, syntaxTreeNodeNum, currentSubProductNodeStack);

        // 归约后需要根据归约后的符号进行GOTO操作
        ItemCollection currentItemCollection = syntaxLRGotoByPredictMap(reduceItem, tokenSyntaxSymbol, itemCollectionStack.top(), itemCollectionStack, predictLRMap, syntaxTreeNodeNum, currentSubProductNodeStack);

        return currentItemCollection;
    }

    /*goto操作*/
    public static ItemCollection syntaxLRGotoByPredictMap(Item reduceItem, SyntaxSymbol tokenSyntaxSymbol,
                                                          ItemCollection currentItemCollection, MyStack<ItemCollection> itemCollectionStack,
                                                          Map<ItemCollection, Map<String, Map<SyntaxSymbol, List<Map<String, Object>>>>> predictLRMap,
                                                          AtomicInteger syntaxTreeNodeNum, MyStack<SyntaxTree.SyntaxTreeNode> currentSubProductNodeStack){

        // 根据归约栈顶的状态进行GOTO操作
        Map<String, Map<SyntaxSymbol, List<Map<String, Object>>>> reduceItemCollectionPredictMap = predictLRMap.get(currentItemCollection);
        Map<SyntaxSymbol, List<Map<String, Object>>> gotoPredictMap = reduceItemCollectionPredictMap.get(LexConstants.SYNTAX_LR_GOTO);

        SyntaxSymbol reduceSyntaxSymbol = reduceItem.getSyntaxProduct().getHead();
        List<Map<String, Object>> gotoOperats = gotoPredictMap.get(reduceSyntaxSymbol);
        if(gotoOperats == null && gotoOperats.size() == 0){
            // 说明对应的操作为报错
            throw new ParseException("LR分析表GOTO异常，项集" + currentItemCollection.getNumber() + ", 输入符：" + tokenSyntaxSymbol.getSymbol());

        }else if(gotoOperats.size() > 1){
            String confictActions = "";
            for (Map<String, Object> gotoInfo : gotoOperats) {
                confictActions += gotoInfo.get(LexConstants.SYNTAX_LR_ACTION_TYPE).toString() + ((Item)gotoInfo.get(LexConstants.SYNTAX_LR_ACTION_NEXT_ITEMCOLLECTION)).getSyntaxProduct().getNumber();
                confictActions += "|";
            }

            throw new ParseException("LR分析表GOTO存在冲突，项集：" + currentItemCollection.getNumber() + ", 终结符：" + tokenSyntaxSymbol.getSymbol() + ", 冲突集合：" + confictActions);

        }else{
            Map<String, Object> gotoInfo = gotoOperats.get(0);
            ItemCollection gotoItemCollection = (ItemCollection) gotoInfo.get(LexConstants.SYNTAX_LR_ACTION_NEXT_ITEMCOLLECTION);
            itemCollectionStack.push(gotoItemCollection);

            System.out.println("GOTO " + gotoItemCollection.getNumber());

            currentItemCollection = gotoItemCollection;

            // FIXME 这里需要嵌套处理GOTO，如果GOTO后的状态在当前输入符下也是规约动作，则继续规约
            Map<String, Map<SyntaxSymbol, List<Map<String, Object>>>> nextItemCollectionPredictMap = predictLRMap.get(currentItemCollection);
            Map<SyntaxSymbol, List<Map<String, Object>>> nextActionPredictMap = nextItemCollectionPredictMap.get(LexConstants.SYNTAX_LR_ACTION);

            List<Map<String, Object>> nextActionOperats = nextActionPredictMap.get(tokenSyntaxSymbol);
            if(nextActionOperats == null || nextActionOperats.size() == 0){
                // 说明对应的操作为报错
                throw new ParseException("LR分析表ACTION异常，项集" + currentItemCollection.getNumber() + ", 输入符：" + tokenSyntaxSymbol.getSymbol());

            }else if(nextActionOperats.size() > 1){
                String confictActions = "";
                for (Map<String, Object> actionInfo : nextActionOperats) {
                    confictActions += actionInfo.get(LexConstants.SYNTAX_LR_ACTION_TYPE).toString() + ((ItemCollection)actionInfo.get(LexConstants.SYNTAX_LR_ACTION_NEXT_ITEMCOLLECTION)).getNumber();
                    confictActions += "|";
                }

                throw new ParseException("LR分析表ACTION存在冲突，项集：" + currentItemCollection.getNumber() + ", 终结符：" + tokenSyntaxSymbol.getSymbol() + ", 冲突集合：" + confictActions);

            }else{
                Map<String, Object> nextActionInfo = nextActionOperats.get(0);
                if(nextActionInfo.get(LexConstants.SYNTAX_LR_ACTION_TYPE).equals(LexConstants.SYNTAX_LR_ACTION_REDUCE)){
                    // 说明是可以继续规约
                    currentItemCollection = syntaxLRReduceByPredictMap(nextActionInfo, tokenSyntaxSymbol, itemCollectionStack, predictLRMap, syntaxTreeNodeNum, currentSubProductNodeStack);

                }
            }

        }

        return currentItemCollection;
    }

}