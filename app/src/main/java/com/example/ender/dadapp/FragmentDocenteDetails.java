package com.example.ender.dadapp;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

import models.Docente;

public class FragmentDocenteDetails extends Fragment {

    GraphView graph;
    PieChart pieChart;
    Docente docente;

    TextView tvDetalhesNome;
    TextView tvDetalhesFormacao;
    TextView tvDetalhesSetor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_fragment, container, false);

        tvDetalhesNome = view.findViewById(R.id.tvDetalhesNome);
        tvDetalhesFormacao = view.findViewById(R.id.tvDetalhesFormacao);
        tvDetalhesSetor = view.findViewById(R.id.tvDetalhesSetor);

        docente = (Docente) getArguments().get("docente");

        tvDetalhesNome.setText(docente.nome);
        tvDetalhesFormacao.setText(docente.formacao);
        tvDetalhesSetor.setText(docente.unidade.lotacao);

        pieChart = view.findViewById(R.id.pieChart);
        graph = view.findViewById(R.id.graph);

        gerarGrafico();
        gerarPieChart();

        return view;
    }

    private void gerarPieChart() {
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        //pieChart.getDescription().setText("Descrição");
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.getLegend().setEnabled(false);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

        ArrayList<PieEntry> yValues = new ArrayList<>();
        yValues.add(new PieEntry(10f,"teste01"));
        yValues.add(new PieEntry(40f,"teste02"));
        yValues.add(new PieEntry(20f,"teste03"));
        yValues.add(new PieEntry(10f,"teste04"));
        yValues.add(new PieEntry(20f,"teste05"));

        PieDataSet dataSet = new PieDataSet(yValues,"");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(8f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);


    }

    private void gerarGrafico() {


        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 5),
                new DataPoint(3, 3.6),
                new DataPoint(4.8, 6.8),
                new DataPoint(1.4, 7.6),
        });

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(8);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(5);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        graph.addSeries(series);

        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(50);

        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);

        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
    }


}
