package com.example.zigorlopezsanpelayo.vibbayza;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

public class FragmentoBuscar extends Fragment {

    String BASE_URL = "http://192.168.0.16:8084/jsonweb/rest/";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    protected ServicioArticulo servicio = retrofit.create(ServicioArticulo.class);
    protected Button botonBuscar;
    protected EditText buscarForm;
    protected String buscarFormS;
    protected boolean encontrado;
    protected TextView articuloEncontrado;

    public FragmentoBuscar() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragmento_buscar, container, false);
        return v;
    }

    public void onViewCreated (View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        buscarForm = (EditText) v.findViewById(R.id.campo_buscar);
        buscarForm.requestFocus();
        botonBuscar = (Button) v.findViewById(R.id.boton_buscar);
        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarFormS = buscarForm.getText().toString().toLowerCase();
                Call<List<Articulos>> call = servicio.findAll();
                call.enqueue(new Callback<List<Articulos>>() {
                    @Override
                    public void onResponse(Call<List<Articulos>> call, Response<List<Articulos>> response) {
                        List<Articulos> articulos = response.body();
                        for (final Articulos articulo : articulos) {
                            Log.i("String", articulo.getTitulo());
                            char[] tituloChar = articulo.getTitulo().toLowerCase().toCharArray();
                            String tituloParcial = "";
                            for (int j=0; j<tituloChar.length; j++) {
                                tituloParcial = tituloParcial + tituloChar[j];
                                if (buscarFormS.equals(tituloParcial)) {
                                    final String nombreArt = articulo.getTitulo();
                                    final float precio = articulo.getPrecio();
                                    encontrado = true;
                                    articuloEncontrado = new TextView(getActivity().getApplicationContext());
                                    articuloEncontrado.setText(nombreArt + "  " + precio + "€");
                                    articuloEncontrado.setTextSize(35);
                                    articuloEncontrado.setTextColor(Color.RED);
                                    LinearLayout arts = (LinearLayout) getView().findViewById(R.id.busqueda);
                                    arts.addView(articuloEncontrado);
                                }
                            }
                        }
                        if (!encontrado) {
                            Toast articuloNoEncontrado = Toast.makeText(getActivity().getApplicationContext(), "No hay coincidencias", Toast.LENGTH_SHORT);
                            articuloNoEncontrado.show();
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Articulos>> call, Throwable t) {

                    }
                });
            }
        });
    }
}