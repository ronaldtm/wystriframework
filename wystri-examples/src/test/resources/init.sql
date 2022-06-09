CREATE TABLE usuario (
    id          BIGINT IDENTITY PRIMARY KEY,
    username    VARCHAR(255)    NOT NULL,
    nome        VARCHAR(255)    NOT NULL,
    ativo       BIT             NOT NULL
);
CREATE UNIQUE INDEX UQ_usuario ON usuario(username);

CREATE TABLE deputado (
    id                      BIGINT IDENTITY PRIMARY KEY,
    nome                    VARCHAR(255)    NOT NULL,
    id_analista_responsavel BIGINT              NULL,
    cpf                     VARCHAR(11)         NULL,
    conta_bancaria          VARCHAR(20)         NULL,
    numero_gabinete         INT                 NULL,
    ativo                   BIT             NOT NULL
);
ALTER TABLE deputado ADD CONSTRAINT FK_deputado_analista_responsavel FOREIGN KEY (id_analista_responsavel) REFERENCES usuario(id);
CREATE UNIQUE INDEX UQ_deputado ON deputado(nome);

CREATE TABLE unidade (
    id          BIGINT IDENTITY PRIMARY KEY,
    sigla       VARCHAR(20)         NULL,
    id_deputado BIGINT              NULL,
    ativo       BIT             NOT NULL
);
ALTER TABLE unidade ADD CONSTRAINT FK_unidade_deputado FOREIGN KEY (id_deputado) REFERENCES deputado (id);
CREATE UNIQUE INDEX UQ_unidade ON unidade(sigla, id_deputado);


CREATE TABLE perfil (
    id          BIGINT IDENTITY PRIMARY KEY,
    nome        VARCHAR(255)    NOT NULL,
    descricao   VARCHAR(255)        NULL,
    id_unidade  BIGINT              NULL,
    restrito    BIT             NOT NULL,
    ativo       BIT             NOT NULL
);
ALTER TABLE perfil ADD CONSTRAINT FK_perfil_unidade  FOREIGN KEY (id_unidade) REFERENCES unidade(id);
CREATE UNIQUE INDEX UQ_perfil ON perfil(nome, id_unidade);


CREATE TABLE papel (
    id    BIGINT        PRIMARY KEY,
    nome  VARCHAR(255)  NOT NULL
);
CREATE UNIQUE INDEX UQ_papel ON papel(nome);

CREATE TABLE perfil_papel (
    id_perfil   BIGINT,
    id_papel    BIGINT,
    CONSTRAINT PK_perfil_papel PRIMARY KEY (id_perfil, id_papel)
);
ALTER TABLE perfil_papel ADD CONSTRAINT FK_perfil_papel_perfil FOREIGN KEY (id_perfil) REFERENCES perfil (id);
ALTER TABLE perfil_papel ADD CONSTRAINT FK_perfil_papel_papel  FOREIGN KEY (id_papel ) REFERENCES papel (id);

CREATE TABLE credencial (
    id_usuario  BIGINT,
    id_unidade  BIGINT,
    id_perfil   BIGINT,
    CONSTRAINT PK_credencial PRIMARY KEY (id_usuario, id_unidade, id_perfil)
);
ALTER TABLE credencial ADD CONSTRAINT FK_credencial_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id);
ALTER TABLE credencial ADD CONSTRAINT FK_credencial_unidade FOREIGN KEY (id_unidade) REFERENCES unidade(id);
ALTER TABLE credencial ADD CONSTRAINT FK_credencial_perfil  FOREIGN KEY (id_perfil ) REFERENCES perfil (id);



CREATE TABLE classificacao (
    id        INT          PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL
);
CREATE UNIQUE INDEX UQ_classificacao ON classificacao(descricao);

CREATE TABLE situacao_pacote_nota (
    id        BIGINT       PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL
);
CREATE UNIQUE INDEX UQ_situacao_pacote_nota ON situacao_pacote_nota(descricao);

