//package com.practice.test.Repositories;
//
//import com.practice.test.Entities.Product.Product;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public class ProductRepository {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public ProductRepository(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    public void createProductsTable() {
//        jdbcTemplate.execute("""
//            CREATE TABLE Products(
//                id INT AUTO_INCREMENT PRIMARY KEY,
//                name VARCHAR(100) NOT NULL,
//                price DECIMAL NOT NULL,
//                stock INT NOT NULL
//            )
//        """);
//    }
//
//    public void createProduct(String name, double price, int stock) {
//        String sqlQuery = "INSERT INTO Products(name, price, stock) VALUES(?, ?, ?)";
//
//        jdbcTemplate.update(sqlQuery, name, price, stock);
//    }
//
//    public List<Product> getAllProducts() {
//        return jdbcTemplate.query("SELECT * FROM Products", (rs, rowNum) -> new Product(
//                rs.getInt("id"),
//                rs.getString("name"),
//                rs.getDouble("price"),
//                rs.getInt("stock")
//        ));
//    }
//}