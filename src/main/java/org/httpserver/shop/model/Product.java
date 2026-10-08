package org.httpserver.shop.model;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Map;

public class Product {
    Integer id;
    public String name;
    public BigDecimal price;
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


    public String toJsonObject(Product product) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        sb.append('{');

        var reflection = this.getClass();
        var fiels =  reflection.getFields();

        for(Field field : reflection.getFields()){
            String fieldName = field.getName();
            var fieldValue   = field.get(product);

            boolean valueIsString = fieldValue instanceof String;


            sb.append('"')
                    .append(fieldName)
                    .append('"')
                    .append(":");

            if(valueIsString){
                sb.append('"').append(fieldValue).append('"');
            } else{
                sb.append(fieldValue);
            }

            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append('}');
        return sb.toString();
    }

}
