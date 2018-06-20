package api;

import java.util.List;

import models.Avaliacao;
import models.Docente;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface AvaliacaoService {

    @GET("avaliacao")
    Call<List<Avaliacao>> getAvaliacoes(@Query("id_docente") String nome);

    @GET("avaliacao")
    Call<List<Avaliacao>> getAvaliacoesByComponente(@Query("id_componente") Integer id);

}
