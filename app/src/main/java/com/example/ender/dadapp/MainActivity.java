package com.example.ender.dadapp;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import models.Docente;

public class MainActivity extends AppCompatActivity implements FragmentSearch.DetalharDocenteListener{

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout,new FragmentMain())
                .commit();


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
                                .commit();
                        break;
                    case R.id.avalItem:
                        FragmentRatings fragmentRate = new FragmentRatings();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_layout,fragmentRate)
                                .commit();
                        break;
                    case R.id.procurarItem:
                        FragmentSearch fragmentSearch= new FragmentSearch();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_layout,fragmentSearch)
                                .commit();
                        break;
                    case R.id.sobreItem:
                        FragmentMain fragmentSobre = new FragmentMain();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_layout,fragmentSobre)
                                .commit();
                        break;
//                    case R.id.optItem:
//                        FragmentMain fragmentOpt = new FragmentMain();
//                        getFragmentManager()
//                                .beginTransaction()
//                                .replace(R.id.frame_layout,fragmentOpt)
//                                .commit();
//                        break;
                }
                return true;
            }
        });

    }

    @Override
    public void detalharDocente(Docente docente) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("docente", docente);

        FragmentDocenteDetails fragmentDocenteDetails = new FragmentDocenteDetails();
        fragmentDocenteDetails.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout,fragmentDocenteDetails)
                .commit();
    }
}
