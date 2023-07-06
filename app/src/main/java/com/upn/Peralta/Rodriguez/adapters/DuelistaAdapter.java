package com.upn.Peralta.Rodriguez.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upn.Peralta.Rodriguez.R;
import com.upn.Peralta.Rodriguez.entities.Duelista;

import java.util.List;

public class DuelistaAdapter extends RecyclerView.Adapter{
    List<Duelista> datos;

    public DuelistaAdapter(List<Duelista> datos) {
        this.datos=datos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_duelista,parent,false);
        return new DuelistaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView tvNombre = holder.itemView.findViewById(R.id.txtShowDuelistName);
        tvNombre.setText(datos.get(position).nombre);
        Log.i("MAIN_APP", datos.get(position).nombre);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    static class DuelistaViewHolder extends RecyclerView.ViewHolder{
        public DuelistaViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
