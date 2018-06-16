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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    private static final String MEDIA_APROVADOS = "0xAp215Amud2";
    private static final String APROVADOS = "0xXpWE51Amr3";
    private static final String QTD_ALUNOS = "0xAJsnfUs2Sk";
    private static final String POSTURA_PROFISSIONAL = "0xBJsn145s2SSk";
    private static final String ATUACAO_PROFISSIONAL = "0xJEsnU7P811";

    Docente docente;
    TextView tvDetalhesNome;
    TextView tvDetalhesFormacao;
    TextView tvSetor;
    TextView tvData;
    private BarChart chart1,chart2,chart3,chart4, chart5;
    private Retrofit retrofit;
    private int codeResponse;
    private List<Avaliacao> listaAvaliacoes;
    Spinner compSpinner;
    List<String> periodosLabel;


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
        tvSetor = view.findViewById(R.id.tvSetor);
        tvData = view.findViewById(R.id.tvData);
        compSpinner = view.findViewById(R.id.spinnerComp);
        chart1 = view.findViewById(R.id.chart1);
        chart2 = view.findViewById(R.id.chart2);
        chart3 = view.findViewById(R.id.chart3);
        chart4 = view.findViewById(R.id.chart4);
        chart5 = view.findViewById(R.id.chart5);

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
                ///TODO
            }

            @Override
            public void onFailure(Call<List<Avaliacao>> call, Throwable t) {
                ///TODO
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
        }

        //Spinner
        ArrayAdapter<ComponenteCurricular> adapter =
                new ArrayAdapter<ComponenteCurricular>(getActivity(),  android.R.layout.simple_spinner_dropdown_item, nomeComps);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        compSpinner.setAdapter(adapter);

        compSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int idComponente = nomeComps.get(position).id_componente;

                gerarGraficoQtdAlunos(gerarDados(idComponente,FragmentDocenteDetails.QTD_ALUNOS));
                gerarGraficoAprovados(gerarDados(idComponente,FragmentDocenteDetails.APROVADOS));
                gerarGraficoMediaAprovados(gerarDados(idComponente,FragmentDocenteDetails.MEDIA_APROVADOS));
                gerarGraficoPosturaProfissional(gerarDados(idComponente,FragmentDocenteDetails.POSTURA_PROFISSIONAL));
                gerarGraficoAtuacaoProfissional(gerarDados(idComponente,FragmentDocenteDetails.ATUACAO_PROFISSIONAL));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private List<Float> gerarDados(int id_componente, String tipoDado) {

        periodosLabel = new ArrayList<>();
        List<Float> dados = new ArrayList<>();

        for(int i=0;i<listaAvaliacoes.size();i++){
            boolean igual = false;
            if(id_componente == listaAvaliacoes.get(i).turma.componente.id_componente){
                String periodoAux = listaAvaliacoes.get(i).turma.ano+"."+listaAvaliacoes.get(i).turma.periodo;
                for(int j=0;j<periodosLabel.size();j++){
                    if(periodoAux == periodosLabel.get(j)){
                        igual=true;
                    }
                }
                if(!igual) {
                    periodosLabel.add(periodoAux);

                    if(tipoDado.equals(FragmentDocenteDetails.MEDIA_APROVADOS))
                        dados.add(Float.parseFloat(String.valueOf(listaAvaliacoes.get(i).media_aprovados)));
                    if(tipoDado.equals(FragmentDocenteDetails.QTD_ALUNOS))
                        dados.add(Float.parseFloat(String.valueOf(listaAvaliacoes.get(i).qtd_discentes)));
                    if(tipoDado.equals(FragmentDocenteDetails.POSTURA_PROFISSIONAL))
                        dados.add(Float.parseFloat(String.valueOf(listaAvaliacoes.get(i).postura_profissional)));
                    if(tipoDado.equals(FragmentDocenteDetails.ATUACAO_PROFISSIONAL))
                        dados.add(Float.parseFloat(String.valueOf(listaAvaliacoes.get(i).atuacao_profissional)));
                    if(tipoDado.equals(FragmentDocenteDetails.APROVADOS))
                        dados.add(100*(Float.parseFloat(String.valueOf(listaAvaliacoes.get(i).aprovados))));
                    Log.i("TAG_AVAL", "PERIODO->"+periodoAux);

                }
            }
        }

        return dados;

    }
    public void gerarGraficoMediaAprovados(List<Float> dados) {

        chart1.getDescription().setEnabled(false);
        chart1.setFitBars(false);
        chart1.animateY(500);
        chart1.setScaleEnabled(false);

        XAxis xAxis = chart1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(13f);
        xAxis.setLabelCount(dados.size(), false);

        YAxis leftAxis = chart1.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setLabelCount(dados.size(), true);
        leftAxis.setAxisMinimum(0f); // start at zero
        leftAxis.setAxisMaximum(10f); // the axis maximum is 10
        leftAxis.setTextColor(Color.BLACK);
        //leftAxis.setValueFormatter(new IndexAxisValueFormatter());
        leftAxis.setGranularity(1f); // interval 1

        YAxis rigthAxis = chart1.getAxisRight();
        rigthAxis.setEnabled(false);

        List<BarEntry> dadosBar = new ArrayList<>();
        List<String> xLabels = new ArrayList<>();

        for(int i = 0; i<dados.size();i++){
            dadosBar.add(new BarEntry(i, dados.get(i)));
            xLabels.add(periodosLabel.get(i));
            Log.i("TAG_AVAL","Período: "+periodosLabel.get(i)+",  Média Aprovados: "+dados.get(i));
        }


        BarDataSet barDataSet = new BarDataSet(dadosBar,"POR PERÍODO");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setBarShadowColor(Color.rgb(203,203,203));
        barDataSet.setValueFormatter(new MyPorcentFormatter(MyPorcentFormatter.ONE_DECIMAL));
        barDataSet.setDrawValues(true);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);
        chart1.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabels));
        chart5.setDrawGridBackground(false);
        chart5.invalidate();
        chart1.setData(barData);

    }
    public void gerarGraficoQtdAlunos(List<Float> dados) {

        chart2.getDescription().setEnabled(false);
        chart2.setFitBars(false);
        chart2.animateY(500);
        chart2.setScaleEnabled(false);

        XAxis xAxis = chart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(13f);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(dados.size(), false);

        YAxis leftAxis = chart2.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setLabelCount(dados.size(), true);
        leftAxis.setAxisMinimum(0f); // start at zero
        leftAxis.setAxisMaximum(100f); // the axis maximum is 100
        leftAxis.setTextColor(Color.BLACK);
        //leftAxis.setValueFormatter(new IndexAxisValueFormatter());
        leftAxis.setGranularity(1f); // interval 1

        YAxis rigthAxis = chart2.getAxisRight();
        rigthAxis.setEnabled(false);

        List<BarEntry> dadosBar = new ArrayList<>();
        List<String> xLabels = new ArrayList<>();

        for(int i = 0; i<dados.size();i++){
            dadosBar.add(new BarEntry(i, dados.get(i)));
            xLabels.add(periodosLabel.get(i));
            Log.i("TAG_AVAL","Ano: "+periodosLabel.get(i)+",  Qtd-Discentes: "+dados.get(i));
        }


        BarDataSet barDataSet = new BarDataSet(dadosBar,"POR PERÍODO");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setBarShadowColor(Color.rgb(203,203,203));
        barDataSet.setValueFormatter(new MyPorcentFormatter(MyPorcentFormatter.NO_DECIMALS));
        barDataSet.setDrawValues(true);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);
        chart2.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabels));
        chart5.setDrawGridBackground(false);
        chart5.invalidate();
        chart2.setData(barData);

    }
    public void gerarGraficoPosturaProfissional(List<Float> dados) {

        chart3.getDescription().setEnabled(false);
        chart3.setFitBars(false);
        chart3.animateY(500);
        chart3.setScaleEnabled(false);

        XAxis xAxis = chart3.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(13f);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(dados.size(), false);

        YAxis leftAxis = chart3.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setLabelCount(dados.size(), true);
        leftAxis.setAxisMinimum(0f); // start at zero
        leftAxis.setAxisMaximum(10f); // the axis maximum is 10
        leftAxis.setTextColor(Color.BLACK);
        //leftAxis.setValueFormatter(new IndexAxisValueFormatter());
        leftAxis.setGranularity(1f); // interval 1

        YAxis rigthAxis = chart3.getAxisRight();
        rigthAxis.setEnabled(false);

        List<BarEntry> dadosBar = new ArrayList<>();
        List<String> xLabels = new ArrayList<>();

        for(int i = 0; i<dados.size();i++){
            dadosBar.add(new BarEntry(i, dados.get(i)));
            xLabels.add(periodosLabel.get(i));
            Log.i("TAG_AVAL","Ano: "+periodosLabel.get(i)+",  Postura Profissional: "+dados.get(i));
        }


        BarDataSet barDataSet = new BarDataSet(dadosBar,"POR PERÍODO");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setBarShadowColor(Color.rgb(203,203,203));
        barDataSet.setValueFormatter(new MyPorcentFormatter(MyPorcentFormatter.TWO_DECIMAL));
        barDataSet.setDrawValues(true);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);
        chart3.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabels));
        chart5.setDrawGridBackground(false);
        chart5.invalidate();
        chart3.setData(barData);

    }
    public void gerarGraficoAtuacaoProfissional(List<Float> dados) {

        chart4.getDescription().setEnabled(false);
        chart4.setFitBars(false);
        chart4.animateY(500);
        chart4.setScaleEnabled(false);

        XAxis xAxis = chart4.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(13f);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(dados.size(), false);

        YAxis leftAxis = chart4.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setLabelCount(dados.size(), true);
        leftAxis.setAxisMinimum(0f); // start at zero
        leftAxis.setAxisMaximum(10f); // the axis maximum is 10
        leftAxis.setTextColor(Color.BLACK);
        //leftAxis.setValueFormatter(new IndexAxisValueFormatter());
        leftAxis.setGranularity(1f); // interval 1

        YAxis rigthAxis = chart4.getAxisRight();
        rigthAxis.setEnabled(false);

        List<BarEntry> dadosBar = new ArrayList<>();
        List<String> xLabels = new ArrayList<>();

        for(int i = 0; i<dados.size();i++){
            dadosBar.add(new BarEntry(i, dados.get(i)));
            xLabels.add(periodosLabel.get(i));
            Log.i("TAG_AVAL","Ano: "+periodosLabel.get(i)+",  Atuação Profissional: "+dados.get(i));
        }


        BarDataSet barDataSet = new BarDataSet(dadosBar,"POR PERÍODO");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setBarShadowColor(Color.rgb(203,203,203));
        barDataSet.setValueFormatter(new MyPorcentFormatter(MyPorcentFormatter.TWO_DECIMAL));
        barDataSet.setDrawValues(true);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);
        chart4.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabels));
        chart5.setDrawGridBackground(false);
        chart5.invalidate();
        chart4.setData(barData);

    }
    public void gerarGraficoAprovados(List<Float> dados) {

        chart5.getDescription().setEnabled(false);
        chart5.setFitBars(false);
        chart5.animateY(500);
        chart5.setScaleEnabled(false);


        XAxis xAxis = chart5.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(13f);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(dados.size(), false);


        YAxis leftAxis = chart5.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setLabelCount(dados.size(), true);
        leftAxis.setAxisMinimum(0f); // start at zero
        leftAxis.setAxisMaximum(100f); // the axis maximum is 100
        leftAxis.setTextColor(Color.BLACK);
        //leftAxis.setValueFormatter(new IndexAxisValueFormatter());
        leftAxis.setGranularity(1f); // interval 1


        YAxis rigthAxis = chart5.getAxisRight();
        rigthAxis.setEnabled(false);


        List<BarEntry> dadosBar = new ArrayList<>();
        List<String> xLabels = new ArrayList<>();

        for(int i = 0; i<dados.size();i++){
            dadosBar.add(new BarEntry(i, dados.get(i)));
            xLabels.add(periodosLabel.get(i));
            Log.i("TAG_AVAL","Ano: "+periodosLabel.get(i)+",  Atuação Profissional: "+dados.get(i));
        }


        BarDataSet barDataSet = new BarDataSet(dadosBar,"POR PERÍODO");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setBarShadowColor(Color.rgb(203,203,203));
        barDataSet.setValueFormatter(new MyPorcentFormatter(MyPorcentFormatter.PORCENT));
        barDataSet.setDrawValues(true);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);
        chart5.setDrawGridBackground(false);
        chart5.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabels));
        chart5.invalidate();
        chart5.setData(barData);

    }
    private void gerarPerfilDocente() {
        tvDetalhesNome.setText(docente.nome.toUpperCase());
        tvDetalhesFormacao.setText(docente.formacao);
        tvSetor.setText(docente.unidade.lotacao);

        Date data = new Date(docente.data_admissao.getTime());
        String dataFormatada = new SimpleDateFormat("dd-MM-yyyy").format(data);
        tvData.setText(dataFormatada);
    }


  }
