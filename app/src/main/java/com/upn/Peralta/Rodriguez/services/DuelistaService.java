package com.upn.Peralta.Rodriguez.services;

import com.upn.Peralta.Rodriguez.entities.Duelista;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DuelistaService {
    @GET("Duelista")
    Call<List<Duelista>> getAllDuelists();
    @POST("Duelista")
    Call<Void> createDuelist(@Body Duelista duelista);
}
