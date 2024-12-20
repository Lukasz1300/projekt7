-- Tabela ról
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Tabela użytkowników
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE CHECK (email ~* '^.+@.+\..+$'), -- Sprawdza poprawność adresu email
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    phone_number VARCHAR(15) CHECK (phone_number ~ '^\+?[0-9]{10,15}$'), -- Sprawdza numer telefonu
    address_city VARCHAR(50),
    address_street VARCHAR(50)
);

-- Tabela właścicieli restauracji
CREATE TABLE restaurant_owners (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100) NOT NULL UNIQUE CHECK (email ~* '^.+@.+\..+$'),
    phone_number VARCHAR(15) CHECK (phone_number ~ '^\+?[0-9]{10,15}$')
);

-- Tabela restauracji
CREATE TABLE restaurants (
    id SERIAL PRIMARY KEY,
    owner_id INT NOT NULL,  -- Odniesienie do właściciela restauracji
    name VARCHAR(100) NOT NULL,
    address_city VARCHAR(50),
    address_street VARCHAR(50),
    phone_number VARCHAR(15) CHECK (phone_number ~ '^\+?[0-9]{10,15}$'), -- Sprawdza numer telefonu
    email VARCHAR(100) CHECK (email ~* '^.+@.+\..+$'), -- Sprawdza poprawność adresu email
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_owner_restaurant FOREIGN KEY(owner_id) REFERENCES restaurant_owners(id) ON DELETE CASCADE
);

-- Tabela kategorii menu
CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Tabela menu_items
CREATE TABLE menu_items (
    id SERIAL PRIMARY KEY,
    restaurant_id INT NOT NULL,  -- Odniesienie do restauracji
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,  -- Cena z dokładnością do dwóch miejsc po przecinku
    image_url VARCHAR(255),
    CONSTRAINT fk_restaurant_menu_item FOREIGN KEY(restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE  -- Klucz obcy do restauracji
);

-- Tabela łącząca pozycje menu z kategoriami
CREATE TABLE menu_item_categories (
    menu_item_id INT NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY (menu_item_id, category_id),
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

-- Tabela zamówień
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    restaurant_id INT NOT NULL,
    order_status VARCHAR(50) DEFAULT 'PENDING',
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    address_city VARCHAR(50),
    address_street VARCHAR(50),
    CONSTRAINT fk_user_order FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_restaurant_order FOREIGN KEY(restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
);

-- Tabela pozycji zamówienia
CREATE TABLE order_items (
   id SERIAL PRIMARY KEY,
   order_id INT NOT NULL,
   menu_item_id INT NOT NULL,
   quantity INT NOT NULL DEFAULT 1,
   price DECIMAL(10, 2) NOT NULL,
   CONSTRAINT fk_order_order_item FOREIGN KEY(order_id) REFERENCES orders(id) ON DELETE CASCADE,
   CONSTRAINT fk_menu_item_order_item FOREIGN KEY(menu_item_id) REFERENCES menu_items(id) ON DELETE CASCADE
);

-- Tabela powiązań użytkowników z rolami
CREATE TABLE user_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_role FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_role_user_role FOREIGN KEY(role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Tabela adres dostawy
CREATE TABLE delivery_areas (
    id SERIAL PRIMARY KEY,
    restaurant_id INT NOT NULL,
    city_name VARCHAR(255) NOT NULL,
    street_name VARCHAR(255) NOT NULL,
    CONSTRAINT fk_restaurant FOREIGN KEY(restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
);
