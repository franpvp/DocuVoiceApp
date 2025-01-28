package com.example.semana01.components;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("usuarios/")
    fun createUsuario(@Body usuario: Usuario): Call<Void>
    
}
