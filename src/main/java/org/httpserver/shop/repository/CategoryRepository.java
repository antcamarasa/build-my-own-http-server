package org.httpserver.shop.repository;

import org.httpserver.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryRepository {
    Database database;

    public CategoryRepository(){
        this.database = Database.getDatabase();
    }

    public void findAll() throws SQLException {
        // 1. On ouvre une connection.
        try(Connection connection = database.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM category");
            ResultSet rs = ps.executeQuery())
        {
            while (rs.next()){
                // Ici je récuépre ligne par ligne.
                // Une ligne représente j'imagine les champs de la requête.
                //
                rs.getInt("id");
            }
            // Do something with the resultSet...
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
