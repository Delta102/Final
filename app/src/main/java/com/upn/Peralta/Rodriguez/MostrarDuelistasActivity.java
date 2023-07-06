package com.upn.Peralta.Rodriguez;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.upn.Peralta.Rodriguez.adapters.DuelistaAdapter;
import com.upn.Peralta.Rodriguez.database.ConfigDB;
import com.upn.Peralta.Rodriguez.entities.Duelista;
import com.upn.Peralta.Rodriguez.services.DuelistaService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MostrarDuelistasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_duelistas);

        RecyclerView rvDuelistas = findViewById(R.id.rvDuelistas);

        // Obtener datos de la base de datos local
        List<Duelista> duelistas = (List<Duelista>) getIntent().getSerializableExtra("duelistas");

        if (duelistas != null) {
            Log.i("MAIN_APP", "Hay: " + duelistas.size());
            rvDuelistas.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rvDuelistas.setAdapter(new DuelistaAdapter(duelistas));
        } else {
            Log.e("MAIN_APP", "La lista de duelistas es nula");
        }
    }
}