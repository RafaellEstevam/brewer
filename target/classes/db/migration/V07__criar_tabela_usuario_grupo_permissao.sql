CREATE TABLE usuario(
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL,
	senha VARCHAR(120) NOT NULL,
	ativo BOOLEAN DEFAULT true,
	data_nascimento DATE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE grupo(
	id BIGINT(20) PRIMARY KEY,
	nome VARCHAR(50) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE usuario_grupo(
	usuario_id BIGINT(20) NOT NULL,
	grupo_id BIGINT(20) NOT NULL,
	PRIMARY KEY (usuario_id, grupo_id),
	FOREIGN KEY (usuario_id) REFERENCES usuario(id),
	FOREIGN KEY (grupo_id) REFERENCES grupo(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE permissao(
	id BIGINT(20) PRIMARY KEY,
	nome VARCHAR(50) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE grupo_permissao(
	grupo_id BIGINT(20) NOT NULL,
	permissao_id BIGINT(20) NOT NULL,
	PRIMARY KEY (grupo_id, permissao_id),
	FOREIGN KEY (grupo_id) REFERENCES grupo(id),
	FOREIGN KEY (permissao_id) REFERENCES permissao(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;