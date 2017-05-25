package br.com.postero.ifrnmessenger.models;

import java.util.Date;

/**
 * Created by Francisco on 25/05/2017.
 */

public class Mensagem {
    public long tempo;
    public Usuario usuario;
    public Disciplina disciplina;
    public String conteudo;

    public Mensagem() {
        tempo = new Date().getTime();
    }

    public void atualizarTempo() {
        this.tempo = new Date().getTime();
    }
}
