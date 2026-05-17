import { fetchUsdBrlQuote } from "./cotacaoApi.js";

const STORAGE_KEY = "estoque-cli-produtos";

/** @type {Array<{ id: string, nome: string, quantidade: number, preco: number, quantidadeMinima: number }>} */
let produtos = [];

function carregarDoLocalStorage() {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) {
      return [];
    }
    const parsed = JSON.parse(raw);
    if (!Array.isArray(parsed)) {
      return [];
    }
    return parsed.filter(
      (p) =>
        p &&
        typeof p.id === "string" &&
        typeof p.nome === "string" &&
        typeof p.quantidade === "number" &&
        typeof p.preco === "number" &&
        typeof p.quantidadeMinima === "number",
    );
  } catch {
    return [];
  }
}

function salvarNoLocalStorage() {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(produtos));
}

function proximoId() {
  let max = 0;
  for (const p of produtos) {
    const n = Number.parseInt(String(p.id).trim(), 10);
    if (!Number.isNaN(n) && n > max) {
      max = n;
    }
  }
  return String(max + 1);
}

/**
 * @param {string} nome
 * @param {number} quantidade
 * @param {number} preco
 * @param {number} quantidadeMinima
 * @returns {{ ok: true } | { ok: false, message: string }}
 */
function validarFormulario(nome, quantidade, preco, quantidadeMinima) {
  if (nome === null || nome.trim() === "") {
    return { ok: false, message: "Nome invalido." };
  }
  if (!Number.isFinite(quantidade) || quantidade < 0) {
    return { ok: false, message: "Quantidade invalida (use um numero >= 0)." };
  }
  if (!Number.isFinite(preco) || preco < 0) {
    return { ok: false, message: "Preco invalido (use um numero >= 0)." };
  }
  if (!Number.isFinite(quantidadeMinima) || quantidadeMinima < 0) {
    return { ok: false, message: "Quantidade minima invalida (use um numero >= 0)." };
  }
  return { ok: true };
}

/**
 * @param {Array<{ id: string, nome: string, quantidade: number, preco: number, quantidadeMinima: number }>} itens
 * @param {string} trecho
 */
function filtrarPorNome(itens, trecho) {
  if (trecho === null || trecho.trim() === "") {
    return [...itens];
  }
  const t = trecho.trim().toLowerCase();
  return itens.filter((p) => p.nome.toLowerCase().includes(t));
}

/**
 * @param {Array<{ id: string, nome: string, quantidade: number, preco: number, quantidadeMinima: number }>} itensParaExibir
 */
function renderizarTabela(itensParaExibir) {
  const tbody = document.querySelector("#tabela-produtos");
  if (!tbody) {
    return;
  }
  tbody.replaceChildren();
  if (itensParaExibir.length === 0) {
    const tr = document.createElement("tr");
    const td = document.createElement("td");
    td.colSpan = 5;
    td.className = "cell-empty";
    td.textContent = "Nenhum produto para exibir.";
    tr.appendChild(td);
    tbody.appendChild(tr);
    return;
  }
  for (const p of itensParaExibir) {
    const tr = document.createElement("tr");
    if (p.quantidade <= p.quantidadeMinima) {
      tr.classList.add("row-low");
    }
    const tdNome = document.createElement("td");
    tdNome.textContent = p.nome;
    if (p.quantidade <= p.quantidadeMinima) {
      const badge = document.createElement("span");
      badge.className = "badge-low";
      badge.textContent = "Estoque baixo";
      tdNome.appendChild(document.createTextNode(" "));
      tdNome.appendChild(badge);
    }
    const tdQtd = document.createElement("td");
    tdQtd.textContent = String(p.quantidade);
    const tdPreco = document.createElement("td");
    tdPreco.textContent = formatarPreco(p.preco);
    const tdMin = document.createElement("td");
    tdMin.textContent = String(p.quantidadeMinima);
    const tdAcoes = document.createElement("td");
    tdAcoes.className = "cell-actions";

    const wrap = document.createElement("div");
    wrap.className = "actions";

    const inp = document.createElement("input");
    inp.type = "number";
    inp.min = "0";
    inp.className = "input-qtd";
    inp.setAttribute("aria-label", `Nova quantidade para ${p.nome}`);
    inp.placeholder = "Nova qtd";

    const btnAtualizar = document.createElement("button");
    btnAtualizar.type = "button";
    btnAtualizar.className = "btn btn-secondary";
    btnAtualizar.textContent = "Atualizar";
    btnAtualizar.addEventListener("click", () => {
      const q = Number.parseInt(inp.value.trim(), 10);
      atualizarQuantidade(p.id, q);
    });

    const btnRemover = document.createElement("button");
    btnRemover.type = "button";
    btnRemover.className = "btn btn-danger";
    btnRemover.textContent = "Remover";
    btnRemover.addEventListener("click", () => removerProduto(p.id));

    wrap.appendChild(inp);
    wrap.appendChild(btnAtualizar);
    wrap.appendChild(btnRemover);
    tdAcoes.appendChild(wrap);

    tr.appendChild(tdNome);
    tr.appendChild(tdQtd);
    tr.appendChild(tdPreco);
    tr.appendChild(tdMin);
    tr.appendChild(tdAcoes);
    tbody.appendChild(tr);
  }
}

