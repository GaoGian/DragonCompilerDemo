package gian.compiler.practice.syntactic;

import gian.compiler.practice.exception.ParseException;
import gian.compiler.practice.lexical.parser.Token;
import gian.compiler.practice.lexical.transform.LexConstants;
import gian.compiler.practice.lexical.transform.MyStack;
import gian.compiler.practice.syntactic.lrsyntax.Item;
import gian.compiler.practice.syntactic.lrsyntax.ItemCollection;
import gian.compiler.practice.syntactic.symbol.SyntaxProduct;
import gian.compiler.practice.syntactic.symbol.SyntaxSymbol;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * LR 分析器
 * Created by gaojian on 2019/3/1.
 */
public class SyntacticLRParser {

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
        List<SyntaxProduct> syntaxProducts = getSyntaxProducts(augmentedSyntax);

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
        List<SyntaxProduct> syntaxProducts = new ArrayList<>();
        int number = 0;
        for(SyntaxSymbol syntaxSymbol : syntaxSymbols){
            for(List<SyntaxSymbol> product : syntaxSymbol.getBody()){
                SyntaxProduct syntaxProduct = new SyntaxProduct(number, syntaxSymbol, product);
                syntaxProducts.add(syntaxProduct);
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
     */
    public static boolean syntaxParseLR0(ItemCollection startItemCollection, List<Token> tokenList){
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
                if(nextItemCollection.getItemList().size() == 1) {
                    // 有后继状态，并且项集只有一个项，推导位置处于末尾，说明是归约操作     // TODO 归约状态判定条件是否正确
                    syntaxReduceLR(nextItemCollection, itemCollectionStack, syntaxSymbolStack);
                }else{
                    // 不表示直接规约项，先将符号和项集压入栈中，根据后续输入符号进行一下不操作
                    syntaxShiftLR(nextItemCollection, moveSymbol, itemCollectionStack, syntaxSymbolStack);
                }
            }else{
                // TODO 如果没有后继状态，则判断是否有归约项，如果有则先归约再根据归约后的符号进行移入操作，如果没有则报错
                // TODO 可能会有 规约/规约 冲突
                syntaxReduceLR(currentItemCollection, itemCollectionStack, syntaxSymbolStack);
            }

        }

        return acceptSuccess;
    }

    /**执行规约动作**/
    public static void syntaxReduceLR(ItemCollection reduceItemCollection,
                                      MyStack<ItemCollection> itemCollectionStack,
                                      MyStack<SyntaxSymbol> syntaxSymbolStack){

        // TODO 按照项集的生成方式，规约项只会在首位
        Set<Item> reduceItemSet = new HashSet<>();
        for(Item item : reduceItemCollection.getItemList()){
            if(item.getIndex() == item.getSyntaxProduct().getProduct().size()){
                reduceItemSet.add(item);
            }
        }

        if(reduceItemSet.size() > 1){
            throw new ParseException("存在 规约/规约 冲突，项集：" + reduceItemCollection.getNumber());
        }else {
            // 正常情况时只有一个规约项
            for (Item reduceItem : reduceItemSet) {
                if(reduceItem.getIndex() == reduceItem.getSyntaxProduct().getProduct().size()){
                    // 规约产生式
                    SyntaxProduct reduceProduct = reduceItem.getSyntaxProduct();
                    List<SyntaxSymbol> reduceProductBody = reduceProduct.getProduct();
                    // 输出规约信息
                    System.out.println("按照 " + reduceProduct.toString() + " 规约");
                    // 将产生式体对应的项集探针（按照产生式的长度）
                    for(int i=0; i<reduceProductBody.size(); i++){
                        itemCollectionStack.pop();
                        syntaxSymbolStack.pop();
                    }

                    // 规约后回退到的项集
                    ItemCollection currentItemCollection = itemCollectionStack.top();
                    // 规约后的文法符号
                    SyntaxSymbol moveSymbol = reduceItem.getSyntaxProduct().getHead();

                    // 根据输入符获取离开后的项集
                    ItemCollection nextItemCollection = currentItemCollection.getMoveItemCollectionMap().get(moveSymbol);

                    // 根据项集判断是移入还是归约操作
                    if(nextItemCollection != null){
                        // 判断是移入还是归约操作
                        if(nextItemCollection.getItemList().size() == 1) {
                            // 有后继状态，并且项集只有一个项，推导位置处于末尾，说明是归约操作
                            // TODO 归约状态判定条件是否正确
                            syntaxReduceLR(reduceItemCollection, itemCollectionStack, syntaxSymbolStack);
                        }else{
                            // 不表示直接规约项，先将符号和项集压入栈中，根据后续输入符号进行一下不操作
                            syntaxShiftLR(nextItemCollection, moveSymbol, itemCollectionStack, syntaxSymbolStack);
                        }
                    }else{
                        throw new ParseException("规约后发生错误, 规约项集：" + currentItemCollection.getNumber() + ", 规约符号：" + moveSymbol.getSymbol());
                    }
                }else{
                    throw new ParseException("项集状态错误：" + reduceItem.toString());
                }
            }
        }

    }

    /**执行移入动作**/
    public static void syntaxShiftLR(ItemCollection shiftItemCollection, SyntaxSymbol shiftSyntaxSymbol,
                                     MyStack<ItemCollection> itemCollectionStack, MyStack<SyntaxSymbol> syntaxSymbolStack){

        System.out.println("移入：" + shiftSyntaxSymbol.getSymbol());
        itemCollectionStack.push(shiftItemCollection);
        syntaxSymbolStack.push(shiftSyntaxSymbol);

    }

}