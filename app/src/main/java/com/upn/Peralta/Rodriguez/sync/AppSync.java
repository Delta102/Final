package com.upn.Peralta.Rodriguez.sync;

import android.content.Context;
import android.util.Log;

import com.upn.Peralta.Rodriguez.CartaDetalleActivity;
import com.upn.Peralta.Rodriguez.MainActivity;
import com.upn.Peralta.Rodriguez.database.ConfigDB;
import com.upn.Peralta.Rodriguez.entities.Duelista;
import com.upn.Peralta.Rodriguez.services.DuelistaService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppSync {

    private static AppSync instance;
    private DuelistaService service;
    private ConfigDB db;

    private AppSync(Context context) {
        db = ConfigDB.getInstance(context);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://63023858c6dda4f287b57c96.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(DuelistaService.class);
    }

    public static AppSync getInstance(Context context) {
        if (instance == null) {
            synchronized (AppSync.class) {
                if (instance == null) {
                    instance = new AppSync(context);
                }
            }
        }
        return instance;
    }

    public void syncDataWithWebService() {
        List<Duelista> duelistas = db.duelistaDao().listarDuelistas();

        for (Duelista duelist : duelistas) {
            Call<Void> call = service.createDuelist(duelist);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.i("MAIN_APP", "Se sincronizó un duelist con el web service");
                    //yourDuelistaDao.deleteDuelist(duelist);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.i("MAIN_APP", "No se sincronizó un duelist con el web service");
                }
            });
        }
    }

    public void syncWebServiceWithDataBase(Call<List<Duelista>> call) {
        call.enqueue(new Callback<List<Duelista>>() {
            @Override
            public void onResponse(Call<List<Duelista>> call, Response<List<Duelista>> response) {
                if (response.isSuccessful()) {
                    List<Duelista> duelistas = response.body();
                    if (duelistas != null) {
                        // Guardar los nuevos datos en la base de datos local
                        db.duelistaDao().deleteAllDuelists();
                        for (Duelista duelista : duelistas) {
                            db.duelistaDao().createDuelist(duelista);
                        }
                    } else {
                        Log.e("MAIN_APP", "La respuesta no contiene datos");
                    }
                } else {
                    Log.e("MAIN_APP", "Error en la respuesta: ");
                }
            }

            @Override
            public void onFailure(Call<List<Duelista>> call, Throwable t) {
                Log.e("MAIN_APP", "Error en la solicitud de sincronización: " + t.getMessage());
            }
        });
    }
}