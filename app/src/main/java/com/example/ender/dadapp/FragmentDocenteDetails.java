package com.example.ender.dadapp;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import api.AvaliacaoService;
import models.Avaliacao;
import models.ComponenteCurricular;
import models.Docente;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentDocenteDetails extends Fragment {

    Docente docente;
    TextView tvDetalhesNome;
    TextView tvDetalhesFormacao;
    TextView tvDetalhesSetor;
    private BarChart chart1;
    private Retrofit retrofit;
    private int codeResponse;
    private List<Avaliacao> listaAvaliacoes;
    Spinner compSpinner;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_fragment, container, false);


        retrofit = new Retrofit.Builder()
                .baseUrl(AvaliacaoService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        inicializarObjetos(view);
        gerarPerfilDocente();

        carregarAvaliacao(String.valueOf(docente.id_docente));

        return view;
    }
    private void inicializarObjetos(View view) {
        tvDetalhesNome = view.findViewById(R.id.tvDetalhesNome);
        tvDetalhesFormacao = view.findViewById(R.id.tvDetalhesFormacao);
        tvDetalhesSetor = view.findViewById(R.id.tvDetalhesSetor);
        compSpinner = view.findViewById(R.id.spinnerComp);
        chart1 = view.findViewById(R.id.chart1);

        docente = (Docente) getArguments().get("docente");
    }
    public void carregarAvaliacao(String id_docente) {
        AvaliacaoService avalService = retrofit.create(AvaliacaoService.class);
        final Call<List<Avaliacao>> call = avalService.getAvaliacoes(id_docente);

        call.enqueue(new Callback<List<Avaliacao>>() {
            @Override
            public void onResponse(Call<List<Avaliacao>> call, Response<List<Avaliacao>> response) {
                listaAvaliacoes = response.body();
                gerarComponentes();

            }

            @Override
            public void onFailure(Call<List<Avaliacao>> call, Throwable t) {

            }
        });



    }

    private void gerarComponentes() {

        final List<ComponenteCurricular> nomeComps = new ArrayList<>();


        for(int i=0; i<listaAvaliacoes.size();i++) {
            String compInicial = listaAvaliacoes.get(i).turma.componente.nome;
            boolean igual = false;

            for (ComponenteCurricular comp : nomeComps) {
                if (compInicial.equals(comp.nome)) {
                    igual = true;
                }
            }
            if (!igual) {
                nomeComps.add(new ComponenteCurricular(listaAvaliacoes.get(i).turma.componente.id_componente,
                        listaAvaliacoes.get(i).turma.componente.codigo, listaAvaliacoes.get(i).turma.componente.nome,
                        null, null));
            }

            ArrayAdapter<ComponenteCurricular> adapter =
                    new ArrayAdapter<ComponenteCurricular>(getActivity(),  android.R.layout.simple_spinner_dropdown_item, nomeComps);
            adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

            compSpinner.setAdapter(adapter);

           compSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   gerarDadosAprovados(nomeComps.get(position).id_componente);
               }

               @Override
               public void onNothingSelected(AdapterView<?> parent) {

               }
           });

        }

    }

    private void gerarDadosAprovados(int id_componente) {


        List<AprovadosTurma> anos = new ArrayList<>();

        for(Avaliacao a: listaAvaliacoes){
            boolean igual = false;
            if(id_componente == a.turma.componente.id_componente){
                String anoAux = a.turma.ano;
                for(AprovadosTurma ano: anos){
                    if(anoAux == ano.ano){
                        igual=true;
                    }
                }
                if(!igual) {
                    anos.add(new AprovadosTurma(anoAux,a.media_aprovados));
                }

            }

        }



        gerarGraficoAprovados(anos);

    }
    public void gerarGraficoAprovados(List<AprovadosTurma> aprovadosTurma) {

        chart1.getDescription().setEnabled(false);
        chart1.setFitBars(false);
        chart1.setDrawGridBackground(false);
        chart1.animateY(500);
        chart1.setScaleEnabled(false);

        XAxis xAxis = chart1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(aprovadosTurma.size(), false);

        YAxis leftAxis = chart1.getAxisLeft();
        leftAxis.setLabelCount(aprovadosTurma.size(), false);
        leftAxis.setSpaceTop(10f);

        YAxis rigthAxis = chart1.getAxisRight();
        rigthAxis.setLabelCount(aprovadosTurma.size(), false);
        rigthAxis.setSpaceTop(10f);

        List<BarEntry> dados = new ArrayList<>();
        List<String> xLabels = new ArrayList<>();

        for(int i = 0; i<aprovadosTurma.size();i++){
            dados.add(new BarEntry(i, Float.parseFloat(String.valueOf(aprovadosTurma.get(i).media))));
            xLabels.add(aprovadosTurma.get(i).ano);
            Log.i("TAG_AVAL","Ano: "+aprovadosTurma.get(i).ano+",  Média Aprovados: "+dados.get(i));
        }


        BarDataSet barDataSet = new BarDataSet(dados,"ANUAL");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setBarShadowColor(Color.rgb(203,203,203));
        barDataSet.setDrawValues(true);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);
        chart1.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabels));
        chart1.setData(barData);

    }
    private void gerarPerfilDocente() {
        tvDetalhesNome.setText(docente.nome.toUpperCase());
        tvDetalhesFormacao.setText(docente.formacao);
        tvDetalhesSetor.setText(docente.unidade.lotacao);
    }

    public class AprovadosTurma{
        String ano;
        Double media;

        public AprovadosTurma(String ano, Double media) {
            this.ano = ano;
            this.media = media;
        }

    }

    public class ComponenteId{
        String nome;
        Integer id;

        public ComponenteId(String nome, Integer id) {
            this.nome = nome;
            this.id = id;
        }
    }

  }
