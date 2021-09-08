package com.sahungra.digitalsahungra.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.sahungra.digitalsahungra.R;

import java.util.List;

public class dashboard_graph_values_rv_adapter extends RecyclerView.Adapter<dashboard_graph_values_rv_adapter.ViewHolder> {

    private Context context;
    private List<Integer> values;


    public dashboard_graph_values_rv_adapter(Context context, List<Integer> uploads) {
        this.values = uploads;
        this.context = context;

    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.dashboard_values_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {






        ObjectAnimator.ofInt(holder.progressBar, "progress",20)
                .setDuration(900)
                .start();






    }

    @Override
    public int getItemCount() {
        return values.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;


        View item;

        public ViewHolder(final View itemView) {
            super(itemView);
            progressBar=itemView.findViewById(R.id.progless);

            this.item = itemView;

        }


    }


}
