package gian.compiler.practice.syntactic.lrsyntax;

import gian.compiler.practice.lexical.transform.LexConstants;
import gian.compiler.practice.syntactic.symbol.SyntaxProduct;

import java.util.HashSet;
import java.util.Set;

/**
 * LR 状态项
 * Created by gaojian on 2019/3/1.
 */
public class Item {

    protected SyntaxProduct syntaxProduct;
    protected int index;

    protected Set<String> lookForwardSymbolSet = new HashSet<>();

    public Item(){}

    public Item(SyntaxProduct syntaxProduct, int index) {
        this.syntaxProduct = syntaxProduct;
        this.index = index;
    }

    public Item(SyntaxProduct syntaxProduct, int index, String lookForwardSymbol) {
        this.syntaxProduct = syntaxProduct;
        this.index = index;
        this.lookForwardSymbolSet.add(lookForwardSymbol);
    }

    public Item(SyntaxProduct syntaxProduct, int index, Set<String> lookForwardSymbolSet) {
        this.syntaxProduct = syntaxProduct;
        this.index = index;
        this.lookForwardSymbolSet = lookForwardSymbolSet;
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

    public Set<String> getLookForwardSymbolSet() {
        return lookForwardSymbolSet;
    }

    public void setLookForwardSymbolSet(Set<String> lookForwardSymbolSet) {
        this.lookForwardSymbolSet = lookForwardSymbolSet;
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

        if(this.lookForwardSymbolSet.size() > 0){
            str.append(", ");
            str.append(this.lookForwardSymbolSet.toString());
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

        if(!this.lookForwardSymbolSet.equals(otherItem.getLookForwardSymbolSet())){
            return false;
        }

        return true;

    }

    @Override
    public int hashCode(){
        return 0;
    }

}