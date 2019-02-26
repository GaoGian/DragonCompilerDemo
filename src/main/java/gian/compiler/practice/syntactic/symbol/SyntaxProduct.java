package gian.compiler.practice.syntactic.symbol;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojian on 2019/2/26.
 */
public class SyntaxProduct {

    protected SyntaxSymbol head;
    protected List<SyntaxSymbol> product = new ArrayList<>();

    public SyntaxProduct(){}

    public SyntaxProduct(SyntaxSymbol symbol, List<SyntaxSymbol> product) {
        this.head = symbol;
        this.product = product;
    }

    public SyntaxSymbol getHead() {
        return head;
    }

    public void setHead(SyntaxSymbol head) {
        this.head = head;
    }

    public List<SyntaxSymbol> getProduct() {
        return product;
    }

    public void setProduct(List<SyntaxSymbol> product) {
        this.product = product;
    }

    @Override
    public String toString(){
        return this.head.getSymbol() + "→" + this.product.toString();
    }

    @Override
    public boolean equals(Object other){
        if(other == null){
            return false;
        }

        if(other == this){
            return true;
        }

        SyntaxProduct otherProduct = (SyntaxProduct) other;
        if(!this.head.equals(otherProduct.getHead())){
            return false;
        }
        if(!this.product.equals(otherProduct.getProduct())){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode(){
        return this.toString().hashCode();
    }

}