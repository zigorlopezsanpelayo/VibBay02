package com.example.zigorlopezsanpelayo.vibbayza;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentoArticulos extends Fragment {

    protected TextView articuloEncontrado;
    protected ImageView imagenArticulo;
    protected Button botonEditar;

    DatabaseReference refArticulos =
            FirebaseDatabase.getInstance().getReference()
                    .child("articulos");

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
        final String emailUsuario = getActivity().getIntent().getExtras().getString("emailUsuario");

        refArticulos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String propietario = (String) snapshot.child("email").getValue();
                    if (propietario.equals(emailUsuario)) {
                        String titulo = (String) snapshot.child("titulo").getValue();
                        long precio = (long) snapshot.child("precio").getValue();

                        String imagenB64 = (String) snapshot.child("nombreImagen").getValue();
                        byte[] imagenByte = Base64.decode(imagenB64, Base64.DEFAULT);
                        Bitmap imagen = BitmapFactory.decodeByteArray(imagenByte , 0, imagenByte.length);
                        imagenArticulo = new ImageView(getActivity().getApplicationContext());
                        articuloEncontrado = new TextView(getActivity().getApplicationContext());
                        articuloEncontrado.setText(titulo + "  " + precio + "€");
                        articuloEncontrado.setTextSize(20);
                        articuloEncontrado.setTextColor(Color.parseColor("#000000"));
                        LinearLayout arts = (LinearLayout) getView().findViewById(R.id.fragmento_articulos);
                        LinearLayout art = new LinearLayout(getActivity().getApplicationContext());
                        botonEditar = new Button(getActivity().getApplicationContext());
                        botonEditar.setText("Editar");
                        botonEditar.setBackgroundColor(Color.parseColor("#F8BBD0"));
                        botonEditar.setPadding(10, 10, 10, 10);
                        art.setOrientation(LinearLayout.VERTICAL);
                        art.setGravity(Gravity.CENTER_HORIZONTAL);
                        art.setBackgroundColor(Color.parseColor("#E3F2FD"));
                        art.addView(articuloEncontrado);
                        art.addView(imagenArticulo);
                        art.addView(botonEditar);
                        imagenArticulo.getLayoutParams().height = 350;
                        imagenArticulo.getLayoutParams().width = 500;
                        imagenArticulo.setImageBitmap(imagen);
                        arts.addView(art);
                    }

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}