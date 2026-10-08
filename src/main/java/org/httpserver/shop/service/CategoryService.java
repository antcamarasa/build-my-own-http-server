package org.httpserver.shop.service;

import org.httpserver.http.response.MyHttpResponse;
import org.httpserver.shop.model.Category;
import org.httpserver.shop.model.Product;
import org.httpserver.shop.repository.CategoryRepository;

import java.sql.SQLException;
import java.util.List;

public class CategoryService {

    public static List<Category> findOne()throws SQLException{
        CategoryRepository repository = new CategoryRepository();
        List<Category> result = repository.findAll();
        return result;
    }

    public static List<Category> findAll() throws SQLException {
        CategoryRepository repository = new CategoryRepository();
        List<Category> result = repository.findAll();
        return result;
    }

    public static List<Product> findProductsByCategoryId(String categoryId) {
        CategoryRepository repository = new CategoryRepository();

        // 1. Vérifie si les params existes en BDD via repository.
        boolean isValidCategoryId = repository.checkValidCategoryName(Integer.parseInt(categoryId));
        if (!isValidCategoryId) {
            // Requête bien formé, mais la category n'existe pas. 404.
            throw new RuntimeException("********* Invalid category name ***********");
        }

        // 2. Demander les données a la BDD, via CategoryRepository. je stock la réponse.
        return repository.findAllByCategory(Integer.parseInt(categoryId));
    }
}
