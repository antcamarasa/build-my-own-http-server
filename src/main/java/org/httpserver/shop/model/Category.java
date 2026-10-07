package org.httpserver.shop.model;

public class Category {
    Integer id;
    String name;

    public Category(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    public Integer getId(){return this.id;}
    public String getName(){
        return this.name;
    }
}
