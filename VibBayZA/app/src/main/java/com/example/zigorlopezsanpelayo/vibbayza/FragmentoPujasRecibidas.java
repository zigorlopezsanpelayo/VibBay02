package com.example.zigorlopezsanpelayo.vibbayza;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.renderscript.ScriptIntrinsicBLAS.RIGHT;
import static android.view.Gravity.LEFT;

public class FragmentoPujasRecibidas extends Fragment {

    protected TextView pujaEncontrada;
    protected Button botonAceptarPuja;

    DatabaseReference refPujas =
            FirebaseDatabase.getInstance().getReference()
                    .child("pujas");

    DatabaseReference refArticulos =
            FirebaseDatabase.getInstance().getReference()
                    .child("articulos");

    public FragmentoPujasRecibidas() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragmento_pujas_recibidas, container, false);

    }

    @Override
    public void onViewCreated (View v, Bundle savedInstanceState) {
        refPujas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String nombreArticuloPujado = (String) snapshot.child("titulo").getValue();
                    String cantidad = (String) snapshot.child("cantidad").getValue();
                    final String titulo = getArguments().getString("titulo");

                    if (nombreArticuloPujado.equals(titulo)) {
                        pujaEncontrada = new TextView(getActivity().getApplicationContext());
                        pujaEncontrada.setText("" + cantidad + "€");
                        pujaEncontrada.setBackgroundColor(Color.WHITE);
                        pujaEncontrada.setTextColor(Color.BLACK);
                        pujaEncontrada.setTextSize(30);

                        botonAceptarPuja = new Button(getActivity().getApplicationContext());
                        botonAceptarPuja.setText("Aceptar puja");
                        botonAceptarPuja.setBackgroundColor(Color.parseColor("#F8BBD0"));
                        botonAceptarPuja.setPadding(10, 10, 10, 10);
                        botonAceptarPuja.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                refArticulos.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            String titArt = (String) snapshot.child("titulo").getValue();
                                            if (titArt.equals(titulo)) {
                                                Articulos articuloActualizado = new Articulos(snapshot.child("titulo").getValue().toString(), snapshot.child("nombreImagen").getValue().toString(), snapshot.child("email").getValue().toString(), true, Float.parseFloat(snapshot.child("precio").getValue().toString()));
                                                refArticulos.child(snapshot.getKey()).setValue(articuloActualizado);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        });

                        LinearLayout pujas = (LinearLayout) getView().findViewById(R.id.pujas_encontradas);
                        LinearLayout puja = new LinearLayout(getActivity().getApplicationContext());

                        puja.setOrientation(LinearLayout.HORIZONTAL);
                        puja.setBackgroundColor(Color.parseColor("#E3F2FD"));
                        puja.addView(pujaEncontrada);
                        puja.addView(botonAceptarPuja);
                        pujas.addView(puja);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}