package org.httpserver.shop.model;

import java.math.BigDecimal;

public class Product {
    Integer id;
    String name;
    BigDecimal price;
    Category category;

    public Product(Integer id, String name, BigDecimal price, Category category){
        this.id       = id;
        this.name     = name;
        this.price    = price;
        this.category = category;
    }

    // _______________________________________________ GETTER & SETTER _________________________________________________
    public Integer getId(){return this.id;}
    public String getName(){
        return this.name;
    }
    public BigDecimal getPrice(){
        return this.price;
    }
    public Category getCategory(){
        return this.category;
    }

}
