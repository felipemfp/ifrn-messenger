package br.com.postero.ifrnmessenger.models;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import br.com.postero.ifrnmessenger.utils.AppController;
import br.com.postero.ifrnmessenger.utils.P;
import br.com.postero.ifrnmessenger.utils.SuapAPI;

/**
 * Created by Francisco on 23/05/2017.
 */

public class PeriodoLetivo implements Serializable {
    public static String SEPARATOR = ".";
    public int ano;
    public int periodo;

    public interface ApiListener {
        void onSuccess(Object object);

        void onError(VolleyError error);
    }

    @Override
    public String toString() {
        return this.ano + PeriodoLetivo.SEPARATOR + this.periodo;
    }

    public static void listarTodos(final PeriodoLetivo.ApiListener listener) {

        StringRequest request = new StringRequest(
                SuapAPI.URL_PERIODOS_LETIVOS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Meus periodos letivos", response);
                        JsonArray object = new Gson().fromJson(response, JsonArray.class);
                        ArrayList<PeriodoLetivo> periodoLetivos = new ArrayList<>();
                        Iterator<JsonElement> iterator = object.iterator();
                        while (iterator.hasNext()) {
                            JsonObject element = iterator.next().getAsJsonObject();
                            PeriodoLetivo periodoLetivo = new PeriodoLetivo();
                            periodoLetivo.ano = element.get("ano_letivo").getAsInt();
                            periodoLetivo.periodo = element.get("periodo_letivo").getAsInt();
                            periodoLetivos.add(periodoLetivo);
                            Log.i("PeriodoLetivo", periodoLetivo.ano + " / " + periodoLetivo.periodo);
                        }
                        listener.onSuccess(periodoLetivos);
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
