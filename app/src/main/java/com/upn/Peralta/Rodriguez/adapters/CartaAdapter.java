package com.upn.Peralta.Rodriguez.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upn.Peralta.Rodriguez.CartaDetalleActivity;
import com.upn.Peralta.Rodriguez.R;
import com.upn.Peralta.Rodriguez.entities.Carta;
import com.upn.Peralta.Rodriguez.entities.Duelista;

import org.w3c.dom.Text;

import java.util.List;

public class CartaAdapter extends RecyclerView.Adapter{
    List<Carta> datos;
    public CartaAdapter( List<Carta> datos) {
        this.datos = datos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_carta,parent,false);
        return new CartaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Carta cartas = datos.get(position);

        TextView tvNombre = holder.itemView.findViewById(R.id.txtNameCart);
        TextView txtAtaque = holder.itemView.findViewById(R.id.txtPtA);
        TextView txtDefensa = holder.itemView.findViewById(R.id.txtPtD);

        tvNombre.setText("Nombre: "+ cartas.nombreCarta);
        txtAtaque.setText("Ataque: "+ String.valueOf(cartas.ptosAtaque));
        txtDefensa.setText("Defensa: "+ String.valueOf(cartas.idDuelista));

    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    static class CartaViewHolder extends RecyclerView.ViewHolder{
        public CartaViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
