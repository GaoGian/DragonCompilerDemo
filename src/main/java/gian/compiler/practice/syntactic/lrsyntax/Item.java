package gian.compiler.practice.syntactic.lrsyntax;

import gian.compiler.practice.lexical.transform.LexConstants;
import gian.compiler.practice.syntactic.symbol.SyntaxProduct;

/**
 * LR 状态项
 * Created by gaojian on 2019/3/1.
 */
public class Item {

    protected SyntaxProduct syntaxProduct;
    protected int index;

    public Item(){}

    public Item(SyntaxProduct syntaxProduct, int index) {
        this.syntaxProduct = syntaxProduct;
        this.index = index;
    }

    public SyntaxProduct getSyntaxProduct() {
        return syntaxProduct;
    }

    public void setSyntaxProduct(SyntaxProduct syntaxProduct) {
        this.syntaxProduct = syntaxProduct;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString(){

        StringBuilder str = new StringBuilder();
        str.append(this.syntaxProduct.getHead().getSymbol());

        str.append(" → ");
        for(int i = 0; i< syntaxProduct.getProduct().size(); i++){
            if(this.index == i){
                str.append(LexConstants.AUGMENT_SYNTAX_INDEX_TAG);
                str.append(" ");
            }

            String bodySymbol = syntaxProduct.getProduct().get(i).getSymbol();
            if(LexConstants.SYNTAX_EMPTY.equals(bodySymbol)){
                str.append(LexConstants.SYNTAX_EMPTY);
            }else {
                str.append(bodySymbol);
            }

            if(i< syntaxProduct.getProduct().size()-1) {
                str.append(" ");
            }
        }

        if(this.index == syntaxProduct.getProduct().size()){
            str.append(LexConstants.AUGMENT_SYNTAX_INDEX_TAG);
        }

        return str.toString();
    }

    @Override
    public boolean equals(Object other){
        if(other == null){
            return false;
        }

        if(this == other){
            return true;
        }

        Item otherItem = (Item) other;
        if(this.index != otherItem.getIndex() || !this.syntaxProduct.equals(otherItem.getSyntaxProduct())){
            return false;
        }

        return true;

    }

    @Override
    public int hashCode(){
        return 0;
    }

}