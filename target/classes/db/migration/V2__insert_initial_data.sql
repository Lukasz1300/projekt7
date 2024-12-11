-- Dodawanie podstawowych ról
INSERT INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');

-- Wstawienie przykładowych użytkowników
INSERT INTO users (username, password, email, first_name, last_name, phone_number, address_city, address_street)
VALUES
('john_doe', 'password123', 'john.doe@example.com', 'John', 'Doe', '1234567890', 'Warszawa', 'ul. Piękna 12'),
('testuser', 'password', 'testuser@example.com', 'test', 'user', '1287654321', 'Kraków', 'ul. Lechitów 45');

-- Wstawienie właścicieli restauracji
INSERT INTO restaurant_owners (first_name, last_name, email, phone_number)
VALUES
('Jan', 'Kowalski', 'jan.kowalski@example.com', '48123456789'),
('Piotr', 'Nowak', 'piotr.nowak@example.com', '48124353739');

-- Wstawienie restauracji
INSERT INTO restaurants (owner_id, name, address_city, address_street, phone_number, email, description)
VALUES
((SELECT id FROM restaurant_owners WHERE first_name = 'Jan' AND last_name = 'Kowalski'), 'Pizzeria Bella', 'Warszawa', 'Kwiatowa 10', '2212345600', 'kontakt@pizzeriabella.pl', 'Pyszna pizza na cienkim cieście.'),
((SELECT id FROM restaurant_owners WHERE first_name = 'Piotr' AND last_name = 'Nowak'), 'Sushi Mistrz', 'Kraków', 'Leśna 5', '1234567800', 'info@sushimistrz.pl', 'Autentyczne sushi przygotowywane przez mistrza kuchni.');

-- Wstawienie obszarów dostawy
INSERT INTO delivery_areas (restaurant_id, city_name, street_name)
VALUES
    (1, 'Warszawa', 'ul. Piękna'),
    (1, 'Warszawa', 'ul. Marszałkowska'),
    (1, 'Warszawa', 'ul. Leśna'),

    (2, 'Kraków', 'Wawelska'),
    (2, 'Kraków', 'Floriańska'),
    (2, 'Kraków', 'Leśna');

-- Wstawienie kategorii
INSERT INTO categories (name)
VALUES
('Dania główne'),
('Desery'),
('Napoje');

-- Wstawienie pozycji menu dla restauracji Pizzeria Bella
INSERT INTO menu_items (restaurant_id, name, price, description)
VALUES
((SELECT id FROM restaurants WHERE name = 'Pizzeria Bella'), 'Pizza Margherita', 25.00, 'Klasyczna pizza z sosem pomidorowym i mozzarellą'),
((SELECT id FROM restaurants WHERE name = 'Pizzeria Bella'), 'Pizza Pepperoni', 30.00, 'Pizza z pepperoni, mozzarellą i sosem pomidorowym'),
((SELECT id FROM restaurants WHERE name = 'Pizzeria Bella'), 'Pizza Hawaiian', 28.00, 'Pizza z ananasem, szynką i mozzarellą'),
((SELECT id FROM restaurants WHERE name = 'Pizzeria Bella'), 'Herbata', 10.00, 'Czarna herbata'),
((SELECT id FROM restaurants WHERE name = 'Pizzeria Bella'), 'Kawa', 12.00, 'Świeżo mielona kawa'),
((SELECT id FROM restaurants WHERE name = 'Pizzeria Bella'), 'Piwo', 15.00, 'Piwo regionalne'),
((SELECT id FROM restaurants WHERE name = 'Pizzeria Bella'), 'Sok', 8.00, 'Sok pomarańczowy'),
((SELECT id FROM restaurants WHERE name = 'Pizzeria Bella'), 'Tiramisu', 18.00, 'Deser włoski z mascarpone'),
((SELECT id FROM restaurants WHERE name = 'Pizzeria Bella'), 'Panna Cotta', 16.00, 'Delikatny deser śmietanowy'),
((SELECT id FROM restaurants WHERE name = 'Pizzeria Bella'), 'Lody', 12.00, 'Lody waniliowe');

