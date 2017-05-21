package br.com.postero.ifrnmessenger.adapters;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.postero.ifrnmessenger.R;
import br.com.postero.ifrnmessenger.models.Disciplina;

/**
 * Created by Francisco on 21/05/2017.
 */

public class DisciplinaAdapter extends RecyclerView.Adapter<DisciplinaAdapter.ViewHolder> {

    private ArrayList<Disciplina> disciplinas;

    public DisciplinaAdapter(ArrayList<Disciplina> disciplinas) {
        this.disciplinas= disciplinas;
    }

    @Override
    public DisciplinaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_disciplina, parent, false);

        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(DisciplinaAdapter.ViewHolder holder, int position) {

        Disciplina disciplina = disciplinas.get(position);

        holder.lblDescricao.setText(disciplina.disciplina);
    }

    @Override
    public int getItemCount() {
        return disciplinas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View itemView;
        public TextView lblDescricao;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            lblDescricao = (TextView) itemView.findViewById(R.id.lblDescricao);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            TextView lblMovimentacaoDescricao = (TextView) v.findViewById(R.id.lblDescricao);
            Snackbar.make(v, lblMovimentacaoDescricao.getText(), Snackbar.LENGTH_LONG).show();
        }
    }
}
