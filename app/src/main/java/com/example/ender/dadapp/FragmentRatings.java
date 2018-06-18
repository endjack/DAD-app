package com.example.ender.dadapp;



import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentRatings extends Fragment {

    private TextView descricaoPostura, descricaoAtuacao, descricaoAutoAvaliacao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ratings_fragment, container,false);

        descricaoPostura = view.findViewById(R.id.descricaoPostura);
        descricaoAtuacao = view.findViewById(R.id.descricaoAtuacao);
        descricaoAutoAvaliacao = view.findViewById(R.id.descricaoAutoAvaliacao);
        escreverDescricoes();

        return view;
    }

    private void escreverDescricoes() {

        descricaoPostura.setText("Avalia-se no questionário o domínio do conteúdo, técnicas de ensino, e organização do docente.");

        descricaoAtuacao.setText("Destaca-se no questionário a pontualidade, assiduidade e disponibilidade para atendimentos aos alunos.");

        descricaoAutoAvaliacao.setText("Questiona-se sobre o seu desempenho no curso, a pontualidade, a assiduidade, dedicação e os resultados da aprendizagem");
    }


}

