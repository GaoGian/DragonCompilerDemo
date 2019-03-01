package gian.compiler.practice.syntactic.lrsyntax;

import gian.compiler.practice.syntactic.symbol.SyntaxProduct;

/**
 * LR 状态项
 * Created by gaojian on 2019/3/1.
 */
public class Item {

    protected Integer productNo;
    protected SyntaxProduct product;
    protected int index;

    public Item(){}

    public Item(Integer productNo, SyntaxProduct product, int index) {
        this.productNo = productNo;
        this.product = product;
        this.index = index;
    }

    public Integer getProductNo() {
        return productNo;
    }

    public void setProductNo(Integer productNo) {
        this.productNo = productNo;
    }

    public SyntaxProduct getProduct() {
        return product;
    }

    public void setProduct(SyntaxProduct product) {
        this.product = product;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}