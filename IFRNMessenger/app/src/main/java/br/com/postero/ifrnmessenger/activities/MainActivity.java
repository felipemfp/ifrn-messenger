package br.com.postero.ifrnmessenger.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import br.com.postero.ifrnmessenger.R;
import br.com.postero.ifrnmessenger.adapters.DisciplinaAdapter;
import br.com.postero.ifrnmessenger.models.Curso;
import br.com.postero.ifrnmessenger.models.Disciplina;
import br.com.postero.ifrnmessenger.utils.AppController;
import br.com.postero.ifrnmessenger.utils.GsonRequest;
import br.com.postero.ifrnmessenger.utils.ItemClickSupport;
import br.com.postero.ifrnmessenger.utils.P;
import br.com.postero.ifrnmessenger.utils.SuapAPI;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Disciplina> disciplinas;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Disciplina disciplina = disciplinas.get(position);

                Snackbar.make(v,
                        "Você tem " + disciplina.numero_faltas + " faltas na disciplina " + disciplina.disciplina + "!",
                        Snackbar.LENGTH_SHORT).show();
            }
        });


        Disciplina.listarTodos(new Disciplina.ApiListener() {
            @Override
            public void onSuccess(Object object) {
                disciplinas = (ArrayList<Disciplina>) object;
                adapter = new DisciplinaAdapter(disciplinas);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
