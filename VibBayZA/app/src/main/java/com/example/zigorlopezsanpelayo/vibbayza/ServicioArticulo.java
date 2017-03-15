package com.example.zigorlopezsanpelayo.vibbayza;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by zigorlopezsanpelayo on 15/3/17.
 */

public interface ServicioArticulo {
    @GET("articulos")
    Call<List<Articulos>> findAll();

    @GET("articulos/Articulos/{email}")
    Call<List<Articulos>> findArtByEmail(@Path("email") String email);

    @POST("articulos")
    Call<Articulos> create(Articulos entity);

    @PUT("articulos/{id}")
    Call<Articulos> edit(@Path("id") int id, Articulos entity);

    @DELETE("articulos/{id}")
    Call<Articulos> remove(@Path("id") int id);
}
