-- Active: 1751232933894@@127.0.0.1@1433@model
-- Crear la tabla
CREATE TABLE product (
    product_id INT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(60) NOT NULL,
    unit_price INT NOT NULL,
    highest_price INT NOT NULL,
    category VARCHAR(60) NOT NULL,
    brand VARCHAR(60) NOT NULL,
    entry_date DATETIME2 NOT NULL,
    exit_date DATETIME2 NOT NULL,
    period DATETIME2 NOT NULL,
    state CHAR(1) NOT NULL DEFAULT 'A',
    CONSTRAINT UQ_product_name UNIQUE (name),
    CHECK (highest_price > 0 AND highest_price >= unit_price)
);
drop table product;