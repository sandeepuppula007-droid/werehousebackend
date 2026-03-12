-- ================================
-- WAREHOUSE MANAGEMENT DATABASE
-- PostgreSQL SQL Schema + Inserts
-- ================================

-- Create users table
-- CREATE TABLE IF NOT EXISTS users (
-- id BIGSERIAL PRIMARY KEY,
-- username VARCHAR(255) UNIQUE NOT NULL,
-- password VARCHAR(255) NOT NULL,
-- role VARCHAR(50) NOT NULL);

-- Insert sample users (password = 'password123' encrypted with BCrypt)
-- INSERT INTO users (username, password, role) VALUES
-- ('admin', '123456789','ADMIN'),
-- ('nagaraju', '123456789','USER');

-- ================================
-- DROP TABLES (Only if needed)
-- ================================
-- DROP TABLE IF EXISTS stock_out;
-- DROP TABLE IF EXISTS stock_in;
-- DROP TABLE IF EXISTS product;

-- ================================
-- CREATE TABLES
-- ================================

-- CREATE TABLE product (
-- product_id SERIAL PRIMARY KEY,
-- name VARCHAR(255));

-- CREATE TABLE stock_in (
-- id SERIAL PRIMARY KEY,
-- quantity INTEGER,
-- received_from VARCHAR(255),
-- in_date TIMESTAMP,
-- product_id BIGINT,
-- CONSTRAINT fk_stockin_product
-- FOREIGN KEY (product_id) REFERENCES product(product_id));

-- CREATE TABLE stock_out (
-- id SERIAL PRIMARY KEY,
-- quantity INTEGER,
-- given_to VARCHAR(255),
-- out_date TIMESTAMP,
-- product_id BIGINT,
-- CONSTRAINT fk_stockout_product
-- FOREIGN KEY (product_id) REFERENCES product(product_id));