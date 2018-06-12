package com.example.ender.dadapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import adapters.SearchResultsListAdapter;
import models.Docente;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentSearch extends Fragment {

    RecyclerView recyclerView;
    SearchResultsListAdapter searchResultsListAdapter;
    private Retrofit retrofit;
    EditText edtBuscar;
    Button bttBuscar;
    DetalharDocenteListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buscar_fragment, container,false );

        Gson gson = new GsonBuilder().registerTypeAdapter(Docente.class, new DocenteDeserializable()).create();
        retrofit = new Retrofit.Builder()
                .baseUrl(DocenteService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


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
    public void abrirDetalhesDocente(){

    }

    public void carregarDadosDocente(String nome) {
        DocenteService docenteService = retrofit.create(DocenteService.class);
        Call<Docente> call = docenteService.getDocente(nome);

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
        DocenteService docenteService = retrofit.create(DocenteService.class);
        final Call<List<Docente>> call = docenteService.getDocentes();
        final Handler handler = new Handler(Looper.getMainLooper());

        new Thread(new Runnable() {
            List<Docente> listaDocentes;
            @Override
            public void run() {

                try {
                    listaDocentes = call.execute().body();

                    if(listaDocentes != null){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                searchResultsListAdapter = new SearchResultsListAdapter(getActivity(), listaDocentes, new SearchResultsListAdapter.ItemDetailsListener() {
                                    @Override
                                    public void onDetailsClick(Docente docente) {
                                       listener.detalharDocente(docente);
                                    }
                                });

                                recyclerView.setAdapter(searchResultsListAdapter);

                            }
                        });

                    }

//                    for(Docente d: docenteList){
//                        Log.i("LOG-RESULTS", d.nome+"\n");
//                        Log.i("LOG-RESULTS", d.unidade.lotacao+"\n");
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface DetalharDocenteListener{
        void detalharDocente(Docente docente);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FragmentSearch.DetalharDocenteListener) {
            listener = (FragmentSearch.DetalharDocenteListener) activity;
        } else {
            throw new ClassCastException();
        }
    }

}
