/**
 * Cliente mínimo para a AwesomeAPI (cotação USD-BRL).
 * Documentação: https://docs.awesomeapi.com.br/api-de-moedas
 */
export const USD_BRL_AWESOME_URL =
  "https://economia.awesomeapi.com.br/json/last/USD-BRL";

/**
 * Interpreta o JSON retornado pela AwesomeAPI para USD-BRL.
 * @param {unknown} data
 * @returns {{ ok: true, bid: string, ask: string, varBid: string, createDate: string } | { ok: false, error: string }}
 */
export function parseUsdBrlAwesomeResponse(data) {
  if (data === null || typeof data !== "object") {
    return { ok: false, error: "resposta invalida" };
  }
  const row = data.USDBRL;
  if (row === null || typeof row !== "object") {
    return { ok: false, error: "campo USDBRL ausente" };
  }
  const bid = row.bid;
  const ask = row.ask;
  const varBid = row.varBid;
  if (typeof bid !== "string" || typeof ask !== "string" || typeof varBid !== "string") {
    return { ok: false, error: "campos bid/ask/varBid invalidos" };
  }
  const createDate =
    typeof row.create_date === "string"
      ? row.create_date
      : typeof row.createDate === "string"
        ? row.createDate
        : "";
  return { ok: true, bid, ask, varBid, createDate };
}

/**
 * Busca cotação USD-BRL na AwesomeAPI.
 * @param {object} [options]
 * @param {typeof fetch} [options.fetchImpl]
 * @param {string} [options.url]
 * @returns {Promise<{ ok: true, bid: string, ask: string, varBid: string, createDate: string, fetchedAtIso: string } | { ok: false, error: string }>}
 */
export async function fetchUsdBrlQuote(options = {}) {
  const fetchImpl = options.fetchImpl ?? globalThis.fetch;
  const url = options.url ?? USD_BRL_AWESOME_URL;
  if (typeof fetchImpl !== "function") {
    return { ok: false, error: "fetch indisponivel" };
  }
  try {
    const response = await fetchImpl(url, { method: "GET" });
    if (!response.ok) {
      return { ok: false, error: `http ${response.status}` };
    }
    const data = await response.json();
    const parsed = parseUsdBrlAwesomeResponse(data);
    if (!parsed.ok) {
      return parsed;
    }
    return {
      ok: true,
      bid: parsed.bid,
      ask: parsed.ask,
      varBid: parsed.varBid,
      createDate: parsed.createDate,
      fetchedAtIso: new Date().toISOString(),
    };
  } catch (e) {
    const message = e instanceof Error ? e.message : "erro de rede";
    return { ok: false, error: message };
  }
}
