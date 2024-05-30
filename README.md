# Projeto e Modelagem de Bancos de Dados - 2024.1 - turma A
## Projeto AV2
### Equipe: Arthur Wolmer e Diego Peter

O processo de desenvolvimento da aplicação cobriu os pontos de 1. a 4. aproximadamente em sequência, com a exploração das tecnologias correndo em paralelo.

# 1. **Análise de requisitos**

Minimundo (criado pela equipe):

Um **armazém de produtos de informática** precisa controlar seu e**stoque ao longo do tempo e gerenciar aspectos logísticos** relacionados ao fluxo de mercadorias.

O que o armazém armazena são itens, que representam uma "instância" de um produto, fornecido por um fornecedor. É possível que o mesmo produto seja fornecido por mais de um fornecedor, como no caso de GPUs de marcas distintas com o mesmo chipset, ou um mesmo periférico fornecido por importadores distintos. Cada item presente no banco de dados precisa ter um funcionário associado, responsável pelo cadastro, e um timestamp (data e hora) relativo ao cadastro.

Produtos representam um modelo de produto e tem nome, categoria e uma descrição (opcional). Cada produto pode ou não ser frágil. Um produto deve pertencer a uma ou mais categorias.

Categorias possuem nome e descrição (opcional).
 
Fornecedores têm cnpj, nome e dados de contato (telefone e e-mail).

A perda de itens do estoque relaciona o item perdido, funcionário responsável pela identificação e um timestamp (data e hora) para a perda. Só podem ser perdidos itens previamente cadastrados no banco e que não foram expedidos em algum pedido (vendidos). Além disso, deve ser registrado um motivo para a perda, o qual só pode assumir alguns valores distintos: extravio, dano de acondicionamento (armazém), problema de fabricação ou transporte (fornecedor).

Por fim, um ou mais itens cadastrados podem ser expedidos como parte de um pedido (venda). Deve ser relacionado o funcionário responsável, os itens envolvidos (entre os cadastrados e não perdidos), um timestamp (data e hora) e o cliente que realizou o pedido.

Funcionários têm cpf, nome e dados de contato (telefone e email). Podem ser gerentes ou não. Todo funcionário gerente pode ser supervisor de outros funcionários e todo funcionário não-gerente deve ter um outro funcionário, gerente, como supervisor.

Clientes têm nome, dados de contato (telefone e e-mail) e podem ser pessoas físicas ou pessoas jurídicas; pessoas físicas têm cpf e pessoas jurídicas, cnpj.

Cada cliente pode ter um número arbitrário de endereços de entrega. Endereços de entrega são compostos pelo nome do destinatário e endereço (com estado, municipio, CEP, logradouro, numero e complemento).

Cada pedido expedido, além de cliente e funcionário, também está relacionado a um endereço de entrega, uma transportadora e possui código de rastreamento.

Transportadoras têm cpnj, nome, dados de contato (telefone e e-mail).

<div style="page-break-after: always;"></div>

# 2. Modelagem Conceitual

