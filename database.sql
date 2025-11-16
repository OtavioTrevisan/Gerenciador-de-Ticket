/*
Esse arquivo visa criar automáticamente as tabelas do banco de dado, sendo elas as duas principais no projeto:

Funcionário, que vai ser a tabela que guarda as informações de funcionários cadastrados no sistema

Ticket_entrega, que vai ser a tabela que guarda as informações dos tickets cadastrados no sistema e o seu relacionamento com funcionários
*/

--SQLite tem como padrão chaves estrangeiras desligadas, por conta disso, precisamos ligar essa feature
PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS funcionario (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    cpf TEXT NOT NULL UNIQUE,
    situacao TEXT NOT NULL CHECK (situacao IN ('A','I')),
    data_alteracao TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS ticket_entrega (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    funcionario_id INTEGER NOT NULL,
    quantidade INTEGER NOT NULL,
    situacao TEXT NOT NULL CHECK (situacao IN ('A','I')),
    data_entrega TEXT NOT NULL,
    FOREIGN KEY (funcionario_id) REFERENCES funcionario(id)
);
