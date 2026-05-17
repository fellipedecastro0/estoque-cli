import {
  fetchUsdBrlQuote,
  parseUsdBrlAwesomeResponse,
  USD_BRL_AWESOME_URL,
} from "../cotacaoApi.js";

describe("AwesomeAPI USD-BRL (fetch mock)", () => {
  const originalFetch = global.fetch;

  afterEach(() => {
    global.fetch = originalFetch;
    jest.resetAllMocks();
  });

  test("parseUsdBrlAwesomeResponse extrai bid, ask e varBid", () => {
    const data = {
      USDBRL: {
        bid: "5.1234",
        ask: "5.124",
        varBid: "-0.0123",
        create_date: "2026-05-17 10:00:00",
      },
    };
    const resultado = parseUsdBrlAwesomeResponse(data);
    expect(resultado.ok).toBe(true);
    if (resultado.ok) {
      expect(resultado.bid).toBe("5.1234");
      expect(resultado.ask).toBe("5.124");
      expect(resultado.varBid).toBe("-0.0123");
      expect(resultado.createDate).toBe("2026-05-17 10:00:00");
    }
  });

  test("fetchUsdBrlQuote retorna dados quando HTTP 200 e JSON valido", async () => {
    global.fetch = jest.fn(async () => ({
      ok: true,
      status: 200,
      json: async () => ({
        USDBRL: {
          bid: "5.1",
          ask: "5.2",
          varBid: "0.01",
          create_date: "2026-05-17 10:00:00",
        },
      }),
    }));

    const resultado = await fetchUsdBrlQuote({ fetchImpl: global.fetch });
    expect(resultado.ok).toBe(true);
    if (resultado.ok) {
      expect(resultado.bid).toBe("5.1");
      expect(resultado.ask).toBe("5.2");
      expect(resultado.varBid).toBe("0.01");
      expect(resultado.fetchedAtIso).toBeTruthy();
    }
    expect(global.fetch).toHaveBeenCalledWith(USD_BRL_AWESOME_URL, { method: "GET" });
  });

  test("fetchUsdBrlQuote trata HTTP nao OK sem lancar excecao", async () => {
    global.fetch = jest.fn(async () => ({
      ok: false,
      status: 502,
      json: async () => ({}),
    }));

    const resultado = await fetchUsdBrlQuote({ fetchImpl: global.fetch });
    expect(resultado.ok).toBe(false);
    if (!resultado.ok) {
      expect(resultado.error).toContain("502");
    }
  });

  test("fetchUsdBrlQuote trata falha de rede sem lancar excecao nao capturada", async () => {
    global.fetch = jest.fn(async () => {
      throw new Error("falha simulada");
    });

    const resultado = await fetchUsdBrlQuote({ fetchImpl: global.fetch });
    expect(resultado.ok).toBe(false);
    if (!resultado.ok) {
      expect(resultado.error).toBe("falha simulada");
    }
  });
});
