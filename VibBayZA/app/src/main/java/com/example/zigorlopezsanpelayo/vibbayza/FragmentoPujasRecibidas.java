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
    protected TextView noHayPujas;
    protected Button botonCerrarPuja;
    protected LinearLayout pujas;
    protected LinearLayout puja;
    protected String titulo;
    protected boolean hayPujas;

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
                    pujas = (LinearLayout) getView().findViewById(R.id.pujas_encontradas);
                    puja = new LinearLayout(getActivity().getApplicationContext());
                    if (snapshot.getChildrenCount() == 0) {
                        hayPujas = false;
                    }
                    else {
                        String nombreArticuloPujado = (String) snapshot.child("titulo").getValue();
                        String nombrePujador = (String) snapshot.child("email").getValue();
                        double cantidad = ((Number)snapshot.child("cantidad").getValue()).doubleValue();

                        titulo = getArguments().getString("titulo");

                        if (nombreArticuloPujado.equals(titulo)) {
                            hayPujas = true;
                            pujaEncontrada = new TextView(getActivity().getApplicationContext());
                            pujaEncontrada.setText("CANTIDAD:  " + cantidad + "€\n" + "PUJADOR:    " + nombrePujador);
                            pujaEncontrada.setBackgroundColor(Color.WHITE);
                            pujaEncontrada.setTextColor(Color.BLACK);
                            pujaEncontrada.setTextSize(30);

                            puja.setOrientation(LinearLayout.VERTICAL);
                            puja.setBackgroundColor(Color.parseColor("#E3F2FD"));
                            puja.addView(pujaEncontrada);
                            pujas.addView(puja);
                        }
                    }
                }
                if (!hayPujas) {
                    noHayPujas = new TextView(getActivity().getApplicationContext());
                    noHayPujas.setText("NADIE HA PUJADO POR ESTE ARTÍCULO TODAVÍA");
                    noHayPujas.setBackgroundColor(Color.WHITE);
                    noHayPujas.setTextColor(Color.GRAY);
                    noHayPujas.setTextSize(10);
                    pujas.addView(noHayPujas);
                }
                else {
                    botonCerrarPuja = new Button(getActivity().getApplicationContext());
                    botonCerrarPuja.setText("Cerrar puja");
                    botonCerrarPuja.setBackgroundColor(Color.parseColor("#F8BBD0"));
                    botonCerrarPuja.setPadding(10, 10, 10, 10);
                    botonCerrarPuja.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refArticulos.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String titArt = (String) snapshot.child("titulo").getValue();
                                        String nombreImagen = (String) snapshot.child("nombreImagen").getValue();
                                        String email = (String) snapshot.child("email").getValue();
                                        double precio = ((Number)snapshot.child("precio").getValue()).doubleValue();
                                        double pujaMaxima = (double) snapshot.child("pujaMaxima").getValue();
                                        if (titArt.equals(titulo)) {
                                            Articulos articuloActualizado = new Articulos(titArt, nombreImagen, email, true, precio, pujaMaxima);
                                            refArticulos.child(snapshot.getKey()).setValue(articuloActualizado);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            boolean fragmentTransaction = false;

                            FragmentoArticulos fragmentoArticulos = new FragmentoArticulos();
                            fragmentTransaction = true;

                            if(fragmentTransaction) {
                                getFragmentManager().beginTransaction().addToBackStack("pujasRecibidas");
                                Fragment fragmentPrevio = getFragmentManager().findFragmentByTag("pujasRecibidas");
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.content_main, fragmentoArticulos)
                                        .commit();

                                ((ProfileActivity)getActivity()).getSupportActionBar().setTitle("Artículos");
                                getFragmentManager().beginTransaction().remove(fragmentPrevio);
                            }
                        }
                    });
                    pujas.addView(botonCerrarPuja);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}