package br.com.postero.ifrnmessenger.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import br.com.postero.ifrnmessenger.R;
import br.com.postero.ifrnmessenger.models.Disciplina;

public class DisciplinaActivity extends AppCompatActivity {

    private Disciplina disciplina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.vincularElementos();
    }

    private void vincularElementos() {
        setContentView(R.layout.activity_disciplina);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        disciplina = (Disciplina) getIntent().getSerializableExtra("disciplina");

        this.setTitle(disciplina.toString());
        toolbar.setSubtitle("Última mensagem às 12h15...");
    }

}
