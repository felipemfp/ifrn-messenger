package br.com.postero.ifrnmessenger.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;

import br.com.postero.ifrnmessenger.R;
import br.com.postero.ifrnmessenger.models.Usuario;
import br.com.postero.ifrnmessenger.utils.P;

public class LoginActivity extends AppCompatActivity {

    private EditText txtMatricula;
    private EditText txtSenha;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtMatricula = (EditText) findViewById(R.id.txtLogin);
        txtSenha = (EditText) findViewById(R.id.txtSenha);

        txtSenha.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!txtMatricula.getText().toString().isEmpty() && !txtSenha.getText().toString().isEmpty()) {
                    onLoginClick(v);
                    return true;
                }
                return false;
            }

            ;
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void onLoginClick(View v) {
        String username = txtMatricula.getText().toString();
        String password = txtSenha.getText().toString();

        if (username.isEmpty()) {
            txtMatricula.setError(getString(R.string.login_error_credential));
            txtMatricula.requestFocus();
            return;
        }
        else if (password.isEmpty()) {
            txtSenha.setError(getString(R.string.login_error_password));
            txtSenha.requestFocus();
            return;
        }

        progressDialog.setMessage(getString(R.string.login_loading));
        progressDialog.show();

        Usuario.autenticar(username, password, new Usuario.ApiListener() {
            @Override
            public void onSuccess(Object object) {
                Usuario.meusDados(new Usuario.ApiListener() {
                    @Override
                    public void onSuccess(Object object) {
                        Usuario usuario = (Usuario) object;
                        P.setUsuario(usuario);
                        progressDialog.hide();
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }

                    @Override
                    public void onError(VolleyError error) {
                        progressDialog.hide();
                        showAlertError();
                    }
                });
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.hide();
                showAlertError();
            }
        });
    }

    private void showAlertError() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.alert_error_title)
                .setMessage(R.string.login_error)
                .setPositiveButton(R.string.alert_error_button,null)
                .create()
                .show();
    }
}
