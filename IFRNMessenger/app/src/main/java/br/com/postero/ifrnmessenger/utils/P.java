package br.com.postero.ifrnmessenger.utils;

import android.content.Context;
import android.content.SharedPreferences;

import br.com.postero.ifrnmessenger.models.Aluno;

/**
 * Created by Francisco on 21/05/2017.
 */

public class P {

    public static final String PREF_KEY = "br.com.postero.ifrnmessenger.shared_preferences";

    public static final String ALUNO_JSON = "shared_preference_aluno_json";
    public static final String ALUNO_MATRICULA = "shared_preference_aluno_matricula";
    public static final String ALUNO_NOME = "shared_preference_aluno_nome";
    public static final String ALUNO_CURSO = "shared_preference_aluno_curso";
    public static final String ALUNO_CAMPUS = "shared_preference_aluno_campus";
    public static final String ALUNO_SITUACAO = "shared_preference_aluno_situacao";
    public static final String ALUNO_COTA_SISTEC = "shared_preference_aluno_cota_sistec";
    public static final String ALUNO_COTA_MEC = "shared_preference_aluno_cota_mec";
    public static final String ALUNO_SITUACAO_SISTEMICA = "shared_preference_aluno_situacao_sistemica";

    public static Aluno aluno = null;

    public static SharedPreferences prefs = AppController.getContext().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);

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
        prefs.edit().clear().apply();
    }
}
