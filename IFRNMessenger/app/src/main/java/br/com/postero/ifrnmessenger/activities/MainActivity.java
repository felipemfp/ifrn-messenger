package br.com.postero.ifrnmessenger.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import br.com.postero.ifrnmessenger.R;
import br.com.postero.ifrnmessenger.adapters.DisciplinaAdapter;
import br.com.postero.ifrnmessenger.models.Disciplina;
import br.com.postero.ifrnmessenger.models.PeriodoLetivo;
import br.com.postero.ifrnmessenger.models.Usuario;
import br.com.postero.ifrnmessenger.utils.ItemClickSupport;
import br.com.postero.ifrnmessenger.utils.P;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView lblUsuarioNome;
    private TextView lblUsuarioEmail;

    private Usuario usuario;
    private PeriodoLetivo periodoLetivo;
    private ArrayList<PeriodoLetivo> periodoLetivos;
    private ArrayList<Disciplina> disciplinas;

    private ProgressDialog progressDialog;

    private Toolbar toolbar;
    private View navHeader;
    private Menu navMenu;
    private MenuItem lastMenuItem;
    private DrawerLayout drawerLayout;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    static boolean isFirebaseInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            if (!isFirebaseInitialized) {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                isFirebaseInitialized = true;
            } else {
                Log.d("Firebase:", "Already initialized.");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        usuario = Usuario.first(Usuario.class);
        if (usuario != null) {
            setContentView(R.layout.activity_main);
            this.vincularElementos();

            if (PeriodoLetivo.count(PeriodoLetivo.class) == 0) {
                progressDialog.setMessage("Carregando Periodos...");
                progressDialog.show();
            }
            this.carregarPeriodos();
        } else {
            this.finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void vincularElementos() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        navMenu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);

        lblUsuarioNome = (TextView) navHeader.findViewById(R.id.lblUsuarioNome);
        lblUsuarioEmail = (TextView) navHeader.findViewById(R.id.lblUsuarioEmail);

        lblUsuarioNome.setText(usuario.nome_usual);
        lblUsuarioEmail.setText(usuario.email);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Disciplina disciplina = disciplinas.get(position);
                onDisciplinaSelected(disciplina);
            }
        });
    }

    private void carregarDisciplinas() {
        Disciplina.listarTodos(periodoLetivo, new Disciplina.ApiListener() {
            @Override
            public void onSuccess(Object object) {
                disciplinas = (ArrayList<Disciplina>) object;
                PeriodoLetivo periodo = disciplinas.get(0).periodoLetivo;
                Log.i("Periodo:", periodoLetivo.toString() + " " + periodo.toString());
                if (periodoLetivo.getId().equals(periodo.getId())) {
                    adapter = new DisciplinaAdapter(disciplinas);
                    recyclerView.setAdapter(adapter);
                    progressDialog.hide();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private void carregarPeriodos() {
        PeriodoLetivo.listarTodos(new PeriodoLetivo.ApiListener() {
            @Override
            public void onSuccess(Object object) {
                periodoLetivos = (ArrayList<PeriodoLetivo>) object;
                SubMenu subMenu = navMenu.getItem(0).getSubMenu();
                int i = 1;
                for (PeriodoLetivo periodo : periodoLetivos) {
                    MenuItem item = subMenu.add(periodo.toString()).setIcon(R.drawable.ic_menu_send).setCheckable(true);
                    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (lastMenuItem != null) {
                                lastMenuItem.setChecked(false);
                            }
                            item.setChecked(true);
                            lastMenuItem = item;
                            return onPeriodoLetivoSelected(item);
                        }
                    });
                    periodoLetivo = periodo;
                    carregarDisciplinas();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private void onDisciplinaSelected(Disciplina disciplina) {
        Intent intent = new Intent(this, DisciplinaActivity.class);
        intent.putExtra("disciplina", disciplina);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                new AlertDialog.Builder(this)
                        .setTitle("Deseja mesmo sair?")
                        .setMessage("Essa operação removerá seus dados já salvos no dispositivo")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                P.limpar();
                                Usuario.deleteAll(Usuario.class);
                                Disciplina.deleteAll(Disciplina.class);
                                PeriodoLetivo.deleteAll(PeriodoLetivo.class);
                                finish();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }
                        })
                        .setNegativeButton("Não", null)
                        .create()
                        .show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean onPeriodoLetivoSelected(MenuItem item) {
        String[] valores = item.getTitle().toString().split("\\" + PeriodoLetivo.SEPARATOR);

        periodoLetivo = PeriodoLetivo.find(PeriodoLetivo.class, "ano = ? and periodo = ?", valores).get(0);

        toolbar.setTitle(periodoLetivo.toString());
        this.carregarDisciplinas();
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
