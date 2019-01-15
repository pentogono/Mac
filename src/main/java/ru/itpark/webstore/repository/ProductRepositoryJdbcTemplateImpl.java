package ru.itpark.webstore.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itpark.webstore.domain.Product;

import java.util.List;
import java.util.Optional;

//@Repository
public class ProductRepositoryJdbcTemplateImpl implements ProductRepository {
    private final JdbcTemplate template;

    // DI
    public ProductRepositoryJdbcTemplateImpl(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<Product> getAll() {
        return template.query(
                "SELECT id, name, price, productUrl FROM products",
                (rs, i) -> new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getString("productUrl")
                )
        );
    }

    @Override
    public Optional<Product> getById(int id) {
        return template.query(
                "SELECT id, name, price, productUrl FROM products WHERE id = ? LIMIT 1",
                (rs, i) -> new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getString("productUrl")
                ),
                id
        ).stream().findFirst();
    }

    @Override
    public void save(Product item) {
        if (item.getId() == 0) {
            // INSERT
            template.update(
                    "INSERT INTO products(name, price, productUrl) VALUES (?, ?, ?)",
                    item.getName(), item.getPrice(), item.getProductUrl()
            );
            return;
        }

        // UPDATE
        template.update(
                "UPDATE products SET name = ?, price = ?, productUrl = & WHERE id = ?",
                item.getName(), item.getPrice(), item.getProductUrl(), item.getId()
        );
    }

    @Override
    public void removeById(int id) {
        template.update(
                "DELETE FROM products WHERE id = ?",
                id
        );
    }
}
