package com.example.ender.dadapp;

import java.util.List;

import models.Docente;
import models.Unidade;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface RetrofitConfig {

    public static final String BASE_URL = "http://10.0.2.2:8080/"; //localhost para o emulador
    //String BASE_URL = "http://localhost:8080/";

    @GET("unidade")
    Call<List<Unidade>> getUnidades();

    @GET("unidade/{id}")
    Call<Unidade> getUnidade(@Path("id") Integer id);

    @GET("docente")
    Call<List<Docente>> getDocentes();

    @GET("docente/nome/{nome}")
    Call<Docente> getDocente(@Path("nome") String nome);


    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
