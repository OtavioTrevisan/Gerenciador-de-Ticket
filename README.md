Gerenciador de Tickets – Sistema de Controle de Entregas

Este projeto é um sistema desktop desenvolvido em Java 17, utilizando Swing, Maven, SQLite e testado com JUnit 5.
Ele permite gerenciar Funcionários e Tickets de Entrega, possibilitando cadastro, listagem, edição e geração de relatórios por período.

O projeto foi desenvolvido como parte da Prova Prática de Programação, seguindo boas práticas de arquitetura, organização e testes.

Funcionalidades

Funcionários:
Cadastrar novo funcionário
Listar funcionários cadastrados
Editar informações (nome, CPF e situação)
Validação completa de CPF
Registro automático de data de alteração

Tickets de Entrega:
Cadastrar ticket vinculado a um funcionário
Listar tickets existentes
Editar ticket (quantidade, situação e funcionário relacionado)
Registro automático de data da entrega/alteração

Relatórios:
Filtrar tickets por período
Tabela detalhada de entregas
Totais por funcionário
Cálculo automático do total geral

Arquitetura do Projeto

O projeto segue o padrão MVC + camadas:

model, pacote que representa os dados

dao, pacote que comunica com o SQLite

service, pacote que realiza as validações, regras de negócio

view, pacote que armazena as janelas Swing

config, pacote que possui inicialização e conexão com o banco

Essa divisão melhora a manutenção, testabilidade e organização do projeto.

Tecnologias Utilizadas:
Java 17	Linguagem principal, orientação a objetos
Swing	Interface gráfica
Maven	Gerenciamento de dependências e build
SQLite	Banco de dados local
JUnit 5	Testes unitários

Instalação e Execução
1. Clonar o repositório
https://github.com/OtavioTrevisan/Gerenciador-de-Ticket

2. Acessar dentro de um editor de código o arquivo src/main/java/br/com/ticket/Main.java
Rodar o arquivo

Executando os Testes:
Para rodar todos os testes, entrar dentro do arquivo de teste de funcionário (src/test/java/br/com/ticket/service/FuncionarioServiceTest.java) ou o arquivo de tickets (src/test/java/br/com/ticket/service/TicketServiceTest.java)

Rodar os arquivos dentro do editor de código

Banco de Dados:

Banco utilizado foi o SQLite

Arquivo gerado é o ticket.db, criado automaticamente na primeira execução.
Caso queira recriar o banco, basta excluir o arquivo ticket.db e executar o sistema novamente.

Fluxo Principal da Aplicação:

Tela inicial com menu lateral

Cadastro/Listagem/Edição de Funcionários

Cadastro/Listagem/Edição de Tickets

Relatórios por período

O sistema valida todos os campos importantes, incluindo CPF e regras de negócio.

Licença:

Este projeto utiliza a licença MIT, permitindo uso livre para fins acadêmicos e profissionais.

Para informações mais detalhadas acessar documentação técnica dentro do git do projeto (Documento técnico.docx.pdf)
