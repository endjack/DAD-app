package adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ender.dadapp.R;

import java.util.ArrayList;
import java.util.List;

import models.Docente;

public class SearchResultsListAdapter extends RecyclerView.Adapter<SearchResultsListAdapter.ResultsHolder> {

    private Context context;
    private List<Docente> itens;

    public SearchResultsListAdapter(Context context, List lista) {
        this.context = context;
        this.itens = lista;
        Log.i("LOG-RESULTS", "TAMANHO LISTA: "+getItemCount());
    }

    @NonNull
    @Override
    public ResultsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_results_searchs,parent,false);
        ResultsHolder resultsHolder = new ResultsHolder(view);
        return  resultsHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsHolder holder, int position) {
       Docente docente = itens.get(position);
       holder.tvResultadoNome.setText(docente.getNome().toUpperCase());
       holder.tvResultadoSetor.setText("Setor: ");
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }


    public class ResultsHolder extends RecyclerView.ViewHolder{

        private TextView tvResultadoNome;
        private TextView tvResultadoSetor;

        public ResultsHolder(View itemView) {
            super(itemView);
            tvResultadoNome = itemView.findViewById(R.id.tvResultadoNome);
            tvResultadoSetor = itemView.findViewById(R.id.tvResultadoSetor);
        }
    }
}
