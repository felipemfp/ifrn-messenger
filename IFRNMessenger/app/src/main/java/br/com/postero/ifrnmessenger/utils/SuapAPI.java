package br.com.postero.ifrnmessenger.utils;

/**
 * Created by Francisco on 21/05/2017.
 */

public class SuapAPI {

    public static String URL_BASE = "https://suap.ifrn.edu.br/api/v2/";
    public static String URL_AUTH = URL_BASE + "autenticacao/token/";

    public static String URL_MEUS_DADOS = URL_BASE + "minhas-informacoes/meus-dados/";
    public static String URL_BOLETIM = URL_BASE + "minhas-informacoes/boletim/";
    public static String URL_PERIODOS_LETIVOS = URL_BASE + "minhas-informacoes/meus-periodos-letivos/";
}