CREATE TABLE limite_verba (
    id            BIGINT IDENTITY PRIMARY KEY,
    limite_1      DECIMAL(19,2)   NOT NULL,
    limite_2      DECIMAL(19,2)   NOT NULL,
    limite_3      DECIMAL(19,2)   NOT NULL,
    limite_4      DECIMAL(19,2)   NOT NULL,
    limite_5      DECIMAL(19,2)   NOT NULL,
    limite_6      DECIMAL(19,2)   NOT NULL,
    limite_7      DECIMAL(19,2)   NOT NULL,
    limite_8      DECIMAL(19,2)   NOT NULL,
    limite_total  DECIMAL(19,2)   NOT NULL,
    data_inicial  DATE            NOT NULL
);
CREATE UNIQUE INDEX UQ_limite_verba ON limite_verba(data_inicial);








CREATE TABLE processo (
    id          BIGINT IDENTITY PRIMARY KEY,
    id_deputado BIGINT          NOT NULL,
    ano         INT             NOT NULL,
    numero      VARCHAR(255)    NOT NULL,
    ativo       BIT             NOT NULL
);
ALTER TABLE processo ADD CONSTRAINT FK_processo_deputado FOREIGN KEY (id_deputado) REFERENCES deputado(id);
CREATE UNIQUE INDEX UQ_processo ON processo(id_deputado, ano, numero);

CREATE TABLE contrato (
    id                    BIGINT IDENTITY PRIMARY KEY,
    id_deputado           BIGINT          NOT NULL,
    objeto                VARCHAR(4000)   NOT NULL,
    classificacao         INT             NOT NULL,
    cpf_cnpj              VARCHAR(14)     NOT NULL,
    nome_contratado       VARCHAR(255)    NOT NULL,
    valor                 DECIMAL(19,2)   NOT NULL,
    data_inicial_vigencia DATE            NOT NULL,
    data_final_vigencia   DATE            NOT NULL,
    id_contrato_aditivado BIGINT              NULL
);
ALTER TABLE contrato ADD CONSTRAINT FK_contrato_deputado      FOREIGN KEY (id_deputado          ) REFERENCES deputado(id);
ALTER TABLE contrato ADD CONSTRAINT FK_contrato_classificacao FOREIGN KEY (classificacao        ) REFERENCES classificacao(id);
ALTER TABLE contrato ADD CONSTRAINT FK_contrato_aditivado     FOREIGN KEY (id_contrato_aditivado) REFERENCES contrato(id);

CREATE TABLE pacote_nota (
    id              BIGINT IDENTITY PRIMARY KEY,
    id_deputado     BIGINT          NOT NULL,
    ano             INT             NOT NULL,
    mes             INT             NOT NULL,
    situacao        BIGINT          NOT NULL,
    id_limite_verba BIGINT              NULL
);
ALTER TABLE pacote_nota ADD CONSTRAINT FK_pacote_nota_deputado     FOREIGN KEY (id_deputado    ) REFERENCES deputado(id);
ALTER TABLE pacote_nota ADD CONSTRAINT FK_pacote_nota_situacao     FOREIGN KEY (situacao       ) REFERENCES situacao_pacote_nota(id);
ALTER TABLE pacote_nota ADD CONSTRAINT FK_pacote_nota_limite_verba FOREIGN KEY (id_limite_verba) REFERENCES limite_verba(id);
CREATE UNIQUE INDEX UQ_pacote_nota ON pacote_nota(id_deputado, ano, mes);

