O processo de desenvolvimento da aplicação "Armazém de seu Zé" percorreu as seguintes etapas:

1. **Análise de requisitos**

Minimundo (criado pela equipe):

Um armazém de produtos de informática precisa controlar seu estoque ao longo do tempo e gerenciar aspectos logísticos relacionados ao fluxo de mercadorias.

O que o armazém armazena são itens, que representam uma "instância" de um produto, fornecido por um fornecedor. É possível que o mesmo produto seja fornecido por mais de um fornecedor, como no caso de GPUs de marcas distintas com o mesmo chipset, ou um mesmo periférico fornecido por importadores distintos. Cada item presente no banco de dados precisa ter um funcionário associado, responsável pelo cadastro, e um timestamp (data e hora) relativo ao cadastro.

Produtos representam um modelo de produto e tem nome, categoria e uma descrição (opcional). Cada produto pode ou não ser frágil. Um produto deve pertencer a uma ou mais categorias.

Categorias possuem nome e descrição (opcional).
 
Fornecedores têm cnpj, nome e dados de contato (telefone e e-mail).

A perda de itens do estoque relaciona o item perdido, funcionário responsável pela identificação e um timestamp (data e hora) para a perda. Só podem ser perdidos itens previamente cadastrados no banco e que não foram expedidos em algum pedido (vendidos). Além disso, deve ser registrado um motivo para a perda, o qual só pode assumir alguns valores distintos: extravio, dano de acondicionamento (armazém), problema de fabricação ou transporte (fornecedor).

Por fim, um ou mais itens cadastrados podem ser expedidos como parte de um pedido (venda). Deve ser relacionado o funcionário responsável, os itens envolvidos (entre os cadastrados e não perdidos), um timestamp (data e hora) e o cliente que realizou o pedido.

Funcionários têm cpf, nome e dados de contato (telefone e email). Podem ser gerentes ou não. Todo funcionário gerente pode ser supervisor de outros funcionários e todo funcionário não-gerente deve ter um outro funcionário, gerente, como supervisor.

Clientes têm nome, dados de contato (telefone e e-mail) e podem ser pessoas físicas ou pessoas jurídicas; pessoas físicas têm cpf e pessoas jurídicas, cnpj.

Cada cliente pode ter um número arbitrário de endereços de entrega. Endereços de entrega são compostos pelo nome do destinatário e endereço (com estado, municipio, CEP, logradouro, numero e complemento).

Cada pedido expedido, além de cliente e funcionário, também está relacionado a um endereço de entrega e a uma transportadora e gera um ou mais pacotes, que combinados contém todos os itens envolvidos no pedido. Opcionalmente, um pedido pode conter observações de entrega para a transportadora.

Cada pacote está relacionado a exatamente um pedido e um ou mais produtos e contém um código de rastreamento. Pacotes podem ser convencionais ou marcados como frágeis. Somente pacotes marcados como frágeis podem transportar itens frágeis.

Transportadoras têm cpnj, nome, dados de contato (telefone e e-mail).

2. Modelagem Conceitual
3. Mapeamento Lógico-relacional
4. Modelagem física (init.sql e povoamento)
   1. 
5. Desenvolvimento da aplicação
   1. Front-end
      - Framework: React com Vite.
      - Shadcn para componentes de UI e TanStack para roteamento,queries, tabelas e forms.    
   2. Back-end
       - Framework: SpringBoot (Maven).
      -  Sem uso de ORM, mas preservando arquitetura do Springboot (controllers-services-repositories), classes JDBC para acesso direto ao banco de dados com queries SQL escritas à mão.
   3. Conteinerização
      - Docker Compose: Para orquestrar os containers do backend, frontend e banco de dados (inclusive execução automática do script de inicialização)