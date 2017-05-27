package br.com.postero.ifrnmessenger.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.postero.ifrnmessenger.R;
import br.com.postero.ifrnmessenger.models.Disciplina;
import br.com.postero.ifrnmessenger.models.Mensagem;
import br.com.postero.ifrnmessenger.models.Usuario;

public class DisciplinaActivity extends AppCompatActivity {

    private Usuario usuario;
    private Disciplina disciplina;
    private Mensagem mensagem;
    private Mensagem ultimaMensagem;

    private EditText txtMensagem;
    private Toolbar toolbar;
    private ListView listMessages;

    private FirebaseDatabase db;
    private DatabaseReference chat;

    private FirebaseListAdapter<Mensagem> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.vincularElementos();
        this.carregarMensagens();
    }

    private void vincularElementos() {
        setContentView(R.layout.activity_disciplina);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton btnSend = (ImageButton)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendClick(view);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        disciplina = (Disciplina) getIntent().getSerializableExtra("disciplina");
        usuario = Usuario.first(Usuario.class);
        db = FirebaseDatabase.getInstance();
        chat = db.getReference(disciplina.codigo);

        mensagem = new Mensagem();
        mensagem.usuario = usuario;
        mensagem.disciplina = disciplina;

        txtMensagem = (EditText)findViewById(R.id.txtMensagem);

        this.setTitle(disciplina.toString());
        //toolbar.setSubtitle("Última mensagem às 12h15...");
    }

    private void onSendClick(View v) {
        String conteudo = txtMensagem.getText().toString().trim();

        if (conteudo.isEmpty()) {
            return;
        }
        mensagem.atualizarTempo();
        mensagem.conteudo = conteudo;
        chat.push().setValue(mensagem);

        txtMensagem.setText("");
    }

    private void carregarMensagens() {
        listMessages = (ListView) findViewById(R.id.listMessages);
        adapter = new FirebaseListAdapter<Mensagem>(this, Mensagem.class,
                R.layout.disciplina_menssagem, chat) {
            @Override
            protected void populateView(View v, Mensagem model, int position) {
                RelativeLayout layBalao = (RelativeLayout) v.findViewById(R.id.layBalao);
                TextView lblMensagemConteudo = (TextView) v.findViewById(R.id.lblMensagemConteudo);
                TextView lblMensagemUsuario = (TextView) v.findViewById(R.id.lblMensagemUsuario);
                TextView lblMensagemTempo = (TextView) v.findViewById(R.id.lblMensagemTempo);

                if (model.usuario.matricula.equals(usuario.matricula)) {
                    layBalao.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.my_message_background));
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layBalao.getLayoutParams();
                    if (marginLayoutParams.rightMargin > marginLayoutParams.leftMargin) {
                        marginLayoutParams.setMargins(marginLayoutParams.rightMargin, marginLayoutParams.topMargin, marginLayoutParams.leftMargin, marginLayoutParams.bottomMargin);
                    }
                }

                lblMensagemConteudo.setText(model.conteudo);
                lblMensagemUsuario.setText(model.usuario.nome_usual);
                lblMensagemTempo.setText(DateFormat.format("HH:mm", model.tempo));

                if (ultimaMensagem == null) {
                    ultimaMensagem = model;
                } else if (model.tempo > ultimaMensagem.tempo) {
                    ultimaMensagem = model;
                }

                toolbar.setSubtitle("Última mensagem às " + DateFormat.format("HH:mm", ultimaMensagem.tempo));
            }
        };



        listMessages.setAdapter(adapter);
    }

}
