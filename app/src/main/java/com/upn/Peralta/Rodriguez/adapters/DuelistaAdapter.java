package com.upn.Peralta.Rodriguez.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.upn.Peralta.Rodriguez.MainActivity;
import com.upn.Peralta.Rodriguez.MapsActivity;
import com.upn.Peralta.Rodriguez.MostrarDuelistasActivity;
import com.upn.Peralta.Rodriguez.R;
import com.upn.Peralta.Rodriguez.database.ConfigDB;
import com.upn.Peralta.Rodriguez.entities.Carta;
import com.upn.Peralta.Rodriguez.entities.Duelista;
import com.upn.Peralta.Rodriguez.services.DuelistaService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DuelistaAdapter extends RecyclerView.Adapter{
    List<Duelista> datos;
    Context context;

    Retrofit retrofit;

    public DuelistaAdapter( List<Duelista> datos, Context context) {
        this.context = context;
        this.datos = datos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_duelista,parent,false);
        return new DuelistaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint(
            "RecyclerView") int position) {
        TextView tvNombre = holder.itemView.findViewById(R.id.txtShowDuelistName);
        Button btnCreateCart = holder.itemView.findViewById(R.id.btnCreateCarta);
        Button btnShowCart = holder.itemView.findViewById(R.id.btnVerCartas);
        Button btnMapa = holder.itemView.findViewById(R.id.btnMapa);
        tvNombre.setText(datos.get(position).nombre);
        int idDuel = datos.get(position).id;

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
                Intent intent = new Intent(holder.itemView.getContext(), CartaDetalleActivity.class);
                intent.putExtra("idDuelista", idDuel);
                holder.itemView.getContext().startActivity(intent);
            }
        });

        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), MapsActivity.class);
                intent.putExtra("idDuelista", idDuel);
                holder.itemView.getContext().startActivity(intent);
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
