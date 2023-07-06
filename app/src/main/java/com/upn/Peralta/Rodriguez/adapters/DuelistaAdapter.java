package com.upn.Peralta.Rodriguez.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upn.Peralta.Rodriguez.CartaDetalleActivity;
import com.upn.Peralta.Rodriguez.CreateCartaActivity;
import com.upn.Peralta.Rodriguez.MostrarDuelistasActivity;
import com.upn.Peralta.Rodriguez.R;
import com.upn.Peralta.Rodriguez.entities.Duelista;

import java.util.ArrayList;
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
        Button btnCreateCart = holder.itemView.findViewById(R.id.btnCreateCarta);
        Button btnShowCart = holder.itemView.findViewById(R.id.btnVerCartas);
        tvNombre.setText(datos.get(position).nombre);

        btnCreateCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CreateCartaActivity.class);
                intent.putExtra("idDuelista", datos.get(position).id);
                v.getContext().startActivity(intent);
            }
        });

        btnShowCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CartaDetalleActivity.class);
                intent.putExtra("idDuelista", datos.get(position).id);
                v.getContext().startActivity(intent);
            }
        });
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
