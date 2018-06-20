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

import models.ComponenteCurricular;
import models.Docente;

public class ListComponentesAdapter extends RecyclerView.Adapter<ListComponentesAdapter.ResultsHolder> {

    private Context context;
    private List<ComponenteCurricular> itens;
    ComponenteDetailsListener listener;

    public ListComponentesAdapter(Context context, List lista, ComponenteDetailsListener listener) {
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
       final ComponenteCurricular componenteCurricular = itens.get(position);
       holder.tvResultadoNome.setText(componenteCurricular.nome.toUpperCase());
       holder.tvResultadoSetor.setText("Código: "+componenteCurricular.codigo);

       holder.linearLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               listener.onDetailsClick(componenteCurricular);
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

    public interface ComponenteDetailsListener {
        void onDetailsClick(ComponenteCurricular componente);
    }

}
