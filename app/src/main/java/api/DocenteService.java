package api;

import java.util.List;
import java.util.Map;

import models.Docente;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public interface DocenteService {

    //String BASE_URL = "http://10.0.2.2:8080/"; //localhost para o emulador
    String BASE_URL = "http://192.168.0.2:8080/";

    @GET("docente")
    Call<List<Docente>> getDocentes(@Query("nome") String nome);


}
