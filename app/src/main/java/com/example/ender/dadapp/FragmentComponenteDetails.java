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
import android.widget.LinearLayout;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import api.AvaliacaoService;
import api.ConfigAPI;
import api.DocenteService;
import dto.DocenteMediasDTO;
import models.Avaliacao;
import models.ComponenteCurricular;
import models.Docente;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentComponenteDetails extends Fragment {

    private static final String MEDIA_APROVADOS = "0xAp215Amud2";
    private static final String APROVADOS = "0xXpWE51Amr3";
    private static final String QTD_ALUNOS = "0xAJsnfUs2Sk";
    private static final String POSTURA_PROFISSIONAL = "0xBJsn145s2SSk";
    private static final String ATUACAO_PROFISSIONAL = "0xJEsnU7P811";

    private ComponenteCurricular componente;
    private DocenteMediasDTO docenteMediasDTO;
    private TextView tvDetalhesNome, tvDetalhesFormacao, tvSetor, tvComponenteHeader, tvData;
    private TextView tvGeralAprovados, tvGeralMediaAprovados, tvGeralPosturaProf, tvGeralAtuacaoProf;
    private LinearLayout llDadosGerais;
    private BarChart chart1,chart2,chart3,chart4, chart5;
    private Retrofit retrofit;
    private List<Avaliacao> listaAvaliacoes;
    private Spinner compSpinner;
    private List<String> periodosLabel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_fragment, container, false);


        retrofit = new Retrofit.Builder()
                .baseUrl(ConfigAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        inicializarObjetos(view);
        gerarPerfilComponente();
        //carregarDadosMediasGerais(componente.id_componente);
        carregarAvaliacoes(componente.id_componente);

        return view;
    }
    public void inicializarObjetos(View view) {
        tvDetalhesNome = view.findViewById(R.id.tvDetalhesNome);
        tvDetalhesFormacao = view.findViewById(R.id.tvDetalhesFormacao);
        tvSetor = view.findViewById(R.id.tvSetor);
        tvData = view.findViewById(R.id.tvData);
        tvGeralAprovados = view.findViewById(R.id.tvGeralAprovados);
        tvGeralMediaAprovados = view.findViewById(R.id.tvGeralMediaAprovados);
        tvGeralPosturaProf = view.findViewById(R.id.tvGeralPosturaProf);
        tvGeralAtuacaoProf = view.findViewById(R.id.tvGeralAtuacaoProf);
        tvComponenteHeader = view.findViewById(R.id.tvComponenteHeader);
        compSpinner = view.findViewById(R.id.spinnerComp);
        chart1 = view.findViewById(R.id.chart1);
        chart2 = view.findViewById(R.id.chart2);
        chart3 = view.findViewById(R.id.chart3);
        chart4 = view.findViewById(R.id.chart4);
        chart5 = view.findViewById(R.id.chart5);

        componente = (ComponenteCurricular) getArguments().get("componente");

        llDadosGerais = view.findViewById(R.id.llDadosGerais);
        llDadosGerais.setVisibility(View.GONE);

    }
    private void carregarDadosMediasGerais(Integer id_docente) {

        final DocenteService docenteService = retrofit.create(DocenteService.class);
        final Call<DocenteMediasDTO> callDto = docenteService.getMediasById(id_docente);

        callDto.enqueue(new Callback<DocenteMediasDTO>() {
            @Override
            public void onResponse(Call<DocenteMediasDTO> call, Response<DocenteMediasDTO> response) {

                docenteMediasDTO = response.body();
                gerarMediasGerais(docenteMediasDTO);

            }

            @Override
            public void onFailure(Call<DocenteMediasDTO> call, Throwable t) {

            }
        });

    }
    private void gerarMediasGerais(DocenteMediasDTO docenteMediasDTO) {

        DecimalFormat mFormat = new DecimalFormat("###,###,##0");
            tvGeralAprovados.setText(String.valueOf(mFormat.format(docenteMediasDTO.aprovados*100)+"%"));
        mFormat = new DecimalFormat("###,###,##0.0");
            tvGeralMediaAprovados.setText(String.valueOf(mFormat.format(docenteMediasDTO.media_aprovados)));
        mFormat = new DecimalFormat("###,###,##0.00");
            tvGeralPosturaProf.setText(String.valueOf(mFormat.format(docenteMediasDTO.postura_profissional)));
            tvGeralAtuacaoProf.setText(String.valueOf(mFormat.format(docenteMediasDTO.atuacao_profissional)));

    }
    private void carregarAvaliacoes(Integer id_componente) {


        AvaliacaoService avalService = retrofit.create(AvaliacaoService.class);
        final Call<List<Avaliacao>> call = avalService.getAvaliacoesByComponente(id_componente);
        Log.i("JSON", "ID-COMPONENTE->"+id_componente);

        call.enqueue(new Callback<List<Avaliacao>>() {
            @Override
            public void onResponse(Call<List<Avaliacao>> call, Response<List<Avaliacao>> response) {
                listaAvaliacoes = response.body();
                Log.i("JSON", "JSON->"+listaAvaliacoes.toString());
                gerarDocentes();
                ///TODO
            }

            @Override
            public void onFailure(Call<List<Avaliacao>> call, Throwable t) {
                ///TODO
            }
        });
    }
    private void gerarDocentes() {
        final List<Docente> nomeDocentes = new ArrayList<>();

        for(int i=0; i<listaAvaliacoes.size();i++) {
            String docenteInicial = listaAvaliacoes.get(i).docente.nome;
            boolean igual = false;

            for (Docente docente : nomeDocentes) {
                if (docenteInicial.equals(docente.nome)) {
                    igual = true;
                }
            }
            if (!igual) {
                nomeDocentes.add(new Docente(listaAvaliacoes.get(i).docente.id_docente,
                        listaAvaliacoes.get(i).docente.nome,null ,
                        null, null));
            }
        }

        //Spinner
        ArrayAdapter<Docente> adapter =
                new ArrayAdapter(getActivity(),  android.R.layout.simple_spinner_dropdown_item, nomeDocentes);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        compSpinner.setAdapter(adapter);

        compSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int idDocente = nomeDocentes.get(position).id_docente;

                gerarGraficoQtdAlunos(gerarDados(idDocente, FragmentComponenteDetails.QTD_ALUNOS));
                gerarGraficoAprovados(gerarDados(idDocente, FragmentComponenteDetails.APROVADOS));
                gerarGraficoMediaAprovados(gerarDados(idDocente, FragmentComponenteDetails.MEDIA_APROVADOS));
                gerarGraficoPosturaProfissional(gerarDados(idDocente, FragmentComponenteDetails.POSTURA_PROFISSIONAL));
                gerarGraficoAtuacaoProfissional(gerarDados(idDocente, FragmentComponenteDetails.ATUACAO_PROFISSIONAL));

                tvComponenteHeader.setText(nomeDocentes.get(position).nome);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private List<Float> gerarDados(int id_docente, String tipoDado) {

        String periodoAux;
        periodosLabel = new ArrayList<>();
        List<Float> dados = new ArrayList<>();
        Log.i("JSON", "SIZE - LISTA->"+listaAvaliacoes.size());

        for(Avaliacao a: listaAvaliacoes){
            if(id_docente == a.docente.id_docente){

                periodoAux = a.turma.ano+"."+a.turma.periodo;

                    periodosLabel.add(periodoAux);

                    if(tipoDado.equals(FragmentComponenteDetails.MEDIA_APROVADOS))
                        dados.add(Float.parseFloat(String.valueOf(a.media_aprovados)));
                    if(tipoDado.equals(FragmentComponenteDetails.QTD_ALUNOS))
                        dados.add(Float.parseFloat(String.valueOf(a.qtd_discentes)));
                    if(tipoDado.equals(FragmentComponenteDetails.POSTURA_PROFISSIONAL))
                        dados.add(Float.parseFloat(String.valueOf(a.postura_profissional)));
                    if(tipoDado.equals(FragmentComponenteDetails.ATUACAO_PROFISSIONAL))
                        dados.add(Float.parseFloat(String.valueOf(a.atuacao_profissional)));
                    if(tipoDado.equals(FragmentComponenteDetails.APROVADOS))
                        dados.add(100*(Float.parseFloat(String.valueOf(a.aprovados))));

            }
        }

        for(String s: periodosLabel){
            Log.i("JSON", "ROTULOS->"+s+"\n");

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
        leftAxis.setLabelCount(dados.size(), false);
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
        chart1.setDrawGridBackground(false);

        chart1.setData(barData);
        //chart1.setVisibleXRangeMaximum(maxViewPort);
        chart1.invalidate();


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
        leftAxis.setLabelCount(dados.size(), false);
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
        chart2.setDrawGridBackground(false);

        chart2.setData(barData);
       // chart2.setVisibleXRangeMaximum(maxViewPort);
        chart2.invalidate();

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
        chart3.setDrawGridBackground(false);
        chart3.setData(barData);

        //chart3.setVisibleXRangeMaximum(maxViewPort);
        chart3.invalidate();

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
        chart4.setDrawGridBackground(false);

        chart4.setData(barData);
       // chart4.setVisibleXRangeMaximum(maxViewPort);
        chart4.invalidate();

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

        chart5.setData(barData);
       // chart5.setVisibleXRangeMaximum(maxViewPort);
        chart5.invalidate();

    }
    private void gerarPerfilComponente() {
        tvDetalhesNome.setText(componente.nome.toUpperCase());
        tvDetalhesFormacao.setText(componente.codigo);
        tvSetor.setText(componente.unidade.lotacao);
        tvData.setText(componente.codigo);
    }

  }
