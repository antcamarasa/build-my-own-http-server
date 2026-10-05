package org.httpserver.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Ici, je veux une seule instance de Database et une seule instance de Connection.
public class Database {
    static Database database;

    final String url = "jdbc:postgresql://localhost:5432/httpserver";
    final String name = "httpserver";
    final String pass = "httpserver";

    private Database(){}

    public Connection getConnection() {
        try{
            return DriverManager.getConnection(url, name, pass);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage() + e);
        }
    }

    public static Database getDatabase() {
        database = database == null ? new Database() : database;
        return database;
    }
}
