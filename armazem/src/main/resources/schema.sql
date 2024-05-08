CREATE DATABASE IF NOT EXISTS storage;
USE storage;

CREATE TABLE IF NOT EXISTS category (
    id VARCHAR(36) NOT NULL,
    cat_name VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS product (
    id VARCHAR(36) NOT NULL,
    prod_name VARCHAR(255) NOT NULL,
    expiry DATE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS supplier (
    cnpj VARCHAR(18) NOT NULL,
    sup_name VARCHAR(255) NOT NULL,
    sup_state VARCHAR(2) NOT NULL,
    sup_city VARCHAR(100) NOT NULL,
    sup_zip VARCHAR(9) NOT NULL,
    sup_street VARCHAR(255) NOT NULL,
    sup_num VARCHAR(50) NOT NULL,
    sup_comp VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY(cnpj)
);

CREATE TABLE IF NOT EXISTS employee (
    cpf VARCHAR(14) NOT NULL,
    emp_name VARCHAR(255) NOT NULL,
    emp_state VARCHAR(2) NOT NULL,
    emp_city VARCHAR(100) NOT NULL,
    emp_zip VARCHAR(9) NOT NULL,
    emp_street VARCHAR(255) NOT NULL,
    emp_num VARCHAR(50) NOT NULL,
    emp_comp VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    passwordHash VARCHAR(255) NOT NULL,
    manager_cpf VARCHAR(14),
    PRIMARY KEY(cpf),
    FOREIGN KEY(manager_cpf) REFERENCES employee(cpf)
);

CREATE TABLE IF NOT EXISTS refresh_token (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    employee_cpf VARCHAR(14) NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date DATETIME(6) NOT NULL,
    CONSTRAINT fk_employee
        FOREIGN KEY (employee_cpf) REFERENCES employee(cpf)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