CREATE TABLE nota (
    id                             BIGINT IDENTITY  PRIMARY KEY,
    id_pacote                      BIGINT           NOT NULL,
    id_contrato                    BIGINT               NULL,
    nome_contratado                VARCHAR(255)         NULL,
    cpf_cnpj                       VARCHAR(14)          NULL,
    data_emissao                   DATE                 NULL,
    numero_documento               VARCHAR(50)          NULL,
    valor                          DECIMAL(19,2)        NULL,
    valor_utilizado_saldo_anterior DECIMAL(19,2)        NULL,
    classificacao                  INT              NOT NULL,
    glosa_valor                    DECIMAL(19,2)    NOT NULL DEFAULT 0,
    glosa_justificativa            VARCHAR(4000)        NULL,
    filename_comprovante           VARCHAR(255)         NULL,
    mimetype_comprovante           VARCHAR(100)         NULL,
    length_comprovante             BIGINT           NOT NULL DEFAULT 0,
    content_comprovante            VARBINARY(MAX)       NULL,
    filename_relatorio             VARCHAR(255)         NULL,
    mimetype_relatorio             VARCHAR(100)         NULL,
    length_relatorio               BIGINT           NOT NULL DEFAULT 0,
    content_relatorio              VARBINARY(MAX)       NULL
);
ALTER TABLE nota ADD CONSTRAINT FK_nota_pacote_nota   FOREIGN KEY (id_pacote    ) REFERENCES pacote_nota(id);
ALTER TABLE nota ADD CONSTRAINT FK_nota_contrato      FOREIGN KEY (id_contrato  ) REFERENCES contrato(id);
ALTER TABLE nota ADD CONSTRAINT FK_nota_classificacao FOREIGN KEY (classificacao) REFERENCES classificacao(id);


-- ############################################################################################################
-- ############################################################################################################
-- ############################################################################################################

INSERT INTO classificacao (id, descricao) VALUES (0, '-');
INSERT INTO classificacao (id, descricao) VALUES (1, 'Locação e manutenção de imóveis');
INSERT INTO classificacao (id, descricao) VALUES (2, 'Locação de bens móveis, máquinas e equipamentos');
INSERT INTO classificacao (id, descricao) VALUES (3, 'Aquisição de materiais');
INSERT INTO classificacao (id, descricao) VALUES (4, 'Locação de veículos');
INSERT INTO classificacao (id, descricao) VALUES (5, 'Combustíveis e lubrificantes');
INSERT INTO classificacao (id, descricao) VALUES (6, 'Assessoria / Consultoria Jurídica');
INSERT INTO classificacao (id, descricao) VALUES (7, 'Assessoria / Consultoria especializada');
INSERT INTO classificacao (id, descricao) VALUES (8, 'Divulgação de atividade parlamentar');

INSERT INTO situacao_pacote_nota (id, descricao) VALUES ( 1, 'Novo');
INSERT INTO situacao_pacote_nota (id, descricao) VALUES ( 2, 'Enviado ao NVI');
INSERT INTO situacao_pacote_nota (id, descricao) VALUES ( 3, 'Enviado à Mesa');
INSERT INTO situacao_pacote_nota (id, descricao) VALUES ( 4, 'Aprovado pela Mesa');
INSERT INTO situacao_pacote_nota (id, descricao) VALUES ( 9, 'Publicado');

INSERT INTO limite_verba (limite_1, limite_2, limite_3, limite_4, limite_5, limite_6, limite_7, limite_8, limite_total, data_inicial)
                  VALUES ( 6077.34,  6077.34,  6077.34,  6077.34,  6077.34,  6077.34,  7596.68,  7596.68,     15193.35, '2017-03-13');



INSERT INTO papel (id, nome) VALUES ( 1, 'CADASTRO_USUARIOS' );
INSERT INTO papel (id, nome) VALUES ( 2, 'CADASTRO_UNIDADES' );
INSERT INTO papel (id, nome) VALUES ( 3, 'CADASTRO_DEPUTADOS');
INSERT INTO papel (id, nome) VALUES ( 4, 'CADASTRO_NOTAS'    );
INSERT INTO papel (id, nome) VALUES ( 5, 'ANALISE_NOTAS'     );
INSERT INTO papel (id, nome) VALUES ( 6, 'CADASTRO_GLOSAS'   );
INSERT INTO papel (id, nome) VALUES ( 7, 'CADASTRO_CONTRATOS');
INSERT INTO papel (id, nome) VALUES ( 8, 'DADOS_DEPUTADO'    );
INSERT INTO papel (id, nome) VALUES ( 9, 'ATRIBUIR_ANALISTA' );
INSERT INTO papel (id, nome) VALUES (10, 'PARAMETRIZACAO'    );

