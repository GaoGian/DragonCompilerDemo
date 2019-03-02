package gian.compiler.practice.syntactic;

import gian.compiler.practice.exception.ParseException;
import gian.compiler.practice.lexical.transform.LexConstants;
import gian.compiler.practice.syntactic.lrsyntax.Item;
import gian.compiler.practice.syntactic.lrsyntax.ItemCollection;
import gian.compiler.practice.syntactic.symbol.SyntaxProduct;
import gian.compiler.practice.syntactic.symbol.SyntaxSymbol;

import java.util.*;

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
    public static ItemCollection moveItem(List<Item> items, SyntaxSymbol moveSymbol, int itemCollectionNo, Map<SyntaxSymbol, Set<SyntaxProduct>> symbolProductMap){
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

        // 计算新项集的 CLOSURE 闭包
        ItemCollection moveItemCollection = closure(moveItemList, itemCollectionNo, symbolProductMap);

        return moveItemCollection;
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
                if(!syntaxSymbol.getSymbol().equals(LexConstants.SYNTAX_EMPTY)
                        && !syntaxSymbol.getSymbol().equals(LexConstants.SYNTAX_END)){

                    if (!gotoSyntaxSymbol.contains(syntaxSymbol)) {
                        gotoSyntaxSymbol.add(syntaxSymbol);
                    }
                }
            }
        }

        return gotoSyntaxSymbol;
    }

}