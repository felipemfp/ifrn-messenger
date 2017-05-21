package br.com.postero.ifrnmessenger.models;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import br.com.postero.ifrnmessenger.utils.SuapAPI;

/**
 * Created by Francisco on 21/05/2017.
 */

public class Curso implements Serializable {

    public String codigo;
    public String descricao;
    public String diretoria;
    public int carga_horaria;
    public String natureza_participacao;
    public Object eixo;
    public String modalidade;
    public String coordenador;
    public ArrayList<ComponentesCurriculares> componentesCurriculares;

    public interface ApiListener {
        void onSuccess(Curso curso);
        void onError(VolleyError error);
    }

//    public static void listarTodos(final ApiListener listener) {
//        JsonObjectRequest request = new JsonObjectRequest(
//                Request.Method.GET,
//                SuapAPI.URL_CURSOS,
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Movimentacao resposta = new Gson().fromJson(response.toString(), Movimentacao.class);
//                        Movimentacao.editar(movimentacao);
//                        listener.sucesso(resposta);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if (error instanceof NoConnectionError) {
//                    Movimentacao m = Movimentacao.editar(movimentacao);
//                    Requisicao.adicionar(new Requisicao(metodo, Requisicao.MOVIMENTACAO, new Gson().toJson(m)));
//                    listener.sucesso(m);
//                } else {
//                    listener.erro(error);
//                }
//            }
//        });
//        AppController.getInstance().addToRequestQueue(request);
//    }
}
