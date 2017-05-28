package br.com.postero.ifrnmessenger.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
    private RecyclerView listMessages;
    private LinearLayoutManager listMessagesManager;

    private FirebaseDatabase db;
    private DatabaseReference chat;

    private FirebaseRecyclerAdapter<Mensagem, MensagemViewHolder> adapter;

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

        listMessages = (RecyclerView) findViewById(R.id.listMessages);
        listMessagesManager = new LinearLayoutManager(this);
        listMessagesManager.setStackFromEnd(true);
        listMessages.setLayoutManager(listMessagesManager);

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

        listMessages.post(new Runnable() {
            @Override
            public void run() {
                listMessages.smoothScrollToPosition(adapter.getItemCount() - 1);
            }
        });
    }

    private void carregarMensagens() {

        adapter = new FirebaseRecyclerAdapter<Mensagem, MensagemViewHolder>(
                Mensagem.class,
                R.layout.disciplina_menssagem,
                MensagemViewHolder.class,
                chat
        ) {

            @Override
            protected void populateViewHolder(MensagemViewHolder viewHolder, Mensagem model, int position) {

                // Personalizando
                if (model.usuario.matricula.equals(usuario.matricula)) {
                    // Item
                    viewHolder.layItem.setGravity(Gravity.RIGHT);

                    // Nome do Usuário
                    viewHolder.lblMensagemUsuario.setVisibility(View.GONE);

                    // Balão
                    viewHolder.layBalao.setGravity(Gravity.RIGHT);
                    viewHolder.layBalao.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.my_message_background));
                    LinearLayout.LayoutParams balaoParams = (LinearLayout.LayoutParams) viewHolder.layBalao.getLayoutParams();
                    if (balaoParams.rightMargin > balaoParams.leftMargin) {
                        balaoParams.setMargins(balaoParams.rightMargin, balaoParams.topMargin, balaoParams.leftMargin, balaoParams.bottomMargin);
                    }
                } else {
                    // Item
                    viewHolder.layItem.setGravity(Gravity.LEFT);

                    // Nome do Usuário
                    viewHolder.lblMensagemUsuario.setVisibility(View.VISIBLE);

                    // Balão
                    viewHolder.layBalao.setGravity(Gravity.LEFT);
                    viewHolder.layBalao.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background));
                    LinearLayout.LayoutParams balaoParams = (LinearLayout.LayoutParams) viewHolder.layBalao.getLayoutParams();
                    if (balaoParams.leftMargin > balaoParams.rightMargin) {
                        balaoParams.setMargins(balaoParams.rightMargin, balaoParams.topMargin, balaoParams.leftMargin, balaoParams.bottomMargin);
                    }
                }

                viewHolder.lblMensagemConteudo.setText(model.conteudo);
                viewHolder.lblMensagemUsuario.setText(model.usuario.nome_usual);
                viewHolder.lblMensagemTempo.setText(DateFormat.format("HH:mm", model.tempo));

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

    public static class MensagemViewHolder extends RecyclerView.ViewHolder {
        TextView lblMensagemConteudo;
        TextView lblMensagemUsuario;
        TextView lblMensagemTempo;
        LinearLayout layBalao;
        LinearLayout layItem;


        public MensagemViewHolder(View v) {
            super(v);
            lblMensagemConteudo = (TextView) v.findViewById(R.id.lblMensagemConteudo);
            lblMensagemUsuario = (TextView) v.findViewById(R.id.lblMensagemUsuario);
            lblMensagemTempo = (TextView) v.findViewById(R.id.lblMensagemTempo);
            layBalao = (LinearLayout) v.findViewById(R.id.layBalao);
            layItem = (LinearLayout) v.findViewById(R.id.layItem);
        }
    }

}
