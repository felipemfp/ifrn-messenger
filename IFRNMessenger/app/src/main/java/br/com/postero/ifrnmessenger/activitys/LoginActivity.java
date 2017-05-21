package br.com.postero.ifrnmessenger.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.postero.ifrnmessenger.R;
import br.com.postero.ifrnmessenger.utils.AppController;
import br.com.postero.ifrnmessenger.utils.SuapAPI;

public class LoginActivity extends AppCompatActivity {

    private EditText txtMatricula;
    private EditText txtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtMatricula = (EditText) findViewById(R.id.txtLogin);
        txtSenha = (EditText) findViewById(R.id.txtSenha);
    }


    public void onLoginClick(View v){
        final HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("username", txtMatricula.getText().toString());
        hashMap.put("password", txtSenha.getText().toString());

        String url = SuapAPI.URL_AUTH;
        int metodo = Request.Method.POST;
        JSONObject obj = new JSONObject();
        try {
            obj.put("username", txtMatricula.getText().toString());
            obj.put("password", txtSenha.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Context context = this;

        Log.i("EIIIIIIIIIII", hashMap.toString() + " metodo " + metodo + " obj " + obj.toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                SuapAPI.URL_AUTH,
                obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String token = response.getString("token");


                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("token", token);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtMatricula.setText("2699");
            }
        });
        AppController.getInstance(this).addToRequestQueue(request);
    }
}
