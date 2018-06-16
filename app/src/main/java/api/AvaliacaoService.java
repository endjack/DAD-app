package api;

import java.util.List;

import models.Avaliacao;
import models.Docente;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface AvaliacaoService {

    //String BASE_URL = "http://10.0.2.2:8080/"; //localhost para o emulador
    String BASE_URL = "http://192.168.0.2:8080/";

    @GET("avaliacao")
    Call<List<Avaliacao>> getAvaliacoes(@Query("id_docente") String nome);


}
