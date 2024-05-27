CREATE TABLE IF NOT EXISTS category (
    id CHAR(23) NOT NULL,
    cat_name VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS product (
    id CHAR(23) NOT NULL,
    prod_name VARCHAR(255) NOT NULL,
    weight FLOAT NOT NULL,
    volume FLOAT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS classification (
    product_id CHAR(23) NOT NULL,
    category_id CHAR(23) NOT NULL,
    PRIMARY KEY (product_id, category_id),
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE IF NOT EXISTS product_supplier (
    cnpj CHAR(18) NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone CHAR(15) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY(cnpj)
);

CREATE TABLE IF NOT EXISTS employee (
    cpf CHAR(14) NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone CHAR(15) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    passwordHash VARCHAR(255) NOT NULL,
    is_manager BOOLEAN NOT NULL,
    manager_cpf VARCHAR(14),
    PRIMARY KEY(cpf),
    FOREIGN KEY(manager_cpf) REFERENCES employee(cpf)
);

CREATE TABLE IF NOT EXISTS item (
    id CHAR(23) NOT NULL,
    product_id CHAR(23) NOT NULL,
    supplier_cnpj CHAR(18) NOT NULL,
    employee_cpf CHAR(14) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (supplier_cnpj) REFERENCES product_supplier(cnpj),
    FOREIGN KEY (employee_cpf) REFERENCES employee(cpf)
);

CREATE TABLE IF NOT EXISTS discard (
    employee_cpf CHAR(14) NOT NULL,
    item_id CHAR(23) NOT NULL,
    discard_reason VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_cpf) REFERENCES employee(cpf),
    FOREIGN KEY (item_id) REFERENCES item(id),
    CONSTRAINT discardOptions CHECK (discard_reason='Extravio' OR discard_reason='Acondicionamento' OR discard_reason='Fabricação/transporte')
);

CREATE TABLE IF NOT EXISTS carrier (
    cnpj CHAR(18) NOT NULL,
    phone CHAR(15) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    max_weight FLOAT NOT NULL,
    max_volume FLOAT NOT NULL,
    PRIMARY KEY(cnpj)
);

CREATE TABLE IF NOT EXISTS client (
    id CHAR(23) NOT NULL, 
    name VARCHAR(255) NOT NULL,
    phone CHAR(15) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    cpf CHAR(14) UNIQUE,
    cnpj CHAR(18) UNIQUE,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS delivery_address (
    id CHAR(23) NOT NULL,
    recipient_name VARCHAR(255) NOT NULL,
    state CHAR(2) NOT NULL,
    city VARCHAR(100) NOT NULL,
    zip CHAR(9) NOT NULL,
    street VARCHAR(255) NOT NULL,
    number VARCHAR(50) NOT NULL,
    details VARCHAR(100) NOT NULL,
    client_id CHAR(23) NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(client_id) REFERENCES client(id)
);

CREATE TABLE IF NOT EXISTS orders (
    id CHAR(23) NOT NULL,
    client_id CHAR(23) NOT NULL,
    employee_cpf CHAR(14) NOT NULL,
    delivery_address_id CHAR(23) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(id),
    FOREIGN KEY(client_id) REFERENCES client(id),
    FOREIGN KEY(employee_cpf) REFERENCES employee(cpf),
    FOREIGN KEY(delivery_address_id) REFERENCES delivery_address(id)
);

CREATE TABLE IF NOT EXISTS package (
    id CHAR(23) NOT NULL,
    tracking_code VARCHAR(255) NOT NULL,
    delivery_notes VARCHAR(255),
    order_id CHAR(23) NOT NULL,
    carrier_cnpj CHAR(18) NOT NULL,
    fragile BOOLEAN,
    PRIMARY KEY(id),
    FOREIGN KEY(order_id) REFERENCES orders(id),
    FOREIGN KEY(carrier_cnpj) REFERENCES carrier(cnpj)
);

CREATE TABLE IF NOT EXISTS packaged_item (
    item_id CHAR(23) NOT NULL,
    package_id CHAR(23) NOT NULL,
    PRIMARY KEY(item_id, package_id),
    FOREIGN KEY(item_id) REFERENCES item(id),
    FOREIGN KEY(package_id) REFERENCES package(id)
);

CREATE TABLE IF NOT EXISTS ordered_item (
    item_id CHAR(23) NOT NULL,
    order_id CHAR(23) NOT NULL,
    PRIMARY KEY(item_id, order_id),
    FOREIGN KEY(item_id) REFERENCES item(id),
    FOREIGN KEY(order_id) REFERENCES orders(id)
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

DELIMITER //

CREATE TRIGGER before_package_insert
BEFORE INSERT ON package
FOR EACH ROW
BEGIN
    DECLARE item_count INT;
    SELECT COUNT(*) INTO item_count
    FROM packaged_item pi
    JOIN item i ON pi.item_id = i.id
    JOIN product p ON p.id = i.product_id
    WHERE pi.package_id = NEW.id AND p.fragile = TRUE;

    IF item_count > 0 AND NEW.fragile = FALSE THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'A package containing fragile items must be marked as fragile.';
    END IF;
END //

DELIMITER ;