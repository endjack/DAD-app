package adapters;

import android.content.Context;
import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ender.dadapp.R;

import java.util.List;

import models.Docente;

public class SearchResultsListAdapter extends RecyclerView.Adapter<SearchResultsListAdapter.ResultsHolder> {

    private Context context;
    private List<Docente> itens;
    DocenteDetailsListener listener;


    public SearchResultsListAdapter(Context context, List lista, DocenteDetailsListener listener) {
        this.context = context;
        this.itens = lista;
        this.listener = listener;
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
    public void onBindViewHolder(@NonNull final ResultsHolder holder, final int position) {
       final Docente docente = itens.get(position);
       holder.tvResultadoNome.setText(docente.nome.toUpperCase());
       holder.tvResultadoSetor.setText("Setor: "+docente.unidade.lotacao);

       holder.linearLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               listener.onDetailsClick(docente);
               notifyDataSetChanged();
           }
       });

    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public void clear() {
        if(!itens.isEmpty()) {
            final int size = itens.size();
            itens.clear();
            notifyItemRangeRemoved(0, size);
        }
    }


    public class ResultsHolder extends RecyclerView.ViewHolder{

        private TextView tvResultadoNome;
        private TextView tvResultadoSetor;
        private LinearLayout linearLayout;

        public ResultsHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layoutResults);
            tvResultadoNome = itemView.findViewById(R.id.tvResultadoNome);
            tvResultadoSetor = itemView.findViewById(R.id.tvResultadoSetor);

        }
    }

    public interface DocenteDetailsListener {
        public void onDetailsClick(Docente docente);
    }

}