function formatarPreco(n) {
  return n.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
}

function obterTrechoBusca() {
  const input = document.querySelector("#busca");
  return input ? input.value : "";
}

function atualizarVisao() {
  const lista = filtrarPorNome(produtos, obterTrechoBusca());
  renderizarTabela(lista);
}

function adicionarProduto(nome, quantidade, preco, quantidadeMinima) {
  const v = validarFormulario(nome, quantidade, preco, quantidadeMinima);
  if (!v.ok) {
    mostrarStatusFormulario(v.message, true);
    return;
  }
  produtos.push({
    id: proximoId(),
    nome: nome.trim(),
    quantidade,
    preco,
    quantidadeMinima,
  });
  salvarNoLocalStorage();
  mostrarStatusFormulario("Produto adicionado.", false);
  limparFormulario();
  atualizarVisao();
}

function atualizarQuantidade(id, novaQuantidade) {
  if (!Number.isFinite(novaQuantidade) || novaQuantidade < 0) {
    mostrarStatusFormulario("Quantidade invalida.", true);
    return;
  }
  const p = produtos.find((x) => x.id === id);
  if (!p) {
    mostrarStatusFormulario("Produto nao encontrado.", true);
    return;
  }
  p.quantidade = novaQuantidade;
  salvarNoLocalStorage();
  mostrarStatusFormulario("Quantidade atualizada.", false);
  atualizarVisao();
}

function removerProduto(id) {
  const antes = produtos.length;
  produtos = produtos.filter((x) => x.id !== id);
  if (produtos.length === antes) {
    mostrarStatusFormulario("Produto nao encontrado.", true);
    return;
  }
  salvarNoLocalStorage();
  mostrarStatusFormulario("Produto removido.", false);
  atualizarVisao();
}

function limparFormulario() {
  const f = document.querySelector("#form-produto");
  if (f && f instanceof HTMLFormElement) {
    f.reset();
  }
}

function mostrarStatusFormulario(msg, isError) {
  const el = document.querySelector("#form-status");
  if (!el) {
    return;
  }
  el.textContent = msg;
  el.classList.toggle("is-error", Boolean(isError));
}

function atualizarUiCotacao(resultado) {
  const bidEl = document.querySelector("#cotacao-bid");
  const askEl = document.querySelector("#cotacao-ask");
  const varEl = document.querySelector("#cotacao-var");
  const horaEl = document.querySelector("#cotacao-atualizado");
  const errEl = document.querySelector("#cotacao-erro");

  if (!bidEl || !askEl || !varEl || !horaEl || !errEl) {
    return;
  }

  errEl.textContent = "";
  errEl.hidden = true;

  if (!resultado.ok) {
    bidEl.textContent = "—";
    askEl.textContent = "—";
    varEl.textContent = "—";
    horaEl.textContent = "";
    errEl.textContent = `Nao foi possivel carregar a cotacao: ${resultado.error}`;
    errEl.hidden = false;
    return;
  }

  bidEl.textContent = resultado.bid;
  askEl.textContent = resultado.ask;
  varEl.textContent = resultado.varBid;
  const horario =
    resultado.createDate && resultado.createDate.length > 0
      ? resultado.createDate
      : new Date(resultado.fetchedAtIso).toLocaleString("pt-BR");
  horaEl.textContent = `Ultima atualizacao: ${horario}`;
}

async function buscarECarregarCotacao() {
  const resultado = await fetchUsdBrlQuote();
  atualizarUiCotacao(resultado);
  return resultado;
}

function configurarEventos() {
  const form = document.querySelector("#form-produto");
  if (form && form instanceof HTMLFormElement) {
    form.addEventListener("submit", (e) => {
      e.preventDefault();
      const nome = document.querySelector("#nome")?.value ?? "";
      const qtdRaw = document.querySelector("#quantidade")?.value ?? "";
      const precoRaw = document.querySelector("#preco")?.value ?? "";
      const minRaw = document.querySelector("#quantidade-minima")?.value ?? "";
      const quantidade = Number.parseInt(String(qtdRaw).trim(), 10);
      const preco = Number.parseFloat(String(precoRaw).trim().replace(",", "."));
      const quantidadeMinima = Number.parseInt(String(minRaw).trim(), 10);
      adicionarProduto(nome, quantidade, preco, quantidadeMinima);
    });
  }

  const busca = document.querySelector("#busca");
  if (busca) {
    busca.addEventListener("input", () => atualizarVisao());
  }

  const btnCotacao = document.querySelector("#btn-atualizar-cotacao");
  if (btnCotacao) {
    btnCotacao.addEventListener("click", () => {
      void buscarECarregarCotacao();
    });
  }
}

function iniciar() {
  produtos = carregarDoLocalStorage();
  configurarEventos();
  atualizarVisao();
  void buscarECarregarCotacao();
}

iniciar();
