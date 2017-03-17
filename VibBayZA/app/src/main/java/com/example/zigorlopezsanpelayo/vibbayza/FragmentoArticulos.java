package com.example.zigorlopezsanpelayo.vibbayza;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Blob;
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
    protected ImageView imagenArticulo;

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
                    final String imagenB64 = articulo.getNombreImagen();
                    byte[] imagenByte = Base64.decode(imagenB64, Base64.DEFAULT);
                    Bitmap imagen = BitmapFactory.decodeByteArray(imagenByte , 0, imagenByte.length);
                    imagenArticulo = new ImageView(getActivity().getApplicationContext());
                    articuloEncontrado = new TextView(getActivity().getApplicationContext());
                    articuloEncontrado.setText(nombreArt + "  " + precio + "€");
                    articuloEncontrado.setTextSize(20);
                    articuloEncontrado.setTextColor(Color.parseColor("#000000"));
                    LinearLayout arts = (LinearLayout) getView().findViewById(R.id.fragmento_articulos);
                    LinearLayout art = new LinearLayout(getActivity().getApplicationContext());
                    art.addView(articuloEncontrado);
                    art.addView(imagenArticulo);
                    imagenArticulo.getLayoutParams().height = 300;
                    imagenArticulo.getLayoutParams().width = 300;
                    imagenArticulo.setImageBitmap(imagen);
                    arts.addView(art);
                }
            }
            @Override
            public void onFailure(Call<List<Articulos>> call, Throwable t) {

            }
        });
    }

}