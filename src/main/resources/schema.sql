-- Этот файл будет читаться Spring Boot и автоматически выполняться
CREATE TABLE products (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  price INTEGER NOT NULL CHECK (price > 0),
  productUrl VARCHAR(255) NOT NULL
  );