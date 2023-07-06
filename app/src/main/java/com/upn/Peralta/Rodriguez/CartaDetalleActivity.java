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

        Log.i("MAIN_APP", "id "+ idDuelista);

        if (hayConexionInternet()) {
            // Si hay conexión a internet, realizar sincronización de datos
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://63023858c6dda4f287b57c96.mockapi.io/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            DuelistaService service = retrofit.create(DuelistaService.class);
            Call<List<Carta>> call = service.getAllCarts();

            sincronizarDatos(call);
        } else {
            // Si no hay conexión a internet, obtener los datos locales
            obtenerDatosLocales();
        }
    }

    private void sincronizarDatos(Call<List<Carta>> call) {
        call.enqueue(new Callback<List<Carta>>() {
            @Override
            public void onResponse(Call<List<Carta>> call, Response<List<Carta>> response) {
                if (response.isSuccessful()) {
                    List<Carta> cartas = response.body();
                    if (cartas != null) {
                        // Guardar los nuevos datos en la base de datos local
                        ConfigDB db = ConfigDB.getInstance(CartaDetalleActivity.this);
                        db.duelistaDao().deleteAllCarts();
                        for (Carta carta : cartas) {
                            db.duelistaDao().createCart(carta);
                        }
                        mostrarCartas(cartas);
                    } else {
                        Log.e("CARTA_DETALLE", "La respuesta no contiene datos");
                    }
                } else {
                    Log.e("CARTA_DETALLE", "Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Carta>> call, Throwable t) {
                Log.e("CARTA_DETALLE", "Error en la solicitud de sincronización: " + t.getMessage());
                // En caso de error, obtener los datos locales
                obtenerDatosLocales();
            }
        });
    }

    private void obtenerDatosLocales() {
        ConfigDB db = ConfigDB.getInstance(CartaDetalleActivity.this);
        List<Carta> cartas = db.duelistaDao().getCartasByDuelistaId(idDuelista);
        mostrarCartas(cartas);
    }

    private void mostrarCartas(List<Carta> cartas) {
        this.cartas = cartas;
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