INSERT INTO unidade (sigla, ativo) VALUES ('NVI' , 1); -- 1
INSERT INTO unidade (sigla, ativo) VALUES ('GMD' , 1); -- 2
INSERT INTO unidade (sigla, ativo) VALUES ('DOFC', 1); -- 3
INSERT INTO unidade (sigla, ativo) VALUES ('CPEO', 1); -- 4
INSERT INTO unidade (sigla, ativo) VALUES ('CMI' , 1); -- 5

INSERT INTO perfil (nome, descricao, id_unidade, restrito, ativo) VALUES ('Administrador'         , 'Gestor administrativo do sistema', 5   , 1, 1); -- 1
INSERT INTO perfil (nome, descricao, id_unidade, restrito, ativo) VALUES ('Chefe da unidade'      , 'Chefe da unidade'                , null, 0, 1); -- 2
INSERT INTO perfil (nome, descricao, id_unidade, restrito, ativo) VALUES ('Deputado/Representante', 'Deputado ou representante'       , null, 0, 1); -- 3
INSERT INTO perfil (nome, descricao, id_unidade, restrito, ativo) VALUES ('Analista NVI'          , 'Analista NVI'                    , 1   , 0, 1); -- 4
INSERT INTO perfil (nome, descricao, id_unidade, restrito, ativo) VALUES ('Analista GMD'          , 'Analista GMD'                    , 2   , 0, 1); -- 5
INSERT INTO perfil (nome, descricao, id_unidade, restrito, ativo) VALUES ('Analista DAF'          , 'Analista DAF'                    , 3   , 0, 1); -- 6
INSERT INTO perfil (nome, descricao, id_unidade, restrito, ativo) VALUES ('Analista CPEO'         , 'Analista CPEO'                   , 4   , 0, 1); -- 7

INSERT INTO perfil_papel (id_perfil, id_papel) VALUES (1,  1); -- Administrador              CADASTRO_USUARIOS
INSERT INTO perfil_papel (id_perfil, id_papel) VALUES (1,  2); -- Administrador              CADASTRO_UNIDADES
INSERT INTO perfil_papel (id_perfil, id_papel) VALUES (1,  3); -- Administrador              CADASTRO_DEPUTADOS
INSERT INTO perfil_papel (id_perfil, id_papel) VALUES (1,  9); -- Administrador              ATRIBUIR_ANALISTA
INSERT INTO perfil_papel (id_perfil, id_papel) VALUES (1, 10); -- Administrador              PARAMETRIZACAO
INSERT INTO perfil_papel (id_perfil, id_papel) VALUES (2,  1); -- Chefe da unidade           CADASTRO_USUARIOS
INSERT INTO perfil_papel (id_perfil, id_papel) VALUES (2,  8); -- Chefe da unidade           DADOS_DEPUTADO
INSERT INTO perfil_papel (id_perfil, id_papel) VALUES (3,  4); -- Deputado/Representante     CADASTRO_NOTAS
INSERT INTO perfil_papel (id_perfil, id_papel) VALUES (3,  7); -- Deputado/Representante     CADASTRO_CONTRATOS
INSERT INTO perfil_papel (id_perfil, id_papel) VALUES (4,  5); -- Analista NVI               ANALISE_NOTAS
INSERT INTO perfil_papel (id_perfil, id_papel) VALUES (4,  6); -- Analista NVI               CADASTRO_GLOSAS
INSERT INTO perfil_papel (id_perfil, id_papel) VALUES (5,  6); -- Analista GMD               CADASTRO_GLOSAS


-- ############################################################################################################
-- ############################################################################################################
-- ############################################################################################################


INSERT INTO usuario (username, nome, ativo) VALUES ('admin', 'Administrador', 1);

INSERT INTO credencial (id_usuario, id_unidade, id_perfil) VALUES (
    (SELECT id FROM usuario WHERE username = 'admin'),
    (SELECT id FROM unidade WHERE sigla    = 'CMI'),
    (SELECT id FROM perfil  WHERE nome     = 'Administrador')
);
