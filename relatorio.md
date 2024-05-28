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

<div style="page-break-after: always;"></div>

# 3. Mapeamento Lógico-relacional

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

```

```


```

```

- Nota: para as id's das entidades, quando não naturais (cpf/cnpj), foram utilizados identificadores únicos no padrão NanoId (strings curtas, seguras, amigáveis à URL e customizáveis com baixa chance de colisão). Para mais especificações: https://github.com/ai/nanoid


<div style="page-break-after: always;"></div>


# 5. Desenvolvimento da aplicação
   1. Front-end
      - Framework: React com Vite (https://vitejs.dev/)
      - Shadcn para componentes de UI (https://ui.shadcn.com/) e TanStack para roteamento, queries, tabelas e forms (https://tanstack.com/)    
   2. Back-end
       - Framework: SpringBoot (Maven).
      -  Sem uso de ORM, mas preservando arquitetura do Springboot (controllers-services-repositories), classes JDBC para acesso direto ao banco de dados com queries SQL escritas à mão.
   3. Banco de dados:
      - Docker Compose para orquestrar os containers do backend, frontend e banco de dados (inclusive execução automática do script de inicialização)
      - Triggers para garantir integridade do banco dentro das regras de negócio e impedir a invalidação da funcionalidade do histórico de estoque
      - Procedures e views para extração de relatórios