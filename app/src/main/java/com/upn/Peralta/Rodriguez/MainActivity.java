package com.upn.Peralta.Rodriguez;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

public class MainActivity extends AppCompatActivity {
    Intent intent;
    DuelistaService duelistaService;
    ConfigDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCreate = findViewById(R.id.btnCrearDuelista);
        Button btnMostrar = findViewById(R.id.btnMostrarDuelistas);
        Button btnSync = findViewById(R.id.btnSync);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://63023858c6dda4f287b57c96.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        duelistaService = retrofit.create(DuelistaService.class);
        db = ConfigDB.getInstance(MainActivity.this);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), CreateDuelistaActivity.class);
                startActivity(intent);
            }
        });

        btnMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Duelista> duelistas = db.duelistaDao().listarDuelistas();
                mostrarDuelistas(duelistas);
            }
        });

        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los datos de la base de datos
                List<Duelista> duelistasDB = db.duelistaDao().listarDuelistas();
                List<Carta> cartasDB = db.duelistaDao().getAllCarts();

                // Obtener los datos del web service
                Call<List<Duelista>> callDuelistas = duelistaService.getAllDuelists();
                Call<List<Carta>> callCartas = duelistaService.getAllCarts();

                callDuelistas.enqueue(new Callback<List<Duelista>>() {
                    @Override
                    public void onResponse(Call<List<Duelista>> call, Response<List<Duelista>> response) {
                        if (response.isSuccessful()) {
                            List<Duelista> duelistasWS = response.body();

                            // Realizar la sincronización de los duelistas
                            sincronizarDuelistas(duelistasDB, duelistasWS, db);
                        } else {
                            // Error en la solicitud al web service para obtener los duelistas
                            Log.e("Sync", "Error al obtener los duelistas del web service: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Duelista>> call, Throwable t) {
                        // Error en la comunicación con el web service al obtener los duelistas
                        Log.e("Sync", "Error al obtener los duelistas del web service: " + t.getMessage());
                    }
                });

                callCartas.enqueue(new Callback<List<Carta>>() {
                    @Override
                    public void onResponse(Call<List<Carta>> call, Response<List<Carta>> response) {
                        if (response.isSuccessful()) {
                            List<Carta> cartasWS = response.body();

                            // Realizar la sincronización de las cartas
                            sincronizarCartas(cartasDB, cartasWS, db);
                        } else {
                            // Error en la solicitud al web service para obtener las cartas
                            Log.e("Sync", "Error al obtener las cartas del web service: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Carta>> call, Throwable t) {
                        // Error en la comunicación con el web service al obtener las cartas
                        Log.e("Sync", "Error al obtener las cartas del web service: " + t.getMessage());
                    }
                });
            }
        });
    }

    private void sincronizarDuelistas(List<Duelista> duelistasDB, List<Duelista> duelistasWS, ConfigDB db) {
        // Obtener los IDs de los duelistas de la base de datos
        List<Integer> dbIds = new ArrayList<>();
        for (Duelista duelistaDB : duelistasDB) {
            dbIds.add(duelistaDB.getId());
        }

        // Obtener los IDs de los duelistas del web service
        List<Integer> wsIds = new ArrayList<>();
        for (Duelista duelistaWS : duelistasWS) {
            wsIds.add(duelistaWS.getId());
        }

        // Sincronizar desde la base de datos al web service
        for (Duelista duelistaDB : duelistasDB) {
            if (!wsIds.contains(duelistaDB.getId())) {
                // El duelista de la base de datos no existe en el web service, crearlo
                Call<Void> callCreateDuelist = duelistaService.createDuelist(duelistaDB);
                callCreateDuelist.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            // Duelista creado en el web service
                        } else {
                            // Error al crear el duelista en el web service
                            Log.e("Sync", "Error al crear el duelista en el web service: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // Error en la comunicación con el web service al crear el duelista
                        Log.e("Sync", "Error al crear el duelista en el web service: " + t.getMessage());
                    }
                });
            }
        }

        // Sincronizar desde el web service a la base de datos
        for (Duelista duelistaWS : duelistasWS) {
            if (!dbIds.contains(duelistaWS.getId())) {
                // El duelista del web service no existe en la base de datos, guardarlo en la base de datos
                db.duelistaDao().createDuelist(duelistaWS);
            }
        }
    }

    private void sincronizarCartas(List<Carta> cartasDB, List<Carta> cartasWS, ConfigDB db) {
        // Obtener los IDs de las cartas de la base de datos
        List<Integer> dbIds = new ArrayList<>();
        for (Carta cartaDB : cartasDB) {
            dbIds.add(cartaDB.getIdCarta());
        }

        // Obtener los IDs de las cartas del web service
        List<Integer> wsIds = new ArrayList<>();
        for (Carta cartaWS : cartasWS) {
            wsIds.add(cartaWS.getIdCarta());
        }

        // Sincronizar desde la base de datos al web service
        for (Carta cartaDB : cartasDB) {
            if (!wsIds.contains(cartaDB.getIdCarta())) {
                // La carta de la base de datos no existe en el web service, crearla
                Call<Void> callCreateCard = duelistaService.createCart(cartaDB);
                callCreateCard.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            // Carta creada en el web service
                        } else {
                            // Error al crear la carta en el web service
                            Log.e("Sync", "Error al crear la carta en el web service: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // Error en la comunicación con el web service al crear la carta
                        Log.e("Sync", "Error al crear la carta en el web service: " + t.getMessage());
                    }
                });
            }
        }

        // Sincronizar desde el web service a la base de datos
        for (Carta cartaWS : cartasWS) {
            if (!dbIds.contains(cartaWS.getIdCarta())) {
                // La carta del web service no existe en la base de datos, guardarla en la base de datos
                db.duelistaDao().createCart(cartaWS);
            }
        }
    }

    void mostrarDuelistas(List<Duelista> duelistas) {
        intent = new Intent(getApplicationContext(), MostrarDuelistasActivity.class);
        intent.putExtra("duelistas", (ArrayList<Duelista>) duelistas);
        startActivity(intent);
    }
}