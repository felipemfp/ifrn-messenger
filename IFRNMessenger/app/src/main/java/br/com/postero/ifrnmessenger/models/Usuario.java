package br.com.postero.ifrnmessenger.models;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.orm.SugarRecord;
import com.orm.dsl.Unique;

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

public class Usuario extends SugarRecord implements Serializable {
    public String codigo;
    public String token;
    public String matricula;
    public String nome_usual;
    public String email;
    public String foto;
    public String nome;
    public String curso;
    public String campus;

    public Usuario() {

    }

    public interface ApiListener {
        void onSuccess(Object object);

        void onError(VolleyError error);
    }

    public static void autenticar(final String username, final String password, final Usuario.ApiListener listener) {
        final HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("username", username);
        hashMap.put("password", password);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                SuapAPI.URL_AUTH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = new Gson().fromJson(response, JsonObject.class);
                        String token = object.get("token").getAsString();
                        Usuario usuario = new Usuario();
                        usuario.matricula = username;
                        usuario.token = token;
                        Log.i("Usuario ID: ", "" + usuario.save());
                        listener.onSuccess(token);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                listener.onError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    public static void meusDados(final Usuario.ApiListener listener) {
        StringRequest request = new StringRequest(
                SuapAPI.URL_MEUS_DADOS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject aluno = new Gson().fromJson(response, JsonObject.class);
                        JsonObject vinculo = aluno.getAsJsonObject("vinculo");
                        Usuario usuario = Usuario.first(Usuario.class);

                        usuario.codigo = aluno.get("id").getAsString();
                        usuario.nome_usual = aluno.get("nome_usual").getAsString();
                        usuario.email = aluno.get("email").getAsString();
                        usuario.foto = aluno.get("url_foto_75x100").getAsString();
                        usuario.nome = vinculo.get("nome").getAsString();
                        usuario.curso = vinculo.get("curso").getAsString();
                        usuario.campus = vinculo.get("campus").getAsString();
                        usuario.save();

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
                headers.put("Authorization", "JWT " + Usuario.first(Usuario.class).token);
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}
