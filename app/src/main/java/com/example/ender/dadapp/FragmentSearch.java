package com.example.ender.dadapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
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
import api.DocenteDeserializable;
import api.DocenteService;
import models.Docente;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentSearch extends Fragment {

    private RecyclerView recyclerView;
    private SearchResultsListAdapter searchResultsListAdapter;
    private Retrofit retrofit;
    private EditText edtBuscar;
    private Button bttBuscar;
    private DetalharDocenteListener listener;
    private String ultimaPesquisa="";

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
                ultimaPesquisa = edtBuscar.getText().toString();
                Toast.makeText(getActivity(), ultimaPesquisa, Toast.LENGTH_SHORT).show();
                carregarTodosDocentes();

            }
        });

        return view;
    }

    /**
     * Chamado ao mudar de Fragment pelo MenuNavigation ou pelo voltão voltar.
     * Ao ser chamado ele salva em memória o valor da ultima pesquisa no EditText em
     * um Bundle (Pacote) que está na MainActivity (saveDadosFragments)
     */
    @Override
    public void onStop() {
        Log.i("TAG_ESTADO", "Entrei onStop");
        super.onStop();
        getActivity().getIntent().getBundleExtra("save").putString("ultimaPesquisa",ultimaPesquisa);
    }

    /**
     * Chamado ao retornar ao Fragment.
     * Pega o Bundle (Pacote) que está na MainActivity (saveDadosFragments) e insere a ultima string
     * chamada no campo de pesquisa novamente.
     * Também chama o método carregarTodosDocentes() para recarregar a pesquisa na lista de Resultados
     * @param savedInstanceState Bundle da activity (Chega vazia)
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        savedInstanceState = getActivity().getIntent().getBundleExtra("save");
        Log.i("TAG_ESTADO", "Entrei onActivityCreated");

        if(savedInstanceState != null) {
            ultimaPesquisa = savedInstanceState.getString("ultimaPesquisa");

            if (ultimaPesquisa == null) {
                //fazer nada
            }else{
                edtBuscar.setText(savedInstanceState.getString("ultimaPesquisa"));
                carregarTodosDocentes();
            }
        }

    }

    /**
     * Faz uma chamada ao Service de Docentes e recebe um Docente pesquisa por nome.
     * @param nome Nome do Docente pesquisado na EditText
     */
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

    /**
     * Faz uma chamada ao Service de Docentes e recebe uma lista com todos os Docentes.
     */
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Interface para comunicação com a MainActivity
     */
    public interface DetalharDocenteListener{
        void detalharDocente(Docente docente);
    }

    /**
     * Necessário para instanciar o objeto correto
     * @param activity Activity atrelada (MainActivity)
     */
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
