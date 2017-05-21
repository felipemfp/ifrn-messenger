package br.com.postero.ifrnmessenger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import br.com.postero.ifrnmessenger.R;
import br.com.postero.ifrnmessenger.utils.AppController;
import br.com.postero.ifrnmessenger.utils.P;
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


    public void onLoginClick(View v) {
        final HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("username", txtMatricula.getText().toString());
        hashMap.put("password", txtSenha.getText().toString());

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
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Show errors
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }
}
