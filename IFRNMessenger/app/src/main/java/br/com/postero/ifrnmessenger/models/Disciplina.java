package br.com.postero.ifrnmessenger.models;

import android.util.ArrayMap;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import br.com.postero.ifrnmessenger.utils.AppController;
import br.com.postero.ifrnmessenger.utils.P;
import br.com.postero.ifrnmessenger.utils.SuapAPI;

/**
 * Created by Francisco on 21/05/2017.
 */

public class Disciplina implements Serializable {

    public String codigo_diario;
    public String disciplina;
    public int numero_faltas;
    public String situacao;

    public interface ApiListener {
        void onSuccess(Object object);

        void onError(VolleyError error);
    }

    public static void listarTodos(final ApiListener listener) {
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int periodo = calendar.get(Calendar.MONTH) > 6 ? 2 : 1;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                SuapAPI.URL_BOLETIM + ano + "/" + periodo + "/",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Type type = new TypeToken<ArrayList<Disciplina>>() {
                        }.getType();
                        String results = response.toString();

                        ArrayList<Disciplina> disciplinas = new Gson().fromJson(results, type);
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
                headers.put("Authorization", "JWT " + P.get("token"));
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(request);
    }
}
