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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapters.SearchResultsListAdapter;
import api.AvaliacaoService;
import api.ConfigAPI;
import api.DocenteDeserializable;
import api.DocenteService;
import models.Avaliacao;
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
    private TextView tvInfo;
    private Button bttBuscar;
    private DetalharDocenteListener listener;
    private String ultimaPesquisa="";
    private ProgressBar progressBarTop;
    private String nome;
    private int codeResponse;
    private volatile boolean isRunning = false;
    private String msgError;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buscar_fragment, container,false );

        //Gson gson = new GsonBuilder().registerTypeAdapter(Docente.class, new DocenteDeserializable()).create();
        retrofit = new Retrofit.Builder()
                .baseUrl(ConfigAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        edtBuscar = view.findViewById(R.id.edtBuscar);
        bttBuscar = view.findViewById(R.id.bttBuscar);
        progressBarTop = view.findViewById(R.id.progressBar);
        tvInfo = view.findViewById(R.id.tvInfo);
        exibirInfo(false);


        recyclerView = view.findViewById(R.id.recycler_view_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        exibirProgressBar(false);

        bttBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nome = edtBuscar.getText().toString();
                if(!nome.isEmpty()){
                    ultimaPesquisa = nome;
                    carregarTodosDocentes(nome);
                }else{
                    tvInfo.setText("Consulta vazia, digite nome");
                    exibirInfo(true);
                }
            }
        });

        return view;
    }

    /**
     * Exibe ou não o progressBar na tela
     * @param exibir Booleano para definir exibir ou não
     */
    private void exibirProgressBar(boolean exibir) {
        progressBarTop.setVisibility(exibir ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(exibir ? View.GONE : View.VISIBLE);
    }

    /**
     * Exibe ou não a Informação de RenponseHTTP na tela
     * @param exibir Booleano para definir exibir ou não
     */
    private void exibirInfo(boolean exibir) {
        tvInfo.setVisibility(exibir ? View.VISIBLE : View.GONE);
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
                //carregarTodosDocentes(ultimaPesquisa);
            }
        }

    }

    /**
     * Faz uma chamada ao Service de Docentes e recebe uma lista com todos os Docentes.
     */
    public void carregarTodosDocentes(String nome) {
        DocenteService docenteService = retrofit.create(DocenteService.class);
        final Call<List<Docente>> call = docenteService.getDocentes(nome);



        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            List<Docente> listaDocentes;
            @Override
            public void run() {
                isRunning = true;
                    try {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                exibirProgressBar(true);
                            }
                        });
                        //listaDocentes = call.execute().body();

                        listaDocentes = call.execute().body();


                        Log.i("TAG_RESPONSE","Code Response: "+listaDocentes);

                        if(codeResponse == 400){
                            isRunning = false;

                        }

                        if(isRunning) {
                            //listaDocentes = (List<Docente>) response.body();


                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (!listaDocentes.isEmpty()) {
                                            searchResultsListAdapter = new SearchResultsListAdapter(getActivity(),
                                                    listaDocentes, new SearchResultsListAdapter.ItemDetailsListener() {
                                                @Override
                                                public void onDetailsClick(Docente docente) {
                                                    listener.detalharDocente(docente);
                                                }
                                            });

                                            recyclerView.setAdapter(searchResultsListAdapter);
                                            exibirProgressBar(false);
                                            exibirInfo(false);
                                        }else {

                                            searchResultsListAdapter = new SearchResultsListAdapter(getActivity(), listaDocentes, null);
                                            searchResultsListAdapter.notifyDataSetChanged();

                                            tvInfo.setText("Sem resultados");
                                            exibirProgressBar(false);
                                            exibirInfo(true);
                                            isRunning = false;
                                            return;
                                        }


                                    }
                                });

                        }else{
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    exibirProgressBar(false);
                                    Toast.makeText(getActivity(), "Servidor off-line", Toast.LENGTH_SHORT).show();
                                }
                            });

                            return;
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
