package com.example.ender.dadapp;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;

public class FragmentRatings extends Fragment {

    private Button btnCard1, btnCard2, btnCard3, btnCard4;
    private DetalharAvaliacao listener;
    private Spinner spinnerAnos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ratings_fragment, container,false);

        btnCard1 = view.findViewById(R.id.btnCard1);
        btnCard2 = view.findViewById(R.id.btnCard2);
        btnCard3 = view.findViewById(R.id.btnCard3);
        btnCard4 = view.findViewById(R.id.btnCard4);

        spinnerAnos = view.findViewById(R.id.spinnerAnos);
        spinnerAnos.setBackgroundColor(0xFFf5f5f5);

        btnCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    listener.detalharAvaliacao("Melhor Média de Postura Profissional");
            }
        });
        btnCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.detalharAvaliacao("Pior Média de Postura Profissional");
            }
        });
        btnCard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.detalharAvaliacao("Melhor Média de Atuação Profissional");
            }
        });
        btnCard4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.detalharAvaliacao("Pior Média de Atuação Profissional");
            }
        });



        CharSequence[] listaAnos = getResources().getTextArray(R.array.listAnos);


        ArrayAdapter<CharSequence> adapter= new ArrayAdapter<>(spinnerAnos.getContext(), R.layout.spinner_layout_item, listaAnos);
        adapter.setDropDownViewResource(R.layout.spinner_layout_dropdown);

        spinnerAnos.setAdapter(adapter);
        spinnerAnos.setSelection(listaAnos.length-1);

        return view;
    }

    public interface DetalharAvaliacao{
        void detalharAvaliacao(String btn);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof DetalharAvaliacao) {
            listener = (DetalharAvaliacao) activity;
        } else {
            throw new ClassCastException();
        }
    }

}

