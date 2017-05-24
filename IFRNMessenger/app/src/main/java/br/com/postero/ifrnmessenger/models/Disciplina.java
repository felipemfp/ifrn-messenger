package br.com.postero.ifrnmessenger.models;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.postero.ifrnmessenger.utils.AppController;
import br.com.postero.ifrnmessenger.utils.SuapAPI;

/**
 * Created by Francisco on 21/05/2017.
 */

public class Disciplina extends SugarRecord implements Serializable {

    public String codigo;
    public String nome;
    public int faltas;
    public String situacao;

    public PeriodoLetivo periodoLetivo;

    @Override
    public String toString() {
        String[] res = this.nome.split(" - ");
        return res.length > 1 ? res[1] : this.nome;
    }

    public interface ApiListener {
        void onSuccess(Object object);

        void onError(VolleyError error);
    }

    public static void listarTodos(final PeriodoLetivo periodoLetivo, final ApiListener listener) {
        int ano, periodo;
        if (periodoLetivo != null) {
            ano = periodoLetivo.ano;
            periodo = periodoLetivo.periodo;

            List<Disciplina> disciplinas = Disciplina.find(Disciplina.class, "periodo_letivo = ?", periodoLetivo.getId().toString());

            if (disciplinas.size() > 0) {
                Log.i("Achou disciplinas!", disciplinas.size() + "");
                listener.onSuccess(disciplinas);
                return;
            }
        } else {
            Calendar calendar = Calendar.getInstance();
            ano = calendar.get(Calendar.YEAR);
            periodo = calendar.get(Calendar.MONTH) > 6 ? 2 : 1;
        }

        StringRequest request = new StringRequest(
                SuapAPI.URL_BOLETIM + ano + "/" + periodo + "/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<Disciplina> disciplinas = new ArrayList<>();
                        JsonArray object = new Gson().fromJson(response, JsonArray.class);
                        Iterator<JsonElement> iterator = object.iterator();
                        while (iterator.hasNext()) {
                            JsonObject element = iterator.next().getAsJsonObject();
                            Disciplina disciplina = new Disciplina();
                            disciplina.codigo = element.get("codigo_diario").getAsString();
                            disciplina.nome = element.get("disciplina").getAsString();
                            disciplina.faltas = element.get("numero_faltas").getAsInt();
                            disciplina.situacao = element.get("situacao").getAsString();
                            disciplina.periodoLetivo = periodoLetivo;
                            disciplina.save();
                            disciplinas.add(disciplina);
                            Log.i("Disciplina ", disciplina.nome);
                        }

                        listener.onSuccess(disciplinas);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                listener.onError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "JWT " + Usuario.first(Usuario.class).token);
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(request);
    }
}