![conceitual_armazem_2](https://github.com/alwolmer/projeto_bd/assets/108356950/ada51219-10f9-4202-b078-667310b982bf)


<div style="page-break-after: always;"></div>

# 3. Mapeamento Lógico-relacional

![logico_armazem_2](https://github.com/alwolmer/projeto_bd/assets/108356950/efae0702-85bd-4c2a-aa76-c003a7152872)


<div style="page-break-after: always;"></div>

# 4. Modelagem física (inclusive init.sql e povoamento)

## Script de criação

``` 
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
```

## Consultas (ligadas a operações CRUD)

```
-- CATEGORIAS

- Insere uma nova categoria na tabela 'category'
INSERT INTO category (id, cat_name) VALUES (?, ?);

-- Seleciona todas as categorias da tabela 'category'
SELECT * FROM category;

-- Deleta uma categoria pelo seu ID na tabela 'category'
DELETE FROM category WHERE id = ?;

-- Atualiza o nome de uma categoria pelo seu ID na tabela 'category'
UPDATE category SET cat_name = ? WHERE id = ?;

-- Seleciona uma categoria pelo seu nome na tabela 'category'
SELECT * FROM category WHERE cat_name = ?;

-- Seleciona uma categoria pelo seu ID na tabela 'category'
SELECT * FROM category WHERE id = ?;

-- PRODUTOS

-- Insere um novo produto na tabela 'product'
INSERT INTO product (id, prod_name) VALUES (?, ?);

-- Seleciona todos os produtos da tabela 'product'
SELECT * FROM product;

-- Seleciona um produto pelo seu ID na tabela 'product'
SELECT * FROM product WHERE id = ?;

-- Atualiza o nome de um produto pelo seu ID na tabela 'product'
UPDATE product SET prod_name = ? WHERE id = ?;

-- Deleta um produto pelo seu ID na tabela 'product'
DELETE FROM product WHERE id = ?;

-- FORNECEDOR

-- Insere um novo fornecedor na tabela 'product_supplier'
INSERT INTO product_supplier (cnpj, name, email, phone) VALUES (?, ?, ?, ?);

-- Seleciona todos os fornecedores da tabela 'product_supplier'
SELECT * FROM product_supplier;

-- Seleciona um fornecedor pelo seu CNPJ na tabela 'product_supplier'
SELECT * FROM product_supplier WHERE cnpj = ?;

-- Atualiza os dados de um fornecedor pelo seu CNPJ na tabela 'product_supplier'
UPDATE product_supplier SET name = ?, email = ?, phone = ? WHERE cnpj = ?;

-- Deleta um fornecedor pelo seu CNPJ na tabela 'product_supplier'
DELETE FROM product_supplier WHERE cnpj = ?;

-- FUNCIONÁRIO

-- Seleciona um empregado pelo seu CPF na tabela 'employee'
SELECT * FROM employee WHERE cpf = ?;

-- Insere um novo empregado na tabela 'employee'
INSERT INTO employee (cpf, name, email, phone, is_manager, manager_cpf, passwordHash) VALUES (?, ?, ?, ?, ?, ?, ?);

-- Seleciona todos os empregados da tabela 'employee'
SELECT * FROM employee;

-- Atualiza os dados de um empregado pelo seu CPF na tabela 'employee'
UPDATE employee SET name = ?, email = ?, phone = ?, is_manager = ?, manager_cpf = ?, passwordHash = ? WHERE cpf = ?;

-- Deleta um empregado pelo seu CPF na tabela 'employee'
DELETE FROM employee WHERE cpf = ?;

-- ITEM CADASTRADO

-- Insere um novo item na tabela 'item'
INSERT INTO item (id, product_id, supplier_cnpj, employee_cpf) VALUES (?, ?, ?, ?);

-- Seleciona um item pelo seu ID na tabela 'item'
SELECT * FROM item WHERE id = ?;

-- Seleciona todos os itens da tabela 'item'
SELECT * FROM item;

-- Deleta um item pelo seu ID na tabela 'item'
DELETE FROM item WHERE id = ?;

-- Seleciona todos os itens do estoque atual da tabela 'current_stock'
SELECT * FROM current_stock;

-- (ITEM) DESCARTADO

-- Insere um novo descarte na tabela 'discard'
INSERT INTO discard (employee_cpf, item_id, discard_reason) VALUES (?, ?, ?);

-- Seleciona um descarte pela combinação de CPF do empregado e ID do item na tabela 'discard'
SELECT * FROM discard WHERE employee_cpf = ? AND item_id = ?;

-- Seleciona todos os descartes da tabela 'discard'
SELECT * FROM discard;

-- Deleta um descarte pela combinação de CPF do empregado e ID do item na tabela 'discard'
DELETE FROM discard WHERE employee_cpf = ? AND item_id = ?;

-- Deleta um descarte pelo ID do item na tabela 'discard'
DELETE FROM discard WHERE item_id = ?;

-- TRANSPORTADORA

-- Insere uma nova transportadora na tabela 'carrier'
INSERT INTO carrier (cnpj, name, email, phone) VALUES (?, ?, ?, ?);

-- Seleciona uma transportadora pelo seu CNPJ na tabela 'carrier'
SELECT * FROM carrier WHERE cnpj = ?;

-- Atualiza os dados de uma transportadora pelo seu CNPJ na tabela 'carrier'
UPDATE carrier SET name = ?, email = ?, phone = ? WHERE cnpj = ?;

-- Deleta uma transportadora pelo seu CNPJ na tabela 'carrier'
DELETE FROM carrier WHERE cnpj = ?;

-- Seleciona todas as transportadoras da tabela 'carrier'
SELECT * FROM carrier;

-- CLIENTE

-- Insere um novo cliente na tabela 'client' usando CPF
INSERT INTO client (id, name, phone, email, cpf) VALUES (?, ?, ?, ?, ?);

-- Insere um novo cliente na tabela 'client' usando CNPJ
INSERT INTO client (id, name, phone, email, cnpj) VALUES (?, ?, ?, ?, ?);

-- Seleciona um cliente pelo seu ID na tabela 'client'
SELECT * FROM client WHERE id = ?;

-- Atualiza os dados de um cliente pelo seu ID na tabela 'client'
UPDATE client SET name = ?, phone = ?, email = ?, cpf = ?, cnpj = ? WHERE id = ?;

-- Deleta um cliente pelo seu ID na tabela 'client'
DELETE FROM client WHERE id = ?;

-- Seleciona todos os clientes da tabela 'client'
SELECT * FROM client;

-- ENDEREÇO ENTREGA

-- Insere um novo endereço de entrega na tabela 'delivery_address'
INSERT INTO delivery_address (id, recipient_name, state, city, zip, street, number, details, client_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);

-- Seleciona um endereço de entrega pelo seu ID na tabela 'delivery_address'
SELECT * FROM delivery_address WHERE id = ?;

-- Atualiza os dados de um endereço de entrega pelo seu ID na tabela 'delivery_address'
UPDATE delivery_address SET recipient_name = ?, state = ?, city = ?, zip = ?, street = ?, number = ?, details = ?, client_id = ? WHERE id = ?;

-- Deleta um endereço de entrega pelo seu ID na tabela 'delivery_address'
DELETE FROM delivery_address WHERE id = ?;

-- Seleciona todos os endereços de entrega da tabela 'delivery_address'
SELECT * FROM delivery_address;

-- PEDIDO

-- Insere um novo pedido na tabela 'orders'
INSERT INTO orders (id, client_id, delivery_address_id, employee_cpf, carrier_cnpj, tracking_code) VALUES (?, ?, ?, ?, ?, ?);

-- Seleciona um pedido pelo seu ID na tabela 'orders'
SELECT * FROM orders WHERE id = ?;

-- Atualiza os dados de um pedido pelo seu ID na tabela 'orders'
UPDATE orders SET client_id = ?, delivery_address_id = ?, employee_cpf = ?, carrier_cnpj = ?, tracking_code = ?, created_at = ?, updated_at = ? WHERE id = ?;

-- Deleta um pedido pelo seu ID na tabela 'orders'
DELETE FROM orders WHERE id = ?;

-- Seleciona todos os pedidos da tabela 'orders'
SELECT * FROM orders;

-- RELAÇÃO ITEM-PEDIDO

-- Insere um novo item ordenado na tabela 'ordered_item'
INSERT INTO ordered_item (item_id, order_id) VALUES (?, ?);

-- Deleta um item ordenado pela combinação de ID do item e ID do pedido na tabela 'ordered_item'
DELETE FROM ordered_item WHERE item_id = ? AND order_id = ?;
```

## Consultas (relatórios)

```
-- PROCEDURES


-- procedure to get all items ever registered, able to filter by date, product, supplier, employee or category
-- all filters optional 
-- CALL GetStockHistory(NULL, NULL, NULL, NULL, NULL, NULL);

DELIMITER //

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
```

## Triggers (garantias de integridade)
```
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

DELIMITER ;
```

- Nota: para as id's das entidades, quando não naturais (cpf/cnpj), foram utilizados identificadores únicos no padrão NanoId (strings curtas, seguras, amigáveis à URL e customizáveis com baixa chance de colisão). Para mais especificações: https://github.com/ai/nanoid

## Script de povoamento

```
-- POPULAR CATEGORIAS
INSERT INTO category (id, cat_name) VALUES 
('MFaHmqyYdMeztRkWc4eGXrq', 'Motherboards'),
('kSjDUSds9ucAM5XSyh4911g', 'Processors'),
('w5B8xPtg26A6n6q3WwGGS6F', 'RAM'),
('UE2uucTcpYEnCpfbEtTgYGd', 'Graphics Card'),
('Ds5M67jbRKVg6fkp2xXFHPs', 'SSD'),
('E8DBHzPAjWDJXnuxbZf3JtK', 'Keyboard'),
('4VuyVqAKz3u49YP8bayhBcX', 'Mouse');

-- POPULAR PRODUCTS
INSERT INTO product (id, prod_name) VALUES
('AyAurtQGureTsGruqGqabvE', 'Intel Core i5-12600K'),
('MY26fVpYMBRE5dJUJ3DfMCC', 'RTX 4090'),
('Rh7dUcHQ975K2WXbECF7wAV', 'Corsair Vengeance RGB Pro 32GB'),
('kt64HYU56aUEhEY6Mez5n8m', 'ASUS ROG Strix Z690-E Gaming WiFi'),
('7FvgvE83SNpQZfKwWPQ5ebP', 'Samsung 970 EVO Plus 1TB'),
('qURnfEbrC8msnVmqPhskXcE', 'Logitech G Pro X Superlight'),
('WnQYM5rmMa5F88GmwGFqevf', 'Razer BlackWidow V3 Pro');

-- POPULAR classifications
INSERT INTO classification (product_id, category_id) VALUES
('AyAurtQGureTsGruqGqabvE', 'kSjDUSds9ucAM5XSyh4911g'),
('MY26fVpYMBRE5dJUJ3DfMCC', 'UE2uucTcpYEnCpfbEtTgYGd'),
('Rh7dUcHQ975K2WXbECF7wAV', 'w5B8xPtg26A6n6q3WwGGS6F'),
('kt64HYU56aUEhEY6Mez5n8m', 'MFaHmqyYdMeztRkWc4eGXrq'),
('7FvgvE83SNpQZfKwWPQ5ebP', 'Ds5M67jbRKVg6fkp2xXFHPs'),
('qURnfEbrC8msnVmqPhskXcE', '4VuyVqAKz3u49YP8bayhBcX'),
('WnQYM5rmMa5F88GmwGFqevf', 'E8DBHzPAjWDJXnuxbZf3JtK');

-- POPULAR SUPPLIERS
INSERT INTO product_supplier (cnpj, name, phone, email) VALUES
('71.265.232/0001-10', 'ASUS', '(17) 98162-4696', 'contato@fornecedorasus.com'),
('78.907.424/0001-21', 'Intel', '(94) 96770-6596', 'contato@fornecedorintel.com'),
('58.194.520/0001-62', 'Nvidia', '(96) 97644-5774', 'contato@fornecedornvidia.com'),
('85.893.777/0001-85', 'Corsair', '(89) 96814-8553', 'contato@fornecedorcorsair.com'),
('76.043.670/0001-57', 'Samsung', '(63) 97321-7078', 'contato@fornecedorsamsung.com'),
('02.206.558/0001-53', 'Logitech', '(99) 99417-4743', 'contato@fornecedorlogitech.com'),
('77.209.429/0001-18', 'Razer', '(64) 98452-7351', 'contato@fornecedorrazer.com');

-- POPULAR EMPLOYEES
INSERT INTO employee (cpf, name, phone, email, passwordHash, is_manager, manager_cpf) VALUES
('302.411.040-25', 'João Silva', '(27) 97610-8281', 'joao.silva@example.com', '123456', TRUE, NULL),
('122.110.370-97', 'Maria Santos', '(98) 97167-6246', 'maria.santos@example.com', '123456', FALSE, '302.411.040-25'),
('183.488.760-73', 'Carlos Pereira', '(27) 98045-3486', 'carlos.pereira@example.com', '123456', FALSE, '302.411.040-25');

-- POPULAR ITEMS
INSERT INTO item (id, product_id, supplier_cnpj, employee_cpf, created_at) VALUES
('1a2b3c4d5e6f7g8h9i0j1k', 'AyAurtQGureTsGruqGqabvE', '78.907.424/0001-21', '302.411.040-25', '2022-01-01 00:00:00'),
('2b3c4d5e6f7g8h9i0j1k2l', 'MY26fVpYMBRE5dJUJ3DfMCC', '58.194.520/0001-62', '302.411.040-25', '2023-05-01 00:00:00'),
('3c4d5e6f7g8h9i0j1k2l3m', 'Rh7dUcHQ975K2WXbECF7wAV', '85.893.777/0001-85', '302.411.040-25', '2023-08-20 00:00:00'),
('4d5e6f7g8h9i0j1k2l3m4n', 'kt64HYU56aUEhEY6Mez5n8m', '71.265.232/0001-10', '302.411.040-25', '2024-01-15 00:00:00'),
('5e6f7g8h9i0j1k2l3m4n5o', '7FvgvE83SNpQZfKwWPQ5ebP', '76.043.670/0001-57', '302.411.040-25', '2024-02-01 00:00:00'),
('6f7g8h9i0j1k2l3m4n5o6p', 'qURnfEbrC8msnVmqPhskXcE', '02.206.558/0001-53', '302.411.040-25', '2024-03-01 00:00:00'),
('7g8h9i0j1k2l3m4n5o6p7q', 'WnQYM5rmMa5F88GmwGFqevf', '77.209.429/0001-18', '302.411.040-25', '2024-04-01 00:00:00'),
('8h9i0j1k2l3m4n5o6p7q8r', 'AyAurtQGureTsGruqGqabvE', '78.907.424/0001-21', '302.411.040-25', '2024-05-01 00:00:00'),
('9i0j1k2l3m4n5o6p7q8r9s', 'MY26fVpYMBRE5dJUJ3DfMCC', '58.194.520/0001-62', '302.411.040-25', '2024-06-01 00:00:00'),
('0j1k2l3m4n5o6p7q8r9s0t', 'Rh7dUcHQ975K2WXbECF7wAV', '85.893.777/0001-85', '302.411.040-25', '2024-07-01 00:00:00'),
('1k2l3m4n5o6p7q8r9s0t1u', 'kt64HYU56aUEhEY6Mez5n8m', '71.265.232/0001-10', '302.411.040-25', '2024-07-01 00:00:00'),
('2l3m4n5o6p7q8r9s0t1u2v', '7FvgvE83SNpQZfKwWPQ5ebP', '76.043.670/0001-57', '302.411.040-25', '2024-07-01 00:00:00'),
('3m4n5o6p7q8r9s0t1u2v3w', 'qURnfEbrC8msnVmqPhskXcE', '02.206.558/0001-53', '302.411.040-25', '2024-07-01 00:00:00'),
('4n5o6p7q8r9s0t1u2v3w4x', 'WnQYM5rmMa5F88GmwGFqevf', '77.209.429/0001-18', '302.411.040-25', '2024-07-01 00:00:00');



-- POPULAR DISCARDS
INSERT INTO discard (employee_cpf, item_id, discard_reason) VALUES
('302.411.040-25', '1a2b3c4d5e6f7g8h9i0j1k', 'Loss'),
('302.411.040-25', '2b3c4d5e6f7g8h9i0j1k2l', 'Bad Conditioning');

-- POPULAR CLIENTS
INSERT INTO client (id, name, phone, email, cpf, cnpj) VALUES
('Gb7G4enpkCDJDAJs6Nucfff', 'João Pedro', '(87) 97696-8526', 'joao.pedro@example.com', '700.074.620-34', NULL),
('mfFBn4FjSTTGHfka21Z5gSh', 'Luiz Vinicius', '(98) 97167-6246', 'luiz.vinicius@example.com', '782.441.400-03', NULL),
('nfzgCh7yNatb7hg8Ku5j7M4', 'Kabum', '(83) 96910-7542', 'kabum@example.com', NULL, '18.352.796/0001-07');

-- POPULAR DELIVERY ADDRESSES
INSERT INTO delivery_address (id, recipient_name, state, city, zip, street, number, details, client_id) VALUES
('KvfGuzZbr5qjGsVKcugKE3t', 'João Pedro', 'SP', 'São Paulo', '01001-000', 'Rua Augusta', '1000', 'Apto 101', 'Gb7G4enpkCDJDAJs6Nucfff'),
('Cfe5VNzHM5fAncuJQ7PTH5H', 'Luiz Vinicius', 'SP', 'São Paulo', '01001-000', 'Rua Augusta', '1000', 'Apto 101', 'mfFBn4FjSTTGHfka21Z5gSh'),
('E8Cmvneue3nuyuaCnfQPypP', 'Kabum', 'SP', 'São Paulo', '01001-000', 'Rua Augusta', '1000', 'Apto 101', 'nfzgCh7yNatb7hg8Ku5j7M4');

-- POPULAR CARRIERS
INSERT INTO carrier (cnpj, name, phone, email) VALUES
('25.636.662/0001-83', 'Correios', '(19) 99941-2621', 'correios@example.com'),
('09.604.111/0001-81', 'Fedex', '(32) 97494-8955', 'fedex@example.com'),
('69.715.892/0001-03', 'DHL', '(47) 98298-9111', 'dhl@example.com');

-- POPULAR ORDERS
INSERT INTO orders (id, client_id, employee_cpf, delivery_address_id, carrier_cnpj, tracking_code) VALUES
('7Fh4Z4nv6pKnvFRU15mW95y', 'Gb7G4enpkCDJDAJs6Nucfff', '302.411.040-25', 'KvfGuzZbr5qjGsVKcugKE3t', '25.636.662/0001-83', '55e524c7-1f2b-46a4-80e9-186d47c5a222'),
('7Nyj9H5Vnzjq5Z1j99N29Mf', 'mfFBn4FjSTTGHfka21Z5gSh', '302.411.040-25', 'Cfe5VNzHM5fAncuJQ7PTH5H', '09.604.111/0001-81', 'ff6ee794-1a9d-4db9-bfa9-0db64d1d1cd6');

-- POPULAR ORDERED ITEMS
INSERT INTO ordered_item (item_id, order_id) VALUES
('3c4d5e6f7g8h9i0j1k2l3m', '7Fh4Z4nv6pKnvFRU15mW95y'),
('6f7g8h9i0j1k2l3m4n5o6p', '7Nyj9H5Vnzjq5Z1j99N29Mf');
```


<div style="page-break-after: always;"></div>


# 5. Desenvolvimento da aplicação
   1. Front-end
      - Framework: React com Vite (https://vitejs.dev/)
      - Shadcn para componentes de UI (https://ui.shadcn.com/) e TanStack para roteamento, queries, tabelas e forms (https://tanstack.com/)    
   2. Back-end
       - Framework: SpringBoot (Maven).
      -  Sem uso de ORM, mas preservando arquitetura do Springboot (controllers-services-repositories), classes JDBC para acesso direto ao banco de dados com queries SQL escritas à mão.
   3. Banco de dados:
      - Docker Compose para orquestrar o container do banco de dados (inclusive execução automática do script de inicialização)
      - Triggers para garantir integridade do banco dentro das regras de negócio e impedir a invalidação da funcionalidade do histórico de estoque
      - Procedures e views para extração de relatórios

<div style="page-break-after: always;"></div>

# 6. Instalação

Banco de dados
```
# ./projeto_bd
(sudo) docker compose up
# OR
(sudo) docker-compose up
```
  
Backend
```
# ./projeto_bd/Storage
./mvnw install
# THEN
./mvnw spring-boot:run
```

Frontend
```
# ./projeto_bd/front
npm install
# THEN
npm run dev
```
