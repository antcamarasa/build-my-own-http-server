package org.httpserver.shop.model;

public class Category {
    Integer id;
    String name;

    public Category(String name){
        this.name = name;
    }

    public Integer getId(){return this.id;}
    public String getName(){
        return this.name;
    }
}