-- Wstawienie pozycji menu dla restauracji Sushi Mistrz
INSERT INTO menu_items (restaurant_id, name, price, description)
VALUES
--Dania główne
((SELECT id FROM restaurants WHERE name = 'Sushi Mistrz'), 'Sushi Nigiri', 35.00, 'Nigiri z łososiem'),
((SELECT id FROM restaurants WHERE name = 'Sushi Mistrz'), 'Sushi Maki', 40.00, 'Sushi Maki z tuńczykiem i ogórkiem'),
((SELECT id FROM restaurants WHERE name = 'Sushi Mistrz'), 'Sushi Sashimi', 50.00, 'Sashimi z łososia i tuńczyka'),
--Napoje
((SELECT id FROM restaurants WHERE name = 'Sushi Mistrz'), 'Herbata Zielona', 8.00, 'Zielona herbata'),
((SELECT id FROM restaurants WHERE name = 'Sushi Mistrz'), 'Piwo Asahi', 18.00, 'Japońskie piwo Asahi'),
((SELECT id FROM restaurants WHERE name = 'Sushi Mistrz'), 'Sake', 30.00, 'Japońska wódka ryżowa'),
((SELECT id FROM restaurants WHERE name = 'Sushi Mistrz'), 'Woda Mineralna', 6.00, 'Woda mineralna'),
--Desery
((SELECT id FROM restaurants WHERE name = 'Sushi Mistrz'), 'Matcha Lody', 16.00, 'Lody smakowe matcha'),
((SELECT id FROM restaurants WHERE name = 'Sushi Mistrz'), 'Deser Azjatycki', 18.00, 'Deser z owocami i mlekiem kokosowym');

-- Powiązanie pozycji menu z kategoriami dla Pizzerii Bella
INSERT INTO menu_item_categories (menu_item_id, category_id)
VALUES
((SELECT id FROM menu_items WHERE name = 'Pizza Margherita'), (SELECT id FROM categories WHERE name = 'Dania główne')),
((SELECT id FROM menu_items WHERE name = 'Pizza Pepperoni'), (SELECT id FROM categories WHERE name = 'Dania główne')),
((SELECT id FROM menu_items WHERE name = 'Pizza Hawaiian'), (SELECT id FROM categories WHERE name = 'Dania główne')),
((SELECT id FROM menu_items WHERE name = 'Herbata'), (SELECT id FROM categories WHERE name = 'Napoje')),
((SELECT id FROM menu_items WHERE name = 'Kawa'), (SELECT id FROM categories WHERE name = 'Napoje')),
((SELECT id FROM menu_items WHERE name = 'Piwo'), (SELECT id FROM categories WHERE name = 'Napoje')),
((SELECT id FROM menu_items WHERE name = 'Sok'), (SELECT id FROM categories WHERE name = 'Napoje')),
((SELECT id FROM menu_items WHERE name = 'Tiramisu'), (SELECT id FROM categories WHERE name = 'Desery')),
((SELECT id FROM menu_items WHERE name = 'Panna Cotta'), (SELECT id FROM categories WHERE name = 'Desery')),
((SELECT id FROM menu_items WHERE name = 'Lody'), (SELECT id FROM categories WHERE name = 'Desery'));

-- Powiązanie pozycji menu z kategoriami dla Sushi Mistrz
INSERT INTO menu_item_categories (menu_item_id, category_id)
VALUES
((SELECT id FROM menu_items WHERE name = 'Sushi Nigiri'), (SELECT id FROM categories WHERE name = 'Dania główne')),
((SELECT id FROM menu_items WHERE name = 'Sushi Maki'), (SELECT id FROM categories WHERE name = 'Dania główne')),
((SELECT id FROM menu_items WHERE name = 'Sushi Sashimi'), (SELECT id FROM categories WHERE name = 'Dania główne')),
((SELECT id FROM menu_items WHERE name = 'Herbata Zielona'), (SELECT id FROM categories WHERE name = 'Napoje')),
((SELECT id FROM menu_items WHERE name = 'Piwo Asahi'), (SELECT id FROM categories WHERE name = 'Napoje')),
((SELECT id FROM menu_items WHERE name = 'Sake'), (SELECT id FROM categories WHERE name = 'Napoje')),
((SELECT id FROM menu_items WHERE name = 'Woda Mineralna'), (SELECT id FROM categories WHERE name = 'Napoje')),
((SELECT id FROM menu_items WHERE name = 'Matcha Lody'), (SELECT id FROM categories WHERE name = 'Desery')),
((SELECT id FROM menu_items WHERE name = 'Deser Azjatycki'), (SELECT id FROM categories WHERE name = 'Desery'));

-- zamowienie order
--INSERT INTO orders (user_id, restaurant_id, order_status, order_date, address_city, address_street, total_price)
--VALUES
--(1, 2, 'pending', '2024-11-25 14:30:00', 'Warszawa', 'Piękna 12', '59.99');

