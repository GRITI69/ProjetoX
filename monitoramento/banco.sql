CREATE DATABASE projetox;

USE projetox;

CREATE TABLE maquina (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    hostname VARCHAR(100) UNIQUE NOT NULL,
    sistema_operacional VARCHAR(50)
);

CREATE TABLE monitoramento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cpu DOUBLE,
    ram DOUBLE,
    disco DOUBLE,
    temperatura DOUBLE,
    data_hora TIMESTAMP,
    maquina_id BIGINT,
    FOREIGN KEY (maquina_id) REFERENCES maquina(id)
);


SET FOREIGN_KEY_CHECKS = 0;
-- Limpa todas as tabelas
TRUNCATE TABLE Alerta;
TRUNCATE TABLE Servico;
TRUNCATE TABLE Monitoramento;
TRUNCATE TABLE Maquina;

-- Ativa novamente
SET FOREIGN_KEY_CHECKS = 1;