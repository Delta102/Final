package com.upn.Peralta.Rodriguez;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.upn.Peralta.Rodriguez.adapters.CartaAdapter;
import com.upn.Peralta.Rodriguez.adapters.DuelistaAdapter;
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

public class CartaDetalleActivity extends AppCompatActivity {

    private RecyclerView rvCartas;
    private CartaAdapter cartaAdapter;
    private List<Carta> cartas;
    private int idDuelista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carta_detalle);

        rvCartas = findViewById(R.id.rvCartas);
        rvCartas.setLayoutManager(new LinearLayoutManager(this));

        idDuelista = getIntent().getIntExtra("idDuelista", -1);

        Log.i("MAIN_APP", "id carta "+ idDuelista);

        mostrarCartas();
    }

    private void mostrarCartas() {
        ConfigDB db = ConfigDB.getInstance(CartaDetalleActivity.this);
        cartas = db.duelistaDao().getCartasByDuelistaId(idDuelista);
        cartaAdapter = new CartaAdapter(cartas);
        rvCartas.setAdapter(cartaAdapter);
    }

    private boolean hayConexionInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }
}