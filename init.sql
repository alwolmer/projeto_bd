CREATE TABLE IF NOT EXISTS category (
    id CHAR(23) NOT NULL,
    cat_name VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS product (
    id CHAR(23) NOT NULL,
    prod_name VARCHAR(255) NOT NULL,
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
    PRIMARY KEY (item_id),
    FOREIGN KEY (employee_cpf) REFERENCES employee(cpf),
    FOREIGN KEY (item_id) REFERENCES item(id),
    CONSTRAINT discardOptions CHECK (discard_reason='Loss' OR discard_reason='Bad Conditioning' OR discard_reason='Fabrication/Transport')
);

CREATE TABLE IF NOT EXISTS carrier (
    cnpj CHAR(18) NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    phone CHAR(15) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY(cnpj)
);

CREATE TABLE IF NOT EXISTS client (
    id CHAR(23) NOT NULL, 
    name VARCHAR(255) NOT NULL,
    phone CHAR(15) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    cpf CHAR(14), -- uniqueness guaranteed by custom trigger on create/update
    cnpj CHAR(18), -- uniqueness guaranteed by custom trigger on create/update
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
    carrier_cnpj CHAR(18) NOT NULL,
    tracking_code VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(id),
    FOREIGN KEY(client_id) REFERENCES client(id),
    FOREIGN KEY(employee_cpf) REFERENCES employee(cpf),
    FOREIGN KEY(delivery_address_id) REFERENCES delivery_address(id),
    FOREIGN KEY(carrier_cnpj) REFERENCES carrier(cnpj)
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

-- triggers

DELIMITER //

-- cpf xor cnpj for new client

CREATE TRIGGER before_client_insert
BEFORE INSERT ON client
FOR EACH ROW
BEGIN
    DECLARE cpf_count INT;
    DECLARE cnpj_count INT;

    IF (NEW.cpf IS NOT NULL AND NEW.cnpj IS NOT NULL) OR (NEW.cpf IS NULL AND NEW.cnpj IS NULL) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'A client must have either CPF or CNPJ, but not both';
    END IF;

    IF NEW.cpf IS NOT NULL THEN
        SELECT COUNT(*) INTO cpf_count FROM client WHERE cpf = NEW.cpf;
        IF cpf_count > 0 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Duplicate CPF not allowed';
        END IF;
    END IF;

    IF NEW.cnpj IS NOT NULL THEN
        SELECT COUNT(*) INTO cnpj_count FROM client WHERE cnpj = NEW.cnpj;
        IF cnpj_count > 0 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Duplicate CNPJ not allowed';
        END IF;
    END IF;
END //

-- cpf xor cnpj for updated client

CREATE TRIGGER before_client_update
BEFORE UPDATE ON client
FOR EACH ROW
BEGIN
    DECLARE cpf_count INT;
    DECLARE cnpj_count INT;

    IF (NEW.cpf IS NOT NULL AND NEW.cnpj IS NOT NULL) OR (NEW.cpf IS NULL AND NEW.cnpj IS NULL) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'A client must have either CPF or CNPJ, but not both';
    END IF;

    IF NEW.cpf IS NOT NULL THEN
        SELECT COUNT(*) INTO cpf_count FROM client WHERE cpf = NEW.cpf AND id != OLD.id;
        IF cpf_count > 0 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Duplicate CPF not allowed';
        END IF;
    END IF;

    IF NEW.cnpj IS NOT NULL THEN
        SELECT COUNT(*) INTO cnpj_count FROM client WHERE cnpj = NEW.cnpj AND id != OLD.id;
        IF cnpj_count > 0 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Duplicate CNPJ not allowed';
        END IF;
    END IF;
END //

-- don't allow category deletion if it would leave products without any category

CREATE TRIGGER before_category_delete
BEFORE DELETE ON category
FOR EACH ROW
BEGIN
DECLARE orphan_count INT;

    -- Count the number of products exclusively associated with the category being deleted
    SELECT COUNT(*) INTO orphan_count
    FROM product p
    WHERE NOT EXISTS (
        SELECT 1
        FROM classification c
        WHERE c.product_id = p.id
        AND c.category_id != OLD.id
    );

    -- If there are any products exclusively associated with the category being deleted, prevent deletion
    IF orphan_count > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cannot delete category because it would leave one or more products without any categories';
    END IF;
END //

CREATE TRIGGER before_product_delete
BEFORE DELETE ON product
FOR EACH ROW
BEGIN
    DECLARE has_orphaned_items BOOLEAN DEFAULT FALSE;

    -- Check if there are any items that would be left without a product
    SELECT EXISTS (
        SELECT 1
        FROM item
        WHERE product_id = OLD.id
    ) INTO has_orphaned_items;

    -- If there are orphaned items, prevent deletion
    IF has_orphaned_items THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cannot delete product because it would leave one or more items without a product';
    ELSE
        -- If no orphaned items, proceed with deletion
        DELETE FROM classification WHERE product_id = OLD.id;  -- Example deletion from classification table
    END IF;
END //

CREATE TRIGGER before_supplier_delete
BEFORE DELETE ON product_supplier
FOR EACH ROW
BEGIN
    DECLARE has_orphaned_items BOOLEAN DEFAULT FALSE;

    -- Check if there are any items that would be left without a supplier
    SELECT EXISTS (
        SELECT 1
        FROM item
        WHERE supplier_cnpj = OLD.cnpj
    ) INTO has_orphaned_items;

    -- If there are orphaned items, prevent deletion
    IF has_orphaned_items THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cannot delete supplier because it would leave one or more items without a supplier';
    END IF;
END //

CREATE TRIGGER before_employee_delete
BEFORE DELETE ON employee
FOR EACH ROW
BEGIN
    DECLARE has_orphans BOOLEAN DEFAULT FALSE;

    -- Check if there are any related rows (items, discards, or orders) associated with the employee being deleted
    SELECT EXISTS (
        SELECT 1
        FROM item
        WHERE employee_cpf = OLD.cpf
        UNION ALL
        SELECT 1
        FROM discard
        WHERE employee_cpf = OLD.cpf
        UNION ALL
        SELECT 1
        FROM orders
        WHERE employee_cpf = OLD.cpf
    ) INTO has_orphans;

    -- If there are orphaned items, discards, or orders, prevent deletion
    IF has_orphans THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cannot delete employee because it would leave related items, discards, or orders orphaned';
    END IF;
END //

CREATE TRIGGER before_item_delete
BEFORE DELETE ON item
FOR EACH ROW
BEGIN
    DECLARE has_orphans BOOLEAN DEFAULT FALSE;

    -- Check if there are any related rows (items, discards, or orders) associated with the employee being deleted
    SELECT EXISTS (
        SELECT 1
        FROM discard
        WHERE item_id = OLD.id
        UNION ALL
        SELECT 1
        FROM ordered_item
        WHERE item_id = OLD.id
    ) INTO has_orphans;

    -- If there are orphaned items, discards, or orders, prevent deletion
    IF has_orphans THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cannot delete item because it would leave related discards or ordered items orphaned';
    END IF;
END //

CREATE TRIGGER before_client_delete
BEFORE DELETE ON client
FOR EACH ROW
BEGIN
    DECLARE has_orphaned_orders BOOLEAN DEFAULT FALSE;

    -- Check if there are any orders that would be left without a client
    SELECT EXISTS (
        SELECT 1
        FROM orders
        WHERE client_id = OLD.id
    ) INTO has_orphaned_orders;

    -- If there are orphaned orders, prevent deletion
    IF has_orphaned_orders THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cannot delete client because it would leave one or more orders without a client';
    ELSE
        -- If no orphaned orders, proceed with deletion
        DELETE FROM delivery_address WHERE client_id = OLD.id;
    END IF;
END //

CREATE TRIGGER before_address_delete
BEFORE DELETE ON delivery_address
FOR EACH ROW
BEGIN
    DECLARE has_orphaned_orders BOOLEAN DEFAULT FALSE;

    -- Check if there are any orders that would be left without a client
    SELECT EXISTS (
        SELECT 1
        FROM orders
        WHERE delivery_address_id = OLD.id
    ) INTO has_orphaned_orders;

    -- If there are orphaned orders, prevent deletion
    IF has_orphaned_orders THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cannot delete delivery address because it would leave one or more orders without a delivery address';
    END IF;
END //

CREATE TRIGGER before_carrier_delete
BEFORE DELETE ON carrier
FOR EACH ROW
BEGIN
    DECLARE has_orphaned_orders BOOLEAN DEFAULT FALSE;

    -- Check if there are any orders that would be left without a client
    SELECT EXISTS (
        SELECT 1
        FROM orders
        WHERE carrier_cnpj = OLD.cnpj
    ) INTO has_orphaned_orders;

    -- If there are orphaned orders, prevent deletion
    IF has_orphaned_orders THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cannot delete carrier because it would leave one or more orders without a carrier';
    END IF;
END //


-- triggers to make sure an item to be discarded has not been previously shipped (for creation and update of entries on discard)

CREATE TRIGGER before_discard_insert
BEFORE INSERT ON discard
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1
        FROM ordered_item oi
        WHERE oi.item_id = NEW.item_id
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Item has already been shipped and cannot be discarded.';
    END IF;
END //

CREATE TRIGGER before_discard_update
BEFORE UPDATE ON discard
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1
        FROM ordered_item oi
        WHERE oi.item_id = NEW.item_id
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Item has already been shipped and cannot be discarded.';
    END IF;
END //

-- triggers to make sure an item to be shipped has not been previously discarded (for creation and update of entries on ordered_items)

CREATE TRIGGER before_ordered_item_insert
BEFORE INSERT ON ordered_item
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1
        FROM discard d
        WHERE d.item_id = NEW.item_id
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Item has already been discarded and cannot be shipped.';
    END IF;
END //

CREATE TRIGGER before_ordered_item_update
BEFORE UPDATE ON ordered_item
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1
        FROM discard d
        WHERE d.item_id = NEW.item_id
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Item has already been discarded and cannot be shipped.';
    END IF;
END //

-- PROCEDURES


-- procedure to get all items ever registered, able to filter by date, product, supplier, employee or category
-- all filters optional 
-- CALL GetStockHistory(NULL, NULL, NULL, NULL, NULL, NULL);

CREATE PROCEDURE GetStockHistory (
    IN startDate DATETIME,
    IN endDate DATETIME,
    IN productId CHAR(23),
    IN supplierCnpj CHAR(18),
    IN employeeCpf CHAR(14),
    IN categoryId CHAR(23)
)
BEGIN
    SELECT
        i.id AS item_id,
        i.product_id,
        p.prod_name,
        i.supplier_cnpj,
        s.name AS supplier_name,
        i.employee_cpf,
        e.name AS employee_name,
        CASE
            WHEN d.item_id IS NOT NULL THEN 'Discarded'
            WHEN oi.item_id IS NOT NULL THEN 'Ordered'
            ELSE 'In Stock'
        END AS status,
        CASE
            WHEN d.item_id IS NOT NULL THEN d.updated_at
            WHEN oi.item_id IS NOT NULL THEN o.updated_at
            ELSE i.updated_at
        END AS last_update
    FROM item i
    JOIN product p ON i.product_id = p.id
    JOIN product_supplier s ON i.supplier_cnpj = s.cnpj
    JOIN employee e ON i.employee_cpf = e.cpf
    JOIN classification c ON p.id = c.product_id
    JOIN category cat ON c.category_id = cat.id
    LEFT JOIN discard d ON i.id = d.item_id
    LEFT JOIN ordered_item oi ON i.id = oi.item_id
    LEFT JOIN orders o ON oi.order_id = o.id
    WHERE (startDate IS NULL OR i.created_at >= startDate)
        AND (endDate IS NULL OR i.created_at <= endDate)
        AND (productId IS NULL OR i.product_id = productId)
        AND (supplierCnpj IS NULL OR i.supplier_cnpj = supplierCnpj)
        AND (employeeCpf IS NULL OR i.employee_cpf = employeeCpf)
        AND (categoryId IS NULL OR cat.id = categoryId)
    ORDER BY
        i.created_at DESC;
END //

DELIMITER ;

-- VIEWS

-- view to call for items currently in stock, doubles as the list of items eligible for removal

CREATE VIEW current_stock AS
SELECT i.id, i.product_id, i.supplier_cnpj, i.employee_cpf, i.created_at, i.updated_at
FROM item i
LEFT JOIN discard d ON i.id = d.item_id
LEFT JOIN ordered_item oi ON i.id = oi.item_id
WHERE d.item_id IS NULL AND oi.item_id IS NULL;

