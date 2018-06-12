package com.example.ender.dadapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import adapters.SearchResultsListAdapter;
import models.Docente;
import models.Unidade;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentSearch extends Fragment {

    RecyclerView recyclerView;
    SearchResultsListAdapter searchResultsListAdapter;

    EditText edtBuscar;
    Button bttBuscar;
    String nome;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buscar_fragment, container,false );



        edtBuscar = view.findViewById(R.id.edtBuscar);
        bttBuscar = view.findViewById(R.id.bttBuscar);

        recyclerView = view.findViewById(R.id.recycler_view_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        bttBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregarTodosDocentes();

            }
        });

        return view;
    }

    public void carregarDadosDocente(String nome) {
        RetrofitConfig retrofitConfig = RetrofitConfig.retrofit.create(RetrofitConfig.class);

        Call<Docente> call = retrofitConfig.getDocente(nome);

        call.enqueue(new Callback<Docente>() {
            @Override
            public void onResponse(Call<Docente> call, Response<Docente> response) {

                if (response.body() != null) {
                    //tvResultado.setText(response.body().nome + " - " + response.body().formacao);
                }else if(response.code() == 500){
                    //tvResultado.setText("Não encontrado!");
                }else{
                    Toast.makeText(getActivity(), "Favor inserir nome!", Toast.LENGTH_SHORT).show();
                    //tvResultado.setText("Digite Nome");
                }
            }

            @Override
            public void onFailure(Call<Docente> call, Throwable t) {
                Toast.makeText(getActivity(), "Não foi possível carregar os Dados!", Toast.LENGTH_SHORT).show();
                // tvResultado.setText("Digite Nome");
            }
        });

    }

    public void carregarTodosDocentes() {
        RetrofitConfig retrofitConfig = RetrofitConfig.retrofit.create(RetrofitConfig.class);
        Call<List<Docente>> call = retrofitConfig.getDocentes();

        call.enqueue(new Callback<List<Docente>>() {
            @Override
            public void onResponse(Call<List<Docente>> call, Response<List<Docente>> response) {
                List<Docente> listaDocentes = response.body();
                Log.i("LOG-RESULTS", "RESPONSE CODE: "+response.code());

                for (Docente docente: listaDocentes){

                    Log.i("LOG-RESULTS", docente.nome+"\n");

                }

                searchResultsListAdapter = new SearchResultsListAdapter(getActivity(), listaDocentes);
                recyclerView.setAdapter(searchResultsListAdapter);

            }

            @Override
            public void onFailure(Call<List<Docente>> call, Throwable t) {
                Toast.makeText(getActivity(), "Falha no carregamento dos dados!", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
