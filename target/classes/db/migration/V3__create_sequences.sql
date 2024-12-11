-- Tworzenie sekwencji dla kategorii, categories
CREATE SEQUENCE IF NOT EXISTS categories_seq START WITH 1 INCREMENT BY 1;
-- Ustawienie początkowej wartości sekwencji na podstawie maksymalnego ID w tabeli 'categories'
SELECT setval('categories_seq', COALESCE((SELECT MAX(id) FROM categories), 0) + 1, false);

CREATE SEQUENCE IF NOT EXISTS menu_item_seq START WITH 1 INCREMENT BY 1;
SELECT setval('menu_item_seq', COALESCE((SELECT MAX(id) FROM menu_items), 0) + 1, false);

CREATE SEQUENCE IF NOT EXISTS user_seq START WITH 1 INCREMENT BY 1;
SELECT setval('user_seq', COALESCE((SELECT MAX(id) FROM users), 0) + 1, false);

--CREATE SEQUENCE IF NOT EXISTS order_items_seq START WITH 1 INCREMENT BY 1;
--SELECT setval('order_items_seq', COALESCE((SELECT MAX(id) FROM order_items), 0) + 1, false);

