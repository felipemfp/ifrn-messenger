package br.com.postero.ifrnmessenger.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import br.com.postero.ifrnmessenger.models.Usuario;

/**
 * Created by Francisco on 21/05/2017.
 */

public class P {

    public static final String PREF_KEY = "br.com.postero.ifrnmessenger.shared_preferences";

    public static final String USUARIO_JSON = "shared_preference_usuario_json";

    public static final String USUARIO_ID = "shared_preference_usuario_id";
    public static final String USUARIO_MATRICULA = "shared_preference_usuario_matricula";
    public static final String USUARIO_NOME_USUAL = "shared_preference_usuario_nome_usual";
    public static final String USUARIO_EMAIL = "shared_preference_usuario_email";
    public static final String USUARIO_URL_FOTO_75X100 = "shared_preference_usuario_url_foto_75x100";
    public static final String USUARIO_TIPO_VINCULO = "shared_preference_usuario_tipo_vinculo";

    public static final String ALUNO_MATRICULA = "shared_preference_aluno_matricula";
    public static final String ALUNO_NOME = "shared_preference_aluno_nome";
    public static final String ALUNO_CURSO = "shared_preference_aluno_curso";
    public static final String ALUNO_CAMPUS = "shared_preference_aluno_campus";
    public static final String ALUNO_SITUACAO = "shared_preference_aluno_situacao";
    public static final String ALUNO_COTA_SISTEC = "shared_preference_aluno_cota_sistec";
    public static final String ALUNO_COTA_MEC = "shared_preference_aluno_cota_mec";
    public static final String ALUNO_SITUACAO_SISTEMICA = "shared_preference_aluno_situacao_sistemica";

    public static Usuario usuario = null;

    public static SharedPreferences prefs = AppController.getContext().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);

    public static Usuario getUsuario() {
        if (usuario == null) {
            usuario = new Gson().fromJson(P.get(USUARIO_JSON), Usuario.class);
        }
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        P.usuario = usuario;
        prefs.edit()
                .putString(USUARIO_ID, usuario.id)
                .putString(USUARIO_MATRICULA, usuario.matricula)
                .putString(USUARIO_NOME_USUAL, usuario.nome_usual)
                .putString(USUARIO_EMAIL, usuario.email)
                .putString(USUARIO_URL_FOTO_75X100, usuario.url_foto_75x100)
                .putString(USUARIO_TIPO_VINCULO, usuario.tipo_vinculo)
                .putString(ALUNO_MATRICULA, usuario.vinculo.matricula)
                .putString(ALUNO_NOME, usuario.vinculo.nome)
                .putString(ALUNO_CURSO, usuario.vinculo.curso)
                .putString(ALUNO_CAMPUS, usuario.vinculo.campus)
                .putString(ALUNO_SITUACAO, usuario.vinculo.situacao)
                .putString(ALUNO_COTA_SISTEC, usuario.vinculo.cota_sistec)
                .putString(ALUNO_COTA_MEC, usuario.vinculo.cota_mec)
                .putString(ALUNO_SITUACAO_SISTEMICA, usuario.vinculo.situacao_sistemica)
                .putString(USUARIO_JSON, new Gson().toJson(usuario))
                .apply();
    }

    public static void set(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }

    public static String get(String key) {
        return P.get(key, null);
    }

    public static String get(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }

    public static void limpar() {
        usuario = null;
        prefs.edit().clear().apply();
    }
}
