package com.upn.Peralta.Rodriguez;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.upn.Peralta.Rodriguez.database.ConfigDB;
import com.upn.Peralta.Rodriguez.entities.Duelista;
import com.upn.Peralta.Rodriguez.services.DuelistaService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateDuelistaActivity extends AppCompatActivity {
    DuelistaService service;
    ConfigDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_duelista);

        EditText txtName = findViewById(R.id.txtDuelistName);
        Button btnRegister = findViewById(R.id.btnRegistro);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://63023858c6dda4f287b57c96.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(DuelistaService.class);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Duelista duelista = new Duelista();
                duelista.nombre = txtName.getText().toString();

                db = ConfigDB.getInstance(CreateDuelistaActivity.this);
                db.duelistaDao().createDuelist(duelista);
                Log.i("MAIN_APP", "Se registró el duelista en la base de datos local");
                /*if (hayConexionInternet()) {
                    Log.i("MAIN_APP", "hay internet");
                    // Registrar directamente en el web service
                    Call<Void> call = service.createDuelist(duelista);

                    Log.i("MAIN_APP", "Se registró el duelista en la base de datos local");
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.i("MAIN_APP", "Se registró el duelista en el web service");
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.i("MAIN_APP", "No se registró el duelista en el web service");
                        }
                    });
                } else {
                    Log.i("MAIN_APP", "No hay internet");
                    db.duelistaDao().createDuelist(duelista);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }*/
            }
        });
    }

    private boolean hayConexionInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}