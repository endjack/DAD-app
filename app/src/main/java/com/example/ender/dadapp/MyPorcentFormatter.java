package com.example.ender.dadapp;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class MyPorcentFormatter implements IValueFormatter {

    public static final String PORCENT = "porcento";
    public static final String ONE_DECIMAL = "umdecimal";
    public static final String NO_DECIMALS = "semdecimais";
    public static final String TWO_DECIMAL = "doisdecimais";
    private String tipo;
    private DecimalFormat mFormat;;

    public MyPorcentFormatter(String tipo) {
        this.tipo = tipo;

    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

        if (tipo.equals(MyPorcentFormatter.PORCENT)) {
            mFormat = new DecimalFormat("###,###,##0");
            return mFormat.format(value) + "%";
        }
        if (tipo.equals(MyPorcentFormatter.ONE_DECIMAL)){
            mFormat = new DecimalFormat("###,###,##0.0");
            return mFormat.format(value) + "";
        }
        if (tipo.equals(MyPorcentFormatter.TWO_DECIMAL)){
            mFormat = new DecimalFormat("###,###,##0.00");
            return mFormat.format(value) + "";
        }
        if (tipo.equals(MyPorcentFormatter.NO_DECIMALS)){
            mFormat = new DecimalFormat("###,###,##0");
            return mFormat.format(value) + "";
        }

        return mFormat.format(value) + "";

    }
}
