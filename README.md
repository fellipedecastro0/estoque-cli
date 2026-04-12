# Estoque CLI

[![CI](https://github.com/fellipedecastro0/estoque-cli/actions/workflows/ci.yml/badge.svg)](https://github.com/fellipedecastro0/estoque-cli/actions/workflows/ci.yml)

## Sobre o projeto

Aplicação em linha de comando para **controle simples de estoque**, pensada para **microempreendedores** que perdem tempo e dinheiro por não registrar quantidades, preços mínimos de reposição e itens em falta.

**Problema:** falta de um registro acessível (sem sistema caro) faz com que compras e vendas fiquem no improviso.

**Solução:** cadastrar produtos com nome, quantidade, preço e quantidade mínima; listar, buscar, atualizar, remover e destacar itens com estoque baixo, com dados salvos em arquivo JSON entre uma execução e outra.

**Público-alvo:** pequenos negócios, vendedores autônomos e MEIs que precisam de controle básico no próprio computador.

## Funcionalidades

1. Adicionar produto (nome, quantidade, preço, quantidade mínima para alerta)
2. Listar todos os produtos
3. Atualizar quantidade em estoque
4. Remover produto
5. Buscar produto por nome (trecho, sem diferenciar maiúsculas)
6. Listar produtos com estoque baixo (quantidade menor ou igual ao mínimo)
7. <img width="657" height="843" alt="image" src="https://github.com/user-attachments/assets/18811697-248c-4018-98cd-22610acbaabd" />



## Tecnologias

- Java 17
- Apache Maven
- Gson (persistência JSON)
- JUnit 5 (testes)
- Checkstyle (análise estática)
- GitHub Actions (CI)

## Versão atual

**1.0.0** (definida no `pom.xml` do módulo `estoque-cli`).

## Pré-requisitos

- [JDK 17](https://adoptium.net/) ou superior
- [Apache Maven 3.8+](https://maven.apache.org/)

## Como instalar e executar

Clone o repositório e entre na pasta do **módulo Maven** (onde está o `pom.xml`). O repositório tem essa pasta aninhada com o mesmo nome do projeto:

```bash
git clone https://github.com/fellipedecastro0/estoque-cli.git
cd estoque-cli    # pasta criada pelo clone (raiz do repositório)
cd estoque-cli    # módulo Maven com pom.xml, src/, dados/
```

Gere o JAR e execute (o programa grava em `dados/produtos.json`; o diretório atual deve ser a pasta interna `estoque-cli` com o `pom.xml`):

```bash
mvn package -DskipTests
java -jar target/estoque-cli-1.0.0.jar
```

## Como rodar os testes

Na pasta `estoque-cli`:

```bash
mvn test
```

## Como rodar o lint (Checkstyle)

Na pasta `estoque-cli`:

```bash
mvn checkstyle:check
```

## Estrutura do projeto

```
.
├── .github/
│   └── workflows/
│       └── ci.yml
├── CHANGELOG.md
├── README.md
└── estoque-cli/
    ├── dados/
    ├── pom.xml
    ├── checkstyle.xml
    └── src/
        ├── main/java/com/estoque/
        └── test/java/com/estoque/
```

## CI e repositório

- Repositório **público**: [fellipedecastro0/estoque-cli](https://github.com/fellipedecastro0/estoque-cli)
- Integração contínua: workflow [CI](https://github.com/fellipedecastro0/estoque-cli/actions/workflows/ci.yml) (Checkstyle, testes e build). Após cada alteração, confira na aba Actions se o último run no branch `main` concluiu com sucesso.



## Autor

Fellipe de Castro

## Repositório

https://github.com/fellipedecastro0/estoque-cli
