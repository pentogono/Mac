package ru.itpark.webstore.repository;

import org.springframework.stereotype.Repository;
import ru.itpark.webstore.domain.Product;
import ru.itpark.webstore.exception.DbException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Repository
public class ProductRepositoryJDBCImpl implements ProductRepository {
    private final DataSource dataSource;

    public ProductRepositoryJDBCImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        // Шаблонный код -> Template
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement();) {
                try (ResultSet resultSet = statement.executeQuery("SELECT id, name, price FROM products");) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        int price = resultSet.getInt("price");
                        String productUrl = resultSet.getString( "productUrl");
                        products.add(new Product(id, name, price, productUrl));
                    }

                    return products;
                }
            }
        } catch (SQLException e) {
            // rethrow exception:
            // выкидываем non-checked exception + заворачивает старое
            throw new DbException(e);
        }
    }

    @Override
    public Optional<Product> getById(int id) {
        // Шаблонный код -> Template
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT id, name, price FROM products WHERE id = ?");) {
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery();) {
                    if (resultSet.next()) {
                        String name = resultSet.getString("name");
                        int price = resultSet.getInt("price");
                        String productUrl = resultSet.getString( "productUrl");
                        return Optional.of(new Product(id, name, price, productUrl));
                    }

                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    @Override
    public void save(Product item) {
        // Шаблонный код -> Template
        try (Connection connection = dataSource.getConnection()) {
            if (item.getId() == 0) {
                try (PreparedStatement statement = connection.prepareStatement("INSERT INTO products (name, price) VALUES (?, ?)");) {
                    statement.setString(1, item.getName());
                    statement.setInt(2, item.getPrice());
                    statement.executeUpdate();
                }
                return;
            }

            try (PreparedStatement statement = connection.prepareStatement("UPDATE products SET name = ?, price = ? WHERE id = ?");) {
                statement.setString(1, item.getName());
                statement.setInt(2, item.getPrice());
                statement.setInt(3, item.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    @Override
    public void removeById(int id) {
        // Шаблонный код -> Template
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM products WHERE id = ?");) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }
}
