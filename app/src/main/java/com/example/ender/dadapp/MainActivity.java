package com.example.ender.dadapp;



import android.app.FragmentManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import models.Docente;

public class MainActivity extends AppCompatActivity implements FragmentSearch.DetalharDocenteListener{

    private BottomNavigationView bottomNavigationView;
    private Bundle saveDadosFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    //pacote que guardará em memória todas as informações persistentes dos fragments atrelados a esta activity
        saveDadosFragments = new Bundle();
        getIntent().putExtra("save", saveDadosFragments);

    //Fragment que inicia com a aplicação
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout,new FragmentMain())
                .addToBackStack(getClass().toString())
                .commit();

    //Menu de navegação dos fragments da aplicação
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeItem:
                        FragmentMain fragmentHome = new FragmentMain();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_layout,fragmentHome)
                                .addToBackStack(fragmentHome.getClass().toString())
                                .commit();
                        break;
                    case R.id.avalItem:
                        FragmentRatings fragmentRate = new FragmentRatings();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_layout,fragmentRate)
                                .addToBackStack(fragmentRate.getClass().toString())
                                .commit();
                        break;
                    case R.id.procurarItem:
                        FragmentSearch fragmentSearch= new FragmentSearch();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_layout,fragmentSearch)
                                .addToBackStack(fragmentSearch.getClass().toString())
                                .commit();
                        break;
                    case R.id.sobreItem:
                        FragmentMain fragmentSobre = new FragmentMain();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_layout,fragmentSobre)
                                .addToBackStack(fragmentSobre.getClass().toString())
                                .commit();
                        break;
                }
                return true;
            }
        });
    }

    /**
     * Fragment que detalha as informações de um docente
     * @param docente Docente aser Detalhado
     */
    @Override
    public void detalharDocente(@NonNull Docente docente) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("docente", docente);

        FragmentDocenteDetails fragmentDocenteDetails = new FragmentDocenteDetails();
        fragmentDocenteDetails.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout,fragmentDocenteDetails)
                .addToBackStack(fragmentDocenteDetails.getClass().toString())
                .commit();
    }
}
