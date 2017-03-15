package com.example.zigorlopezsanpelayo.vibbayza;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by zigorlopezsanpelayo on 15/3/17.
 */

public interface ServicioUsuario {
    @GET("usuarios")
    Call<List<Usuario>> findAll();
}
