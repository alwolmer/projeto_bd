CREATE TABLE IF NOT EXISTS category (
    id CHAR(23) NOT NULL,
    cat_name VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS product (
    id CHAR(23) NOT NULL,
    prod_name VARCHAR(255) NOT NULL,
    expiry DATE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS supplier (
    cnpj CHAR(18) NOT NULL,
    name VARCHAR(255) NOT NULL,
    state CHAR(2) NOT NULL,
    city VARCHAR(100) NOT NULL,
    zip CHAR(9) NOT NULL,
    street VARCHAR(255) NOT NULL,
    num VARCHAR(50) NOT NULL,
    comp VARCHAR(100) NOT NULL,
    phone CHAR(15) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY(cnpj)
);

CREATE TABLE IF NOT EXISTS employee (
    cpf CHAR(14) NOT NULL,
    name VARCHAR(255) NOT NULL,
    state CHAR(2) NOT NULL,
    city VARCHAR(100) NOT NULL,
    zip CHAR(9) NOT NULL,
    street VARCHAR(255) NOT NULL,
    num VARCHAR(50) NOT NULL,
    comp VARCHAR(100) NOT NULL,
    phone CHAR(15) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    passwordHash VARCHAR(255) NOT NULL,
    manager_cpf VARCHAR(14),
    PRIMARY KEY(cpf),
    FOREIGN KEY(manager_cpf) REFERENCES employee(cpf)
);

CREATE TABLE IF NOT EXISTS refresh_token (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    employee_cpf CHAR(14) NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date DATETIME(6) NOT NULL,
    CONSTRAINT fk_employee
        FOREIGN KEY (employee_cpf) REFERENCES employee(cpf)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);