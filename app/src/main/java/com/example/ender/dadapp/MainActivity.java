package com.example.ender.dadapp;

import android.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity{

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
                        //fragmentHome.setColorBack(0xFFFF0000);
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_layout,fragmentHome)
                                .commit();
                        break;
                    case R.id.avalItem:
                        FragmentMain fragmentAval = new FragmentMain();
                        //fragmentAval.setColorBack(0xFF00FF00);
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_layout,fragmentAval)
                                .commit();
                        break;
                    case R.id.procurarItem:
                        FragmentMain fragmentProc = new FragmentMain();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_layout,fragmentProc)
                                .commit();
                        break;
                    case R.id.sobreItem:
                        FragmentMain fragmentSobre = new FragmentMain();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_layout,fragmentSobre)
                                .commit();
                        break;
                    case R.id.optItem:
                        FragmentMain fragmentOpt = new FragmentMain();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_layout,fragmentOpt)
                                .commit();
                        break;
                }




                return true;
            }
        });

    }
}
