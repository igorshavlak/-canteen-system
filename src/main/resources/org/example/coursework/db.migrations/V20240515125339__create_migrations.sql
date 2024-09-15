
create table dishes
(
    dish_id     SERIAL PRIMARY KEY,
    name        VARCHAR(255),
    part_image  BYTEA,
    description VARCHAR(255),
    calories    INTEGER,
    category_id INTEGER,
    price       DOUBLE PRECISION,
    FOREIGN KEY (category_id) REFERENCES category (category_id) ON DELETE CASCADE
);
create table menu
(
    menu_id SERIAL PRIMARY KEY,
    name    VARCHAR(255)
);
create table category
(
    category_id SERIAL PRIMARY KEY,
    name        VARCHAR(255)
);
create table orders
(
    order_id    SERIAL PRIMARY KEY,
    date        DATE,
    total_price BIGINT,
    status BOOLEAN
);
create table order_dishes
(
    order_id INTEGER,
    dish_id  INTEGER,
    FOREIGN KEY (order_id) REFERENCES orders (order_id) ON DELETE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES dishes (dish_id) ON DELETE CASCADE
);
create table roles
(
    role_id SERIAL PRIMARY KEY,
    name    VARCHAR(255)
);
create table menu_dishes
(
    menu_id INTEGER,
    dish_id INTEGER,
    FOREIGN KEY (menu_id) REFERENCES menu (menu_id) ON DELETE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES dishes (dish_id) ON DELETE CASCADE
);
create table users
(
    login VARCHAR(255),
    password VARCHAR(255),
    role_id INTEGER,
    FOREIGN KEY (role_id) REFERENCES roles (role_id) ON DELETE CASCADE
);

