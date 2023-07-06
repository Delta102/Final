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
        rvDuelistas.setLayoutManager(new LinearLayoutManager(this));

        ConfigDB db = ConfigDB.getInstance(MostrarDuelistasActivity.this);

        // Verificar si hay conexión a internet disponible
        if (hayConexionInternet()) {
            Log.i("MAIN_APP", "Hay internet");
            // Si hay conexión a internet, realizar sincronización de datos
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://63023858c6dda4f287b57c96.mockapi.io/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            DuelistaService service = retrofit.create(DuelistaService.class);
            Call<List<Duelista>> call = service.getAllDuelists();

            sincronizacionData(call, db);
        }
        else
            Log.i("MAIN_APP", "No Hay internet");

// Obtener datos de la base de datos local
        List<Duelista> duelistas = db.duelistaDao().listarDuelistas();
        rvDuelistas.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvDuelistas.setAdapter(new DuelistaAdapter(duelistas));
    }

    private boolean hayConexionInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    void sincronizacionData(Call<List<Duelista>> call, ConfigDB db) {
        call.enqueue(new Callback<List<Duelista>>() {
            @Override
            public void onResponse(Call<List<Duelista>> call, Response<List<Duelista>> response) {
                if (response.isSuccessful()) {
                    List<Duelista> duelistas = response.body();
                    if (duelistas != null) {
                        // Eliminar todos los registros existentes en la base de datos
                        db.duelistaDao().deleteAllDuelists();

                        // Agregar los nuevos datos desde la API a la base de datos
                        for (int i = 0; i < duelistas.size(); i++) {
                            db.duelistaDao().createDuelist(duelistas.get(i));
                        }

                        Log.i("MAIN_APP", "Datos Sincronizados");
                    } else {
                        Log.e("MAIN_APP", "La respuesta no contiene datos");
                    }
                } else {
                    Log.e("MAIN_APP", "Error en la respuesta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Duelista>> call, Throwable t) {
                Log.e("MAIN_APP", "Error en la solicitud de sincronización: " + t.getMessage());
            }
        });
    }
}