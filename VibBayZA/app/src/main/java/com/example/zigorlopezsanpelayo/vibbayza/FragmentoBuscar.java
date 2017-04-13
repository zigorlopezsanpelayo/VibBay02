package com.example.zigorlopezsanpelayo.vibbayza;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Blob;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentoBuscar extends Fragment {

    protected Button botonBuscar;
    protected EditText buscarForm;
    protected String buscarFormS;
    protected boolean encontrado;
    protected TextView articuloEncontrado;
    protected LinearLayout arts;
    protected ImageView imagenArticulo;
    protected Button botonPujar;

    DatabaseReference refArticulos =
            FirebaseDatabase.getInstance().getReference()
                    .child("articulos");

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
                buscarForm.requestFocus();
                encontrado = false;
                arts = (LinearLayout) getView().findViewById(R.id.encontrados);
                arts.removeAllViews();
                buscarFormS = buscarForm.getText().toString().toLowerCase();

                refArticulos.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String titulo = (String) snapshot.child("titulo").getValue();

                            char[] tituloChar = titulo.toLowerCase().toCharArray();
                            String tituloParcial = "";
                            for (int j=0; j<tituloChar.length; j++) {
                                tituloParcial = tituloParcial + tituloChar[j];
                                if (buscarFormS.equals(tituloParcial)) {
                                    long precio = (long) snapshot.child("precio").getValue();
                                    String imagenB64 = (String) snapshot.child("nombreImagen").getValue();
                                    byte[] imagenByte = Base64.decode(imagenB64, Base64.DEFAULT);
                                    Bitmap imagen = BitmapFactory.decodeByteArray(imagenByte , 0, imagenByte.length);
                                    imagenArticulo = new ImageView(getActivity().getApplicationContext());
                                    imagenArticulo.setImageBitmap(imagen);
                                    encontrado = true;
                                    articuloEncontrado = new TextView(getActivity().getApplicationContext());
                                    articuloEncontrado.setText(titulo + "  " + precio + "€");
                                    articuloEncontrado.setTextSize(20);
                                    articuloEncontrado.setTextColor(Color.parseColor("#000000"));
                                    LinearLayout art = new LinearLayout(getActivity().getApplicationContext());
                                    botonPujar = new Button(getActivity().getApplicationContext());
                                    botonPujar.setText("Pujar");
                                    botonPujar.setBackgroundColor(Color.parseColor("#F8BBD0"));
                                    botonPujar.setPadding(10, 10, 10, 10);
                                    art.setOrientation(LinearLayout.VERTICAL);
                                    art.setGravity(Gravity.CENTER_HORIZONTAL);
                                    art.setBackgroundColor(Color.parseColor("#E3F2FD"));
                                    art.addView(articuloEncontrado);
                                    art.addView(imagenArticulo);
                                    art.addView(botonPujar);
                                    imagenArticulo.getLayoutParams().height = 350;
                                    imagenArticulo.getLayoutParams().width = 500;
                                    imagenArticulo.setImageBitmap(imagen);
                                    arts.addView(art);

                                }
                            }
                        }
                        if (!encontrado) {
                            Toast articuloNoEncontrado = Toast.makeText(getActivity().getApplicationContext(), "No hay coincidencias", Toast.LENGTH_SHORT);
                            articuloNoEncontrado.show();
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
    }


}