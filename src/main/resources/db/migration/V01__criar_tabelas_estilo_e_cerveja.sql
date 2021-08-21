CREATE TABLE estilo(
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE cerveja (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    sku VARCHAR(50) NOT NULL,
    nome VARCHAR(80) NOT NULL,
    descricao TEXT NOT NULL,
    valor DECIMAL(10, 2) NOT NULL,
    teor_alcoolico DECIMAL(10, 2) NOT NULL,
    comissao DECIMAL(10, 2) NOT NULL,
    quantidade_estoque INT NOT NULL,
    sabor VARCHAR(50) NOT NULL,
    origem VARCHAR(50) NOT NULL,
    estilo_id BIGINT(20) NOT NULL,
    FOREIGN KEY (estilo_id) REFERENCES estilo(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO estilo VALUES(0, 'Amber Lager');
INSERT INTO estilo VALUES(0, 'Dark Lager');
INSERT INTO estilo VALUES(0, 'Pale Lager');
INSERT INTO estilo VALUES(0, 'Pilsner');