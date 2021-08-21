CREATE TABLE estado(
	id BIGINT(20) PRIMARY KEY,
	nome VARCHAR(50) NOT NULL,
	sigla  VARCHAR(2) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE cidade(
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	estado_id BIGINT(20) NOT NULL,
	FOREIGN KEY (estado_id) REFERENCES estado(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;



INSERT INTO estado (id, nome, sigla) VALUES (1,'Acre', 'AC');
INSERT INTO estado (id, nome, sigla) VALUES (2,'Bahia', 'BA');
INSERT INTO estado (id, nome, sigla) VALUES (3,'Goiás', 'GO');
INSERT INTO estado (id, nome, sigla) VALUES (4,'Minas Gerais', 'MG');
INSERT INTO estado (id, nome, sigla) VALUES (5,'Santa Catarina', 'SC');
INSERT INTO estado (id, nome, sigla) VALUES (6,'São Paulo', 'SP');


INSERT INTO cidade (nome, estado_id) VALUES ('Rio Branco', 1);
INSERT INTO cidade (nome, estado_id) VALUES ('Cruzeiro do Sul', 1);
INSERT INTO cidade (nome, estado_id) VALUES ('Salvador', 2);
INSERT INTO cidade (nome, estado_id) VALUES ('Porto Seguro', 2);
INSERT INTO cidade (nome, estado_id) VALUES ('Santana', 2);
INSERT INTO cidade (nome, estado_id) VALUES ('Goiânia', 3);
INSERT INTO cidade (nome, estado_id) VALUES ('Itumbiara', 3);
INSERT INTO cidade (nome, estado_id) VALUES ('Novo Brasil', 3);
INSERT INTO cidade (nome, estado_id) VALUES ('Belo Horizonte', 4);
INSERT INTO cidade (nome, estado_id) VALUES ('Uberlândia', 4);
INSERT INTO cidade (nome, estado_id) VALUES ('Montes Claros', 4);
INSERT INTO cidade (nome, estado_id) VALUES ('Florianópolis', 5);
INSERT INTO cidade (nome, estado_id) VALUES ('Criciúma', 5);
INSERT INTO cidade (nome, estado_id) VALUES ('Camboriú', 5);
INSERT INTO cidade (nome, estado_id) VALUES ('Lages', 5);
INSERT INTO cidade (nome, estado_id) VALUES ('São Paulo', 6);
INSERT INTO cidade (nome, estado_id) VALUES ('Ribeirão Preto', 6);
INSERT INTO cidade (nome, estado_id) VALUES ('Campinas', 6);
INSERT INTO cidade (nome, estado_id) VALUES ('Santos', 6);
