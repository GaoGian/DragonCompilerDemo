package gian.compiler.practice.syntactic;

import gian.compiler.practice.exception.ParseException;
import gian.compiler.practice.lexical.transform.LexConstants;
import gian.compiler.practice.syntactic.lrsyntax.Item;
import gian.compiler.practice.syntactic.lrsyntax.ItemCollection;
import gian.compiler.practice.syntactic.symbol.SyntaxProduct;
import gian.compiler.practice.syntactic.symbol.SyntaxSymbol;

import java.util.ArrayList;
import java.util.List;

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
    public static ItemCollection closure(List<Item> items, int itemCollectionNo){
        List<Item> closureItem = new ArrayList<>();
        // 3、不算运用规则1、2，直到没有新项
        boolean hasNew = true;
        while(hasNew) {
            hasNew = false;
            for (Item item : items) {
                if(!closureItem.contains(item)) {
                    // 1、將 项集I 中的各项加入到 CLOSURE (I)中；
                    closureItem.add(item);
                    hasNew = true;
                }
                if(item.getIndex() >= item.getSyntaxProduct().getProduct().size()){
                    throw  new ParseException("计算项集CLOSURE(I)异常，已经存在可以规约的项，请检查GOTO函数");
                }
                SyntaxSymbol syntaxSymbol = item.getSyntaxProduct().getProduct().get(item.getIndex());
                if(!syntaxSymbol.isTerminal()){
                    // 2、如果 A→α· Bβ在 CLOUSE(I) 中，B→γ是一个产生式，B→·γ不在 CLOSURE(I)中，就将B→·γ加入到CLOSURE(I)中；
                    Item newItem = new Item(item.getSyntaxProduct(), 0);
                    if(!closureItem.contains(newItem)){
                        closureItem.add(newItem);
                        hasNew = true;
                    }
                }
            }
        }

        ItemCollection closureItemCollection = new ItemCollection(itemCollectionNo, closureItem);

        return closureItemCollection;
    }

    public static ItemCollection closure(ItemCollection itemollection, int itemCollectionNo){
        List<Item> items = itemollection.getItemList();
        return closure(items, itemCollectionNo);
    }

    /**
     * 计算项集状态迁移
     */
    public static void moveItem(ItemCollection itemCollection, SyntaxSymbol tranSymbol){

    }

}