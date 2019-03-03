package gian.compiler.practice.syntactic.lrsyntax;

import gian.compiler.practice.syntactic.symbol.SyntaxSymbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LR 项集簇
 * Created by gaojian on 2019/3/1.
 */
public class ItemCollection {

    protected Integer number;
    protected List<Item> itemList = new ArrayList<>();

    protected Map<SyntaxSymbol, ItemCollection> moveItemCollectionMap = new HashMap<>();

    public ItemCollection(){}

    public ItemCollection(Integer number, List<Item> itemList) {
        this.number = number;
        this.itemList = itemList;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public Map<SyntaxSymbol, ItemCollection> getMoveItemCollectionMap() {
        return moveItemCollectionMap;
    }

    public void setMoveItemCollectionMap(Map<SyntaxSymbol, ItemCollection> moveItemCollectionMap) {
        this.moveItemCollectionMap = moveItemCollectionMap;
    }

    @Override
    public String toString(){
        return this.itemList.toString();
    }

    @Override
    public boolean equals(Object other){
        return this.itemList.equals(((ItemCollection) other).getItemList());
    }

    @Override
    public int hashCode(){
        return 0;
    }

    public static class AcceptItemCollection extends ItemCollection{
        @Override
        public String toString(){
            return "accept";
        }
    }

}