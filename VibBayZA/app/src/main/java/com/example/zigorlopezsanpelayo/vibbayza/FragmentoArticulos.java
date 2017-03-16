package com.example.zigorlopezsanpelayo.vibbayza;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.zigorlopezsanpelayo.vibbayza.R.menu.main;

public class FragmentoArticulos extends Fragment {

    String BASE_URL = "http://192.168.0.22:8084/jsonweb/rest/";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    protected ServicioArticulo servicio = retrofit.create(ServicioArticulo.class);
    protected TextView articuloEncontrado;

    public FragmentoArticulos() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragmento_articulos, container, false);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        menu.setGroupVisible(R.id.grupoAniadirArticulo, true);
    }

    public void onViewCreated (View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        String emailUsuario = getActivity().getIntent().getExtras().getString("emailUsuario");
        Call<List<Articulos>> call = servicio.findArtByEmail(emailUsuario);
        call.enqueue(new Callback<List<Articulos>>() {
            @Override
            public void onResponse(Call<List<Articulos>> call, Response<List<Articulos>> response) {
                List<Articulos> articulos = response.body();
                for (final Articulos articulo : articulos) {
                    final String nombreArt = articulo.getTitulo();
                    final float precio = articulo.getPrecio();
                    articuloEncontrado = new TextView(getActivity().getApplicationContext());
                    articuloEncontrado.setText(nombreArt + "  " + precio + "€");
                    articuloEncontrado.setTextSize(35);
                    articuloEncontrado.setTextColor(Color.RED);
                    LinearLayout arts = (LinearLayout) getView().findViewById(R.id.fragmento_articulos);
                    arts.addView(articuloEncontrado);
                }
            }
            @Override
            public void onFailure(Call<List<Articulos>> call, Throwable t) {

            }
        });
    }

}