package com.upn.Peralta.Rodriguez;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.upn.Peralta.Rodriguez.adapters.DuelistaAdapter;
import com.upn.Peralta.Rodriguez.entities.Duelista;
import com.upn.Peralta.Rodriguez.services.DuelistaService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCreate = findViewById(R.id.btnCrearDuelista);
        Button btnShow = findViewById(R.id.btnMostrarDuelistas);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), CreateDuelistaActivity.class);
                startActivity(intent);
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar si hay conexión a internet disponible
                if (hayConexionInternet()) {
                    // Si hay conexión a internet, realizar sincronización de datos
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://63023858c6dda4f287b57c96.mockapi.io/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    DuelistaService service = retrofit.create(DuelistaService.class);
                    Call<List<Duelista>> call = service.getAllDuelists();

                    sincronizacionData(call);
                }
                List<Duelista> duelistas = db.duelistaDao().listarDuelistas();
                rvDuelistas.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rvDuelistas.setAdapter(new DuelistaAdapter(duelistas));
            }
        });
    }

    private boolean hayConexionInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    void sincronizacionData(Call<List<Duelista>> call) {
        call.enqueue(new Callback<List<Duelista>>() {
            @Override
            public void onResponse(Call<List<Duelista>> call, Response<List<Duelista>> response) {
                if (response.isSuccessful()) {
                    List<Duelista> duelistas = response.body();
                    if (duelistas != null) {
                        // Guardar los nuevos datos en la base de datos local
                        ConfigDB db = ConfigDB.getInstance(MainActivity.this);
                        db.duelistaDao().deleteAllDuelists();
                        for (Duelista duelista : duelistas) {
                            db.duelistaDao().createDuelist(duelista);
                        }
                        mostrarDuelistas(duelistas);
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

    void mostrarDuelistas(List<Duelista> duelistas) {
        intent = new Intent(getApplicationContext(), MostrarDuelistasActivity.class);
        intent.putParcelableArrayListExtra("duelistas", new ArrayList<>(duelistas));
        startActivity(intent);
    }
}