package com.example.spectacleplus;

import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import java.util.List;

public interface ApiService {
    @GET("spectacles")
    Call<List<Spectacle>> getSpectacles();
    @POST("reserver")
    @Headers("Content-Type: application/json")
    Call<Void> enregistrerReservation(@Body Reservation reservation);
}