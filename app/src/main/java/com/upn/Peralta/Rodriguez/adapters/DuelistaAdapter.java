package com.upn.Peralta.Rodriguez.adapters;

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
                if (hayConexionInternet()) {
                    // Si hay conexión a internet, realizar sincronización de datos
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://63023858c6dda4f287b57c96.mockapi.io/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    DuelistaService service = retrofit.create(DuelistaService.class);
                    Call<List<Carta>> call = service.getAllCarts();

                    sincronizacionData(call, holder);
                }else{
                    ConfigDB db = ConfigDB.getInstance(holder.itemView.getContext());
                    List<Carta> cartas = db.duelistaDao().getAllCarts();
                    mostrarCartas(cartas, holder);
                }
            }
        });
    }

    void sincronizacionData(Call<List<Carta>> call, RecyclerView.ViewHolder holder) {
        call.enqueue(new Callback<List<Carta>>() {
            @Override
            public void onResponse(Call<List<Carta>> call, Response<List<Carta>> response) {
                if (response.isSuccessful()) {
                    List<Carta> cartas = response.body();
                    if (cartas != null) {
                        // Guardar los nuevos datos en la base de datos local
                        ConfigDB db = ConfigDB.getInstance(holder.itemView.getContext());
                        db.duelistaDao().deleteAllCarts();
                        for (Carta carta : cartas) {
                            db.duelistaDao().createCart(carta);
                        }
                        mostrarCartas(cartas, holder);
                    } else {
                        Log.e("MAIN_APP", "La respuesta no contiene datos");
                    }
                } else {
                    Log.e("MAIN_APP", "Error en la respuesta: ");
                }
            }

            @Override
            public void onFailure(Call<List<Carta>> call, Throwable t) {
                Log.e("MAIN_APP", "Error en la solicitud de sincronización: " + t.getMessage());
            }
        });
    }

    void mostrarCartas(List<Carta> cartas, RecyclerView.ViewHolder holder) {
        Intent intent = new Intent(holder.itemView.getContext(), CartaDetalleActivity.class);
        intent.putExtra("cartas", new ArrayList<>(cartas));
        intent.putExtra("idDuelista", datos.get(holder.getAdapterPosition()).id);
        holder.itemView.getContext().startActivity(intent);
    }

    private boolean hayConexionInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
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
