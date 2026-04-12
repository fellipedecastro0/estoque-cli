# Changelog

## [1.1.0] - 2026-05-17

### Adicionado

- Interface web estática em `frontend/` (cadastro, listagem, busca, atualização, remoção; estoque baixo destacado)
- Integração com AwesomeAPI para cotação USD-BRL (bid, ask, varBid)
- Testes de integração no frontend (Jest + mock de `fetch`)
- Deploy automático no GitHub Pages (conteúdo da pasta `frontend/`)
- Job de CI para `frontend` (`npm ci` + `npm test`)

### Alterado

- Versão do artefato Maven para 1.1.0

## [1.0.0] - 2026-04-11

### Adicionado

- Cadastro de produtos com nome, quantidade, preço e quantidade mínima
- Listagem de todos os produtos
- Atualização de quantidade em estoque
- Remoção de produtos
- Busca por nome (parcial, sem diferenciar maiúsculas)
- Alerta de estoque baixo
- Persistência em arquivo JSON (`dados/produtos.json`)
- Interface CLI interativa
- Testes automatizados com JUnit 5
- Análise estática com Checkstyle
- Pipeline de CI com GitHub Actions (Checkstyle, testes e build)
