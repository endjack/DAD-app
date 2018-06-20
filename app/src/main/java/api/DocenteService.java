package api;

import java.util.List;
import java.util.Map;

import dto.DocenteMediasDTO;
import models.Docente;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public interface DocenteService {

    @GET("docente")
    Call<List<Docente>> getDocentes(@Query("nome") String nome);

    @GET("docente/{id}/media")
    Call<DocenteMediasDTO> getMediasById(@Path("id") Integer id);


}
