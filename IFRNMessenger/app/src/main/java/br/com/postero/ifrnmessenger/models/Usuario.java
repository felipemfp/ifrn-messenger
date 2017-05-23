package br.com.postero.ifrnmessenger.models;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import br.com.postero.ifrnmessenger.utils.AppController;
import br.com.postero.ifrnmessenger.utils.P;
import br.com.postero.ifrnmessenger.utils.SuapAPI;

/**
 * Created by Francisco on 21/05/2017.
 */

public class Usuario implements Serializable {
    public String id;
    public String matricula;
    public String nome_usual;
    public String email;
    public String url_foto_75x100;
    public String tipo_vinculo;
    public Aluno vinculo;

    public interface ApiListener {
        void onSuccess(Object object);

        void onError(VolleyError error);
    }

    public static void autenticar(String username, String password, final Usuario.ApiListener listener) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("username", username);
        hashMap.put("password", password);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                SuapAPI.URL_AUTH,
                new JSONObject(hashMap),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String token = null;
                        try {
                            token = response.getString("token");
                            P.set("token", token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        listener.onSuccess(token);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                listener.onError(error);
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }

    public static void meusDados(final Usuario.ApiListener listener) {
        JsonObjectRequest request = new JsonObjectRequest(
                SuapAPI.URL_MEUS_DADOS,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Type type = new TypeToken<Usuario>() {
                        }.getType();
                        String result = response.toString();

                        Usuario usuario = new Gson().fromJson(result, type);
                        listener.onSuccess(usuario);
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
