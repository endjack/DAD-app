package com.example.ender.dadapp;

import java.util.List;

import models.Docente;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface DocenteService {

    String BASE_URL = "http://10.0.2.2:8080/"; //localhost para o emulador
    //String BASE_URL = "http://localhost:8080/";

    @GET("docente")
    Call<List<Docente>> getDocentes();

    @GET("docente/nome/{nome}")
    Call<Docente> getDocente(@Path("nome") String nome);

}
