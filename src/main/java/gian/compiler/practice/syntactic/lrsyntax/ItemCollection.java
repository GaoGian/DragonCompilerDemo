package gian.compiler.practice.syntactic.lrsyntax;

import java.util.ArrayList;
import java.util.List;

/**
 * LR 项集簇
 * Created by gaojian on 2019/3/1.
 */
public class ItemCollection {

    protected Integer number;
    protected List<Item> itemList = new ArrayList<>();

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
}