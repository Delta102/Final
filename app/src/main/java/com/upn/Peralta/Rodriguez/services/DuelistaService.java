package com.upn.Peralta.Rodriguez.services;

import com.google.gson.annotations.SerializedName;
import com.upn.Peralta.Rodriguez.entities.Carta;
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

    @POST("Carta")
    Call<Void> createCart(@Body Carta carta);

    @POST("image")
    Call<ImageResponse> subirImagen(@Body ImageToSave image);

    class ImageResponse{
        @SerializedName("url")
        private String url;

        public String getUrl(){
            return url;
        }
    }

    class ImageToSave{
        String base64Image;

        public ImageToSave(String base64Image){
            this.base64Image = base64Image;
        }
    }
}
