package api;

import java.util.List;

import models.ComponenteCurricular;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ComponenteService {
    @GET("componente")
    Call<List<ComponenteCurricular>> getComponentes(@Query("nome_componente_curricular") String nome);
}
