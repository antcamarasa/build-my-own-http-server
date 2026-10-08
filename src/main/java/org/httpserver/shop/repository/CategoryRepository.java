package org.httpserver.shop.repository;

import org.httpserver.db.Database;
import org.httpserver.shop.model.Category;
import org.httpserver.shop.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    Database database;

    public CategoryRepository(){
        this.database = Database.getDatabase();
    }

    public List<Category> findAll() throws SQLException {
        List<Category> categoryList = new ArrayList<>();
        // 1. On ouvre une connection.
        try(Connection connection = database.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM category");
            ResultSet rs = ps.executeQuery())
        {
            while (rs.next()){
                // Ici je récuépre ligne par ligne.
                // Une ligne représente j'imagine les champs de la requête.
                Integer id  = rs.getInt("id");
                String name = rs.getString("name");
                categoryList.add(new Category(id, name));
            }

            // Do something with the resultSet...
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return categoryList;
    }
    public List<Product>  findAllByCategory(Integer categoryId){
        List<Product> products = new ArrayList<>();

        try (Connection connection = database.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT p.id, p.name, p.price, c.id AS category_id, c.name AS category_name FROM product p INNER JOIN category c ON p.category_id = c.id WHERE c.id = ?");
        ){
                ps.setInt(1, categoryId);
                try(ResultSet rs = ps.executeQuery()){
                    while (rs.next()){
                        Category category = new Category(
                                rs.getInt("category_id"),
                                rs.getString("category_name")
                        );

                        Product product = new Product(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getBigDecimal("price"),
                                category
                        );

                        products.add(product);
                    }
                }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    // ______________________________________________ VALIDATION _______________________________________________________
    public boolean checkValidCategoryName(Integer categoryId){
        boolean isValidCategoryName  = false;

        try(Connection connection = database.getConnection();
            PreparedStatement ps  = connection.prepareStatement("SELECT EXISTS(SELECT 1 FROM category WHERE id = ?) AS category_exists")
        ){
            ps.setInt(1, categoryId);
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    isValidCategoryName = rs.getBoolean("category_exists");
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return isValidCategoryName;
    }
}
