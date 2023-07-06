package com.upn.Peralta.Rodriguez;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

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
        rvDuelistas.setLayoutManager(new LinearLayoutManager(this));

        ConfigDB db = ConfigDB.getInstance(MostrarDuelistasActivity.this);

        // Verificar si hay conexión a internet disponible
        if (hayConexionInternet()) {
            // Si hay conexión a internet, realizar sincronización de datos
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://63023858c6dda4f287b57c96.mockapi.io/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            DuelistaService service = retrofit.create(DuelistaService.class);
            Call<List<Duelista>> call = service.getAllDuelists();

            sincronizacionData(call, db);
        } else {
            // Si no hay conexión a internet, cargar los datos almacenados en la base de datos local
            List<Duelista> duelistas = db.duelistaDao().listarDuelistas();
            rvDuelistas.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rvDuelistas.setAdapter(new DuelistaAdapter(duelistas));
        }
    }

    private boolean hayConexionInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    void sincronizacionData(Call<List<Duelista>> call, ConfigDB db) {
        Call<List<Duelista>> newCall = call.clone();
        newCall.enqueue(new Callback<List<Duelista>>() {

            @Override
            public void onResponse(Call<List<Duelista>> call, Response<List<Duelista>> response) {
                
            }

            @Override
            public void onFailure(Call<List<Duelista>> call, Throwable t) {

            }
        }
    }
}