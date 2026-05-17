# Estoque CLI

## Aplicação publicada (interface web)

**Hospedagem:** [GitHub Pages](https://pages.github.com/) (site estático público gerado pelo repositório).

**URL do deploy:** [https://fellipedecastro0.github.io/estoque-cli/](https://fellipedecastro0.github.io/estoque-cli/)

> O deploy roda automaticamente em todo **push na branch `main`**, pelo workflow [.github/workflows/pages.yml](.github/workflows/pages.yml), que envia o build da pasta `frontend` como artefato do Pages. Se a URL não abrir logo após o primeiro deploy, em **GitHub → Settings → Pages**, defina **Build and deployment → Source: GitHub Actions**. Consulte também a aba **Actions** no repositório (workflow **Deploy GitHub Pages**).

[![CI](https://github.com/fellipedecastro0/estoque-cli/actions/workflows/ci.yml/badge.svg)](https://github.com/fellipedecastro0/estoque-cli/actions/workflows/ci.yml)

## Sobre o projeto

Aplicação em linha de comando para **controle simples de estoque**, pensada para **microempreendedores** que perdem tempo e dinheiro por não registrar quantidades, preços mínimos de reposição e itens em falta.

**Problema:** falta de um registro acessível (sem sistema caro) faz com que compras e vendas fiquem no improviso.

**Solução:** cadastrar produtos com nome, quantidade, preço e quantidade mínima; listar, buscar, atualizar, remover e destacar itens com estoque baixo. O modo **CLI (Java)** persiste em arquivo JSON entre execuções; a **interface web** roda no navegador e usa `localStorage` (sem instalação), exibindo ainda dados de mercado vindos da internet (veja abaixo).

**Interface web vs CLI:** os dados **não** são compartilhados entre os dois modos. Use a web para acesso rápido e referência de câmbio; use a CLI no computador com Java para persistência em arquivo local como na etapa anterior.

**Público-alvo:** pequenos negócios, vendedores autônomos e MEIs que precisam de controle básico no próprio computador.

### Integração com API pública (AwesomeAPI)

A interface web complementa o controle de estoque com **cotação em tempo real do dólar (USD → BRL)** usando a [AwesomeAPI — API de moedas](https://docs.awesomeapi.com.br/api-de-moedas) (serviço gratuito, sem chave obrigatória para o cenário atual).

| Detalhe | Valor |
|--------|--------|
| Método | `GET` |
| Endpoint usado | `https://economia.awesomeapi.com.br/json/last/USD-BRL` |
| Dados exibidos na tela | Compra (**bid**), venda (**ask**), variação (**varBid**) e horário da cotação quando disponível |
| Implementação no código | Cliente e parser da resposta em `cotacaoApi.js`, junto aos arquivos HTML/CSS/JS publicados pelo workflow do GitHub Pages (pasta de origem definida em [.github/workflows/pages.yml](.github/workflows/pages.yml)) |

Ao carregar a página e ao clicar em **Atualizar**, o front faz a requisição, interpreta o JSON (`USDBRL`) e atualiza os campos na interface; falhas de rede ou resposta inválida são tratadas sem travar o restante da aplicação.

## Funcionalidades

### CLI (Java)

1. Adicionar produto (nome, quantidade, preço, quantidade mínima para alerta)
2. Listar todos os produtos
3. Atualizar quantidade em estoque
4. Remover produto
5. Buscar produto por nome (trecho, sem diferenciar maiúsculas)
6. Listar produtos com estoque baixo (quantidade menor ou igual ao mínimo)

### Web (`frontend/`)

1. Cadastrar, listar, buscar, atualizar quantidade e remover produtos no navegador
2. Destaque visual para estoque baixo (quantidade ≤ mínimo)
3. Cotação USD-BRL (bid, ask, varBid) ao abrir a página, com botão **Atualizar**

## Tecnologias

- Java 17 + Apache Maven + Gson + JUnit 5 + Checkstyle
- Interface web: HTML, CSS e JavaScript (ES modules), armazenamento em `localStorage`
- Cotação: AwesomeAPI (USD-BRL)
- Testes do front: Jest + Babel (com mock de `fetch`)
- GitHub Actions (CI Java + CI front; deploy Pages)

## Versão atual

**1.1.0** em conjunto:

- `pom.xml` do módulo `estoque-cli` (CLI / JAR)
- `frontend/package.json` (versão da interface web)

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
java -jar target/estoque-cli-1.1.0.jar
```

## Como rodar os testes

### Backend (Java)

Na pasta `estoque-cli`:

```bash
mvn test
```

### Frontend (Jest)

Na pasta `frontend`:

```bash
npm ci
npm test
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
│       ├── ci.yml
│       └── pages.yml
├── .gitignore
├── CHANGELOG.md
├── README.md
├── frontend/
│   ├── app.js
│   ├── babel.config.cjs
│   ├── cotacaoApi.js
│   ├── index.html
│   ├── jest.config.cjs
│   ├── package.json
│   ├── package-lock.json
│   ├── style.css
│   └── tests/
│       └── integration.test.js
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
- Integração contínua: workflow [CI](https://github.com/fellipedecastro0/estoque-cli/actions/workflows/ci.yml) (Java: Checkstyle, testes e build; **frontend**: `npm ci` + Jest).
- Deploy: workflow [Deploy GitHub Pages](https://github.com/fellipedecastro0/estoque-cli/actions/workflows/pages.yml) após push em `main`.

## Entrega acadêmica (PDF na plataforma)

O enunciado pede um PDF com identificação e link. Inclua, no mínimo:

- Nome completo do aluno
- Nome da disciplina
- Nome do projeto (**Estoque CLI**)
- Breve descrição da proposta
- Link do repositório público: https://github.com/fellipedecastro0/estoque-cli
- **Entrega intermediária:** link da aplicação publicada: https://fellipedecastro0.github.io/estoque-cli/

## Autor

Fellipe de Castro

## Repositório

https://github.com/fellipedecastro0/estoque-cli
